package kiinse.programs.metricswindapi.utils

import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import jakarta.inject.Inject
import kiinse.programs.kiinseapi.core.exceptions.RSAException
import kiinse.programs.kiinseapi.core.exceptions.RequestException
import kiinse.programs.metricswindapi.security.Account
import kiinse.programs.metricswindapi.security.authentication.AuthService
import org.json.JSONException
import org.json.JSONObject
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Suppress("UNUSED")
object RequestUtils {

    @Inject
    private var authService: AuthService? = null
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    fun runOnAccount(request: HttpRequest<String?>, runnable: (Account) -> HttpResponse<Response>): HttpResponse<Response> {
        return runWithCatch { runnable(authService!!.login(getAuthToken(request))) }
    }

    fun runOnBody(request: HttpRequest<String?>, runnable: (JSONObject, Account) -> HttpResponse<Response>): HttpResponse<Response> {
        return runWithCatch { runnable(getBodyJson(request), authService!!.login(getAuthToken(request))) }
    }

    fun runOnBody(request: HttpRequest<String?>, runnable: (JSONObject) -> HttpResponse<Response>): HttpResponse<Response> {
        return runWithCatch { runnable(getBodyJson(request)) }
    }

    fun runWithCatch(runnable: () -> HttpResponse<Response>): HttpResponse<Response> {
        return try {
            runnable()
        } catch (exception: Exception) {
            return when (exception) {
                is HttpStatusException -> ResponseFactory.create(exception)
                else                   -> {
                    logger.warn("Handled anomaly exception! Message:" + exception.message)
                    ResponseFactory.create(HttpStatus.INTERNAL_SERVER_ERROR, exception)
                }
            }
        }
    }

    fun getAuthToken(request: HttpRequest<String?>): String? {
        val headers = request.headers
        if (headers.isEmpty) return null
        return headers["Authorization"]
    }

    fun getHeader(request: HttpRequest<String?>, header: String): String? {
        val headers = request.headers
        if (headers.isEmpty) return null
        return headers[header] ?: return null
    }

    @Throws(JSONException::class, RSAException::class, RequestException::class)
    fun getBodyJson(request: HttpRequest<String?>): JSONObject {
        val optionalBody = request.body
        if (optionalBody.isEmpty || optionalBody.get().isBlank()) throw RequestException(HttpStatus.NOT_ACCEPTABLE, "No content in request body!")
        return JSONObject(optionalBody.get().toByteArray().toString(charset("UTF-8")))
    }
}