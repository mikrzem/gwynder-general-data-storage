package pl.gwynder.general.data.storage.repositories.strategy

import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.gwynder.general.data.storage.entities.MockDataStoreEntity
import pl.gwynder.general.data.storage.repositories.strategy.caching.DataStoreCachingStrategy
import pl.gwynder.general.data.storage.repositories.strategy.reading.DataStoreReadingStrategy
import pl.gwynder.general.data.storage.repositories.strategy.saving.DataStoreSavingStrategy
import java.util.*

internal class StrategyDataStoreRepositoryTest {

    private val id = "id"
    private val idCreated = "created"
    private val idUpdated = "updated"
    private val readEntity: MockDataStoreEntity = MockDataStoreEntity("read")
    private val readEntityList = setOf(readEntity)
    private val cacheEntity: MockDataStoreEntity = MockDataStoreEntity("cache")
    private val cacheEntityList = setOf(cacheEntity)
    private val createdEntity: MockDataStoreEntity = MockDataStoreEntity(idCreated)
    private val updatedEntity: MockDataStoreEntity = MockDataStoreEntity(idUpdated)

    private val readStrategy = mock<DataStoreReadingStrategy<MockDataStoreEntity>> {
        on { all() } doReturn readEntityList
        on { single(id) } doReturn Optional.of(readEntity)
    }

    private val cacheEmptyStrategy = mock<DataStoreCachingStrategy<MockDataStoreEntity>> {
        on { allLoaded() } doReturn false
        on { singleLoaded(any()) } doReturn false
    }

    private val cacheLoadedStrategy = mock<DataStoreCachingStrategy<MockDataStoreEntity>> {
        on { allLoaded() } doReturn true
        on { singleLoaded(any()) } doReturn true
        on { all() } doReturn cacheEntityList
        on { single(id) } doReturn Optional.of(cacheEntity)
    }

    private val saveStrategy = mock<DataStoreSavingStrategy<MockDataStoreEntity>> {
        on { create(any()) } doReturn createdEntity
        on { update(any(), any()) } doReturn updatedEntity
    }


    private val testCacheLoaded = StrategyDataStoreRepository(
        readStrategy,
        cacheLoadedStrategy,
        saveStrategy,
        MockDataStoreEntity::class.java
    )

    private val testCacheEmpty = StrategyDataStoreRepository(
        readStrategy,
        cacheEmptyStrategy,
        saveStrategy,
        MockDataStoreEntity::class.java
    )

    @Test
    fun selectCached() {
        val test = testCacheLoaded
        val result = test.select()
        assertThat(result).isEqualTo(cacheEntityList)
    }

    @Test
    fun selectNotCached() {
        val test = testCacheEmpty
        val result = test.select()
        assertThat(result).isEqualTo(readEntityList)
        verify(cacheEmptyStrategy).set(readEntityList)
    }

    @Test
    fun findCached() {
        val test = testCacheLoaded
        val result = test.find(id)
        assertThat(result.get()).isEqualTo(cacheEntity)
    }

    @Test
    fun findNotCached() {
        val test = testCacheEmpty
        val result = test.find(id)
        assertThat(result.get()).isEqualTo(readEntity)
    }

    @Test
    fun create() {
        val test = testCacheLoaded
        val result = test.create(readEntity)
        assertThat(result).isEqualTo(createdEntity)
        verify(cacheLoadedStrategy).set(idCreated, result)
    }

    @Test
    fun update() {
        val test = testCacheLoaded
        val result = test.update(idUpdated, readEntity)
        assertThat(result).isEqualTo(updatedEntity)
        verify(cacheLoadedStrategy).set(idUpdated, result)
    }

    @Test
    fun delete() {
        val test = testCacheLoaded
        test.delete(id)
        verify(saveStrategy).remove(id)
        verify(cacheLoadedStrategy).remove(id)
    }

    @Test
    fun initialize() {
        val test = testCacheLoaded
        test.initialize()
        verify(cacheLoadedStrategy).initialize(readStrategy)
    }

}
