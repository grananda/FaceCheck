package utils

import com.github.javafaker.Faker
import com.grananda.domain.Organization

class OrganizationFactory {

    static Organization create(params = [:]) {
        return Organization.getInstance([
                'name'     : params['name'] ? params['name'] : Faker.instance().company().name(),
                'deletedAt': params['deletedAt'] ? params['deletedAt'] : null,
        ])
    }

    static List<Organization> createList(int amount) {
        def list = []

        (1..amount).each {
            list << create()
        }

        return list
    }
}
