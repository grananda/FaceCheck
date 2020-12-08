package com.grananda.services

import javax.inject.Singleton

import groovy.transform.CompileStatic
import software.amazon.awssdk.services.rekognition.model.*

@CompileStatic
@Singleton
interface AwsRekognitionFaceService {
    DetectFacesResponse detectFaces(Image image)

    IndexFacesResponse indexFace(String collectionId, Image image)

    SearchFacesByImageResponse identifyFace(String collectionId, Image image)

    SearchFacesResponse searchFace(String collectionId, String faceId)

    SearchFacesByImageResponse searchImage(String collectionId, Image image)

    DeleteFacesResponse forgetFace(String collectionId, String faceId)

    DeleteFacesResponse forgetFaces(String collectionId, Collection<String> faceIds)

    ListFacesResponse listCollectionFaces(String collectionId)
}
