package com.grananda.exchange.collection

import javax.inject.Singleton

import com.grananda.domain.FaceMemoryCollection
import com.grananda.dto.FaceMemoryCollectionDto
import com.grananda.dto.FaceMemoryCollectionMapper
import groovy.transform.CompileStatic

@CompileStatic
@Singleton
class DescribeFaceMemoryCollectionResponse {
    FaceMemoryCollectionDto collection

    DescribeFaceMemoryCollectionResponse() {
    }

    DescribeFaceMemoryCollectionResponse(FaceMemoryCollection collection) {
        this.collection = FaceMemoryCollectionMapper.map(collection)
    }

    static getInstance(FaceMemoryCollection collection) {
        return new DescribeFaceMemoryCollectionResponse(collection)
    }
}
