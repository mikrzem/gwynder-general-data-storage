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
import pl.gwynder.general.data.storage.repositories.strategy.DATA_FILE
import pl.gwynder.general.data.storage.repositories.strategy.ENTITIES
import java.io.InputStream
import java.io.OutputStream

internal class SingleFileDataStoreSavingStrategyTest {

    private val context = DataStoreContext(MockDataStoreEntity::class.java)

    private val newId = "newId"
    private val idGenerator = mock<IdGenerator> {
        on { newId() } doReturn newId
    }

    private val input = mock<InputStream>()
    private val output = mock<OutputStream>()
    private val file = mock<DataStoreFile> {
        on { input() } doReturn input
        on { output() } doReturn output
        on { exists() } doReturn true
    }

    private val fileService = mock<DataStoreFileService> {
        on { json(ENTITIES, context.name, DATA_FILE) } doReturn file
    }

    private val entityId = "entity"
    private val entity = MockDataStoreEntity(entityId)
    private val anotherId = "another"
    private val another = MockDataStoreEntity(anotherId)
    private val entities = setOf(entity, another)

    private val newEntity = MockDataStoreEntity(null)

    private val parser = mock<DataStoreEntityParser> {
        on { readAll(input, context.entityClass) } doReturn entities
    }

    private val test = SingleFileDataStoreSavingStrategy(fileService, parser, idGenerator, context)

    @Test
    fun create() {
        val result = test.create(newEntity)
        assertThat(result.id).isEqualTo(newId)
        verify(parser).writeAll(eq(output), argThat { contains(result) }, eq(context.entityClass))
    }

    @Test
    fun update() {
        val result = test.update(entityId, newEntity)
        assertThat(result).isEqualTo(newEntity)
        verify(parser).writeAll(eq(output), argThat { contains(result) }, eq(context.entityClass))
    }

    @Test
    fun remove() {
        test.remove(entityId)
        verify(parser).writeAll(eq(output), argThat { !contains(entity) }, eq(context.entityClass))
    }

}
