package com.grananda.services

import javax.inject.Singleton

import groovy.transform.CompileStatic

@CompileStatic
@Singleton
interface UuIdGeneratorService {
    UUID generateUuId();
}
