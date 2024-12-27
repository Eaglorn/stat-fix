import com.google.gson.FormattingStyle
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.stream.JsonReader
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Paths
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import kotlin.math.abs

class Data {
    val computers = ArrayList<Computer>()

    private fun findComputerByName(name: String): Computer? {
        for (computer in computers) {
            if (computer.name.equals(name, ignoreCase = true)) {
                return computer
            }
        }
        return null
    }

    fun initComputer(name: String, value: String) {
        val parts = value.split(" ").filter { it.isNotEmpty() }
        val version = parts.first()
        val textDate = parts[1]
        val textTime = parts[2]
        val listFixes = parts.drop(3)

        val operationDateTime = LocalDateTime.parse("$textDate $textTime",DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss"))

        val computer = findComputerByName(name)
        if (computer != null) {
            if (version != computer.version) {
                computer.version = version
            }
            val operations = computer.operations
            val fixes = ArrayList<String>()
            if((operations.filter { it.date.isEqual(operationDateTime)}).isEmpty()) {
                for (operation in operations) {
                    if (version == operation.version) {
                        for (fix in operation.fixes) {
                            fixes.add(fix)
                        }
                    }
                }
                if (listFixes.isNotEmpty()) {
                    val operation = ComputerOperation(operationDateTime, version)
                    for (fix in listFixes) {
                        if (!fixes.contains(fix)) {
                            operation.addFix(fix)
                        }
                    }
                    if (operation.fixes.size != 0) {
                        operation.sort()
                        computer.operations.add(operation)
                        computer.dateUpdate()
                    }
                }
            }
        } else {
            if (listFixes.isNotEmpty()) {
                val operation = ComputerOperation(operationDateTime, version)
                for (fix in listFixes) {
                    operation.addFix(fix)
                }
                operation.sort()
                computers.add(Computer(name, version, operation))
            }
        }
    }

    fun save(pathExport: String) {
        FileWriter(pathExport + "info.json").use { file ->
            val gson: Gson = GsonBuilder()
                .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter("dd.MM.yyyy HH:mm:ss"))
                .create()
            file.write(gson.toJson(this, Data::class.java))
            file.flush()
        }
        FileWriter(pathExport + "infoPretty.json").use { file ->
            val gson: Gson = GsonBuilder().setFormattingStyle(FormattingStyle.PRETTY)
                .registerTypeAdapter(Computer::class.java, ComputerSerializer()).create()
            file.write(gson.toJson(this, Data::class.java))
            file.flush()
        }
    }

    companion object {
        private fun isDifferenceMoreThanMonths(date1: LocalDateTime, date2: LocalDateTime, month: Int): Boolean {
            val monthsBetween: Double = abs(ChronoUnit.MONTHS.between(date1, date2).toDouble())
            return monthsBetween > month
        }

        fun load(pathExport: String, isClean: Boolean): Data {
            return if (Files.exists(Paths.get(pathExport + "info.json"))) {
                val gson: Gson = GsonBuilder()
                    .registerTypeAdapter(LocalDateTime::class.java, LocalDateTimeAdapter("dd.MM.yyyy HH:mm:ss"))
                    .create()
                val data: Data = gson.fromJson(JsonReader(FileReader(pathExport + "info.json")), Data::class.java)
                if(isClean) {
                    val date1: LocalDateTime = LocalDateTime.now()
                    for (computer in data.computers) {
                        var isDelete = false
                        var date2: LocalDateTime = computer.dateChange
                        if (isDifferenceMoreThanMonths(date1, date2, 3)) {
                            isDelete = true
                        }
                        if (!isDelete) {
                            for (operation in computer.operations) {
                                date2 = operation.date
                                if (isDifferenceMoreThanMonths(date1, date2, 1)) {
                                    computer.operations.remove(operation)
                                }
                            }
                            if (computer.operations.size == 0) {
                                isDelete = true
                            }
                        }
                        if (isDelete) {
                            data.computers.remove(computer)
                        }
                    }
                }
                data
            } else {
                Data()
            }
        }
    }
}
