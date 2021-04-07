package com.grananda.repositories

import com.grananda.domain.FaceMemory
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface FaceMemoryRepository extends CrudRepository<FaceMemory, String> {
}
