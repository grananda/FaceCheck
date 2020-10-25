package com.grananda.domain

import com.fasterxml.jackson.annotation.JsonIgnore
import org.hibernate.annotations.CreationTimestamp
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.UpdateTimestamp

import javax.annotation.Nullable
import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size
import java.time.OffsetDateTime

@Entity
@Table(name = "users")
class User {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "StringSequence")
    @GenericGenerator(
            name = "StringSequence",
            strategy = "com.grananda.util.UuIdStringSequenceGenerator"
    )
    @Column(updatable = false, nullable = false)
    String id

    @Column(name = "first_name")
    @NotNull
    @Size(min = 2, max = 15)
    String firstName

    @Column(name = "last_name")
    @NotNull
    @Size(min = 2, max = 25)
    String lastName

    @Column(name = "username")
    @NotNull
    @Size(min = 5, max = 25)
    String username

    @Column(name = "email")
    @NotNull
    @Email
    @Size(min = 6, max = 50)
    String email

    @Column(name = "password")
    @NotNull
    @JsonIgnore
    String password

    @ManyToOne
    @Nullable
    @JoinColumn(name = "organization_id")
    Organization organization

    @OneToOne(mappedBy = "user")
    FaceMemory face

    @CreationTimestamp
    @Column(updatable = false, name = "created_at")
    OffsetDateTime createdAt

    @UpdateTimestamp
    @Column(name = "updated_at")
    OffsetDateTime updatedAt

    static User getInstance(params = [:]) {
        return new User(params)
    }
}
