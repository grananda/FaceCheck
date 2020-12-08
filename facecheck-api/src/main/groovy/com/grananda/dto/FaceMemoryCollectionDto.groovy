package com.grananda.dto

import java.time.OffsetDateTime

import io.micronaut.core.annotation.Introspected

@Introspected
class FaceMemoryCollectionDto {
    String id

    String name

    long faceCount

    OrganizationDto organization

    OffsetDateTime createdAt

    OffsetDateTime updatedAt

    static FaceMemoryCollectionDto getInstance() {
        return new FaceMemoryCollectionDto()
    }
}
