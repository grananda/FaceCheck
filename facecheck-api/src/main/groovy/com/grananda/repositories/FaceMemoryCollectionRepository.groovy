package com.grananda.repositories

import com.grananda.domain.FaceMemoryCollection
import io.micronaut.data.repository.CrudRepository

interface FaceMemoryCollectionRepository extends CrudRepository<FaceMemoryCollection, String> {
}
