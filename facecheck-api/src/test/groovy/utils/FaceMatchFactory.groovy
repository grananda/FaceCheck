package utils

import software.amazon.awssdk.services.rekognition.model.FaceMatch

class FaceMatchFactory {

    static FaceMatch create() {
        return FaceMatch.builder()
                .face(FaceFactory.create())
                .build()
    }
}
