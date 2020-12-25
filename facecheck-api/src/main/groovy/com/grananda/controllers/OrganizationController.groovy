package com.grananda.controllers

import javax.inject.Inject
import javax.transaction.Transactional

import com.grananda.domain.Organization
import com.grananda.exchange.organization.*
import com.grananda.services.OrganizationService
import io.micronaut.http.HttpResponse
import io.micronaut.http.annotation.*
import io.micronaut.security.annotation.Secured
import io.micronaut.security.rules.SecurityRule

@Controller('/organizations')
@Secured(SecurityRule.IS_AUTHENTICATED)
class OrganizationController {

    @Inject
    OrganizationService organizationService

    @Get('/')
    @Transactional
    HttpResponse<ListOrganizationResponse> index() {
        List<Organization> organizations = organizationService.list()

        return HttpResponse.ok(ListOrganizationResponse.getInstance(organizations))
    }

    @Get('/{id}')
    @Transactional
    HttpResponse<DescribeOrganizationResponse> show(@PathVariable String id) {
        Organization organization = organizationService.describe(id)

        return HttpResponse.ok(DescribeOrganizationResponse.getInstance(organization))
    }

    @Post('/')
    @Transactional
    HttpResponse<DescribeOrganizationResponse> save(@Body CreateOrganizationRequest request) {
        Organization organization = organizationService.create(request.organization)

        return HttpResponse.created(DescribeOrganizationResponse.getInstance(organization))
    }

    @Put('/{id}')
    @Transactional
    HttpResponse<DescribeOrganizationResponse> update(@PathVariable String id, @Body UpdateOrganizationRequest request) {
        Organization organization = organizationService.update(id, request.organization)

        return HttpResponse.ok(DescribeOrganizationResponse.getInstance(organization))
    }

    @Delete('/{id}')
    HttpResponse delete(@PathVariable String id) {
        organizationService.delete(id)

        return HttpResponse.noContent()
    }

    @Patch('/')
    HttpResponse<DescribeOrganizationResponse> restore(@Body RestoreOrganizationRequest request) {
        organizationService.restore(request.id)

        return HttpResponse.noContent()
    }
}
