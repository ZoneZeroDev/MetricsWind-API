package kiinse.programs.metricswindapi.mongo

import kiinse.programs.kiinseapi.core.config.YamlConfiguration
import kiinse.programs.kiinseapi.core.database.DataBaseSettings
import kiinse.programs.kiinseapi.core.database.MongoConnection

@Suppress("UNCHECKED_CAST")
object MongoDb : MongoConnection() {

    init {
        connect()
    }

    override fun getSettings(): DataBaseSettings {
        val config = YamlConfiguration().copyAndLoad(this.javaClass, "config.yml")?.get("mongo.api") as HashMap<String, String>
        return DataBaseSettings()
                .setHost(config["host"].toString())
                .setPort(config["port"].toString())
                .setLogin(config["login"].toString())
                .setPassword(config["password"].toString())
                .setDbName(config["dbName"].toString())
                .setAuthDb(config["authDb"].toString())
    }

    override fun createTables() {
        createCollectionIfNotExists("accounts")
        createCollectionIfNotExists("authenticationRequests")
    }
}