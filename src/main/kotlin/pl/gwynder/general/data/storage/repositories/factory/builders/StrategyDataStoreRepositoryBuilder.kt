package pl.gwynder.general.data.storage.repositories.factory.builders

import pl.gwynder.general.data.storage.entities.DataStoreContext
import pl.gwynder.general.data.storage.entities.DataStoreEntity
import pl.gwynder.general.data.storage.repositories.DataStoreRepository
import pl.gwynder.general.data.storage.repositories.strategy.caching.DataStoreCachingStrategy
import pl.gwynder.general.data.storage.repositories.strategy.reading.DataStoreReadingStrategy
import pl.gwynder.general.data.storage.repositories.strategy.saving.DataStoreSavingStrategy

interface StrategyDataStoreRepositoryBuilder<Entity : DataStoreEntity> : FinalizingDataStoreRepositoryBuilder<Entity> {

    fun withSingleFile(): StrategyDataStoreRepositoryBuilder<Entity>

    fun withMultiFile(): StrategyDataStoreRepositoryBuilder<Entity>

    fun withVoidCache(): StrategyDataStoreRepositoryBuilder<Entity>

    fun withHashMapCache(loadOnInitialize: Boolean = false): StrategyDataStoreRepositoryBuilder<Entity>

    fun buildCustom(
        creator: (
            reading: DataStoreReadingStrategy<Entity>,
            caching: DataStoreCachingStrategy<Entity>,
            saving: DataStoreSavingStrategy<Entity>,
            context: DataStoreContext<Entity>
        ) -> DataStoreRepository<Entity>
    ): DataStoreRepository<Entity>

}
