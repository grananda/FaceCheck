package utils

import com.grananda.domain.FaceMemory

class FaceMemoryFactory {

    static FaceMemory create(params = [:]) {
        return FaceMemory.getInstance([
                id        : UUID.randomUUID().toString(),
                faceId    : UUID.randomUUID().toString(),
                collection: params['collection'] ? params['collection'] : FaceMemoryCollectionFactory.create(),
                user      : params['user'] ? params['user'] : UserFactory.create(),
        ])
    }
}
