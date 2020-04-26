package pl.gwynder.general.data.storage.files.path

import pl.gwynder.general.commons.base.BaseService
import pl.gwynder.general.data.storage.errors.FileError
import java.io.FileInputStream
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path

class SimplePathFilesDataStoreService : BaseService(), PathFilesDataStoreService {

    override fun createDirectory(directory: Path) {
        try {
            directory.toFile().mkdirs()
        } catch (ex: Exception) {
            throw FileError("Error creating directory: $directory", ex)
        }
    }

    override fun openInput(file: Path): InputStream {
        try {
            return FileInputStream(file.toFile())
        } catch (ex: Exception) {
            throw FileError("Error opening file for reading: $file", ex)
        }
    }

    override fun openOutput(file: Path): OutputStream {
        try {
            return FileOutputStream(file.toFile())
        } catch (ex: Exception) {
            throw FileError("Error opening file for writing: $file", ex)
        }
    }

    override fun delete(path: Path) {
        try {
            path.toFile().delete()
        } catch (ex: Exception) {
            throw FileError("Error deleting: $path", ex)
        }
    }

    override fun exists(path: Path): Boolean {
        try {
            return path.toFile().exists()
        } catch (ex: Exception) {
            throw FileError("Error checking is file exists: $path", ex)
        }
    }

}
