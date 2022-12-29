package kiinse.programs.metricswindapi.utils

import io.micronaut.http.HttpResponse
import io.micronaut.http.HttpStatus
import io.micronaut.http.exceptions.HttpStatusException
import org.json.JSONArray
import org.json.JSONObject

@Suppress("UNUSED")
object ResponseFactory {
    fun create(status: HttpStatus, exception: Exception): HttpResponse<Response> {
        val data = JSONObject()
        data.put("message", exception.message)
        return create(status, data)
    }

    fun create(exception: HttpStatusException): HttpResponse<Response> {
        val data = JSONObject()
        data.put("message", exception.message)
        return create(exception.status, data)
    }

    fun create(status: HttpStatus): HttpResponse<Response> {
        return HttpResponse.ok(Response(null, status)).status(status)
    }

    fun create(status: HttpStatus, data: JSONObject): HttpResponse<Response> {
        return HttpResponse.ok(Response(data.toMap(), status)).status(status)
    }

    fun create(status: HttpStatus, data: JSONArray): HttpResponse<Response> {
        return HttpResponse.ok(Response(data.toList(), status)).status(status)
    }

    fun create(status: HttpStatus, data: Any?): HttpResponse<Response> {
        return HttpResponse.ok(Response(data, status)).status(status)
    }
}