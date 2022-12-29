package kiinse.programs.metricswindapi.metricswind.restbody

data class MetricsPlugin(val name: String,
                         val online: Int,
                         val maxPlayers: Int,
                         val version: String)
