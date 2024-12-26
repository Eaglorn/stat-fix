import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.Paths

class ConfigResult {
    lateinit var  listComputers: ConfigResultListComputer
    lateinit var date: ConfigResultDate
    lateinit var ekp: ConfigResultEKP
    var listFixes: Boolean = false
    var countAllFixes: Boolean = false
    var countSeparateFixes: Boolean = false
    var saveFile: String = ""

    companion object {
        fun load(pathConfigs: String, nameConfig: String): ConfigResult {
            val pathConfig: String = pathConfigs + nameConfig;
            println(pathConfig)
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

class ConfigResultListComputer {
    var value = false
    var name = ""
}

class ConfigResultDate {
    var value = false
    var type = ""
    var date = ""
}

class ConfigResultEKP {
    var value = false
    var name = ""
}