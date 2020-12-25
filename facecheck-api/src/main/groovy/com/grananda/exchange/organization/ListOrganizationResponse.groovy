package com.grananda.exchange.organization

import com.grananda.domain.Organization
import com.grananda.dto.OrganizationDto
import com.grananda.dto.OrganizationMapper
import groovy.transform.CompileStatic

@CompileStatic
class ListOrganizationResponse {
    List<OrganizationDto> organizations

    ListOrganizationResponse() {
    }

    ListOrganizationResponse(List<Organization> organizations) {
        this.organizations = organizations.collect { OrganizationMapper.map(it) }
    }

    static getInstance(List<Organization> organizations) {
        return new ListOrganizationResponse(organizations)
    }
}
