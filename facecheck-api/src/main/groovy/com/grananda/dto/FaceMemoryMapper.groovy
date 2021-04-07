package com.grananda.dto

import com.grananda.domain.FaceMemory

@Singleton
class FaceMemoryMapper {

    static FaceMemoryDto map(FaceMemory faceMemory) {
        FaceMemoryDto dto = FaceMemoryDto.getInstance()

        dto.id = faceMemory.id
        dto.faceId = faceMemory.faceId
        dto.collection = faceMemory.collection
        dto.createdAt = faceMemory.createdAt
        dto.updatedAt = faceMemory.updatedAt

        return dto
    }
}
