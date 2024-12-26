import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import java.io.FileReader
import java.nio.file.Files
import java.nio.file.Paths

class ConfigServer {
    val infoSource: String = ""
    val infoDestination: String = ""

    companion object {
        fun load(): ConfigServer {
            val pathConfig = System.getProperty("user.dir") + "/config.json"
            return if (Files.exists(Paths.get(pathConfig))) {
                val gson: Gson = GsonBuilder().create()
                val configServer: ConfigServer = gson.fromJson(JsonReader(FileReader(pathConfig)), ConfigServer::class.java)
                configServer
            } else {
                ConfigServer()
            }
        }
    }
}
