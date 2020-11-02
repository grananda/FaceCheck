package com.grananda.dto

import io.micronaut.core.annotation.Introspected

import java.time.OffsetDateTime

@Introspected
class OrganizationDto {
    String id

    String name

    OffsetDateTime createdAt

    OffsetDateTime updatedAt

    static OrganizationDto getInstance() {
        return new OrganizationDto();
    }
}
