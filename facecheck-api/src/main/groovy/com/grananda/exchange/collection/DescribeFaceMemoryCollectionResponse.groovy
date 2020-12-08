package com.grananda.exchange.collection

import javax.inject.Singleton

import com.grananda.dto.FaceMemoryCollectionDto
import groovy.transform.CompileStatic

@CompileStatic
@Singleton
class DescribeFaceMemoryCollectionResponse {
    FaceMemoryCollectionDto collection

    DescribeFaceMemoryCollectionResponse() {
    }

    DescribeFaceMemoryCollectionResponse(FaceMemoryCollectionDto collection) {
        this.collection = collection
    }

    static getInstance(FaceMemoryCollectionDto collection) {
        return new DescribeFaceMemoryCollectionResponse(collection)
    }
}
