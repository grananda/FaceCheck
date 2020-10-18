package com.grananda.domain

import javax.persistence.*
import javax.validation.constraints.Email
import javax.validation.constraints.NotNull
import javax.validation.constraints.Size

@Entity
class User implements BaseEntity {

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
    @Size(min = 5, max = 15)
    String username

    @Column(name = "email")
    @NotNull
    @Email
    @Size(min = 6, max = 50)
    String email

    @Column(name = "password")
    @NotNull
    @Size(min = 8, max = 50)
    String password

    @ManyToOne
    @JoinColumn(name = "organization_id")
    Organization organization

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "face_memory_id")
    FaceMemory face

    static User getInstance(params = [:]) {
        return new User(params)
    }
}
