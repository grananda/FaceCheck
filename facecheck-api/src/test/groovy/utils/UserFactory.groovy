package utils

import com.github.javafaker.Faker
import com.grananda.domain.User

class UserFactory {

    static User create() {
        return User.getInstance([
                email    : Faker.instance().internet().emailAddress(),
                firstName: Faker.instance().name().firstName(),
                lastName : Faker.instance().name().lastName(),
                username : Faker.instance().lorem().word(),
                password : Faker.instance().internet().password(),
        ])
    }
}
