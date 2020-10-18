package com.grananda.services

import com.grananda.domain.FaceMemory
import com.grananda.domain.FaceMemoryCollection
import com.grananda.domain.User
import com.grananda.exceptions.FaceAlreadyInCollectionException
import com.grananda.exceptions.FaceNotInCollectionException
import com.grananda.exceptions.MissingFaceInImageException
import com.grananda.exceptions.MultipleFacesInImageException
import com.grananda.repositories.FaceMemoryRepository
import io.micronaut.test.annotation.MockBean
import io.micronaut.test.extensions.spock.annotation.MicronautTest
import software.amazon.awssdk.services.rekognition.model.*
import spock.lang.Specification
import utils.*

import javax.inject.Inject

@MicronautTest(startApplication = false)
class FaceMemoryServiceImplTest extends Specification {

    @Inject
    AwsRekognitionFaceService awsRekognitionFaceService

    @MockBean(AwsRekognitionFaceServiceImpl)
    AwsRekognitionFaceService mockAwsRekognitionFaceService() {
        Mock(AwsRekognitionFaceService)
    }

    @Inject
    FaceMemoryRepository faceMemoryRepository

    @MockBean(FaceMemoryRepository)
    FaceMemoryRepository mockFaceMemoryRepository() {
        Mock(FaceMemoryRepository)
    }

    @Inject
    FaceMemoryService faceMemoryService

    def 'a face is stored in a collection'() {
        given: 'a set of instances'
        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollectionFactory.create()
        User user = UserFactory.create()
        Image image = ImageFactory.create("assets/image1a.jpg")
        FaceRecord faceRecord = FaceRecordFactory.create()

        and: 'an index faces expected response'
        IndexFacesResponse indexFacesResponse = IndexFacesResponse.builder()
                .faceRecords(faceRecord)
                .build() as IndexFacesResponse

        and: 'a detect faces expected response'
        Set<FaceDetail> faceDetailList = new HashSet<>()
        faceDetailList.add(FaceDetailFactory.create())

        DetectFacesResponse detectFacesResponse = DetectFacesResponse.builder()
                .faceDetails(faceDetailList)
                .build() as DetectFacesResponse

        and: 'a search face image response'
        SearchFacesByImageResponse searchFacesByImageResponse = SearchFacesByImageResponse
                .builder()
                .build() as SearchFacesByImageResponse

        when: 'a face is stored'
        FaceMemory faceMemory = faceMemoryService.storeFacialMemory(user, faceMemoryCollection, image)

        then: 'the following mock interactions occur'
        1 * awsRekognitionFaceService.detectFaces(image) >> detectFacesResponse
        1 * awsRekognitionFaceService.searchImage(faceMemoryCollection.getCollectionId(), image) >> searchFacesByImageResponse
        1 * awsRekognitionFaceService.indexFace(faceMemoryCollection.getCollectionId(), image) >> indexFacesResponse

        and: 'and following assertions occur'
        faceMemory.getCollection().getCollectionId() == faceMemoryCollection.getCollectionId()
        faceMemory.getFaceId() == faceRecord.face().faceId()
        faceMemory.getUser().getEmail() == user.getEmail()
    }

    def 'a non face image is not stored'() {
        given: 'a set of instances'
        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollectionFactory.create()
        User user = UserFactory.create()
        Image image = ImageFactory.create("assets/image4a.jpg")

        and: 'a detect faces expected response'
        DetectFacesResponse detectFacesResponse = DetectFacesResponse.builder()
                .faceDetails(new HashSet<>())
                .build() as DetectFacesResponse

        when: 'an image is stored'
        faceMemoryService.storeFacialMemory(user, faceMemoryCollection, image)

        then: 'the following mock interactions occurs'
        awsRekognitionFaceService.detectFaces(image) >> detectFacesResponse

        and: 'an exception is thrown'
        MissingFaceInImageException exception = thrown()

        and: 'the exception contains a certain text'
        exception.getMessage().contains("FaceMemoryException:FaceMemoryService:storeFacialMemory:")
    }

