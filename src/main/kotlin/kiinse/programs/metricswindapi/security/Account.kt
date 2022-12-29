package kiinse.programs.metricswindapi.security

import java.util.*

data class Account(val uuid: UUID,
                   val userName: String,
                   val login: String,
                   val password: String,
                   val token: String,
                   val plugins: Set<Int>) {

    override fun hashCode(): Int {
        return toString().hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (other != null && other is Account) {
            return other.hashCode() == hashCode()
        }
        return false
    }

    override fun toString(): String {
        return "uuid=${uuid}\nusername=$userName\nlogin=$login\ntoken=$token"
    }
}