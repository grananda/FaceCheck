package com.grananda.domain

import javax.persistence.*
import javax.validation.constraints.NotNull

@Entity
class FaceMemoryCollection implements BaseEntity {

    @NotNull
    @Column(name = "name")
    String name

    @NotNull
    @Column(name = "collection_id")
    String collectionId

    @NotNull
    @Column(name = "collection_arn")
    String collectionArn

    @ManyToOne
    @JoinColumn(name = "organization_id")
    Organization organization

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "collection")
    Set<FaceMemory> faces = new HashSet<>()

    static FaceMemoryCollection getInstance(params = [:]) {
        return new FaceMemoryCollection(params)
    }
}
