package com.grananda.dto

import com.grananda.domain.Organization

@Singleton
class OrganizationMapper {

    static OrganizationDto map(Organization organization) {
        OrganizationDto dto = OrganizationDto.getInstance();

        dto.id = organization.id
        dto.name = organization.name
        dto.createdAt = organization.createdAt
        dto.updatedAt = organization.updatedAt

        return dto
    }
}
