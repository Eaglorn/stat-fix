private lateinit var server: Server

fun main(args: Array<String>) {
    if(args[0] == "server") runServer(args[1], args[2])
}

private fun runServer(pathImport: String, pathExport: String) {
    server = Server(pathImport, pathExport)
    server.run()
}