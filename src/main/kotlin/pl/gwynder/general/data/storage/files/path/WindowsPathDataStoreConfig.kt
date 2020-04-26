package pl.gwynder.general.data.storage.files.path

import pl.gwynder.general.commons.base.BaseService
import java.nio.file.Path

class WindowsPathDataStoreConfig : BaseService(),
    PathDataStoreConfig {

    private val path = Path.of(System.getenv("APPDATA"), "gwynder-general-data-storage")

    override fun basePath(): Path {
        return path
    }

}
