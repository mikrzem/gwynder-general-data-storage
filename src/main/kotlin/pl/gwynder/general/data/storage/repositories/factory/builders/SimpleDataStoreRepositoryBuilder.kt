package pl.gwynder.general.data.storage.repositories.factory.builders

import pl.gwynder.general.commons.base.BaseService
import pl.gwynder.general.data.storage.entities.DataStoreContext
import pl.gwynder.general.data.storage.entities.DataStoreEntity
import pl.gwynder.general.data.storage.files.DataStoreFileService
import pl.gwynder.general.data.storage.parsers.DataStoreEntityParser
import pl.gwynder.general.data.storage.parsers.IdGenerator

class SimpleDataStoreRepositoryBuilder<Entity : DataStoreEntity>(
    private val fileService: DataStoreFileService,
    private val parser: DataStoreEntityParser,
    private val idGenerator: IdGenerator,
    private val context: DataStoreContext<Entity>
) : BaseService(), DataStoreRepositoryBuilder<Entity> {

    override fun byStrategy(): StrategyDataStoreRepositoryBuilder<Entity> {
        return SimpleStrategyDataStoreRepositoryBuilder(
            fileService, parser, idGenerator, context
        )
    }

}
