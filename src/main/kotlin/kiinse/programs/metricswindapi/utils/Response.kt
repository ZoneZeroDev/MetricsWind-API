package kiinse.programs.metricswindapi.utils

import io.micronaut.http.HttpStatus

data class Response(val data: Any?, val status: HttpStatus)