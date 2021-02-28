package com.grananda.services

import javax.inject.Inject
import javax.inject.Singleton

import com.grananda.domain.User
import com.grananda.exceptions.NotFoundException
import com.grananda.repositories.UserRepository
import groovy.transform.CompileStatic

@CompileStatic
@Singleton
class UserServiceImpl implements UserService {
    @Inject
    UserRepository userRepository

    @Override
    User describe(String email) {
        User user = userRepository.findByEmail(email).orElse(null)

        if (!user)
            throw new NotFoundException("Cannot find user with email: $email")

        return user
    }
}
