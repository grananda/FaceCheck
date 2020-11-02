package com.grananda.domain

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import java.time.OffsetDateTime

@Entity
@Table(name = "organizations")
class Organization {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "UUIDStringSequence")
    @GenericGenerator(
            name = "UUIDStringSequence",
            strategy = "com.grananda.util.UuIdStringSequenceGenerator"
    )
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    String id

    @Column(name = "name")
    @NotNull
    @Size(min = 2, max = 50)
    String name

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
    Set<User> users = new HashSet<>()

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
    Set<FaceMemoryCollection> memoryCollections = new HashSet<>()

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    OffsetDateTime createdAt

    @UpdateTimestamp
    @Column(name = "updated_at")
    OffsetDateTime updatedAt

    @Column(name = "deleted_at")
    OffsetDateTime deletedAt

    static Organization getInstance(params = [:]) {
        return new Organization(params)
    }
}
