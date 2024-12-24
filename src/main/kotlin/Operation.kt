import java.time.LocalDateTime

class Operation(date: LocalDateTime, version: String) {
    val fixes: ArrayList<String> = ArrayList()

    fun addFix(fix: String) {
        fixes.add(fix)
    }

    val date: LocalDateTime

    var version = ""

    init {
        this.date = date
        this.version = version
    }

    fun sort() {
        fixes.sortWith { s1, s2 -> s1.toInt().compareTo(s2.toInt()) }
    }
}
