package com.grananda.exchange.organization

import com.grananda.dto.OrganizationDto
import groovy.transform.CompileStatic

@CompileStatic
class ListOrganizationResponse {
    List<OrganizationDto> organizations

    ListOrganizationResponse() {
    }

    ListOrganizationResponse(List<OrganizationDto> organizations) {
        this.organizations = organizations
    }

    static getInstance(List<OrganizationDto> organizations) {
        return new ListOrganizationResponse(organizations)
    }
}
