package com.grananda.services

import io.micronaut.test.extensions.spock.annotation.MicronautTest
import software.amazon.awssdk.services.rekognition.model.*

import javax.inject.Inject

@MicronautTest(startApplication = false)
class AwsRekognitionFaceServiceImplTest extends BaseAwsRekognitionTest {

    @Inject
    AwsRekognitionFaceService rekognitionFaceService

    def 'a face is detected'() {
        given: 'an image with a face'
        Image image = processSourceImage('assets/image1a.jpg')

        when: 'an image is scanned for faces'
        DetectFacesResponse response = rekognitionFaceService.detectFaces(image)

        then: 'one face is detected'
        response.faceDetails().size() == 1
    }

    def 'a face is not detected'() {
        given: 'an image with no a face'
        Image image = processSourceImage('assets/image4a.jpg')

        when: 'an image is scanned for faces'
        DetectFacesResponse response = rekognitionFaceService.detectFaces(image)

        then: 'no face is detected'
        response.faceDetails().size() == 0
    }

    def 'multiple faces are detected'() {
        given: 'an image with multiple a face'
        Image image = processSourceImage('assets/image3a.jpg')

        when: 'an image is scanned for faces'
        DetectFacesResponse response = rekognitionFaceService.detectFaces(image)

        then: 'multiple faces are detected'
        response.faceDetails().size() == 9
    }

    def 'a face is indexed'() {
        given: 'an image with a face'
        Image image = processSourceImage('assets/image1a.jpg')

        and: 'a collection of images'
        String collectionId = createCollection()

        when: 'and image is index within the collection'
        IndexFacesResponse response = rekognitionFaceService.indexFace(collectionId, image)

        then: 'the image is reported to be indexed'
        response.faceRecords().size() == 1
    }

    def 'an image without face is not indexed'() {
        given: 'an image with no face'
        Image image = processSourceImage('assets/image4a.jpg')

        and: 'a collection of images'
        String collectionId = createCollection()

        when: 'and image is index within the collection'
        IndexFacesResponse response = rekognitionFaceService.indexFace(collectionId, image)

        then: 'the image is reported not to be indexed'
        response.faceRecords().size() == 0
    }

    def 'a face is identified'() {
        given: 'and image to be indexed'
        Image image = processSourceImage('assets/image1a.jpg')

        and: 'an image to identify'
        Image imageToIdentify = processSourceImage('assets/image1b.jpg')

        and: 'a collection of images'
        String collectionId = createCollection()

        and: 'the base image is indexed'
        rekognitionFaceService.indexFace(collectionId, image)

        when: 'the image is asked to de identified within the collection'
        SearchFacesByImageResponse response = rekognitionFaceService.identifyFace(collectionId, imageToIdentify)

        then: 'the image is found with high probability'
        response.faceMatches().iterator().next().face().confidence() > 90
    }

    def 'a non indexed face is not identified'() {
        given: 'and image to be indexed'
        Image image = processSourceImage('assets/image1a.jpg')

        and: 'an image to identify'
        Image unIndexedImage = processSourceImage('assets/image2a.jpg')

        and: 'a collection of images'
        String collectionId = createCollection()

        and: 'the base image is indexed'
        rekognitionFaceService.indexFace(collectionId, image)

        when: 'the image is asked to de identified within the collection'
        SearchFacesByImageResponse response = rekognitionFaceService.identifyFace(collectionId, unIndexedImage)

        then: 'the image is not found'
        response.faceMatches().size() == 0
    }

    void 'a face is found by id'() {
        given: 'and image to be indexed'
        Image image = processSourceImage('assets/image1a.jpg')

        and: 'a collection of images'
        String collectionId = createCollection()

        and: 'the base image is indexed'
        IndexFacesResponse indexFacesResponse = rekognitionFaceService.indexFace(collectionId, image)

        when: 'the same image is searched by id'
        String faceId = indexFacesResponse.faceRecords().iterator().next().face().faceId()

        SearchFacesResponse response = rekognitionFaceService.searchFace(collectionId, faceId)

        then: 'the image is reported to be found'
        response.hasFaceMatches()
    }

