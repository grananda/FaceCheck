package com.grananda.services

class UuIdGeneratorServiceImpl implements UuIdGeneratorService {

    @Override
    UUID generateUuId() {
        return UUID.randomUUID()
    }
}
