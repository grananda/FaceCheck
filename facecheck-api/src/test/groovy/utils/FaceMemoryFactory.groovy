package utils

import com.grananda.domain.FaceMemory

class FaceMemoryFactory {

    static FaceMemory create() {
        return FaceMemory.getInstance([
                faceId: UUID.randomUUID().toString()
        ])
    }
}
