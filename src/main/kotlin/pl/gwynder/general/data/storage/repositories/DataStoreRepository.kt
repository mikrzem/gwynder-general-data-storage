package pl.gwynder.general.data.storage.repositories

import pl.gwynder.general.data.storage.entities.DataStoreContext
import pl.gwynder.general.data.storage.entities.DataStoreEntity
import pl.gwynder.general.data.storage.errors.DataNotFound
import java.util.*

interface DataStoreRepository<Entity : DataStoreEntity> {

    val context: DataStoreContext<Entity>

    fun select(): Set<Entity>

    fun find(id: String): Optional<Entity>

    fun get(id: String): Entity {
        return find(id).orElseThrow { DataNotFound(context.name, id) }
    }

    fun create(entity: Entity): Entity

    fun update(id: String, entity: Entity): Entity

    fun save(entity: Entity): Entity {
        val id = entity.id
        return if (id != null) {
            update(id, entity)
        } else {
            create(entity)
        }
    }

    fun delete(id: String)

    fun delete(entity: Entity) {
        val id = entity.id
        if (id != null) {
            delete(id)
        }
    }

    fun initialize()

}
