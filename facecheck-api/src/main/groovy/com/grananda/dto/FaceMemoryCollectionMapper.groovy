package com.grananda.dto

import javax.inject.Singleton

import com.grananda.domain.FaceMemoryCollection
import groovy.transform.CompileStatic

@CompileStatic
@Singleton
class FaceMemoryCollectionMapper {

    static FaceMemoryCollectionDto map(FaceMemoryCollection collection) {
        FaceMemoryCollectionDto dto = FaceMemoryCollectionDto.getInstance()

        dto.id = collection.id
        dto.name = collection.name
        dto.faceCount = collection.faces.size()
        dto.organization = OrganizationMapper.map(collection.organization)
        dto.createdAt = collection.createdAt
        dto.updatedAt = collection.updatedAt

        return dto
    }
}
