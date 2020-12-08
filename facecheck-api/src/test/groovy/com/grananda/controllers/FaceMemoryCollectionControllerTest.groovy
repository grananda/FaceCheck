package com.grananda.controllers

import javax.inject.Inject

import com.github.javafaker.Faker
import com.grananda.Application
import com.grananda.domain.FaceMemoryCollection
import com.grananda.domain.Organization
import com.grananda.exchange.collection.CreateFaceMemoryCollectionRequest
import com.grananda.exchange.collection.DescribeFaceMemoryCollectionResponse
import com.grananda.exchange.collection.ListFaceMemoryCollectionResponse
import com.grananda.exchange.collection.UpdateFaceMemoryCollectionRequest
import com.grananda.repositories.FaceMemoryCollectionRepository
import com.grananda.repositories.OrganizationRepository
import com.grananda.services.AwsRekognitionCollectionService
import com.grananda.services.AwsRekognitionCollectionServiceImpl
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import software.amazon.awssdk.services.rekognition.model.CreateCollectionResponse
import software.amazon.awssdk.services.rekognition.model.DeleteCollectionResponse
import utils.FaceMemoryCollectionFactory
import utils.OrganizationFactory

@MicronautTest(application = Application.class, transactional = false)
class FaceMemoryCollectionControllerTest extends GlobalControllerTest {

    final static private String COLLECTION_API_PREFIX = '/collections'

    @Inject
    OrganizationRepository organizationRepository

    @Inject
    FaceMemoryCollectionRepository faceMemoryCollectionRepository;

    @Inject
    AwsRekognitionCollectionService awsRekognitionCollectionService;

    @MockBean(AwsRekognitionCollectionServiceImpl)
    AwsRekognitionCollectionService mockAwsRekognitionCollectionService() {
        Mock(AwsRekognitionCollectionService)
    }

    def 'a collection is requested'() {
        given: 'an organization'
        Organization organization = OrganizationFactory.create()
        organizationRepository.save(organization);

        and: 'a collection'
        FaceMemoryCollection collection = FaceMemoryCollectionFactory.create()
        collection.addOrganization(organization)

        faceMemoryCollectionRepository.save(collection)

        when: 'an collection is requested'
        HttpRequest request = get("${COLLECTION_API_PREFIX}/${collection.id}")
        HttpResponse<DescribeFaceMemoryCollectionResponse> response = client.toBlocking().exchange(request, DescribeFaceMemoryCollectionResponse.class)

        then: 'the request response with a expected request status'
        response.status == HttpStatus.OK

        and: 'the requested collection matches the stored entity'
        response.body().collection.id == collection.id
    }

    def 'a collection list is requested'() {
        given: 'an organization'
        Organization organization = OrganizationFactory.create()
        organizationRepository.save(organization);

        and: 'a set of collections'
        FaceMemoryCollection collection1 = FaceMemoryCollectionFactory.create()
        collection1.addOrganization(organization)
        faceMemoryCollectionRepository.save(collection1)

        FaceMemoryCollection collection2 = FaceMemoryCollectionFactory.create()
        collection2.addOrganization(organization)
        faceMemoryCollectionRepository.save(collection2)

        when: 'an collection is requested'
        HttpRequest request = get("${COLLECTION_API_PREFIX}/organization/${organization.id}")
        HttpResponse<ListFaceMemoryCollectionResponse> response = client.toBlocking().exchange(request, ListFaceMemoryCollectionResponse.class)

        then: 'the request response with a expected request status'
        response.status == HttpStatus.OK

        and: 'the requested collection matches the stored entity'
        response.body().collections.size() == 2
    }

    def 'a collection is saved'() {
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

        when: 'an collection is saved'
        CreateFaceMemoryCollectionRequest createFaceCollectionRequest = new CreateFaceMemoryCollectionRequest(organizationId: organization.id, collectionName: collectionName)
        HttpRequest request = post("${COLLECTION_API_PREFIX}", createFaceCollectionRequest)
        HttpResponse<DescribeFaceMemoryCollectionResponse> response = client.toBlocking().exchange(request, DescribeFaceMemoryCollectionResponse.class)

        then: 'the following mocked interactions occur'
        1 * awsRekognitionCollectionService.createFaceMemoryCollection(_) >> createCollectionResponse

        and: 'the request response with a expected request status'
        response.status == HttpStatus.CREATED

        and: 'the requested collection matches the stored entity'
        response.body().collection.name == collectionName

        and: 'the collection is registered'
        response.body().collection.getName() == collectionName
    }

    def 'a collection is updated'() {
        given: 'an organization'
        Organization organization = OrganizationFactory.create()
        organizationRepository.save(organization);

        and: 'a collection'
        FaceMemoryCollection collection = FaceMemoryCollectionFactory.create()
        collection.addOrganization(organization)

        faceMemoryCollectionRepository.save(collection)

        and: 'a new collection name'
        String collectionName = Faker.instance().lorem().word()

        when: 'an collection is saved'
        UpdateFaceMemoryCollectionRequest updateFaceMemoryCollectionRequest = new UpdateFaceMemoryCollectionRequest(collectionName: collectionName)
        HttpRequest request = put("${COLLECTION_API_PREFIX}/${collection.id}", updateFaceMemoryCollectionRequest)
        HttpResponse<DescribeFaceMemoryCollectionResponse> response = client.toBlocking().exchange(request, DescribeFaceMemoryCollectionResponse.class)

        then: 'the request response with a expected request status'
        response.status == HttpStatus.OK

        and: 'the requested collection matches the stored entity'
        response.body().collection.name == collectionName
    }

    def 'a collection is deleted'() {
        given: 'an organization'
        Organization organization = OrganizationFactory.create()
        organizationRepository.save(organization);

        and: 'a collection'
        FaceMemoryCollection collection = FaceMemoryCollectionFactory.create()
        collection.addOrganization(organization)

        faceMemoryCollectionRepository.save(collection)

        and: 'a remove collection expected response'
        DeleteCollectionResponse deleteCollectionResponse = DeleteCollectionResponse
                .builder()
                .statusCode(awsRekognitionCollectionService.AWS_SUCCESS_STATUS_CODE)
                .build() as DeleteCollectionResponse;

        when: 'a collection is deleted'
        HttpRequest request = delete("${COLLECTION_API_PREFIX}/${collection.id}")
        HttpResponse<?> response = client.toBlocking().exchange(request)

        then: 'the following mocked interactions occur'
        1 * awsRekognitionCollectionService.deleteFaceMemoryCollection(_) >> deleteCollectionResponse

        and: 'the request response with a expected request status'
        response.status == HttpStatus.NO_CONTENT
    }
}
