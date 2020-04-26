package pl.gwynder.general.data.storage.files.path

import pl.gwynder.general.data.storage.errors.FileError
import pl.gwynder.general.data.storage.files.DataStoreFile
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path

class PathDataStoreFile(
    private val service: PathFilesDataStoreService,
    val path: Path
) : DataStoreFile {

    override fun input(): InputStream {
        return service.openInput(path)
    }

    override fun output(): OutputStream {
        return service.openOutput(path)
    }

    override fun delete() {
        try {
            service.delete(path)
        } catch (ex: FileError) {
            if (service.exists(path)) {
                throw ex
            }
        }
    }

    override fun exists(): Boolean {
        return service.exists(path)
    }

}
