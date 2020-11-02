package com.grananda.services

import com.grananda.domain.Organization
import com.grananda.dto.OrganizationDto

interface OrganizationService {
    List<OrganizationDto> list()

    OrganizationDto describe(String id)

    OrganizationDto create(Organization request)

    OrganizationDto update(String id, Organization request)

    void delete(String id)

    void restore(String id)
}
