package utils

import software.amazon.awssdk.services.rekognition.model.FaceRecord

class FaceRecordFactory {

    static FaceRecord create() {
        return FaceRecord.builder()
                .face(FaceFactory.create())
                .build()
    }
}
