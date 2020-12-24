package com.grananda.exchange.collection

import com.grananda.domain.FaceMemoryCollection
import com.grananda.dto.FaceMemoryCollectionDto
import com.grananda.dto.FaceMemoryCollectionMapper

class ListFaceMemoryCollectionResponse {
    List<FaceMemoryCollectionDto> collections

    ListFaceMemoryCollectionResponse() {
    }

    ListFaceMemoryCollectionResponse(List<FaceMemoryCollection> collections) {
        this.collections = collections.collect { FaceMemoryCollectionMapper.map(it) }
    }

    static getInstance(List<FaceMemoryCollection> collections) {
        return new ListFaceMemoryCollectionResponse(collections)
    }
}
