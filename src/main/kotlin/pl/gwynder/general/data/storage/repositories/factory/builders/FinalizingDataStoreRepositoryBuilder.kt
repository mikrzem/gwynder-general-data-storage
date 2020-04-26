package pl.gwynder.general.data.storage.repositories.factory.builders

import pl.gwynder.general.data.storage.entities.DataStoreEntity
import pl.gwynder.general.data.storage.repositories.DataStoreRepository

interface FinalizingDataStoreRepositoryBuilder<Entity : DataStoreEntity> {

    fun build(): DataStoreRepository<Entity>

}
