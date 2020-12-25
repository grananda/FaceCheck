package com.grananda.exchange.organization

import com.grananda.domain.Organization
import com.grananda.dto.OrganizationDto
import com.grananda.dto.OrganizationMapper
import groovy.transform.CompileStatic

@CompileStatic
class DescribeOrganizationResponse {
    OrganizationDto organization

    DescribeOrganizationResponse() {
    }

    DescribeOrganizationResponse(Organization organization) {
        this.organization = OrganizationMapper.map(organization)
    }

    static getInstance(Organization organization) {
        return new DescribeOrganizationResponse(organization)
    }
}
