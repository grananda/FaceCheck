package utils

import com.github.javafaker.Faker
import com.grananda.domain.FaceMemoryCollection

class FaceMemoryCollectionFactory {

    static FaceMemoryCollection create() {
        return FaceMemoryCollection.getInstance([
                collectionId : UUID.randomUUID().toString(),
                name         : Faker.instance().lorem().word(),
                collectionArn: Faker.instance().lorem().word(),
                faces        : new HashSet<>(),
        ])
    }
}
