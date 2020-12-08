package com.grananda.exchange.collection

import com.grananda.dto.FaceMemoryCollectionDto

class ListFaceMemoryCollectionResponse {
    List<FaceMemoryCollectionDto> collections

    ListFaceMemoryCollectionResponse() {
    }

    ListFaceMemoryCollectionResponse(List<FaceMemoryCollectionDto> collections) {
        this.collections = collections
    }

    static getInstance(List<FaceMemoryCollectionDto> collections) {
        return new ListFaceMemoryCollectionResponse(collections)
    }
}
