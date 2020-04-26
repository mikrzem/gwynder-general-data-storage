package pl.gwynder.general.data.storage.repositories.strategy.reading

import pl.gwynder.general.commons.base.BaseService
import pl.gwynder.general.data.storage.entities.DataStoreContext
import pl.gwynder.general.data.storage.entities.DataStoreEntity
import pl.gwynder.general.data.storage.files.DataStoreFileService
import pl.gwynder.general.data.storage.parsers.DataStoreEntityParser
import pl.gwynder.general.data.storage.repositories.strategy.DATA_FILE
import pl.gwynder.general.data.storage.repositories.strategy.ENTITIES
import java.io.InputStream
import java.util.*

class DataStoreSingleFileReadingStrategy<Entity : DataStoreEntity>(
    private val fileService: DataStoreFileService,
    private val parser: DataStoreEntityParser,
    override val context: DataStoreContext<Entity>
) : BaseService(), DataStoreReadingStrategy<Entity> {

    override fun all(): Set<Entity> {
        fileService.json(ENTITIES, context.name, DATA_FILE)
            .input().use { inputStream: InputStream ->
                return parser.readAll(inputStream, context.entityClass)
            }
    }

    override fun single(id: String): Optional<Entity> {
        return Optional.ofNullable(
            all().find { entity -> entity.id === id }
        )
    }

}
