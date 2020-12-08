package com.grananda.services

import javax.inject.Singleton

import com.grananda.dto.FaceMemoryCollectionDto
import com.grananda.exceptions.UnknownCollectionException
import groovy.transform.CompileStatic

@CompileStatic
@Singleton
interface FaceMemoryCollectionService {
    List<FaceMemoryCollectionDto> list(String organizationId)

    FaceMemoryCollectionDto describe(String id)

    FaceMemoryCollectionDto update(String id, String collectionName)

    FaceMemoryCollectionDto registerFaceMemoryCollection(String organizationId, String collectionName)

    Boolean removeFaceMemoryCollection(String id) throws UnknownCollectionException
}
