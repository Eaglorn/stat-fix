private lateinit var server: Server

fun main(args: Array<String>) {

    /*
     * String arg = args[0]; if (arg.equals("-server")) { runServer(args[1],
     * args[2]); }
     *
     * if (arg.equals("-client")) { runClient(args[1]); }
     */
    val startTime = System.currentTimeMillis()

    runServer("c:/statfix/info/", "c:/statfix/")

    val endTime = System.currentTimeMillis()
    val duration = endTime - startTime
    println("Время выполнения: $duration мс")
}

private fun runServer(pathImport: String, pathExport: String) {
    server = Server(pathImport, pathExport)
    server.run()
}