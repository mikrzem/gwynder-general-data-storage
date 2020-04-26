package pl.gwynder.general.data.storage.errors

class ParseError(message: String, cause: Throwable) : DataStoreException(message, cause)
