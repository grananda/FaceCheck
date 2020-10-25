package com.grananda.domain

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp

import javax.persistence.*
import javax.validation.constraints.NotNull
import java.time.OffsetDateTime

@Entity
@Table(name = "face_memories")
class FaceMemory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "StringSequence")
    @GenericGenerator(
            name = "StringSequence",
            strategy = "com.grananda.util.UuIdStringSequenceGenerator"
    )
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    String id

    @NotNull
    @Column(name = "face_id")
    String faceId

    @ManyToOne
    @JoinColumn(name = "collection_id")
    FaceMemoryCollection collection

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    User user

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    OffsetDateTime createdAt

    @UpdateTimestamp
    @Column(name = "updated_at")
    OffsetDateTime updatedAt

    static FaceMemory getInstance(params = [:]) {
        return new FaceMemory(params)
    }

}
