package kiinse.programs.metricswindapi.metricswind.restbody

data class Metrics(val pluginId: Long,
                   val plugin: MetricsPlugin,
                   val location: MetricsLocation,
                   val server: MetricsServer,
                   val data: MetricsData? = null)
