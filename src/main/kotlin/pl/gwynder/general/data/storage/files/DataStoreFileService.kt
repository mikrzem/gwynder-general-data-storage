package pl.gwynder.general.data.storage.files

interface DataStoreFileService {

    fun json(vararg path: String): DataStoreFile

}
