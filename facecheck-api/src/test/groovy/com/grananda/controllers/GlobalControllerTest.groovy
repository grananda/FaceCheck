package com.grananda.controllers


import javax.inject.Inject

import com.github.javafaker.Faker
import com.grananda.domain.User
import com.grananda.exchange.Request
import com.grananda.repositories.UserRepository
import com.grananda.utils.ControllerAuth
import io.micronaut.http.HttpRequest
import io.micronaut.http.client.HttpClient
import io.micronaut.http.client.annotation.Client
import org.jasypt.util.password.StrongPasswordEncryptor
import spock.lang.*
import utils.UserFactory

abstract class GlobalControllerTest extends Specification {

    @Inject
    UserRepository userRepository

    @Inject
    StrongPasswordEncryptor strongPasswordEncryptor

    String identity

    String password

    User user

    @Inject
    @Client('/')
    HttpClient client

    void setup() {
        identity = Faker.instance().lorem().characters(10)
        password = Faker.instance().internet().password()

        user = UserFactory.create([
                username: identity,
                password: strongPasswordEncryptor.encryptPassword(password)
        ])

        userRepository.save(user)
    }

    HttpRequest get(String url) {
        return ControllerAuth.login(HttpRequest.GET(url), user.username, password, client)
    }

    HttpRequest post(String url, Request request) {
        return ControllerAuth.login(HttpRequest.POST(url, request), user.username, password, client)
    }

    HttpRequest put(String url, Request request) {
        return ControllerAuth.login(HttpRequest.PUT(url, request), user.username, password, client)
    }

    HttpRequest delete(String url) {
        return ControllerAuth.login(HttpRequest.DELETE(url), user.username, password, client)
    }
}
