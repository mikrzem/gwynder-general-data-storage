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


internal class SingleFileDataStoreReadingStrategyTest {

    private val context = DataStoreContext(MockDataStoreEntity::class.java)
    private val missing = "missing"
    private val missingContext = DataStoreContext(MockDataStoreEntity::class.java, missing)

    private val entityId = "entity"
    private val entity = MockDataStoreEntity(entityId)
    private val anotherId = "another"
    private val another = MockDataStoreEntity(anotherId)
    private val entities = setOf(entity, another)

    private val input = mock<InputStream>()

    private val file = mock<DataStoreFile> {
        on { input() } doReturn input
        on { exists() } doReturn true
    }

    private val missingFile = mock<DataStoreFile> {
        on { exists() } doReturn false
    }

    private val fileService = mock<DataStoreFileService> {
        on { json(ENTITIES, context.name, DATA_FILE) } doReturn file
        on { json(ENTITIES, missingContext.name, DATA_FILE) } doReturn missingFile
    }

    private val parser = mock<DataStoreEntityParser> {
        on { readAll(input, context.entityClass) } doReturn entities
    }

    private val testExists = SingleFileDataStoreReadingStrategy(fileService, parser, context)
    private val testMissing = SingleFileDataStoreReadingStrategy(fileService, parser, missingContext)

    @Test
    fun allExists() {
        val result = testExists.all()
        assertThat(result).isEqualTo(entities)
    }

    @Test
    fun allMissing() {
        val result = testMissing.all()
        assertThat(result).isEmpty()
    }

    @Test
    fun singleFound() {
        val result = testExists.single(entityId)
        assertThat(result.isPresent).isTrue()
        assertThat(result.get()).isEqualTo(entity)
    }

    @Test
    fun singleMissing() {
        val result = testExists.single(missing)
        assertThat(result.isPresent).isFalse()
    }

}
