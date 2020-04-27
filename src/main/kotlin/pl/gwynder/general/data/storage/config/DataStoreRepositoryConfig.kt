package pl.gwynder.general.data.storage.config

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.gwynder.general.data.storage.files.DataStoreFileService
import pl.gwynder.general.data.storage.parsers.DataStoreEntityParser
import pl.gwynder.general.data.storage.parsers.IdGenerator
import pl.gwynder.general.data.storage.parsers.JsonDataStoreEntityParser
import pl.gwynder.general.data.storage.parsers.UUIDIdGenerator
import pl.gwynder.general.data.storage.repositories.factory.DataStoreRepositoryFactory
import pl.gwynder.general.data.storage.repositories.factory.SimpleDataStoreRepositoryFactory

@Configuration
open class DataStoreRepositoryConfig(
    private val dataStoreFileService: DataStoreFileService,
    private val objectMapper: ObjectMapper
) {

    @Bean
    open fun dataStoreEntityParser(): DataStoreEntityParser {
        return JsonDataStoreEntityParser(objectMapper)
    }

    @Bean
    open fun idGenerator(): IdGenerator {
        return UUIDIdGenerator()
    }

    @Bean
    open fun dataStoreRepositoryFactory(): DataStoreRepositoryFactory {
        return SimpleDataStoreRepositoryFactory(
            dataStoreFileService,
            dataStoreEntityParser(),
            idGenerator()
        )
    }

}
