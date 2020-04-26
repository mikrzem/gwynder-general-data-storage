package pl.gwynder.general.data.storage.repositories.strategy.saving

import pl.gwynder.general.commons.base.BaseService
import pl.gwynder.general.data.storage.entities.DataStoreContext
import pl.gwynder.general.data.storage.entities.DataStoreEntity
import pl.gwynder.general.data.storage.files.DataStoreFile
import pl.gwynder.general.data.storage.files.DataStoreFileService
import pl.gwynder.general.data.storage.parsers.DataStoreEntityParser
import pl.gwynder.general.data.storage.parsers.IdGenerator
import pl.gwynder.general.data.storage.repositories.strategy.ENTITIES
import pl.gwynder.general.data.storage.repositories.strategy.ENTRIES
import pl.gwynder.general.data.storage.repositories.strategy.ID_LIST
import java.io.InputStream
import java.io.OutputStream

class MultiFileDataStoreSavingStrategy<Entity : DataStoreEntity>(
    private val fileService: DataStoreFileService,
    private val parser: DataStoreEntityParser,
    private val idGenerator: IdGenerator,
    override val context: DataStoreContext<Entity>
) : BaseService(), DataStoreSavingStrategy<Entity> {

    private val current: MutableSet<String> = HashSet()
    private var loaded: Boolean = false

    override fun create(entity: Entity): Entity {
        val id = idGenerator.newId()
        entity.id = id
        return update(id, entity)
    }

    override fun update(id: String, entity: Entity): Entity {
        loadCurrent()
        entity.id = id
        entryFile(id).output().use { outputStream: OutputStream ->
            parser.writeSingle(outputStream, entity, context.entityClass)
        }
        current.add(id)
        saveList()
        return entity
    }

    override fun remove(id: String) {
        loadCurrent()
        entryFile(id).delete()
        current.remove(id)
        saveList()
    }

    private fun entryFile(id: String) = fileService.json(ENTITIES, context.name, ENTRIES, id)

    private fun loadCurrent() {
        if (!loaded) {
            val file = listFile()
            if (file.exists()) {
                current.clear()
                current.addAll(readFile(file))
            }
            loaded = true
        }
    }

    private fun readFile(file: DataStoreFile): Set<String> {
        return file.input().use { inputStream: InputStream ->
            parser.readStrings(inputStream)
        }
    }

    private fun listFile() = fileService.json(ENTITIES, context.name, ID_LIST)

    private fun saveList() {
        val file = listFile()
        file.output().use { outputStream: OutputStream ->
            parser.writeStrings(outputStream, current)
        }
    }

}
