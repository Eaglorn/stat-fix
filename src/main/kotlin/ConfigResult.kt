import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.Paths

class ConfigResult {
    var listComputers: String = ""
    var date: ConfigResultDate = ConfigResultDate()
    var ekp: String = ""
    var listFixes: Boolean = false
    var countAllFixes: Boolean = false
    var countSeparateFixes: Boolean = false
    var saveFile: String = ""

    companion object {
        fun load(pathConfigs: String, nameConfig: String): ConfigResult {
            val pathConfig: String = "${pathConfigs}${nameConfig}.json";
            return if (Files.exists(Paths.get(pathConfig))) {
                val gson: Gson = GsonBuilder().create()
                val configResult: ConfigResult = gson.fromJson(JsonReader(FileReader(pathConfig)), ConfigResult::class.java)
                configResult
            } else {
                ConfigResult()
            }
        }
    }
}

class ConfigResultDate {
    var type = ""
    var date = ""
}
