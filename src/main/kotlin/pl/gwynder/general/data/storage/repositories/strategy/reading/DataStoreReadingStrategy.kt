package pl.gwynder.general.data.storage.repositories.strategy.reading

import pl.gwynder.general.data.storage.entities.DataStoreEntity
import java.util.*

interface DataStoreReadingStrategy<Entity : DataStoreEntity> {

    fun all(): Set<Entity>

    fun single(id: String): Optional<Entity>

}
