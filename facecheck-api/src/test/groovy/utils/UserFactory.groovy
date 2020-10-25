package utils

import com.github.javafaker.Faker
import com.grananda.domain.User

class UserFactory {

    static User create(params = [:]) {
        return User.getInstance([
                email    : Faker.instance().internet().emailAddress(),
                firstName: Faker.instance().name().firstName(),
                lastName : Faker.instance().name().lastName(),
                username : params['username'] ? params['username'] : Faker.instance().lorem().characters(10),
                password : params['password'] ? params['password'] : Faker.instance().internet().password(),
        ])
    }
}
