package com.grananda.domain

import java.time.OffsetDateTime
import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp

@Entity
@Table(name = "face_memory_collections")
class FaceMemoryCollection {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UUIDStringSequence")
    @GenericGenerator(
            name = "UUIDStringSequence",
            strategy = "com.grananda.util.UuIdStringSequenceGenerator"
    )
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    String id

    @NotNull
    @Column(name = "name")
    @Size(min = 2, max = 50)
    String name

    @NotNull
    @Column(name = "collection_id")
    @Size(min = 2, max = 50)
    String collectionId

    @NotNull
    @Column(name = "collection_arn")
    @Size(min = 2, max = 50)
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

    void addOrganization(Organization organization) {
        this.organization = organization
        organization.memoryCollections.add(this)
    }
}
