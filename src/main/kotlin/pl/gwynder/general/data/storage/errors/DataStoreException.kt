package pl.gwynder.general.data.storage.errors

open class DataStoreException : RuntimeException {

    constructor(message: String) : super(message)
    constructor(message: String, cause: Throwable) : super(message, cause)

}
