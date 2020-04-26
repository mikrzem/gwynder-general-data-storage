package pl.gwynder.general.data.storage.files

interface DataStorageFileService {

    fun json(vararg path: String): DataStorageFile

}
