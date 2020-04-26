package pl.gwynder.general.data.storage.repositories.strategy

import pl.gwynder.general.data.storage.entities.DataStoreContext
import pl.gwynder.general.data.storage.entities.DataStoreEntity

interface DataStoreStrategy<Entity : DataStoreEntity> {

    val context: DataStoreContext<Entity>

}
