package pl.gwynder.general.data.storage.files.path

import pl.gwynder.general.commons.base.BaseService
import pl.gwynder.general.data.storage.files.DataStorageFile
import pl.gwynder.general.data.storage.files.DataStorageFileService
import pl.gwynder.general.data.storage.files.DataStoreConfig
import java.nio.file.Path

class PathDataStorageFileService(
    private val fileConfig: PathDataStorageConfig,
    private val config: DataStoreConfig
) : BaseService(), DataStorageFileService {

    override fun json(vararg path: String): DataStorageFile {
        val fixedPath: List<String> = extensionPath(path, ".json")
        val resultPath: Path = Path.of(fileConfig.basePath().toString(), *fixedPath.toTypedArray())
        ensureDirectory(resultPath)
        return PathDataStorageFile(resultPath)
    }

    private fun ensureDirectory(path: Path) {
        val parent = path.parent
        parent?.toFile()?.mkdirs()
    }

    private fun extensionPath(path: Array<out String>, extension: String): List<String> {
        val result: MutableList<String> = ArrayList()
        result.add(config.moduleName)
        result.addAll(path.slice(IntRange(0, path.size - 2)))
        result.add(path[path.size - 1] + extension)
        return result
    }

}
