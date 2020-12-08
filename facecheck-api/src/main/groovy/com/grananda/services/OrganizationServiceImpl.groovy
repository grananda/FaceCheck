package com.grananda.services

import java.time.OffsetDateTime
import javax.inject.Inject

import com.grananda.domain.Organization
import com.grananda.dto.OrganizationDto
import com.grananda.dto.OrganizationMapper
import com.grananda.exceptions.NotFoundException
import com.grananda.repositories.OrganizationRepository

class OrganizationServiceImpl implements OrganizationService {

    @Inject
    OrganizationRepository organizationRepository

    @Override
    List<OrganizationDto> list() {
        List<Organization> organizations = organizationRepository.findAll().toList()

        return organizations.collect { OrganizationMapper.map(it) }
    }

    @Override
    OrganizationDto describe(String id) {
        Organization organization = getOrganization(id)

        return OrganizationMapper.map(organization)
    }

    @Override
    OrganizationDto create(Organization data) {
        Organization organization = organizationRepository.save(data)

        return OrganizationMapper.map(organization)
    }

    @Override
    OrganizationDto update(String id, Organization data) {
        Organization organization = getOrganization(id)

        Organization entity = data
        entity.setId(organization.id)

        entity = organizationRepository.update(entity)

        return OrganizationMapper.map(entity)
    }

    @Override
    void delete(String id) {
        Organization organization = getOrganization(id)
        organization.deletedAt = OffsetDateTime.now()

        organizationRepository.update(organization)
    }

    @Override
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
