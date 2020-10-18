package com.grananda.domain

import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Type
import org.hibernate.annotations.UpdateTimestamp

import javax.persistence.*
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import java.time.OffsetDateTime

@Entity
class Organization {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "org.hibernate.type.UUIDCharType")
    @Column(length = 36, columnDefinition = "varchar", updatable = false, nullable = false)
    UUID id

    @Column(name = "name")
    @NotNull
    @Size(min = 2, max = 30)
    String name

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
    Set<User> users = new HashSet<>()

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "organization")
    Set<FaceMemoryCollection> memoryCollections = new HashSet<>()

    @Version
    Long version

    @CreationTimestamp
    @Column(updatable = false)
    OffsetDateTime createdAt

    @UpdateTimestamp
    OffsetDateTime updatedAt

    static Organization getInstance(params = [:]) {
        return new Organization(params)
    }
}
