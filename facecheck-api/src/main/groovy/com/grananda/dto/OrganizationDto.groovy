package com.grananda.dto

import java.time.OffsetDateTime

import io.micronaut.core.annotation.Introspected

@Introspected
class OrganizationDto {
    String id

    String name

    OffsetDateTime createdAt

    OffsetDateTime updatedAt

    static OrganizationDto getInstance() {
        return new OrganizationDto()
    }
}
