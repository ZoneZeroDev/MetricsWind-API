package kiinse.programs.metricswindapi.security.authentication

import java.net.InetSocketAddress
import java.util.*
import java.util.concurrent.TimeUnit

object AuthTimeout {

    private val timeOut = HashMap<String, Date>()

    fun isTimeOuted(user: String, method: String, address: InetSocketAddress, time: Int): Boolean {
        val account = "$user::$method::${address.hostString}::${address.hostName}"
        val timeOutDate = timeOut[account]
        if (timeOutDate != null) {
            if (TimeUnit.MILLISECONDS.toSeconds(Date().time - timeOutDate.time) < time) return true
            timeOut.remove(account)
            return false
        }
        timeOut[account] = Date()
        return false
    }
}