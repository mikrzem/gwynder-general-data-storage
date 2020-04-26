package pl.gwynder.general.data.storage.repositories.strategy.caching

import pl.gwynder.general.data.storage.entities.DataStoreEntity

interface DataStoreCachingStrategy<Entity : DataStoreEntity> {

    fun allLoaded(): Boolean

    fun singleLoaded(id: String): Boolean

    fun all(): Set<Entity>

    fun single(id: String): Entity

    fun set(all: Set<Entity>)

    fun set(id: String, entity: Entity)

    fun remove(id: String)

}
