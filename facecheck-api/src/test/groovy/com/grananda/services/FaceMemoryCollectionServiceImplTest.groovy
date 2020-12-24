package com.grananda.services

import javax.inject.Inject

import com.github.javafaker.Faker
import com.grananda.domain.FaceMemoryCollection
import com.grananda.domain.Organization
import com.grananda.dto.FaceMemoryCollectionDto
import com.grananda.exceptions.UnknownCollectionException
import com.grananda.repositories.FaceMemoryCollectionRepository
import com.grananda.repositories.OrganizationRepository
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import software.amazon.awssdk.services.rekognition.model.CreateCollectionResponse
import software.amazon.awssdk.services.rekognition.model.DeleteCollectionResponse
import software.amazon.awssdk.services.rekognition.model.ResourceNotFoundException
import spock.lang.*
import utils.FaceMemoryCollectionFactory
import utils.OrganizationFactory

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

    @Inject
    FaceMemoryCollectionServiceImpl service;

    @Inject
    OrganizationRepository organizationRepository;

    def 'a face memory collection is described'() {
        given: 'an organization'
        Organization organization = OrganizationFactory.create()
        organizationRepository.save(organization);

        and: 'a collection'
        FaceMemoryCollection collection = FaceMemoryCollectionFactory.create()
        collection.addOrganization(organization)

        faceMemoryCollectionRepository.save(collection)

        when: 'a collection is requested'
        FaceMemoryCollection response = service.describe(collection.id)

        then: 'the desired collection is retrieved'
        response.id == collection.id
        response.organization.id == organization.id
        organization.memoryCollections.size()
    }

    def 'a list face memory collection is requested'() {
        given: 'an set of organizations'
        Organization organization1 = OrganizationFactory.create()
        organizationRepository.save(organization1);

        Organization organization2 = OrganizationFactory.create()
        organizationRepository.save(organization2);

        and: 'a set of collections'
        FaceMemoryCollection collection1 = FaceMemoryCollectionFactory.create()
        collection1.addOrganization(organization1)
        faceMemoryCollectionRepository.save(collection1)

        FaceMemoryCollection collection2 = FaceMemoryCollectionFactory.create()
        collection2.addOrganization(organization2)
        faceMemoryCollectionRepository.save(collection2)

        when: 'a collection is requested'
        List<FaceMemoryCollectionDto> response = service.list(organization1.id)

        then: 'the desired collection is retrieved'
        response.size() == 1
        response.first().id == collection1.id
    }

    def 'a face memory collection is updated'() {
        given: 'an organization'
        Organization organization = OrganizationFactory.create()
        organizationRepository.save(organization);

        and: 'a collection'
        FaceMemoryCollection collection = FaceMemoryCollectionFactory.create()
        collection.addOrganization(organization)

        faceMemoryCollectionRepository.save(collection)

        and: 'a new collection name'
        String collectionName = Faker.instance().lorem().word()

        when: 'a collection is updated'
        FaceMemoryCollection response = service.update(collection.id, collectionName)

        then: 'the desired collection is retrieved'
        response.id == collection.id
        response.organization.id == organization.id
        response.name == collectionName
    }

    def 'a face memory collection is registered'() {
        given: 'A random set of params'
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

        when: 'a face memory collection is registered'
        FaceMemoryCollection collection = service.registerFaceMemoryCollection(organization.id, collectionName);

        then: 'the following mocked interactions occur'
        1 * awsRekognitionCollectionService.createFaceMemoryCollection(_) >> createCollectionResponse

        and: 'the collection is registered'
        collection.getName() == collectionName
        organizationRepository.findById(organization.id).get().memoryCollections.size() == 1
    }

    def 'a face memory collection is removed'() {
        given: 'an organization'
        Organization organization = OrganizationFactory.create()
        organizationRepository.save(organization);

        and: 'a collection'
        FaceMemoryCollection collection = FaceMemoryCollectionFactory.create()
        collection.addOrganization(organization)

        faceMemoryCollectionRepository.save(collection)

        and: 'a delete collection expected response'
        DeleteCollectionResponse deleteCollectionResponse = DeleteCollectionResponse.builder()
                .statusCode(200)
                .build() as DeleteCollectionResponse;

        when: 'a face collection is removed'
        Boolean response = service.removeFaceMemoryCollection(collection.id);

        then: 'the following mocked interactions occurred'
        1 * awsRekognitionCollectionService.deleteFaceMemoryCollection(collection.collectionId) >> deleteCollectionResponse

        and: 'the response is true'
        response
        faceMemoryCollectionRepository.count() == 0
        organizationRepository.findById(organization.id).get().memoryCollections.size() == 0
    }

    def 'a non existing face memory collection is not removed'() {
        given: 'an organization'
        Organization organization = OrganizationFactory.create()
        organizationRepository.save(organization);

        and: 'a collection'
        FaceMemoryCollection collection = FaceMemoryCollectionFactory.create()
        collection.addOrganization(organization)

        faceMemoryCollectionRepository.save(collection)

        when: 'a non existing collection is removed'
        service.removeFaceMemoryCollection(collection.id);

        then: 'the following interaction occur'
        1 * awsRekognitionCollectionService.deleteFaceMemoryCollection(collection.collectionId) >> {
            throw ResourceNotFoundException::builder().build()
        }

        then: 'throw and exception'
        UnknownCollectionException exception = thrown()

        and: 'the exception has expected text'
        exception.getMessage().contains("FaceMemoryException:FaceMemoryCollectionService:removeFaceMemoryCollection:")
    }
}
