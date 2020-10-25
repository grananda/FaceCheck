package com.grananda.controllers

import com.github.javafaker.Faker
import com.grananda.Application
import com.grananda.domain.User
import com.grananda.repositories.UserRepository
import io.micronaut.http.*
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import io.micronaut.http.client.exceptions.HttpClientResponseException
import io.micronaut.security.authentication.UsernamePasswordCredentials
import io.micronaut.security.token.jwt.render.AccessRefreshToken
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import org.jasypt.util.password.StrongPasswordEncryptor
import spock.lang.Specification
import utils.UserFactory

import javax.inject.Inject

@MicronautTest(application = Application, transactional = false)
class LoginControllerTest extends Specification {

    @Inject
    UserRepository userRepository

    @Inject
    StrongPasswordEncryptor strongPasswordEncryptor

    @Inject
    @Client('/')
    HttpClient client

    def 'an existing user can login'() {
        given: 'an existing user'
        String password = 'password'
        String identity = Faker.instance().lorem().characters(10)

        User user = UserFactory.create([
                username: identity,
                password: strongPasswordEncryptor.encryptPassword(password)
        ])

        userRepository.save(user)

        and: 'a login request exists'
        HttpRequest request = HttpRequest.create(HttpMethod.POST, '/login')
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .body(new UsernamePasswordCredentials(identity, password))

        when: "a login action is performed"
        HttpResponse<AccessRefreshToken> response = client.toBlocking().exchange(request, AccessRefreshToken)

        then:
        response.status == HttpStatus.OK
        response.body.get().accessToken
    }

    def 'an non existing user cannot login'() {
        given: 'an existing user'
        String password = Faker.instance().internet().password()
        String identity = Faker.instance().lorem().characters(10)

        User user = UserFactory.create([
                username: identity,
                password: strongPasswordEncryptor.encryptPassword(password)
        ])

        userRepository.save(user)

        and: 'a login request exists'
        HttpRequest request = HttpRequest.create(HttpMethod.POST, '/login')
                .accept(MediaType.APPLICATION_JSON_TYPE)
                .body(new UsernamePasswordCredentials(identity, Faker.instance().internet().password()))

        when: "a login action is performed"
        client.toBlocking().exchange(request, AccessRefreshToken)

        then:
        HttpClientResponseException exception = thrown(HttpClientResponseException)
        exception.status == HttpStatus.UNAUTHORIZED
    }
}
