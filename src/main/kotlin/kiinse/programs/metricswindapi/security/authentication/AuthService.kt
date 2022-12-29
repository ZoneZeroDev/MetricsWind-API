package kiinse.programs.metricswindapi.security.authentication

import io.micronaut.http.HttpStatus
import jakarta.inject.Inject
import jakarta.inject.Singleton
import kiinse.programs.kiinseapi.core.exceptions.AccountException
import kiinse.programs.kiinseapi.core.exceptions.AuthException
import kiinse.programs.metricswindapi.mongo.queries.AccountQuery
import kiinse.programs.metricswindapi.security.Account

@Singleton
class AuthService {

    @Inject
    private var accountQuery: AccountQuery? = null

    @Throws(AccountException::class, AuthException::class)
    fun login(token: String?): Account {
        if (token.isNullOrBlank()) throw AuthException(HttpStatus.UNAUTHORIZED, "Auth token is blank!")
        return accountQuery!!.getAccount(token) ?: throw AccountException(HttpStatus.UNAUTHORIZED, "Account with this token not founded!")
    }
}