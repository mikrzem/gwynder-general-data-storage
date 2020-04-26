package pl.gwynder.general.data.storage.errors

class FileError(message: String, cause: Throwable) : DataStoreException(message, cause)
