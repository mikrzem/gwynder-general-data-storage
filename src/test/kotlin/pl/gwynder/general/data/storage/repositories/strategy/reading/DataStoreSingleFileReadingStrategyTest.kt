package pl.gwynder.general.data.storage.repositories.strategy.reading

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.gwynder.general.data.storage.entities.DataStoreContext
import pl.gwynder.general.data.storage.entities.MockDataStoreEntity
import pl.gwynder.general.data.storage.files.DataStoreFile
import pl.gwynder.general.data.storage.files.DataStoreFileService
import pl.gwynder.general.data.storage.parsers.DataStoreEntityParser
import pl.gwynder.general.data.storage.repositories.strategy.DATA_FILE
import pl.gwynder.general.data.storage.repositories.strategy.ENTITIES
import java.io.InputStream

internal class DataStoreSingleFileReadingStrategyTest {

    private val context = DataStoreContext(MockDataStoreEntity::class.java)

    private val entityId = "entity"
    private val entity = MockDataStoreEntity(entityId)
    private val anotherId = "another"
    private val another = MockDataStoreEntity(anotherId)
    private val entities = setOf(entity, another)

    private val input = mock<InputStream>()

    private val file = mock<DataStoreFile> {
        on { input() } doReturn input
    }

    private val fileService = mock<DataStoreFileService> {
        on { json(ENTITIES, context.name, DATA_FILE) } doReturn file
    }

    private val parser = mock<DataStoreEntityParser> {
        on { read(input, context.entityClass) } doReturn entities
    }

    private val test = DataStoreSingleFileReadingStrategy(
        fileService,
        parser,
        context
    )

    @Test
    fun all() {
        val result = test.all()
        assertThat(result).isEqualTo(entities)
    }

    @Test
    fun singleFound() {
        val result = test.single(entityId)
        assertThat(result.isPresent).isTrue()
        assertThat(result.get()).isEqualTo(entity)
    }

    @Test
    fun singleMissing() {
        val result = test.single("missing")
        assertThat(result.isPresent).isFalse()
    }

}
