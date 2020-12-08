package com.grananda.services

import javax.inject.Inject
import javax.transaction.Transactional

import com.grananda.domain.FaceMemoryCollection
import com.grananda.domain.Organization
import com.grananda.dto.FaceMemoryCollectionDto
import com.grananda.dto.FaceMemoryCollectionMapper
import com.grananda.exceptions.NotFoundException
import com.grananda.exceptions.UnknownCollectionException
import com.grananda.repositories.FaceMemoryCollectionRepository
import com.grananda.repositories.OrganizationRepository
import groovy.util.logging.Slf4j
import software.amazon.awssdk.services.rekognition.model.CreateCollectionResponse
import software.amazon.awssdk.services.rekognition.model.DeleteCollectionResponse
import software.amazon.awssdk.services.rekognition.model.ResourceNotFoundException

@Slf4j
class FaceMemoryCollectionServiceImpl implements FaceMemoryCollectionService {

    @Inject
    AwsRekognitionCollectionService awsRekognitionCollectionService

    @Inject
    FaceMemoryCollectionRepository faceMemoryCollectionRepository

    @Inject
    OrganizationRepository organizationRepository

    @Inject
    OrganizationService organizationService

    @Inject
    UuIdGeneratorService uuIdGeneratorService

    @Override
    @Transactional
    List<FaceMemoryCollectionDto> list(String organizationId) {
        List<FaceMemoryCollection> collections = faceMemoryCollectionRepository.findAllByOrganizationId(organizationId).toList()

        return collections.collect { FaceMemoryCollectionMapper.map(it) }
    }

    @Override
    @Transactional
    FaceMemoryCollectionDto describe(String id) {
        FaceMemoryCollection collection = getFaceMemoryCollection(id)

        return FaceMemoryCollectionMapper.map(collection)
    }

    @Override
    @Transactional
    FaceMemoryCollectionDto update(String id, String collectionName) {
        FaceMemoryCollection collection = getFaceMemoryCollection(id)

        collection.name = collectionName
        collection = faceMemoryCollectionRepository.update(collection)

        return FaceMemoryCollectionMapper.map(collection)
    }

    @Override
    @Transactional
    FaceMemoryCollectionDto registerFaceMemoryCollection(String organizationId, String collectionName) {
        Organization organization = organizationRepository.findById(organizationId).orElse(null)

        if (!organization)
            throw new NotFoundException("Cannot find Organization with id: $organizationId")

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

        return FaceMemoryCollectionMapper.map(faceMemoryCollection)
    }

    @Override
    @Transactional
    Boolean removeFaceMemoryCollection(String id) throws UnknownCollectionException {
        FaceMemoryCollection collection = getFaceMemoryCollection(id)

        try {

            DeleteCollectionResponse deleteCollectionResponse = awsRekognitionCollectionService.deleteFaceMemoryCollection(collection.getCollectionId())

            collection.organization.memoryCollections.remove(collection)
            faceMemoryCollectionRepository.delete(collection)

            if (deleteCollectionResponse.statusCode() == awsRekognitionCollectionService.AWS_SUCCESS_STATUS_CODE) {
                return true
            }

        } catch (ResourceNotFoundException resourceNotFoundException) {
            throw new UnknownCollectionException("FaceMemoryException:FaceMemoryCollectionService:removeFaceMemoryCollection: Can not find requested collection: " + collection.collectionId, resourceNotFoundException)
        }

        return false
    }

    private FaceMemoryCollection getFaceMemoryCollection(String id) {
        FaceMemoryCollection collection = faceMemoryCollectionRepository.findById(id).orElse(null)

        if (!collection)
            throw new NotFoundException("Cannot find Collection with id: $id")

        return collection
    }
}
