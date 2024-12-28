import org.apache.commons.io.FileUtils
import java.io.File
import java.nio.charset.StandardCharsets

class Server(private val pathImport: String, private val pathExport: String) {
    private lateinit var data: Data
    fun run() {
        data = Data.load(pathExport, true)
        var isChange = false
        val folder = File(pathImport)
        val files = folder.listFiles()
        if (files != null) {
            for (file in files) {
                if (file.isFile()) {
                    val fileContent: String = FileUtils.readFileToString(file, StandardCharsets.UTF_8)
                    if (fileContent.length > 3) {
                        data.initComputer(file.getName(), fileContent)
                    }
                }
                FileUtils.forceDelete(file)
            }
            isChange = true
        }
        if (isChange) {
            data.save(pathExport)
        }
    }
}
