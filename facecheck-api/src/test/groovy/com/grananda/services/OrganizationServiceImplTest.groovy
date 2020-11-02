package com.grananda.services

import com.grananda.Application
import com.grananda.domain.Organization
import com.grananda.dto.OrganizationDto
import com.grananda.repositories.OrganizationRepository
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import spock.lang.Specification
import utils.OrganizationFactory

import javax.inject.Inject
import java.time.OffsetDateTime

@MicronautTest(application = Application.class, startApplication = false)
class OrganizationServiceImplTest extends Specification {
    @Inject
    OrganizationServiceImpl organizationService

    @Inject
    OrganizationRepository organizationRepository

    def "a list of organization is retrieved"() {
        given: 'a set of organizations'
        def listSize = 5

        List<Organization> organizations = OrganizationFactory.createList(listSize)

        organizations.each { organizationRepository.save(it) }

        when: 'a list of all organizations is requested'
        List<OrganizationDto> response = organizationService.list();

        then: 'all created organizations were returned'
        response.size() == listSize
    }

    def "an organization is requested"() {
        given: 'an organization instance'
        Organization organization = OrganizationFactory.create()

        organization = organizationRepository.save(organization)

        when: 'an organization is described'
        OrganizationDto response = organizationService.describe(organization.id)

        then: 'the created organization is requested'
        response.id == organization.id
    }

    def "an organization is created"() {
        given: 'an organization instance'
        Organization organization = OrganizationFactory.create()

        when: 'it is persisted through the proper service'
        OrganizationDto response = organizationService.create(organization)

        then: 'the returned object matches the original'
        response.name == organization.name

        and: 'the object was persisted into the database'
        organizationRepository.count() == 1
    }

    def "and existing organization is updated"() {
        given: 'an organization instance'
        Organization organization = OrganizationFactory.create()

        organization = organizationRepository.save(organization)

        and: 'an organization update request'
        Organization organizationData = OrganizationFactory.create()

        when: 'it is update through the proper service'
        OrganizationDto response = organizationService.update(organization.id, organizationData)

        then: 'the organization is modified'
        response.id == organization.id
        response.name == organizationData.name

        and: 'no new entity was persisted'
        organizationRepository.count() == 1
    }

    def "an organization is deleted"() {
        given: 'an organization instance'
        Organization organization = OrganizationFactory.create()

        organization = organizationRepository.save(organization)

        when: 'it is archived through the proper service'
        organizationService.delete(organization.id)

        then: 'the organization is archived'
        organizationRepository.count() == 1
        organizationRepository.findById(organization.id).get().deletedAt
    }

    def "an organization is restored"() {
        given: 'an organization instance'
        Organization organization = OrganizationFactory.create([
                deletedAt: OffsetDateTime.now()
        ])

        organization = organizationRepository.save(organization)

        when: 'it is archived through the proper service'
        organizationService.restore(organization.id)

        then: 'the organization is archived'
        organizationRepository.count() == 1
        !organizationRepository.findById(organization.id).get().deletedAt
    }
}
