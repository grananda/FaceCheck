package com.grananda.controllers

import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.security.annotation.Secured
import io.micronaut.security.authentication.Authentication
import io.micronaut.security.rules.SecurityRule

@Controller('/')
@Secured(SecurityRule.IS_AUTHENTICATED)
class AuthController {

    @Get("/authenticated")
    authenticated(Authentication authentication) {
        return authentication;
    }
}
