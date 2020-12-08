package com.grananda.controllers

import java.time.OffsetDateTime
import javax.inject.Inject

import com.grananda.Application
import com.grananda.domain.Organization
import com.grananda.exchange.organization.*
import com.grananda.repositories.OrganizationRepository
import com.grananda.utils.ControllerAuth
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import utils.OrganizationFactory

@MicronautTest(application = Application.class, transactional = false)
class OrganizationControllerTest extends GlobalControllerTest {

    final static private String ORGANIZATION_API_PREFIX = '/organizations'

    @Inject
    OrganizationRepository organizationRepository

    def 'an organization is requested'() {
        given: 'an organizations'
        Organization organization = OrganizationFactory.create()
        organizationRepository.save(organization)

        when: 'an organization is requested'
        HttpRequest request = get("${ORGANIZATION_API_PREFIX}/${organization.id}")

        HttpResponse<DescribeOrganizationResponse> response = client.toBlocking().exchange(request, DescribeOrganizationResponse.class)

        then: 'the request response with a expected request status'
        response.status == HttpStatus.OK

        and: 'the requested organization matches the stored entity'
        response.body().organization.id == organization.id

        and: 'table is cleaned up'
        organizationRepository.deleteAll()
    }

    def 'an organization list is requested'() {
        given: 'an organizations'
        Organization organization1 = OrganizationFactory.create()
        organizationRepository.save(organization1)

        Organization organization2 = OrganizationFactory.create()
        organizationRepository.save(organization2)

        when: 'an organization list is requested'
        HttpRequest request = get("${ORGANIZATION_API_PREFIX}")

        HttpResponse<ListOrganizationResponse> response = client.toBlocking().exchange(request, ListOrganizationResponse.class)

        then: 'the request response with a expected request status'
        response.status == HttpStatus.OK

        and: 'the requested organization matches the stored entity'
        response.body().organizations.size() == 2
    }

    def 'an organization is saved'() {
        given: 'an organizations model'
        Organization organization = OrganizationFactory.create()

        when: 'an organization is saved'
        CreateOrganizationRequest createOrganizationRequest = new CreateOrganizationRequest(organization: organization)
        HttpRequest request = ControllerAuth.login(HttpRequest.POST("${ORGANIZATION_API_PREFIX}", createOrganizationRequest), user.username, password, client)

        HttpResponse<DescribeOrganizationResponse> response = client.toBlocking().exchange(request, DescribeOrganizationResponse.class)

        then: 'the request response with a expected request status'
        response.status == HttpStatus.CREATED

        and: 'the requested organization matches the stored entity'
        response.body().organization.name == organization.name
    }

    def 'an organization is updated'() {
        given: 'an organizations model'
        Organization organization = OrganizationFactory.create()
        organizationRepository.save(organization)

        and: 'and organization data'
        Organization organizationData = OrganizationFactory.create()

        when: 'an organization is updated'
        UpdateOrganizationRequest updateOrganizationRequest = new UpdateOrganizationRequest(organization: organizationData)
        HttpRequest request = ControllerAuth.login(HttpRequest.PUT("${ORGANIZATION_API_PREFIX}/${organization.id}", updateOrganizationRequest), user.username, password, client)

        HttpResponse<DescribeOrganizationResponse> response = client.toBlocking().exchange(request, DescribeOrganizationResponse.class)

        then: 'the request response with a expected request status'
        response.status == HttpStatus.OK

        and: 'the requested organization matches the stored entity'
        response.body().organization.name == organizationData.name
    }

    def 'an organization is deleted'() {
        given: 'an organizations model'
        Organization organization = OrganizationFactory.create()
        organizationRepository.save(organization)

        when: 'an organization is deleted'
        HttpRequest request = ControllerAuth.login(HttpRequest.DELETE("${ORGANIZATION_API_PREFIX}/${organization.id}"), user.username, password, client)

        HttpResponse<?> response = client.toBlocking().exchange(request)

        then: 'the request response with a expected request status'
        response.status == HttpStatus.NO_CONTENT

        and: 'the requested organization matches the stored entity'
        organizationRepository.findById(organization.id).get().deletedAt
    }

    def 'an organization is restored'() {
        given: 'an organizations model'
        Organization organization = OrganizationFactory.create(
                deletedAt: OffsetDateTime.now()
        )
        organizationRepository.save(organization)

        when: 'an organization is restored'
        RestoreOrganizationRequest restoreOrganizationRequest = new RestoreOrganizationRequest(id: organization.id)
        HttpRequest request = ControllerAuth.login(HttpRequest.PATCH("${ORGANIZATION_API_PREFIX}", restoreOrganizationRequest), user.username, password, client)

        HttpResponse<?> response = client.toBlocking().exchange(request)

        then: 'the request response with a expected request status'
        response.status == HttpStatus.NO_CONTENT

        and: 'the requested organization matches the stored entity'
        !organizationRepository.findById(organization.id).get().deletedAt
    }
}
