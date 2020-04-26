package pl.gwynder.general.data.storage.files.path

import com.nhaarman.mockitokotlin2.doReturn
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import pl.gwynder.general.data.storage.files.DataStoreConfig
import java.nio.file.Path

internal class PathDataStoreFileServiceTest {

    private val basePath: Path = Path.of("C:\\test")
    private val pathConfig = mock<PathDataStoreConfig> {
        on { basePath() } doReturn basePath
    }

    private val name = "files"
    private val config = mock<DataStoreConfig> {
        on { moduleName } doReturn name
    }

    private val filesService = mock<PathFilesDataStoreService>()

    private val test = PathDataStoreFileService(
        pathConfig,
        config,
        filesService
    )

    private val pathDirectory = "directory"
    private val pathFile = "file"

    private val resultPath: Path = Path.of(basePath.toString(), name, pathDirectory, "$pathFile.json")
    private val parentPath: Path = resultPath.parent

    @Test
    fun json() {
        val result = test.json(pathDirectory, pathFile)
        verify(filesService).createDirectory(parentPath)
        assertThat(result).isInstanceOf(PathDataStoreFile::class.java)
        assertThat((result as PathDataStoreFile).path).isEqualTo(resultPath)
    }

}
