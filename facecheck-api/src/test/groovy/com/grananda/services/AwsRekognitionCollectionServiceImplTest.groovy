package com.grananda.services

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import software.amazon.awssdk.services.rekognition.model.*

@MicronautTest(startApplication = false)
class AwsRekognitionCollectionServiceImplTest extends BaseAwsRekognitionTest {

    def "a collection is created"() {
        given: 'a generated collection id'
        String collectionId = UUID.randomUUID().toString()

        when: 'memory collection is created at AWS'
        CreateCollectionResponse response = rekognitionCollectionService.createFaceMemoryCollection(collectionId)

        then: 'status code is ok'
        response.statusCode().toString() == "200"

        and: 'response collection ARN matches collection id'
        response.collectionArn().toString().split("/")[1] == collectionId
    }

    def 'a collection is described'() {
        given: 'a generated collection id'
        String collectionId = UUID.randomUUID().toString()

        and: 'an existing collection'
        rekognitionCollectionService.createFaceMemoryCollection(collectionId)

        when: 'memory collection is requested at AWS'
        DescribeCollectionResponse response = rekognitionCollectionService.describeFaceMemoryCollection(collectionId)

        then: 'response collection ARN matches collection id'
        response.collectionARN().toString().split("/")[1] == collectionId
    }

    def 'a non existing collection can not be described'() {
        given: 'a generated collection id'
        String collectionId = UUID.randomUUID().toString()

        and: 'an existing collection'
        rekognitionCollectionService.createFaceMemoryCollection(collectionId)

        when: 'memory collection is requested at AWS'
        rekognitionCollectionService.describeFaceMemoryCollection(UUID.randomUUID().toString())

        then: 'en exception is thrown'
        ResourceNotFoundException exception = thrown()

        and: 'and exception contains a certain text'
        exception.getMessage().contains("not found")
    }

    def 'a list of collection is requested'() {
        given: 'a generated collection id'
        String collectionId = UUID.randomUUID().toString()

        and: 'an existing collection'
        rekognitionCollectionService.createFaceMemoryCollection(collectionId)

        when: 'a list of collections is requested'
        ListCollectionsResponse response = rekognitionCollectionService.listFaceMemoryCollections()

        then: 'created collection is within the requested list'
        response.collectionIds().stream().anyMatch(item -> item.equals(collectionId))
    }

    def 'a collection is removed'() {
        given: 'a generated collection id'
        String collectionId = UUID.randomUUID().toString()

        and: 'an existing collection'
        rekognitionCollectionService.createFaceMemoryCollection(collectionId)

        when: 'an existing collection'
        DeleteCollectionResponse response = rekognitionCollectionService.deleteFaceMemoryCollection(collectionId)

        then: 'an ok status code is returned'
        response.statusCode().toString() == "200"
    }

    def 'a non existing collection can not be removed'() {
        given: 'a generated collection id'
        String collectionId = UUID.randomUUID().toString()

        and: 'an existing collection'
        rekognitionCollectionService.createFaceMemoryCollection(collectionId)

        when: 'a collection is deleted'
        rekognitionCollectionService.deleteFaceMemoryCollection(UUID.randomUUID().toString())

        then: 'en exception is thrown'
        ResourceNotFoundException exception = thrown()

        and: 'and exception contains a certain text'
        exception.getMessage().contains("not exist")
    }
}
