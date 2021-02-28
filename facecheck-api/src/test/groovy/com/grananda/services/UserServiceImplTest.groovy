package com.grananda.services


import javax.inject.Inject

import com.grananda.Application
import com.grananda.domain.User
import com.grananda.repositories.UserRepository
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.*
import utils.UserFactory

@MicronautTest(application = Application.class, startApplication = false)
class UserServiceImplTest extends Specification {

    @Inject
    UserService userService

    @Inject
    UserRepository userRepository

    def "a user is described"() {
        given: 'a user'
        User user = UserFactory.create()

        user = userRepository.save(user)

        when: 'the user is requested by id'
        User response = userService.describe(user.email)

        then: 'the expected user is retrieved'
        response.id == user.id
    }
}
