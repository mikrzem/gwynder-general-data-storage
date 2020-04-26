package pl.gwynder.general.data.storage.files.path

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import pl.gwynder.general.data.storage.files.DataStorageFile
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path

class PathDataStorageFile(
    private val path: Path
) : DataStorageFile {

    private val logger: Logger = LoggerFactory.getLogger(javaClass)

    override fun input(): InputStream {
        return FileInputStream(path.toFile())
    }

    override fun output(): OutputStream {
        return FileOutputStream(path.toFile())
    }

    override fun delete() {
        try {
            path.toFile().delete()
        } catch (ex: Exception) {
            logger.warn("Failed to delete file: $path", ex)
        }
    }

}
