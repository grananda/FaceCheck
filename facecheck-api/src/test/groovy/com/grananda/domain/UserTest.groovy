package com.grananda.domain

import com.github.javafaker.Faker
import com.grananda.repositories.UserRepository
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import utils.UserFactory

import javax.inject.Inject

@MicronautTest(startApplication = false)
class UserTest extends Specification {

    @Inject
    UserRepository userRepository

    def "a user is created with custom id generator"() {
        given: 'a user instance is generated'

        User user = UserFactory.create()

        and: 'a desired id value'
        String id = Faker.instance().lorem().characters(10)

        when: 'the instance is persisted'
        User response = userRepository.save(user)

        then: 'a proper id has been generated'
        response.id.length() > 20
        response.id.getClass().toString() == String.class.toString()
    }
}
