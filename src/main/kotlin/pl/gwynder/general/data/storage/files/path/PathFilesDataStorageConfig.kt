package pl.gwynder.general.data.storage.files.path

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import pl.gwynder.general.data.storage.files.DataStorageFileService
import pl.gwynder.general.data.storage.files.DataStoreConfig
import pl.gwynder.general.data.storage.files.path.PathDataStorageConfig
import pl.gwynder.general.data.storage.files.path.PathDataStorageFileService

@Configuration
open class PathFilesDataStorageConfig(
    private val pathConfig: PathDataStorageConfig,
    private val config: DataStoreConfig
) {

    @Bean
    open fun dataStorageFileService(): DataStorageFileService {
        return PathDataStorageFileService(
            pathConfig,
            config
        )
    }

}
