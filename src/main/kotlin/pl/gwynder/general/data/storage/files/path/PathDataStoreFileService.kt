package pl.gwynder.general.data.storage.files.path

import pl.gwynder.general.commons.base.BaseService
import pl.gwynder.general.data.storage.files.DataStorageFile
import pl.gwynder.general.data.storage.files.DataStorageFileService
import pl.gwynder.general.data.storage.files.DataStoreConfig
import java.nio.file.Path

class PathDataStoreFileService(
    private val fileConfig: PathDataStoreConfig,
    private val config: DataStoreConfig,
    private val fileService: PathFilesDataStoreService
) : BaseService(), DataStorageFileService {

    override fun json(vararg path: String): DataStorageFile {
        val fixedPath: List<String> = extensionPath(path, ".json")
        val resultPath: Path = Path.of(fileConfig.basePath().toString(), *fixedPath.toTypedArray())
        ensureDirectory(resultPath)
        return PathDataStoreFile(fileService, resultPath)
    }

    private fun ensureDirectory(path: Path) {
        val parent = path.parent
        if (parent != null) {
            fileService.createDirectory(parent)
        }
    }

    private fun extensionPath(path: Array<out String>, extension: String): List<String> {
        val result: MutableList<String> = ArrayList()
        result.add(config.moduleName)
        result.addAll(path.slice(IntRange(0, path.size - 2)))
        result.add(path[path.size - 1] + extension)
        return result
    }

}
