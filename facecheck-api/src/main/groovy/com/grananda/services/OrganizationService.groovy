package com.grananda.services

import javax.inject.Singleton

import com.grananda.domain.Organization
import com.grananda.dto.OrganizationDto
import groovy.transform.CompileStatic


@CompileStatic
@Singleton
interface OrganizationService {
    List<OrganizationDto> list()

    OrganizationDto describe(String id)

    OrganizationDto create(Organization request)

    OrganizationDto update(String id, Organization request)

    void delete(String id)

    void restore(String id)
}
