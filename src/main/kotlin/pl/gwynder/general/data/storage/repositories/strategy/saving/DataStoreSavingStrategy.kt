package pl.gwynder.general.data.storage.repositories.strategy.saving

import pl.gwynder.general.data.storage.entities.DataStoreEntity

interface DataStoreSavingStrategy<Entity : DataStoreEntity> {

    fun create(entity: Entity): Entity

    fun update(id: String, entity: Entity): Entity

    fun remove(id: String)

}
