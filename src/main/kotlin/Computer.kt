import java.time.LocalDateTime

class Computer(name: String, var version: String, operation: ComputerOperation) {
    var name = name
        private set

    var ekp = ""
        private set

    var dateChange: LocalDateTime

    val operations: ArrayList<ComputerOperation> = ArrayList()

    init {
        ekp = name.substring(0, 5)
        operations.add(operation)
        dateChange = LocalDateTime.now()
    }

    fun dateUpdate() {
        dateChange = LocalDateTime.now()
    }
}
