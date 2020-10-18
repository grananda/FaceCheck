package com.grananda.services

import groovy.transform.CompileStatic
import software.amazon.awssdk.services.rekognition.RekognitionClient
import software.amazon.awssdk.services.rekognition.model.*

import javax.inject.Inject

@CompileStatic
class AwsRekognitionFaceServiceImpl implements AwsRekognitionFaceService {

    @Inject
    RekognitionClient client

    @Override
    DetectFacesResponse detectFaces(Image image) {
        DetectFacesRequest request = DetectFacesRequest.builder()
                .image(image)
                .build() as DetectFacesRequest

        return client.detectFaces(request)
    }

    @Override
    IndexFacesResponse indexFace(String collectionId, Image image) {
        IndexFacesRequest request = IndexFacesRequest.builder()
                .collectionId(collectionId)
                .image(image)
                .maxFaces(1)
                .qualityFilter(QualityFilter.AUTO)
                .detectionAttributes(Attribute.ALL)
                .build() as IndexFacesRequest

        return client.indexFaces(request)
    }

    @Override
    SearchFacesByImageResponse identifyFace(String collectionId, Image image) {
        SearchFacesByImageRequest request = SearchFacesByImageRequest.builder()
                .collectionId(collectionId)
                .image(image)
                .maxFaces(1)
                .faceMatchThreshold(90F)
                .build() as SearchFacesByImageRequest

        return client.searchFacesByImage(request)
    }

    @Override
    SearchFacesResponse searchFace(String collectionId, String faceId) {
        SearchFacesRequest request = SearchFacesRequest.builder()
                .collectionId(collectionId)
                .faceId(faceId)
                .build() as SearchFacesRequest

        return client.searchFaces(request)
    }

    @Override
    SearchFacesByImageResponse searchImage(String collectionId, Image image) {
        SearchFacesByImageRequest request = SearchFacesByImageRequest.builder()
                .collectionId(collectionId)
                .image(image)
                .build() as SearchFacesByImageRequest

        return client.searchFacesByImage(request)
    }

    @Override
    DeleteFacesResponse forgetFace(String collectionId, String faceId) {
        DeleteFacesRequest request = DeleteFacesRequest.builder()
                .collectionId(collectionId)
                .faceIds(faceId)
                .build() as DeleteFacesRequest

        return client.deleteFaces(request)
    }

    @Override
    DeleteFacesResponse forgetFaces(String collectionId, Collection<String> faceIds) {
        DeleteFacesRequest request = DeleteFacesRequest.builder()
                .collectionId(collectionId)
                .faceIds(faceIds)
                .build() as DeleteFacesRequest

        return client.deleteFaces(request)
    }

    @Override
    ListFacesResponse listCollectionFaces(String collectionId) {
        ListFacesRequest request = ListFacesRequest.builder()
                .collectionId(collectionId)
                .build() as ListFacesRequest

        return client.listFaces(request)
    }
}
