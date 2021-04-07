package com.grananda.dto

import java.time.OffsetDateTime

import io.micronaut.core.annotation.Introspected

@Introspected
class FaceMemoryDto {
    String id

    String faceId

    String collection

    OffsetDateTime createdAt

    OffsetDateTime updatedAt

    static FaceMemoryDto getInstance() {
        return new FaceMemoryDto()
    }
}
