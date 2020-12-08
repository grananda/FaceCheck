package com.grananda.repositories

import com.grananda.domain.FaceMemoryCollection
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface FaceMemoryCollectionRepository extends CrudRepository<FaceMemoryCollection, String> {
    List<FaceMemoryCollection> findAllByOrganizationId(String organizationID)
}
