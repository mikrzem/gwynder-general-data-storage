package pl.gwynder.general.data.storage.files

import pl.gwynder.general.commons.base.BaseService

class SimpleDataStoreConfig(
    override val moduleName: String
) : BaseService(), DataStoreConfig
