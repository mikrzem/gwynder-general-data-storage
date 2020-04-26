package pl.gwynder.general.data.storage.repositories.factory

import pl.gwynder.general.data.storage.entities.DataStoreContext
import pl.gwynder.general.data.storage.entities.DataStoreEntity
import pl.gwynder.general.data.storage.repositories.factory.builders.DataStoreRepositoryBuilder

interface DataStoreRepositoryFactory {

    fun <Entity : DataStoreEntity> forContext(context: DataStoreContext<Entity>): DataStoreRepositoryBuilder<Entity>

    fun <Entity : DataStoreEntity> forClass(entityClass: Class<Entity>): DataStoreRepositoryBuilder<Entity>

}
