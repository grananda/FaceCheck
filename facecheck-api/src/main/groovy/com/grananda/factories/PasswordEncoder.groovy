package com.grananda.factories

import io.micronaut.context.annotation.Factory
import io.micronaut.context.annotation.Prototype
import org.jasypt.util.password.StrongPasswordEncryptor

@Factory
class PasswordEncoder {

    @Prototype
    StrongPasswordEncryptor strongPasswordEncryptor() {
        return new StrongPasswordEncryptor();
    }
}
