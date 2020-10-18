package com.grananda.repositories

import com.grananda.domain.User
import io.micronaut.data.repository.CrudRepository

interface UserRepository extends CrudRepository<User, UUID> {
}