    def 'an image with multiple faces is not stored'() {
        given: 'a set of instances'
        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollectionFactory.create()
        User user = UserFactory.create()
        Image image = ImageFactory.create("assets/image3a.jpg")
        FaceRecord faceRecord = FaceRecordFactory.create()

        and: 'an index faces expected response'
        IndexFacesResponse indexFacesResponse = IndexFacesResponse.builder()
                .faceRecords(faceRecord)
                .build() as IndexFacesResponse

        and: 'a detect faces expected response'
        FaceDetail faceDetail1 = FaceDetailFactory.create()
        FaceDetail faceDetail2 = FaceDetailFactory.create()

        Set<FaceDetail> faceDetailList = new HashSet<>()
        faceDetailList.add(faceDetail1)
        faceDetailList.add(faceDetail2)

        DetectFacesResponse detectFacesResponse = DetectFacesResponse.builder()
                .faceDetails(faceDetailList)
                .build() as DetectFacesResponse

        when: 'an image is stored'
        faceMemoryService.storeFacialMemory(user, faceMemoryCollection, image)

        then: 'the following mock interactions occurs'
        awsRekognitionFaceService.detectFaces(image) >> detectFacesResponse

        awsRekognitionFaceService.indexFace(faceMemoryCollection.getCollectionId(), image) >> indexFacesResponse

        and: 'an exception is thrown'
        MultipleFacesInImageException exception = thrown()

        and: 'the exception contains a certain text'
        exception.getMessage().contains("FaceMemoryException:FaceMemoryService:storeFacialMemory:")
    }

    def 'an face can not be stored multiple times once indexed'() {
        given: 'a set of instances'
        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollectionFactory.create()
        User user = UserFactory.create()
        Image image = ImageFactory.create("assets/image1a.jpg")
        FaceMatch faceMatch = FaceMatchFactory.create()

        and: 'an search faces expected response'
        SearchFacesByImageResponse searchFacesByImageResponse = SearchFacesByImageResponse.builder()
                .faceMatches(faceMatch)
                .build() as SearchFacesByImageResponse


        FaceDetail faceDetail = FaceDetailFactory.create()

        Set<FaceDetail> faceDetailList = new HashSet<>()
        faceDetailList.add(faceDetail)

        DetectFacesResponse detectFacesResponse = DetectFacesResponse.builder()
                .faceDetails(faceDetailList)
                .build() as DetectFacesResponse

        when: 'an image is stored'
        faceMemoryService.storeFacialMemory(user, faceMemoryCollection, image)

        then: 'the following mock interactions occurs'
        awsRekognitionFaceService.searchImage(faceMemoryCollection.getCollectionId(), image) >> searchFacesByImageResponse
        awsRekognitionFaceService.detectFaces(image) >> detectFacesResponse

        and: 'an exception is thrown'
        FaceAlreadyInCollectionException exception = thrown()

        and: 'the exception contains a certain text'
        exception.getMessage().contains("FaceMemoryException:FaceMemoryService:storeFacialMemory:")
    }

    def 'a_face_is_removed_from_a_collection'() {
        given: 'a set of instances'
        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollectionFactory.create()
        FaceMemory faceMemory = FaceMemoryFactory.create()

        and: 'an delete faces expected response'
        faceMemory.setCollection(faceMemoryCollection)
        faceMemoryCollection.getFaces().add(faceMemory)

        DeleteFacesResponse deleteFacesResponse = DeleteFacesResponse.builder()
                .deletedFaces(faceMemory.getFaceId())
                .build() as DeleteFacesResponse

        Face face = Face.builder()
                .faceId(faceMemory.getFaceId())
                .build()

        and: 'an list faces expected response'
        ListFacesResponse listFacesResponse = ListFacesResponse.builder()
                .faces(face)
                .build() as ListFacesResponse

        when: 'a face is removed'
        boolean response = faceMemoryService.removeFacialMemory(faceMemory)

        then: 'the following mock interactions occurs'
        awsRekognitionFaceService.listCollectionFaces(faceMemoryCollection.getCollectionId()) >> listFacesResponse
        awsRekognitionFaceService.forgetFace(faceMemory.getFaceId(), faceMemoryCollection.getCollectionId()) >> deleteFacesResponse

        and: 'the response is true'
        response
    }

    def 'a non existing face can not be removed from a collection'() {
        given: 'a set of instances'
        FaceMemoryCollection faceMemoryCollection = FaceMemoryCollectionFactory.create()
        FaceMemory faceMemory = FaceMemoryFactory.create()

        faceMemory.setCollection(faceMemoryCollection)
        faceMemoryCollection.getFaces().add(faceMemory)

        when: 'a face is removed'
        faceMemoryService.removeFacialMemory(faceMemory)

        then: 'the following mock interactions occurs'
        awsRekognitionFaceService.searchFace(faceMemoryCollection.getCollectionId(), faceMemory.getFaceId()) >> {
            throw InvalidParameterException::builder().build()
        }

        and: 'an exception is thrown'
        FaceNotInCollectionException exception = thrown()

        and: 'the exception contains a certain text'
        exception.getMessage().contains("FaceMemoryException:FaceMemoryService:removeFacialMemory:")
    }
}
