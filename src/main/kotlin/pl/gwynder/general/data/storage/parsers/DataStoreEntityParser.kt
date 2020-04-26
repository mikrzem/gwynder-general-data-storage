package pl.gwynder.general.data.storage.parsers

import pl.gwynder.general.data.storage.entities.DataStoreEntity
import java.io.InputStream
import java.io.OutputStream

interface DataStoreEntityParser {

    fun <Entity : DataStoreEntity> readAll(input: InputStream, entityClass: Class<Entity>): Set<Entity>

    fun <Entity : DataStoreEntity> writeAll(output: OutputStream, data: Set<Entity>, entityClass: Class<Entity>)

    fun <Entity : DataStoreEntity> readSingle(input: InputStream, entityClass: Class<Entity>): Entity

    fun <Entity : DataStoreEntity> writeSingle(output: OutputStream, data: Entity, entityClass: Class<Entity>)

    fun readStrings(input: InputStream): Set<String>

    fun writeStrings(output: OutputStream, data: Set<String>)

}
