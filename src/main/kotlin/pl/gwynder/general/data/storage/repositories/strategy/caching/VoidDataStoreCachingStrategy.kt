package pl.gwynder.general.data.storage.repositories.strategy.caching

import pl.gwynder.general.commons.base.BaseService
import pl.gwynder.general.data.storage.entities.DataStoreContext
import pl.gwynder.general.data.storage.entities.DataStoreEntity
import pl.gwynder.general.data.storage.errors.CacheError
import pl.gwynder.general.data.storage.repositories.strategy.reading.DataStoreReadingStrategy
import java.util.*

class VoidDataStoreCachingStrategy<Entity : DataStoreEntity>(
    override val context: DataStoreContext<Entity>
) : BaseService(), DataStoreCachingStrategy<Entity> {

    override fun allLoaded(): Boolean {
        return false
    }

    override fun singleLoaded(id: String): Boolean {
        return false
    }

    override fun all(): Set<Entity> {
        throw CacheError("Void strategy")
    }

    override fun single(id: String): Optional<Entity> {
        throw CacheError("Void strategy")
    }

    override fun set(all: Set<Entity>) {
    }

    override fun set(id: String, entity: Entity) {
    }

    override fun remove(id: String) {
    }

    override fun initialize(reading: DataStoreReadingStrategy<Entity>) {
    }

}
