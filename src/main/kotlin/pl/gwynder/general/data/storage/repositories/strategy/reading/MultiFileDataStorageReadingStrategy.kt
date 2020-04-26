package pl.gwynder.general.data.storage.repositories.strategy.reading

import pl.gwynder.general.commons.base.BaseService
import pl.gwynder.general.data.storage.entities.DataStoreContext
import pl.gwynder.general.data.storage.entities.DataStoreEntity
import pl.gwynder.general.data.storage.errors.FileError
import pl.gwynder.general.data.storage.files.DataStoreFile
import pl.gwynder.general.data.storage.files.DataStoreFileService
import pl.gwynder.general.data.storage.parsers.DataStoreEntityParser
import pl.gwynder.general.data.storage.repositories.strategy.ENTITIES
import pl.gwynder.general.data.storage.repositories.strategy.ENTRIES
import pl.gwynder.general.data.storage.repositories.strategy.ID_LIST
import java.io.InputStream
import java.util.*

class MultiFileDataStorageReadingStrategy<Entity : DataStoreEntity>(
    private val fileService: DataStoreFileService,
    private val parser: DataStoreEntityParser,
    override val context: DataStoreContext<Entity>
) : BaseService(), DataStoreReadingStrategy<Entity> {

    override fun all(): Set<Entity> {
        val listFile = fileService.json(ENTITIES, context.name, ID_LIST)
        if (listFile.exists()) {
            listFile.input().use { inputStream: InputStream ->
                return parser.readStrings(inputStream).map { id -> readId(id) }.toSet()
            }
        } else {
            return setOf()
        }
    }

    private fun readId(id: String): Entity {
        val file = singleFile(id)
        if (file.exists()) {
            return readSingleFile(file)
        } else {
            throw FileError("Failed to find expected file: ${context.name}/$id")
        }
    }

    private fun singleFile(id: String) = fileService.json(ENTITIES, context.name, ENTRIES, id)

    private fun readSingleFile(file: DataStoreFile): Entity {
        file.input().use { inputStream: InputStream ->
            return parser.readSingle(inputStream, context.entityClass)
        }
    }

    override fun single(id: String): Optional<Entity> {
        val file = singleFile(id)
        return if (file.exists()) {
            Optional.of(readSingleFile(file))
        } else {
            Optional.empty()
        }
    }

}
