import java.time.LocalDateTime

class ComputerOperation(val date: LocalDateTime, version: String) {
    val fixes: ArrayList<String> = ArrayList()

    fun addFix(fix: String) {
        fixes.add(fix)
    }

    var version = ""

    init {
        this.version = version
    }

    fun sort() {
        fixes.sortWith { s1, s2 -> s1.toInt().compareTo(s2.toInt()) }
    }
}
