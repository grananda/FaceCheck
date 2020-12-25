package com.grananda.services

import javax.inject.Singleton

import com.grananda.domain.Organization
import groovy.transform.CompileStatic

@CompileStatic
@Singleton
interface OrganizationService {
    List<Organization> list()

    Organization describe(String id)

    Organization create(Organization request)

    Organization update(String id, Organization request)

    void delete(String id)

    void restore(String id)
}
