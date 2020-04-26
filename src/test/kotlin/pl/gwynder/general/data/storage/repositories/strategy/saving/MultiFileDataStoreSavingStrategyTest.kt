package pl.gwynder.general.data.storage.repositories.strategy.saving

import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.gwynder.general.data.storage.entities.DataStoreContext
import pl.gwynder.general.data.storage.entities.MockDataStoreEntity
import pl.gwynder.general.data.storage.files.DataStoreFile
import pl.gwynder.general.data.storage.files.DataStoreFileService
import pl.gwynder.general.data.storage.parsers.DataStoreEntityParser
import pl.gwynder.general.data.storage.parsers.IdGenerator
import pl.gwynder.general.data.storage.repositories.strategy.ENTITIES
import pl.gwynder.general.data.storage.repositories.strategy.ENTRIES
import pl.gwynder.general.data.storage.repositories.strategy.ID_LIST
import java.io.InputStream
import java.io.OutputStream

internal class MultiFileDataStoreSavingStrategyTest {

    private val context = DataStoreContext(MockDataStoreEntity::class.java)

    private val newId = "newId"
    private val idGenerator = mock<IdGenerator> {
        on { newId() } doReturn newId
    }

    private val listInput = mock<InputStream>()
    private val listOutput = mock<OutputStream>()
    private val listFile = mock<DataStoreFile> {
        on { input() } doReturn listInput
        on { output() } doReturn listOutput
        on { exists() } doReturn true
    }

    private val entityOutput = mock<OutputStream>()
    private val entityFile = mock<DataStoreFile> {
        on { output() } doReturn entityOutput
    }

    private val anotherOutput = mock<OutputStream>()
    private val anotherFile = mock<DataStoreFile> {
        on { output() } doReturn anotherOutput
    }

    private val newOutput = mock<OutputStream>()
    private val newFile = mock<DataStoreFile> {
        on { output() } doReturn newOutput
    }

    private val entityId = "entity"
    private val entity = MockDataStoreEntity(entityId)
    private val anotherId = "another"
    private val another = MockDataStoreEntity(anotherId)
    private val entityIds = setOf(entityId, anotherId)

    private val fileService = mock<DataStoreFileService> {
        on { json(ENTITIES, context.name, ID_LIST) } doReturn listFile
        on { json(ENTITIES, context.name, ENTRIES, entityId) } doReturn entityFile
        on { json(ENTITIES, context.name, ENTRIES, anotherId) } doReturn anotherFile
        on { json(ENTITIES, context.name, ENTRIES, newId) } doReturn newFile
    }

    private val newEntity = MockDataStoreEntity(null)

    private val parser = mock<DataStoreEntityParser> {
        on { readStrings(listInput) } doReturn entityIds
    }

    private val test = MultiFileDataStoreSavingStrategy(fileService, parser, idGenerator, context)

    @Test
    fun create() {
        val result = test.create(newEntity)
        assertThat(result.id).isEqualTo(newId)
        verify(parser).writeSingle(newOutput, result, context.entityClass)
        verify(parser).writeStrings(eq(listOutput), argThat { contains(newId) })
    }

    @Test
    fun update() {
        val result = test.update(entityId, newEntity)
        assertThat(result).isEqualTo(newEntity)
        verify(parser).writeSingle(entityOutput, result, context.entityClass)
        verify(parser).writeStrings(eq(listOutput), argThat { contains(entityId) })
    }

    @Test
    fun remove() {
        test.remove(entityId)
        verify(entityFile).delete()
        verify(parser).writeStrings(eq(listOutput), argThat { !contains(entityId) })
    }

}
