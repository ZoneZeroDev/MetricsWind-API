package kiinse.programs.metricswindapi.metricswind.restbody

data class MetricsData(val name: String,
                       val graphId: Int,
                       val value: Any)