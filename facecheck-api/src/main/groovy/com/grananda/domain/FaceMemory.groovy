package com.grananda.domain

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class FaceMemory implements BaseEntity {

    @NotNull
    @Column(name = "face_id")
    String faceId

    @ManyToOne
    @JoinColumn(name = "collection_id")
    FaceMemoryCollection collection

    @OneToOne(mappedBy = "face")
    User user

    static FaceMemory getInstance(params = [:]) {
        return new FaceMemory(params)
    }
}
