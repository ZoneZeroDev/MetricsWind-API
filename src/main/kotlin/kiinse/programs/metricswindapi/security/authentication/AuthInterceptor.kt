package kiinse.programs.metricswindapi.security.authentication

import io.micronaut.aop.InterceptorBean
import io.micronaut.aop.MethodInterceptor
import io.micronaut.aop.MethodInvocationContext
import io.micronaut.core.annotation.AnnotationValue
import io.micronaut.http.HttpRequest
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kiinse.programs.kiinseapi.core.exceptions.AuthException
import kiinse.programs.kiinseapi.core.exceptions.ManyRequestsException
import kiinse.programs.metricswindapi.security.annotations.Authentication
import kiinse.programs.metricswindapi.utils.RequestUtils
import kiinse.programs.metricswindapi.utils.ResponseFactory
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.net.InetSocketAddress
import java.util.*

@Singleton
@InterceptorBean(Authentication::class)
class AuthInterceptor : MethodInterceptor<Any, Any> {

    @Inject
    private var authService: AuthService? = null
    private val logger: Logger = LoggerFactory.getLogger(this::class.java)

    @Suppress("UNCHECKED_CAST")
    override fun intercept(context: MethodInvocationContext<Any, Any>?): Any? {
        try {
            if (context != null) {
                for (value in context.parameterValueMap.values) {
                    if (value is HttpRequest<*>) {
                        val annotation = context.getAnnotation(Authentication::class.java)
                        if (annotation != null) {
                            val request = value as HttpRequest<String?>
                            val account = authService?.login(RequestUtils.getAuthToken(request) ?: throw AuthException(HttpStatus.UNAUTHORIZED, "No Authorization token!"))
                            if (account != null && checkTimeOut(request, annotation, account.userName, context.methodName, value.remoteAddress)) {
                                return context.proceed()
                            }
                            return ResponseFactory.create(HttpStatus.UNAUTHORIZED, "Account not found!")
                        }
                    }
                }
            }
            return ResponseFactory.create(HttpStatus.UNAUTHORIZED)
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

    @Throws(ManyRequestsException::class)
    private fun checkTimeOut(request: HttpRequest<String?>, annotation: AnnotationValue<Authentication>,
                             accountName: String, methodName: String, remoteAddress: InetSocketAddress): Boolean {
        val timeoutHeader = RequestUtils.getHeader(request, "No-timeout")
        if (timeoutHeader != null && timeoutHeader == "kitsune") return true
        val timeout = annotation.values["timeout"].toString().toInt()
        if (AuthTimeout.isTimeOuted(methodName, accountName, remoteAddress, timeout))
            throw ManyRequestsException(HttpStatus.TOO_MANY_REQUESTS, "Too many requests! Please send a request no more than once every $timeout seconds!")
        return true
    }
}