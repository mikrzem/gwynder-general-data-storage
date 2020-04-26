package pl.gwynder.general.data.storage.repositories.strategy.caching

import pl.gwynder.general.commons.base.BaseService
import pl.gwynder.general.data.storage.entities.DataStoreContext
import pl.gwynder.general.data.storage.entities.DataStoreEntity
import pl.gwynder.general.data.storage.errors.CacheError
import pl.gwynder.general.data.storage.repositories.strategy.reading.DataStoreReadingStrategy
import java.util.*
import kotlin.collections.HashMap

class HashMapDataStoreCachingStrategy<Entity : DataStoreEntity>(
    override val context: DataStoreContext<Entity>,
    private val loadOnInitialize: Boolean = false
) : BaseService(), DataStoreCachingStrategy<Entity> {

    private var loaded: Boolean = false
    private val cache: MutableMap<String, Entity> = HashMap()

    override fun allLoaded(): Boolean {
        return loaded
    }

    override fun singleLoaded(id: String): Boolean {
        return loaded
    }

    override fun all(): Set<Entity> {
        if (!loaded) {
            throw CacheError("Cache not loaded")
        }
        return cache.values.toSet()
    }

    override fun single(id: String): Optional<Entity> {
        if (!loaded) {
            throw CacheError("Cache not loaded")
        }
        return Optional.ofNullable(cache[id])
    }

    @Synchronized
    override fun set(all: Set<Entity>) {
        cache.clear()
        for (entity in all) {
            val id = entity.id
            if (id != null) {
                set(id, entity)
            } else {
                throw CacheError("Attempt to cache entity without id")
            }
        }
    }

    @Synchronized
    override fun set(id: String, entity: Entity) {
        cache[id] = entity
    }

    @Synchronized
    override fun remove(id: String) {
        cache.remove(id)
    }

    override fun initialize(reading: DataStoreReadingStrategy<Entity>) {
        if (loadOnInitialize) {
            set(reading.all())
        }
    }

}
