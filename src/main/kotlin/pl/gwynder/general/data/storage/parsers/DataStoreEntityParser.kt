package pl.gwynder.general.data.storage.parsers

import pl.gwynder.general.data.storage.entities.DataStoreEntity
import java.io.InputStream
import java.io.OutputStream

interface DataStoreEntityParser {

    fun <Entity : DataStoreEntity> read(input: InputStream, entityClass: Class<Entity>): Set<Entity>

    fun <Entity : DataStoreEntity> write(output: OutputStream, data: Set<Entity>, entityClass: Class<Entity>)

}
