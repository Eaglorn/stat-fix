import org.apache.commons.io.FileUtils
import java.io.File
import java.text.DateFormat
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.swing.text.DateFormatter

class Client(private val pathInfo: String, private val pathConfigs: String, private val pathResults: String, private val nameConfig: String, private val version:String) {
    private lateinit var data: Data
    private lateinit var configResult: ConfigResult
    fun run() {
        data = Data.load(pathInfo, false)
        configResult = ConfigResult.load(pathConfigs, nameConfig)

        var computers: ArrayList<Computer> = data.computers

        if(configResult.ekp.value) {
            computers = ArrayList(computers.filter { it -> it.ekp == configResult.ekp.name })
        }

        if(configResult.listComputers.value) {
            var listComputer: ArrayList<String>? = null
            listComputer = ArrayList(FileUtils.readLines(File(pathConfigs + configResult.listComputers.name), "UTF8"))
            computers = ArrayList(computers.filter { it -> listComputer.contains(it.name) })
        }

        if(configResult.date.value) {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            if(configResult.date.type == "one") {
                for(computer in computers) {
                    for(operation in computer.operations) {
                        if(!operation.date.format(formatter).equals(configResult.date.date)) {
                            computer.operations.remove(operation)
                        }
                    }
                    if(computer.operations.isEmpty()) {
                        computers.remove(computer)
                    }
                }
            } else if(configResult.date.type == "range") {
                val localDateList: ArrayList<String> = String.
                val localDate: LocalDate = LocalDate.parse(configResult.date.date, formatter)
            }
        }


        val computerResult: ArrayList<String> = ArrayList()
    }
}