    void 'a face is not found by id'() {
        given: 'and image to be indexed'
        Image image = processSourceImage('assets/image1a.jpg')

        and: 'a collection of images'
        String collectionId = createCollection()

        and: 'the base image is indexed'
        rekognitionFaceService.indexFace(collectionId, image)

        when: 'the same image is searched by id random id'
        String faceId = UUID.randomUUID().toString()

        rekognitionFaceService.searchFace(collectionId, faceId)

        then: 'and exception is thrown'
        InvalidParameterException exception = thrown()

        and: 'returned code is 400'
        exception.getMessage().contains('400')
    }

    void 'a face is found by image'() {
        given: 'and image to be indexed'
        Image image = processSourceImage('assets/image1a.jpg')

        and: 'a collection of images'
        String collectionId = createCollection()

        and: 'the base image is indexed'
        rekognitionFaceService.indexFace(collectionId, image)

        when: 'the same image is search by image'
        SearchFacesByImageResponse response = rekognitionFaceService.searchImage(collectionId, image)

        then: 'the image is found'
        response.faceMatches().size() > 0
    }

    void 'a non-indexed face is not found by image'() {
        given: 'and image to be indexed'
        Image image = processSourceImage('assets/image1a.jpg')

        and: 'a collection of images'
        String collectionId = createCollection()

        when: 'the same image is search by image'
        SearchFacesByImageResponse response = rekognitionFaceService.searchImage(collectionId, image)

        then: 'the image is not found'
        response.faceMatches().size() == 0
    }

    void 'a_face_is_forgotten'() {
        given: 'and image to be indexed'
        Image image = processSourceImage('assets/image1a.jpg')

        and: 'a collection of images'
        String collectionId = createCollection()

        and: 'the base image is indexed'
        IndexFacesResponse indexFacesResponse = rekognitionFaceService.indexFace(collectionId, image)

        when: 'the image is requested to be forgotten'
        String faceId = indexFacesResponse.faceRecords().iterator().next().face().faceId()
        DeleteFacesResponse response = rekognitionFaceService.forgetFace(collectionId, faceId)

        then: 'the image is forgotten'
        response.hasDeletedFaces()
    }

    void 'a list of faces are forgotten'() throws IOException {
        given: 'a set of images to be indexed'
        Image image1 = processSourceImage('assets/image1a.jpg')

        Image image2 = processSourceImage('assets/image2a.jpg')

        and: 'a collection of images'
        String collectionId = createCollection()

        and: 'the images are indexed'
        IndexFacesResponse indexFacesResponse1 = rekognitionFaceService.indexFace(collectionId, image1)

        Face face1 = indexFacesResponse1.faceRecords().iterator().next().face()

        String imageId1 = face1.faceId()

        IndexFacesResponse indexFacesResponse2 = rekognitionFaceService.indexFace(collectionId, image2)

        Face face2 = indexFacesResponse2.faceRecords().iterator().next().face()

        String imageId2 = face2.faceId()

        Collection<String> faces = new ArrayList<>()
        faces.add(imageId1)
        faces.add(imageId2)

        when: 'all images are asked to be forgotten'
        DeleteFacesResponse response = rekognitionFaceService.forgetFaces(collectionId, faces)

        then: 'images are reported to be forgotten'
        response.hasDeletedFaces()
    }

    void 'a list of faces per collection is requested'() {
        given: 'an image to be indexed'
        Image image = processSourceImage('assets/image1a.jpg')

        and: 'a collection of images'
        String collectionId = createCollection()

        and: 'and indexed image'
        rekognitionFaceService.indexFace(collectionId, image)

        when: 'all images from a collection are requested'
        ListFacesResponse response = rekognitionFaceService.listCollectionFaces(collectionId)

        then: 'the collected is reported to have images'
        response.hasFaces()
    }
}
