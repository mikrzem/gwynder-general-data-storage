package pl.gwynder.general.data.storage.parsers

import pl.gwynder.general.commons.base.BaseService
import java.util.*

class UUIDIdGenerator : BaseService(), IdGenerator {

    override fun newId(): String {
        return UUID.randomUUID().toString().replace("-", "")
    }

}
