package kiinse.programs.metricswindapi

import io.micronaut.runtime.Micronaut.run
import kiinse.programs.metricswindapi.security.authentication.AuthService

fun main(args: Array<String>) {
    run(AuthService::class.java, *args)
}