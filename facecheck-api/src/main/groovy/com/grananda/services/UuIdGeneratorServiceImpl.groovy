package com.grananda.services

import groovy.transform.CompileStatic

@CompileStatic
class UuIdGeneratorServiceImpl implements UuIdGeneratorService {

    @Override
    UUID generateUuId() {
        return UUID.randomUUID()
    }
}
