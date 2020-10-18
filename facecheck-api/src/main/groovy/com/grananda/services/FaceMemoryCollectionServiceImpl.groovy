package com.grananda.services

import com.grananda.domain.FaceMemoryCollection
import com.grananda.domain.Organization
import com.grananda.exceptions.UnknownCollectionException
import com.grananda.repositories.FaceMemoryCollectionRepository
import groovy.transform.CompileStatic
import groovy.util.logging.Slf4j
import software.amazon.awssdk.services.rekognition.model.CreateCollectionResponse
import software.amazon.awssdk.services.rekognition.model.DeleteCollectionResponse
import software.amazon.awssdk.services.rekognition.model.ResourceNotFoundException

import javax.inject.Inject

@Slf4j
@CompileStatic
class FaceMemoryCollectionServiceImpl implements FaceMemoryCollectionService {

    @Inject
    AwsRekognitionCollectionService awsRekognitionCollectionService

    @Inject
    FaceMemoryCollectionRepository faceMemoryCollectionRepository

    @Inject
    UuIdGeneratorService uuIdGeneratorService

    @Override
    FaceMemoryCollection registerFaceMemoryCollection(Organization organization, String collectionName) {
        String collectionId = uuIdGeneratorService.generateUuId().toString()

        CreateCollectionResponse createCollectionResponse = awsRekognitionCollectionService.createFaceMemoryCollection(collectionId)

        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollection.getInstance([
                name         : collectionName,
                collectionId : collectionId,
                collectionArn: createCollectionResponse.collectionArn()
        ]);

        faceMemoryCollection.setOrganization(organization)
        organization.memoryCollections.add(faceMemoryCollection)

        faceMemoryCollection = faceMemoryCollectionRepository.save(faceMemoryCollection)

        return faceMemoryCollection
    }

    @Override
    Boolean removeFaceMemoryCollection(FaceMemoryCollection collection) throws UnknownCollectionException {
        try {

            DeleteCollectionResponse deleteCollectionResponse = awsRekognitionCollectionService.deleteFaceMemoryCollection(collection.getCollectionId())

            if (deleteCollectionResponse.statusCode() == awsRekognitionCollectionService.AWS_SUCCESS_STATUS_CODE) {
                return true
            }

        } catch (ResourceNotFoundException resourceNotFoundException) {
            throw new UnknownCollectionException("FaceMemoryException:FaceMemoryCollectionService:removeFaceMemoryCollection: Can not find requested collection: " + collection.getCollectionId(), resourceNotFoundException)
        } finally {
            faceMemoryCollectionRepository.delete(collection)
        }

        return false
    }
}
