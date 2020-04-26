package pl.gwynder.general.data.storage.repositories.strategy.reading

import pl.gwynder.general.data.storage.entities.DataStoreEntity
import pl.gwynder.general.data.storage.repositories.strategy.DataStoreStrategy
import java.util.*

interface DataStoreReadingStrategy<Entity : DataStoreEntity> : DataStoreStrategy<Entity> {

    fun all(): Set<Entity>

    fun single(id: String): Optional<Entity>

}
