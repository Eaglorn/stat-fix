private lateinit var server: Server
private lateinit var client: Client

fun main(args: Array<String>) {
    if (args.isNotEmpty()) {
        when (args[0]) {
            "server" -> {
                runServer()
            }

            "client" -> runClient(args[1])
            else -> {}
        }
    }
}

private fun runServer() {
    val configServer = ConfigServer.load()
    server = Server(configServer.infoSource, configServer.infoDestination)
    server.run()
}

private fun runClient(nameConfig: String) {
    val configClient = ConfigClient.load()
    client = Client(
        configClient.pathInfo,
        configClient.pathConfigs,
        configClient.pathResults,
        nameConfig,
        configClient.version
    )
    client.run()
}