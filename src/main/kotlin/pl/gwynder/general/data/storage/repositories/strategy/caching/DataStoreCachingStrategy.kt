package pl.gwynder.general.data.storage.repositories.strategy.caching

import pl.gwynder.general.data.storage.entities.DataStoreEntity
import pl.gwynder.general.data.storage.repositories.strategy.DataStoreStrategy
import pl.gwynder.general.data.storage.repositories.strategy.reading.DataStoreReadingStrategy
import java.util.*

interface DataStoreCachingStrategy<Entity : DataStoreEntity> : DataStoreStrategy<Entity> {

    fun allLoaded(): Boolean

    fun singleLoaded(id: String): Boolean

    fun all(): Set<Entity>

    fun single(id: String): Optional<Entity>

    fun set(all: Set<Entity>)

    fun set(id: String, entity: Entity)

    fun remove(id: String)

    fun initialize(reading: DataStoreReadingStrategy<Entity>)

}
