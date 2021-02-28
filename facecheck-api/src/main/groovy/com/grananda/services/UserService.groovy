package com.grananda.services

import javax.inject.Singleton

import com.grananda.domain.User
import groovy.transform.CompileStatic

@CompileStatic
@Singleton
interface UserService {
    User describe(String email)
}
