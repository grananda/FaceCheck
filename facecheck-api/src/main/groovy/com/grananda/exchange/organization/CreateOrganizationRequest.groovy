package com.grananda.exchange.organization

import com.grananda.domain.Organization

class CreateOrganizationRequest {
    Organization organization

    CreateOrganizationRequest() {
    }

    CreateOrganizationRequest(Organization organization) {
        this.organization = organization
    }
}
