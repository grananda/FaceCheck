package com.grananda.repositories

import com.grananda.domain.Organization
import com.grananda.dto.OrganizationDto
import io.micronaut.data.annotation.Repository
import io.micronaut.data.repository.CrudRepository

@Repository
interface OrganizationRepository extends CrudRepository<Organization, String> {
    List<OrganizationDto> findAll()
}
