package pl.gwynder.general.data.storage.repositories.factory

import pl.gwynder.general.commons.base.BaseService
import pl.gwynder.general.data.storage.entities.DataStoreContext
import pl.gwynder.general.data.storage.entities.DataStoreEntity
import pl.gwynder.general.data.storage.files.DataStoreFileService
import pl.gwynder.general.data.storage.parsers.DataStoreEntityParser
import pl.gwynder.general.data.storage.parsers.IdGenerator
import pl.gwynder.general.data.storage.repositories.factory.builders.DataStoreRepositoryBuilder
import pl.gwynder.general.data.storage.repositories.factory.builders.SimpleDataStoreRepositoryBuilder

class SimpleDataStoreRepositoryFactory(
    private val fileService: DataStoreFileService,
    private val parser: DataStoreEntityParser,
    private val idGenerator: IdGenerator
) : BaseService(), DataStoreRepositoryFactory {

    override fun <Entity : DataStoreEntity> forContext(context: DataStoreContext<Entity>): DataStoreRepositoryBuilder<Entity> {
        return SimpleDataStoreRepositoryBuilder(fileService, parser, idGenerator, context)
    }

    override fun <Entity : DataStoreEntity> forClass(entityClass: Class<Entity>): DataStoreRepositoryBuilder<Entity> {
        return forContext(DataStoreContext(entityClass))
    }

}
