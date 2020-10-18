package com.grananda.services

import groovy.transform.CompileStatic
import software.amazon.awssdk.services.rekognition.RekognitionClient
import software.amazon.awssdk.services.rekognition.model.*

import javax.inject.Inject

@CompileStatic
class AwsRekognitionCollectionServiceImpl implements AwsRekognitionCollectionService {

    @Inject
    RekognitionClient client

    @Override
    CreateCollectionResponse createFaceMemoryCollection(String collectionId) {
        CreateCollectionRequest request = CreateCollectionRequest
                .builder()
                .collectionId(collectionId)
                .build() as CreateCollectionRequest

        return client.createCollection(request);
    }

    @Override
    DescribeCollectionResponse describeFaceMemoryCollection(String collectionId) {
        DescribeCollectionRequest request = DescribeCollectionRequest
                .builder()
                .collectionId(collectionId)
                .build() as DescribeCollectionRequest

        return client.describeCollection(request)
    }

    @Override
    ListCollectionsResponse listFaceMemoryCollections() {
        ListCollectionsRequest request = ListCollectionsRequest
                .builder()
                .build() as ListCollectionsRequest;

        return client.listCollections(request)
    }

    @Override
    DeleteCollectionResponse deleteFaceMemoryCollection(String collectionId) {
        DeleteCollectionRequest request = DeleteCollectionRequest
                .builder()
                .collectionId(collectionId)
                .build() as DeleteCollectionRequest

        return client.deleteCollection(request)
    }
}
