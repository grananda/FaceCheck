package com.grananda.repositories

import com.grananda.domain.FaceMemory
import io.micronaut.data.repository.CrudRepository

interface FaceMemoryRepository extends CrudRepository<FaceMemory, String> {
}
