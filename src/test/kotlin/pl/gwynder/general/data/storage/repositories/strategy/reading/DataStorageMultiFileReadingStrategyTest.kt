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
import pl.gwynder.general.data.storage.repositories.strategy.ENTITIES
import pl.gwynder.general.data.storage.repositories.strategy.ENTRIES
import pl.gwynder.general.data.storage.repositories.strategy.ID_LIST
import java.io.InputStream


internal class DataStorageMultiFileReadingStrategyTest {

    private val entityClass = MockDataStoreEntity::class.java
    private val context = DataStoreContext(entityClass)
    private val missing = "missing"
    private val missingContext = DataStoreContext(entityClass, missing)

    private val entityId = "entity"
    private val entity = MockDataStoreEntity(entityId)
    private val anotherId = "another"
    private val another = MockDataStoreEntity(anotherId)
    private val entitiesIds = setOf(entityId, anotherId)
    private val entities = setOf(entity, another)

    private val listInput = mock<InputStream>()
    private val listFile = mock<DataStoreFile> {
        on { input() } doReturn listInput
        on { exists() } doReturn true
    }

    private val entityInput = mock<InputStream>()
    private val entityFile = mock<DataStoreFile> {
        on { input() } doReturn entityInput
        on { exists() } doReturn true
    }

    private val anotherInput = mock<InputStream>()
    private val anotherFile = mock<DataStoreFile> {
        on { input() } doReturn anotherInput
        on { exists() } doReturn true
    }

    private val missingFile = mock<DataStoreFile> {
        on { exists() } doReturn false
    }

    private val fileService = mock<DataStoreFileService> {
        on { json(ENTITIES, context.name, ID_LIST) } doReturn listFile
        on { json(ENTITIES, context.name, ENTRIES, entityId) } doReturn entityFile
        on { json(ENTITIES, context.name, ENTRIES, anotherId) } doReturn anotherFile
        on { json(ENTITIES, missingContext.name, ID_LIST) } doReturn missingFile
        on { json(ENTITIES, missingContext.name, ENTRIES, entityId) } doReturn missingFile
    }

    private val parser = mock<DataStoreEntityParser> {
        on { readStrings(listInput) } doReturn entitiesIds
        on { readSingle(entityInput, entityClass) } doReturn entity
        on { readSingle(anotherInput, entityClass) } doReturn another
    }

    private val testExists = DataStorageMultiFileReadingStrategy(fileService, parser, context)
    private val testMissing = DataStorageMultiFileReadingStrategy(fileService, parser, missingContext)

    @Test
    fun allExists() {
        val result = testExists.all()
        assertThat(result).containsExactlyInAnyOrder(*entities.toTypedArray())
    }

    @Test
    fun allMissing() {
        val result = testMissing.all()
        assertThat(result).isEmpty()
    }

    @Test
    fun singExists() {
        val result = testExists.single(entityId)
        assertThat(result.isPresent).isTrue()
        assertThat(result.get()).isEqualTo(entity)
    }

    @Test
    fun singleMissing() {
        val result = testMissing.single(entityId)
        assertThat(result.isPresent).isFalse()
    }

}
