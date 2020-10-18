package com.grananda.services

import groovy.util.logging.Slf4j
import software.amazon.awssdk.core.SdkBytes
import software.amazon.awssdk.services.rekognition.model.Image
import software.amazon.awssdk.services.rekognition.model.ListCollectionsResponse
import spock.lang.Specification

import javax.inject.Inject

@Slf4j
class BaseAwsRekognitionTest extends Specification {

    @Inject
    AwsRekognitionCollectionService rekognitionCollectionService;

    void setup() {
        ListCollectionsResponse response = rekognitionCollectionService.listFaceMemoryCollections()

        for (String collectionId : response.collectionIds()) {
            log.info("DELETING COLLECTION: " + collectionId);
            rekognitionCollectionService.deleteFaceMemoryCollection(collectionId);
        }
    }

    protected String createCollection() {
        String collectionId = UUID.randomUUID().toString()

        rekognitionCollectionService.createFaceMemoryCollection(collectionId)

        return collectionId
    }

    protected Image processSourceImage(String sourceImagePath) throws IOException {
        String path = "src/test/resources/";

        File file = new File(path + sourceImagePath)
        InputStream sourceStream = new FileInputStream(file)
        SdkBytes sourceBytes = SdkBytes.fromInputStream(sourceStream)

        return Image
                .builder()
                .bytes(sourceBytes)
                .build();
    }
}
