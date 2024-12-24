import java.time.LocalDateTime

class Computer(name: String, var version: String, operation: Operation) {
    var name = name
        private set

    var ekp = ""
        private set

    var dateChange: LocalDateTime

    val operations: ArrayList<Operation> = ArrayList()

    init {
        ekp = name.substring(0, 5)
        operations.add(operation)
        dateChange = LocalDateTime.now()
    }

    fun dateUpdate() {
        dateChange = LocalDateTime.now()
    }
}
