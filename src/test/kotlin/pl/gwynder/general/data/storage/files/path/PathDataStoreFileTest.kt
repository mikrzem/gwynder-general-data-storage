package pl.gwynder.general.data.storage.files.path

import com.nhaarman.mockitokotlin2.*
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import pl.gwynder.general.data.storage.errors.FileError
import java.io.InputStream
import java.io.OutputStream
import java.nio.file.Path

internal class PathDataStoreFileTest {

    private val path: Path = Path.of("C:\\test\\files\\test.txt")
    private val parent: Path = path.parent

    private val emptyPath: Path = Path.of("C:\\test\\files\\empty")
    private val badPath: Path = Path.of("C:\\test\\files\\bad")

    private val inputStream = mock<InputStream>()
    private val outputStream = mock<OutputStream>()

    private val fileError = FileError("Error", RuntimeException())
    private val fileService = mock<PathFilesDataStoreService> {
        on { openInput(path) } doReturn inputStream
        on { openOutput(path) } doReturn outputStream
        on { delete(emptyPath) } doThrow fileError
        on { delete(badPath) } doThrow fileError
        on { exists(emptyPath) } doReturn false
        on { exists(badPath) } doReturn true
    }

    @Test
    fun input() {
        val test = PathDataStoreFile(fileService, path)
        val result = test.input()
        assertThat(result).isEqualTo(inputStream)
    }

    @Test
    fun output() {
        val test = PathDataStoreFile(fileService, path)
        val result = test.output()
        assertThat(result).isEqualTo(outputStream)
    }

    @Test
    fun deleteSuccessful() {
        val test = PathDataStoreFile(fileService, path)
        test.delete()
        verify(fileService).delete(path)
        verify(fileService, never()).exists(emptyPath)
    }

    @Test
    fun deleteEmpty() {
        val test = PathDataStoreFile(fileService, emptyPath)
        test.delete()
        verify(fileService).delete(emptyPath)
        verify(fileService).exists(emptyPath)
    }

    @Test
    fun deleteBad() {
        val test = PathDataStoreFile(fileService, badPath)
        assertThrows<FileError> {
            test.delete()
        }
    }

}
