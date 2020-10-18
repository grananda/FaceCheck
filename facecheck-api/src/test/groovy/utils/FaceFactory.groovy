package utils

import software.amazon.awssdk.services.rekognition.model.Face

class FaceFactory {

    static Face create(String faceId) {
        return Face.builder()
                .faceId(faceId)
                .build()
    }

    static Face create() {
        return Face.builder()
                .faceId(UUID.randomUUID().toString())
                .build()
    }
}
