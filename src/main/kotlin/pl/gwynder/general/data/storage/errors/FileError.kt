package pl.gwynder.general.data.storage.errors

class FileError : DataStoreException {

    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)

}
