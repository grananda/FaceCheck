package com.grananda.exchange.organization

import com.grananda.dto.OrganizationDto
import groovy.transform.CompileStatic

@CompileStatic
class DescribeOrganizationResponse {
    OrganizationDto organization

    DescribeOrganizationResponse() {
    }

    DescribeOrganizationResponse(OrganizationDto organization) {
        this.organization = organization
    }

    static getInstance(OrganizationDto organization) {
        return new DescribeOrganizationResponse(organization)
    }
}
