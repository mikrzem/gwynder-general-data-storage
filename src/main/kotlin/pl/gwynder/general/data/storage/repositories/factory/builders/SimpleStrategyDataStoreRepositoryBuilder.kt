package pl.gwynder.general.data.storage.repositories.factory.builders

import pl.gwynder.general.commons.base.BaseService
import pl.gwynder.general.data.storage.entities.DataStoreContext
import pl.gwynder.general.data.storage.entities.DataStoreEntity
import pl.gwynder.general.data.storage.files.DataStoreFileService
import pl.gwynder.general.data.storage.parsers.DataStoreEntityParser
import pl.gwynder.general.data.storage.parsers.IdGenerator
import pl.gwynder.general.data.storage.repositories.DataStoreRepository
import pl.gwynder.general.data.storage.repositories.strategy.StrategyDataStoreRepository
import pl.gwynder.general.data.storage.repositories.strategy.caching.DataStoreCachingStrategy
import pl.gwynder.general.data.storage.repositories.strategy.caching.HashMapDataStoreCachingStrategy
import pl.gwynder.general.data.storage.repositories.strategy.caching.VoidDataStoreCachingStrategy
import pl.gwynder.general.data.storage.repositories.strategy.reading.DataStoreReadingStrategy
import pl.gwynder.general.data.storage.repositories.strategy.reading.MultiFileDataStoreReadingStrategy
import pl.gwynder.general.data.storage.repositories.strategy.reading.SingleFileDataStoreReadingStrategy
import pl.gwynder.general.data.storage.repositories.strategy.saving.DataStoreSavingStrategy
import pl.gwynder.general.data.storage.repositories.strategy.saving.MultiFileDataStoreSavingStrategy
import pl.gwynder.general.data.storage.repositories.strategy.saving.SingleFileDataStoreSavingStrategy

class SimpleStrategyDataStoreRepositoryBuilder<Entity : DataStoreEntity>(
    private val fileService: DataStoreFileService,
    private val parser: DataStoreEntityParser,
    private val idGenerator: IdGenerator,
    private val context: DataStoreContext<Entity>
) : BaseService(), StrategyDataStoreRepositoryBuilder<Entity> {

    private var reading: DataStoreReadingStrategy<Entity> = singleFileReading()
    private var saving: DataStoreSavingStrategy<Entity> = singleFileSaving()
    private var caching: DataStoreCachingStrategy<Entity> = voidCaching()

    private fun singleFileReading() = SingleFileDataStoreReadingStrategy(fileService, parser, context)
    private fun multiFileReading() = MultiFileDataStoreReadingStrategy(fileService, parser, context)
    private fun singleFileSaving() = SingleFileDataStoreSavingStrategy(fileService, parser, idGenerator, context)
    private fun multiFileSaving() = MultiFileDataStoreSavingStrategy(fileService, parser, idGenerator, context)
    private fun voidCaching() = VoidDataStoreCachingStrategy(context)
    private fun hashMapCaching() = HashMapDataStoreCachingStrategy(context)

    override fun withSingleFile(): StrategyDataStoreRepositoryBuilder<Entity> {
        reading = singleFileReading()
        saving = singleFileSaving()
        return this
    }

    override fun withMultiFile(): StrategyDataStoreRepositoryBuilder<Entity> {
        reading = multiFileReading()
        saving = multiFileSaving()
        return this
    }

    override fun withVoidCache(): StrategyDataStoreRepositoryBuilder<Entity> {
        caching = voidCaching()
        return this
    }

    override fun withHashMapCache(loadOnInitialize: Boolean): StrategyDataStoreRepositoryBuilder<Entity> {
        caching = hashMapCaching()
        return this
    }

    override fun buildCustom(
        creator: (
            reading: DataStoreReadingStrategy<Entity>,
            caching: DataStoreCachingStrategy<Entity>,
            saving: DataStoreSavingStrategy<Entity>,
            context: DataStoreContext<Entity>
        ) -> DataStoreRepository<Entity>
    ): DataStoreRepository<Entity> {
        val repository = creator(reading, caching, saving, context)
        repository.initialize()
        return repository
    }

    override fun build(): DataStoreRepository<Entity> {
        val repository = StrategyDataStoreRepository(reading, caching, saving, context)
        repository.initialize()
        return repository
    }

}
