package com.grananda.services

import javax.inject.Singleton

import com.grananda.domain.FaceMemoryCollection
import com.grananda.exceptions.UnknownCollectionException
import groovy.transform.CompileStatic

@CompileStatic
@Singleton
interface FaceMemoryCollectionService {
    List<FaceMemoryCollection> list(String organizationId)

    FaceMemoryCollection describe(String id)

    FaceMemoryCollection update(String id, String collectionName)

    FaceMemoryCollection registerFaceMemoryCollection(String organizationId, String collectionName)

    Boolean removeFaceMemoryCollection(String id) throws UnknownCollectionException
}
