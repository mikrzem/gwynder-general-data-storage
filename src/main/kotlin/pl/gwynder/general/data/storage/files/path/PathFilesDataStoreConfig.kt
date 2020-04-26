package pl.gwynder.general.data.storage.files.path

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.gwynder.general.data.storage.files.DataStorageFileService
import pl.gwynder.general.data.storage.files.DataStoreConfig

@Configuration
open class PathFilesDataStoreConfig(
    private val pathConfig: PathDataStoreConfig,
    private val config: DataStoreConfig
) {

    @Bean
    open fun pathFilesDataStorageService(): PathFilesDataStoreService {
        return SimplePathFilesDataStoreService()
    }

    @Bean
    open fun dataStorageFileService(): DataStorageFileService {
        return PathDataStoreFileService(
            pathConfig,
            config,
            pathFilesDataStorageService()
        )
    }

}
