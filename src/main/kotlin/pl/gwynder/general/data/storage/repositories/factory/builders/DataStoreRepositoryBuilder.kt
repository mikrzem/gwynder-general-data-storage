package pl.gwynder.general.data.storage.repositories.factory.builders

import pl.gwynder.general.data.storage.entities.DataStoreEntity

interface DataStoreRepositoryBuilder<Entity : DataStoreEntity> {

    fun byStrategy(): StrategyDataStoreRepositoryBuilder<Entity>

}
