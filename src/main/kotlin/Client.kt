import org.apache.commons.io.FileUtils
import java.io.File
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Client(private val pathInfo: String, private val pathConfigs: String, private val pathResults: String, private val nameConfig: String, private val version:String) {
    private lateinit var data: Data
    private lateinit var configResult: ConfigResult
    fun run() {
        data = Data.load(pathInfo, false)
        configResult = ConfigResult.load(pathConfigs, nameConfig)

        var computers: ArrayList<Computer> = data.computers

        lateinit var listComputers: ArrayList<String>
        val emptyComputers: ArrayList<String> = ArrayList()

        val computerResult: ArrayList<String> = ArrayList()

        computers = ArrayList(computers.filter { it -> it.version == version })

        if(configResult.ekp.value) {
            computers = ArrayList(computers.filter { it -> it.ekp == configResult.ekp.name })
        }

        if(configResult.listComputers.value) {
            listComputers = ArrayList(FileUtils.readLines(File(pathConfigs + configResult.listComputers.name), "UTF8"))
            for(computer in listComputers) {
                emptyComputers.add(computer)
            }
            computers = ArrayList(computers.filter { it -> listComputers.contains(it.name) })
        }

        if(configResult.date.value) {
            val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy")
            val removeComputers: ArrayList<Computer> = ArrayList()
            val removeOperations: ArrayList<ComputerOperation> = ArrayList()
            if(configResult.date.type == "one") {
                for(computer in computers) {
                    for(operation in computer.operations) {
                        if(!operation.date.format(formatter).equals(configResult.date.date)) {
                            removeOperations.add(operation)
                        }
                    }
                    for(operation in removeOperations) {
                        computer.operations.remove(operation)
                    }
                    if(computer.operations.isEmpty()) {
                        removeComputers.add(computer)
                    }
                }
                for(computer in removeComputers) {
                    computers.remove(computer)
                }
            } else if(configResult.date.type == "range") {
                val localDateList: ArrayList<String> = ArrayList(configResult.date.date.split(" ", limit = 2))
                val localDateStart: LocalDate = LocalDate.parse(localDateList[0], formatter)
                val localDateEnd: LocalDate = LocalDate.parse(localDateList[1], formatter)

                for(computer in computers) {
                    for(operation in computer.operations) {
                        val localDate: LocalDate = operation.date.toLocalDate()
                        if(!(localDate.isEqual(localDateStart) || localDate.isEqual(localDateEnd) || (localDate.isAfter(localDateStart) && localDate.isBefore(localDateEnd)))) {
                            removeOperations.add(operation)
                        }
                    }
                    for(operation in removeOperations) {
                        computer.operations.remove(operation)
                    }
                    if(computer.operations.isEmpty()) {
                        removeComputers.add(computer)
                    }
                }
                for(computer in removeComputers) {
                    computers.remove(computer)
                }
            }
        }

        if(configResult.countAllFixes) {
            var countAllFixes: Int = 0
            for (computer in computers) {
                for (operation in computer.operations) {
                    countAllFixes = countAllFixes.plus(operation.fixes.size)
                }
            }
            computerResult.add("Общее количество: ${countAllFixes}")
            computerResult.add("")
        }

        var maxCountFixes = 1;

        for(computer in computers) {
            if(configResult.countSeparateFixes) {
                var countFixes: Int = 0
                for (operation in computer.operations) {
                    countFixes = countFixes.plus(operation.fixes.size)
                }
                if (countFixes >= 100 && maxCountFixes < 3) {
                    maxCountFixes = 3
                } else if(countFixes >= 10 && maxCountFixes < 2) {
                    maxCountFixes = 2
                }
            }
        }

        for(computer in computers) {
            var text: String = ""
            text = text.plus(computer.name)
            if(configResult.countSeparateFixes) {
                var countFixes: Int = 0
                for (operation in computer.operations) {
                    countFixes = countFixes.plus(operation.fixes.size)
                }

                var countNumbers = 1

                if (countFixes >= 100) {
                    countNumbers = 3
                } else if(countFixes >= 10) {
                    countNumbers = 2
                }

                var textSpace = when (maxCountFixes - countNumbers) {
                    0 -> ""
                    1 -> " "
                    else -> "  "
                }

                text = text.plus(" | Количество: ${textSpace}${countFixes}")
            }
            if(configResult.listFixes) {
                var fixes: String = ""
                val listFixes: ArrayList<String> = ArrayList()
                for (operation in computer.operations) {
                    for(fix in operation.fixes) {
                        listFixes.add(fix)
                    }
                }
                fixes = fixes.plus(listFixes.joinToString(separator = ","))
                text = text.plus(" | Список: ${fixes}")
            }
            computerResult.add(text)
            if(configResult.listComputers.value) {
                emptyComputers.remove(computer.name)
            }
        }

        if(configResult.listComputers.value) {
            if(emptyComputers.isNotEmpty()) {
                computerResult.add("")
                computerResult.add("Компьютеры не соответствующие условиям выборки:")
                for(computer in emptyComputers) {
                    computerResult.add(computer)
                }
            }
        }

        var text: String = ""

        if(computerResult.isNotEmpty()) {
            for(result in computerResult) {
                text = text.plus(result).plus("\n")
            }
        }

        FileUtils.write(File(pathResults + configResult.saveFile), text, "UTF8")
    }
}
