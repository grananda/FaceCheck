package com.grananda.services

import java.time.OffsetDateTime
import javax.inject.Inject
import javax.transaction.Transactional

import com.grananda.domain.Organization
import com.grananda.exceptions.NotFoundException
import com.grananda.repositories.OrganizationRepository
import io.micronaut.transaction.annotation.ReadOnly

class OrganizationServiceImpl implements OrganizationService {

    @Inject
    OrganizationRepository organizationRepository

    @Override
    @ReadOnly
    List<Organization> list() {
        List<Organization> organizations = organizationRepository.findAll().toList()

        return organizations
    }

    @Override
    @ReadOnly
    Organization describe(String id) {
        Organization organization = getOrganization(id)

        return organization
    }

    @Override
    @Transactional
    Organization create(Organization data) {
        Organization organization = organizationRepository.save(data)

        return organization
    }

    @Override
    @Transactional
    Organization update(String id, Organization data) {
        Organization organization = getOrganization(id)

        Organization entity = data
        entity.setId(organization.id)

        entity = organizationRepository.update(entity)

        return entity
    }

    @Override
    @Transactional
    void delete(String id) {
        Organization organization = getOrganization(id)
        organization.deletedAt = OffsetDateTime.now()

        organizationRepository.update(organization)
    }

    @Override
    @Transactional
    void restore(String id) {
        Organization organization = getOrganization(id)
        organization.deletedAt = null

        organizationRepository.update(organization)
    }

    private getOrganization(String id) {
        Organization organization = organizationRepository.findById(id).orElse(null)

        if (!organization)
            throw new NotFoundException("Cannot find Organization with id: $id")

        return organization
    }
}
