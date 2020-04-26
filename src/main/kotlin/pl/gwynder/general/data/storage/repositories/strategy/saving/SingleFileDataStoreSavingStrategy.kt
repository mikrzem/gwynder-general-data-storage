package pl.gwynder.general.data.storage.repositories.strategy.saving

import pl.gwynder.general.commons.base.BaseService
import pl.gwynder.general.data.storage.entities.DataStoreContext
import pl.gwynder.general.data.storage.entities.DataStoreEntity
import pl.gwynder.general.data.storage.files.DataStoreFile
import pl.gwynder.general.data.storage.files.DataStoreFileService
import pl.gwynder.general.data.storage.parsers.DataStoreEntityParser
import pl.gwynder.general.data.storage.parsers.IdGenerator
import pl.gwynder.general.data.storage.repositories.strategy.DATA_FILE
import pl.gwynder.general.data.storage.repositories.strategy.ENTITIES
import java.io.InputStream
import java.io.OutputStream

class SingleFileDataStoreSavingStrategy<Entity : DataStoreEntity>(
    private val fileService: DataStoreFileService,
    private val parser: DataStoreEntityParser,
    private val idGenerator: IdGenerator,
    override val context: DataStoreContext<Entity>
) : BaseService(), DataStoreSavingStrategy<Entity> {

    private val current: MutableSet<Entity> = HashSet()
    private var loaded: Boolean = false

    @Synchronized
    override fun create(entity: Entity): Entity {
        val id = idGenerator.newId()
        entity.id = id
        return update(id, entity)
    }

    @Synchronized
    override fun update(id: String, entity: Entity): Entity {
        loadCurrent()
        entity.id = id
        current.removeIf { existing -> existing.id == id }
        current.add(entity)
        saveFile()
        return entity
    }

    @Synchronized
    override fun remove(id: String) {
        loadCurrent()
        current.removeIf { existing -> existing.id == id }
        saveFile()
    }

    private fun loadCurrent() {
        if (!loaded) {
            val file = dataFile()
            if (file.exists()) {
                current.clear()
                current.addAll(readFile(file))
            }
            loaded = true
        }
    }

    private fun readFile(file: DataStoreFile): Set<Entity> {
        return file.input().use { inputStream: InputStream ->
            parser.readAll(inputStream, context.entityClass)
        }
    }

    private fun dataFile() = fileService.json(ENTITIES, context.name, DATA_FILE)

    private fun saveFile() {
        val file = dataFile()
        file.output().use { outputStream: OutputStream ->
            parser.writeAll(outputStream, current, context.entityClass)
        }
    }

}
