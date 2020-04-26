package pl.gwynder.general.data.storage.entities

data class DataStoreContext<Entity : DataStoreEntity>(
    val entityClass: Class<Entity>,
    val name: String = entityClass.name
)
