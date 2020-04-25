package pl.gwynder.general.data.storage.errors

class DataNotFound(type: String, key: Any) : DataStoreException("Data of type '$type' and key '$key' not found")
