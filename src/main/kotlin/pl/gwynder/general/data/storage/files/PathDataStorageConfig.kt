package pl.gwynder.general.data.storage.files

import java.nio.file.Path

interface PathDataStorageConfig {

    fun basePath(): Path

}
