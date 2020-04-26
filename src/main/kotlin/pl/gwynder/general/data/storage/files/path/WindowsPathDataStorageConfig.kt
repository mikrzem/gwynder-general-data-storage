package pl.gwynder.general.data.storage.files.path

import pl.gwynder.general.commons.base.BaseService
import pl.gwynder.general.data.storage.files.path.PathDataStorageConfig
import java.nio.file.Path

class WindowsPathDataStorageConfig : BaseService(),
    PathDataStorageConfig {

    private val path = Path.of(System.getenv("APPDATA"), "gwynder-general-data-storage")

    override fun basePath(): Path {
        return path
    }

}
