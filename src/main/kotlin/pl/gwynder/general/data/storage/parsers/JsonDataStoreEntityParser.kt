package pl.gwynder.general.data.storage.parsers

import com.fasterxml.jackson.databind.ObjectMapper
import pl.gwynder.general.commons.base.BaseService
import pl.gwynder.general.data.storage.entities.DataStoreEntity
import pl.gwynder.general.data.storage.errors.ParseError
import java.io.InputStream
import java.io.OutputStream

class JsonDataStoreEntityParser(
    private val json: ObjectMapper
) : BaseService(), DataStoreEntityParser {

    override fun <Entity : DataStoreEntity> readAll(input: InputStream, entityClass: Class<Entity>): Set<Entity> {
        try {
            @Suppress("UNCHECKED_CAST")
            val arrayType: Class<Array<Entity>> = entityClass.arrayType() as Class<Array<Entity>>
            return setOf(*json.readValue(input, arrayType))
        } catch (ex: Exception) {
            throw ParseError("Failed to parse json", ex)
        }
    }

    override fun <Entity : DataStoreEntity> writeAll(
        output: OutputStream,
        data: Set<Entity>,
        entityClass: Class<Entity>
    ) {
        try {
            json.writeValue(output, data)
        } catch (ex: Exception) {
            throw ParseError("Failed to write json", ex)
        }
    }

    override fun <Entity : DataStoreEntity> readSingle(input: InputStream, entityClass: Class<Entity>): Entity {
        try {
            return json.readValue(input, entityClass)
        } catch (ex: Exception) {
            throw ParseError("Failed to parse json", ex)
        }
    }

    override fun <Entity : DataStoreEntity> writeSingle(
        output: OutputStream,
        data: Entity,
        entityClass: Class<Entity>
    ) {
        try {
            json.writeValue(output, data)
        } catch (ex: Exception) {
            throw ParseError("Failed to write json", ex)
        }
    }

}
