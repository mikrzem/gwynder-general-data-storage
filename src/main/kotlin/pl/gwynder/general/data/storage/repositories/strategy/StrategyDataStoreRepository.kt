package pl.gwynder.general.data.storage.repositories.strategy

import pl.gwynder.general.commons.base.BaseService
import pl.gwynder.general.data.storage.entities.DataStoreContext
import pl.gwynder.general.data.storage.entities.DataStoreEntity
import pl.gwynder.general.data.storage.repositories.DataStoreRepository
import pl.gwynder.general.data.storage.repositories.strategy.caching.DataStoreCachingStrategy
import pl.gwynder.general.data.storage.repositories.strategy.reading.DataStoreReadingStrategy
import pl.gwynder.general.data.storage.repositories.strategy.saving.DataStoreSavingStrategy
import java.util.*
import javax.annotation.PostConstruct

open class StrategyDataStoreRepository<Entity : DataStoreEntity>(
    private val reading: DataStoreReadingStrategy<Entity>,
    private val caching: DataStoreCachingStrategy<Entity>,
    private val saving: DataStoreSavingStrategy<Entity>,
    override val context: DataStoreContext<Entity>
) : BaseService(), DataStoreRepository<Entity> {

    @Synchronized
    override fun select(): Set<Entity> {
        return if (caching.allLoaded()) {
            caching.all()
        } else {
            val loaded = reading.all()
            caching.set(loaded)
            loaded
        }
    }

    @Synchronized
    override fun find(id: String): Optional<Entity> {
        return if (caching.singleLoaded(id)) {
            caching.single(id)
        } else {
            val loaded = reading.single(id)
            loaded.ifPresent { loadedEntity -> caching.set(id, loadedEntity) }
            loaded
        }
    }

    @Synchronized
    override fun create(entity: Entity): Entity {
        val saved = saving.create(entity)
        val id = saved.id
        if (id != null) {
            caching.set(id, saved)
        }
        return saved
    }

    @Synchronized
    override fun update(id: String, entity: Entity): Entity {
        val saved = saving.update(id, entity)
        caching.set(id, saved)
        return saved
    }

    @Synchronized
    override fun delete(id: String) {
        saving.remove(id)
        caching.remove(id)
    }

    @Synchronized
    @PostConstruct
    override fun initialize() {
        caching.initialize(reading)
    }

}
