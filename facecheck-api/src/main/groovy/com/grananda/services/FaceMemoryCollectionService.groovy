package com.grananda.services

import com.grananda.domain.FaceMemoryCollection
import com.grananda.domain.Organization
import com.grananda.exceptions.UnknownCollectionException

interface FaceMemoryCollectionService {
    FaceMemoryCollection registerFaceMemoryCollection(Organization organization, String collectionName)

    Boolean removeFaceMemoryCollection(FaceMemoryCollection collection) throws UnknownCollectionException
}
