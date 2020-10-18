package com.grananda.services

import com.grananda.domain.FaceMemory
import com.grananda.domain.FaceMemoryCollection
import com.grananda.domain.User
import com.grananda.exceptions.*
import com.grananda.repositories.FaceMemoryRepository
import software.amazon.awssdk.services.rekognition.model.*

import javax.inject.Inject

class FaceMemoryServiceImpl implements FaceMemoryService {

    @Inject
    AwsRekognitionFaceService awsRekognitionFaceService

    @Inject
    FaceMemoryRepository faceMemoryRepository

    @Override
    FaceMemory storeFacialMemory(User user, FaceMemoryCollection collection, Image image) throws FaceCheckException {
        DetectFacesResponse detectFacesResponse = awsRekognitionFaceService.detectFaces(image)

        if (detectFacesResponse.faceDetails().size() == 0) {
            throw new MissingFaceInImageException("FaceMemoryException:FaceMemoryService:storeFacialMemory: No faces were detected in provided image for user " + user.getId())
        }

        if (detectFacesResponse.faceDetails().size() > 1) {
            throw new MultipleFacesInImageException("FaceMemoryException:FaceMemoryService:storeFacialMemory: Multiple faces were detected in provided image for user " + user.getId())
        }

        SearchFacesByImageResponse searchFacesByImageResponse = awsRekognitionFaceService.searchImage(collection.getCollectionId(), image)

        if (searchFacesByImageResponse.faceMatches().size() > 0) {
            throw new FaceAlreadyInCollectionException("FaceMemoryException:FaceMemoryService:storeFacialMemory: Face already exists into collection " + user.getId())
        }

        IndexFacesResponse response = awsRekognitionFaceService.indexFace(collection.getCollectionId(), image)

        FaceRecord faceRecord = response.faceRecords().iterator().next()

        FaceMemory faceMemory = FaceMemory.getInstance([
                faceId: faceRecord.face().faceId()
        ])

        faceMemoryRepository.save(faceMemory)

        faceMemory.setCollection(collection)
        faceMemory.setUser(user)

        collection.getFaces().add(faceMemory)

        user.setFace(faceMemory)

        return faceMemory
    }

    @Override
    boolean removeFacialMemory(FaceMemory faceMemory) throws FaceNotInCollectionException {
        try {
            awsRekognitionFaceService.searchFace(faceMemory.getCollection().getCollectionId(), faceMemory.getFaceId())
        } catch (InvalidParameterException invalidParameterException) {
            throw new FaceNotInCollectionException("FaceMemoryException:FaceMemoryService:removeFacialMemory: Face is not in collection " + faceMemory.getFaceId(), invalidParameterException)
        }

        DeleteFacesResponse deleteFacesResponse = awsRekognitionFaceService.forgetFace(faceMemory.getFaceId(), faceMemory.getCollection().getCollectionId())

        long deletedFaces = deleteFacesResponse.deletedFaces()
                .stream()
                .filter(item -> item == faceMemory.getFaceId())
                .count()

        return deletedFaces > 0L
    }
}
