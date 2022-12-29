package kiinse.programs.metricswindapi.security.annotations

import io.micronaut.aop.Around
import io.micronaut.core.annotation.Internal

@Suppress("UNUSED")
@Target(AnnotationTarget.FUNCTION)
@Retention(AnnotationRetention.RUNTIME)
@Around
@Internal
annotation class Authentication(val timeout: Int = 0)