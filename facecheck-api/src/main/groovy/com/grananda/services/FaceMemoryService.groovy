package com.grananda.services

import com.grananda.domain.FaceMemory
import com.grananda.domain.FaceMemoryCollection
import com.grananda.domain.User
import com.grananda.exceptions.FaceCheckException
import com.grananda.exceptions.FaceNotInCollectionException
import software.amazon.awssdk.services.rekognition.model.Image

interface FaceMemoryService {
    FaceMemory storeFacialMemory(User user, FaceMemoryCollection collection, Image image) throws FaceCheckException

    boolean removeFacialMemory(FaceMemory faceMemory) throws FaceNotInCollectionException
}
