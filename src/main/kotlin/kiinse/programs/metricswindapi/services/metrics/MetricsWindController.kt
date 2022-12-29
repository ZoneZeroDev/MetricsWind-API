package kiinse.programs.metricswindapi.services.metrics

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Body
import io.micronaut.http.annotation.Controller
import io.micronaut.http.annotation.Get
import io.micronaut.http.annotation.Post
import kiinse.programs.metricswindapi.security.annotations.Authentication
import kiinse.programs.metricswindapi.utils.Response
import kiinse.programs.metricswindapi.utils.ResponseFactory

@Controller("/controller")
open class MetricsWindController {

    @Get
    fun index(): HttpResponse<Response> {
        return ResponseFactory.create(HttpStatus.OK)
    }

    @Post("/add")
    @Authentication(timeout = 10)
    open fun addMetrics(request: HttpRequest<String?>, @Body metrics: Metrics): HttpResponse<Response> {
        return ResponseFactory.create(HttpStatus.OK)
    }
}