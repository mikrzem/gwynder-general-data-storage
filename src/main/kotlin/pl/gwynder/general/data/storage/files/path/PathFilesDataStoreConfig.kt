package pl.gwynder.general.data.storage.files.path

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.gwynder.general.data.storage.files.DataStoreConfig
import pl.gwynder.general.data.storage.files.DataStoreFileService

@Configuration
open class PathFilesDataStoreConfig(
    private val pathConfig: PathDataStoreConfig,
    private val config: DataStoreConfig
) {

    @Bean
    open fun pathFilesDataStoreService(): PathFilesDataStoreService {
        return SimplePathFilesDataStoreService()
    }

    @Bean
    open fun dataStoreFileService(): DataStoreFileService {
        return PathDataStoreFileService(
            pathConfig,
            config,
            pathFilesDataStoreService()
        )
    }

}
