package pl.gwynder.general.data.storage.files

import java.io.InputStream
import java.io.OutputStream

interface DataStorageFile {

    fun input(): InputStream

    fun output(): OutputStream

    fun delete()

}
