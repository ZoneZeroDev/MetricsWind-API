package kiinse.programs.metricswindapi.mongo.queries

import com.mongodb.client.MongoCollection
import jakarta.inject.Singleton
import kiinse.programs.kiinseapi.core.exceptions.ConfigException
import kiinse.programs.metricswindapi.mongo.MongoDb
import kiinse.programs.metricswindapi.security.Account
import org.bson.Document
import java.util.UUID

@Suppress("UNUSED")
@Singleton
class AccountQuery {

    private var collection: MongoCollection<Document>? = null

    init {
        reload()
    }

    @Throws(ConfigException::class)
    fun reload() {
        collection = MongoDb.getDataBase().getCollection("accounts")
    }

    fun hasAccount(uuid: UUID): Boolean {
        val query = Document()
        query["_id"] = uuid.toString()
        val result = collection!!.find(query).first()
        return result != null && !result.isEmpty()
    }

    fun hasAccount(token: String): Boolean {
        val query = Document()
        query["token"] = token
        val result = collection!!.find(query).first()
        return result != null && !result.isEmpty()
    }

    fun hasAccount(account: Account): Boolean {
        val query = Document()
        query["_id"] = account.uuid.toString()
        val result = collection!!.find(query).first()
        return result != null && !result.isEmpty()
    }

    fun getAccount(uuid: UUID): Account? {
        val query = Document()
        query["_id"] = uuid.toString()
        return parseAccountResult(collection!!.find(query).first())
    }

    fun getAccount(token: String): Account? {
        val query = Document()
        query["token"] = token
        return parseAccountResult(collection!!.find(query).first())
    }

    @Suppress("UNCHECKED_CAST")
    private fun parseAccountResult(result: Document?): Account? {
        if (result == null || result.keys.size < 3) return null
        return Account(UUID.fromString(result["_id"].toString()),
                       result["username"].toString(),
                       result["login"].toString(),
                       result["password"].toString(),
                       result["token"].toString(),
                       arrayToSet((result["plugins"] as ArrayList<Int>?)!!))
    }

    fun countAccounts(): Long {
        return collection!!.countDocuments()
    }

    val allAccounts: Set<Account?>
        get() {
            val result = HashSet<Account?>()
            for (document in collection!!.find()) {
                result.add(parseAccountResult(document))
            }
            return result
        }

    private fun arrayToSet(values: ArrayList<Int>): Set<Int> {
        val set = HashSet<Int>()
        for (value in values) {
            set.add(value)
        }
        return set
    }
}