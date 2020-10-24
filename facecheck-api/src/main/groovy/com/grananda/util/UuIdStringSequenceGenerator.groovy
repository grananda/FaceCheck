package com.grananda.util

import com.devskiller.friendly_id.FriendlyId
import org.hibernate.HibernateException
import org.hibernate.engine.spi.SharedSessionContractImplementor
import org.hibernate.id.enhanced.SequenceStyleGenerator

//https://thorben-janssen.com/custom-sequence-based-idgenerator/
class UuIdStringSequenceGenerator extends SequenceStyleGenerator {

    @Override
    Serializable generate(SharedSessionContractImplementor session, Object object) throws HibernateException {
        return FriendlyId.toFriendlyId(UUID.randomUUID())
    }
}
