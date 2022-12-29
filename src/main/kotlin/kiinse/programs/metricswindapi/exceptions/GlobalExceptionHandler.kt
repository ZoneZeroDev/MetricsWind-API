package kiinse.programs.metricswindapi.exceptions

import io.micronaut.context.annotation.Requires
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.annotation.Produces
import io.micronaut.http.codec.CodecException
import io.micronaut.http.exceptions.HttpStatusException
import io.micronaut.http.server.exceptions.ExceptionHandler
import jakarta.inject.Singleton
import kiinse.programs.kiinseapi.core.exceptions.APIExceptionBase
import kiinse.programs.metricswindapi.utils.Response
import kiinse.programs.metricswindapi.utils.ResponseFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Produces
@Singleton
@Requires(classes = [Exception::class, ExceptionHandler::class])
class GlobalExceptionHandler : ExceptionHandler<Exception?, HttpResponse<Response>> {

    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    override fun handle(request: HttpRequest<*>?, exception: Exception?): HttpResponse<Response> {
        return when (exception) {
            is HttpStatusException -> ResponseFactory.create(exception)
            is CodecException      -> ResponseFactory.create(HttpStatus.NOT_ACCEPTABLE,
                                                             APIExceptionBase(HttpStatus.NOT_ACCEPTABLE,
                                                                              "Cannot convert body to object due to invalid data in it!"))
            else                   -> {
                logger.warn("Handled anomaly exception! Message:" + exception?.message)
                ResponseFactory.create(HttpStatus.INTERNAL_SERVER_ERROR, exception)
            }
        }
    }
}