import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.Paths

class ConfigClient {
    val version: String = ""
    val pathInfo: String = ""
    val pathConfigs: String = ""
    val pathResults: String = ""

    companion object {
        fun load(): ConfigClient {
            val pathConfig = System.getProperty("user.dir") + "/config.json"
            return if (Files.exists(Paths.get(pathConfig))) {
                val gson: Gson = GsonBuilder().create()
                val configServer: ConfigClient = gson.fromJson(JsonReader(FileReader(pathConfig)), ConfigClient::class.java)
                configServer
            } else {
                ConfigClient()
            }
        }
    }
}
