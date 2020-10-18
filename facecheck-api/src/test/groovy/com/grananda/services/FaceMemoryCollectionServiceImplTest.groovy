package com.grananda.services

import com.github.javafaker.Faker
import com.grananda.domain.FaceMemoryCollection
import com.grananda.domain.Organization
import com.grananda.exceptions.UnknownCollectionException
import com.grananda.repositories.FaceMemoryCollectionRepository
import com.grananda.repositories.OrganizationRepository
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import software.amazon.awssdk.services.rekognition.model.CreateCollectionResponse
import software.amazon.awssdk.services.rekognition.model.DeleteCollectionResponse
import software.amazon.awssdk.services.rekognition.model.ResourceNotFoundException
import spock.lang.Specification

import javax.inject.Inject

@MicronautTest(startApplication = false)
class FaceMemoryCollectionServiceImplTest extends Specification {

    @Inject
    AwsRekognitionCollectionService awsRekognitionCollectionService;

    @MockBean(AwsRekognitionCollectionServiceImpl)
    AwsRekognitionCollectionService mockAwsRekognitionCollectionService() {
        Mock(AwsRekognitionCollectionService)
    }

    @Inject
    FaceMemoryCollectionRepository faceMemoryCollectionRepository;

    @MockBean(FaceMemoryCollectionRepository)
    FaceMemoryCollectionRepository mockFaceMemoryCollectionRepository() {
        Mock(FaceMemoryCollectionRepository)
    }

    @Inject
    UuIdGeneratorService uuIdGeneratorService;

    @MockBean(UuIdGeneratorService)
    UuIdGeneratorService mockUuIDGeneratorService() {
        Mock(UuIdGeneratorService)
    }

    @Inject
    FaceMemoryCollectionServiceImpl service;

    @Inject
    OrganizationRepository organizationRepository;

    def 'a face memory collection is registered'() {
        given: 'A random set of params'
        UUID collectionId = UUID.randomUUID();
        String collectionArn = Faker.instance().lorem().word();
        String collectionName = Faker.instance().lorem().word();

        and: 'an organization'
        Organization organization = Organization.getInstance([
                name: Faker.instance().company().name(),
        ])

        organizationRepository.save(organization);

        and: 'a create collection expected response'
        CreateCollectionResponse createCollectionResponse = CreateCollectionResponse
                .builder()
                .collectionArn(collectionArn)
                .statusCode(200)
                .build() as CreateCollectionResponse;

        and: 'a an expected face memory collection'
        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollection.getInstance([
                name         : collectionName,
                collectionId : collectionId.toString(),
                collectionArn: createCollectionResponse.collectionArn(),
        ])

        when: 'a face memory collection is registered'
        FaceMemoryCollection collection = service.registerFaceMemoryCollection(organization, collectionName);

        then: 'the following mocked interactions occur'
        1 * uuIdGeneratorService.generateUuId() >> collectionId
        1 * awsRekognitionCollectionService.createFaceMemoryCollection(collectionId.toString()) >> createCollectionResponse
        1 * faceMemoryCollectionRepository.save(_) >> faceMemoryCollection

        and: 'the collection is registered'
        collection.getCollectionArn() == collectionArn
        collection.getCollectionId() == collectionId.toString()
        collection.getName() == collectionName
        organization.memoryCollections.size() == 1
    }

    def 'a face memory collection is removed'() {
        given: 'A random set of params'
        String collectionId = UUID.randomUUID().toString();
        String collectionArn = Faker.instance().lorem().word();
        String collectionName = Faker.instance().lorem().word();

        and: 'a delete collection expected response'
        DeleteCollectionResponse deleteCollectionResponse = DeleteCollectionResponse.builder()
                .statusCode(200)
                .build() as DeleteCollectionResponse;

        and: 'a memory collection'
        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollection.getInstance([
                collectionId : collectionId,
                name         : collectionName,
                collectionArn: collectionArn,
        ])

        when: 'a face collection is removed'
        Boolean response = service.removeFaceMemoryCollection(faceMemoryCollection);

        then: 'the following mocked interactions occurred'
        1 * awsRekognitionCollectionService.deleteFaceMemoryCollection(faceMemoryCollection.collectionId) >> deleteCollectionResponse

        and: ' the following real interactions occurred'
        1 * faceMemoryCollectionRepository.delete(_)

        and: 'the response is true'
        response
    }

    def 'a non existing face memory collection is not removed'() {
        given: 'a random param'
        String collectionId = UUID.randomUUID().toString();

        and: 'a face collection'
        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollection.getInstance([
                collectionId: collectionId
        ])

        when: 'a non existing collection is removed'
        service.removeFaceMemoryCollection(faceMemoryCollection);

        then: 'the following interaction occur'
        1 * awsRekognitionCollectionService.deleteFaceMemoryCollection(faceMemoryCollection.collectionId) >> {
            throw ResourceNotFoundException::builder().build()
        }

        and: 'the following real interaction occur'
        1 * faceMemoryCollectionRepository.delete(faceMemoryCollection)

        then: 'throw and exception'
        UnknownCollectionException exception = thrown()

        and: 'the exception has expected text'
        exception.getMessage().contains("FaceMemoryException:FaceMemoryCollectionService:removeFaceMemoryCollection:")
    }
}
