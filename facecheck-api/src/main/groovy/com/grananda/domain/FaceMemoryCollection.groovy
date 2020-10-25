package com.grananda.domain

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import org.hibernate.annotations.UpdateTimestamp

import javax.persistence.*
import javax.validation.constraints.NotNull
import java.time.OffsetDateTime

@Entity
@Table(name = "face_memorie_collections")
class FaceMemoryCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UUIDStringSequence")
    @GenericGenerator(
            name = "UUIDStringSequence",
            strategy = "com.grananda.util.UuIdStringSequenceGenerator"
    )
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    String id

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

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    OffsetDateTime createdAt

    @UpdateTimestamp
    @Column(name = "updated_at")
    OffsetDateTime updatedAt

    static FaceMemoryCollection getInstance(params = [:]) {
        return new FaceMemoryCollection(params)
    }
}
