package com.grananda.repositories

import com.grananda.domain.User
import groovy.transform.CompileStatic
import io.micronaut.context.annotation.Executable
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
@CompileStatic
interface UserRepository extends CrudRepository<User, String> {
    @Executable
    User findByUsernameOrEmail(String username, String email)

    Optional<User> findByEmail(String username)
}
