package pl.gwynder.general.data.storage.files.path

import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path

interface PathFilesDataStorageService {

    fun createDirectory(directory: Path)

    fun openInput(file: Path): InputStream

    fun openOutput(file: Path): OutputStream

    fun delete(path: Path)

    fun exists(path: Path): Boolean

}
