package no.nav.bakveientilarbeid.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource

class PostgreSqlDatabase(env: Map<String, String?>) : Database {
    private val envDataSource: HikariDataSource


    init {
        envDataSource = createConnection(env)
    }

    override val dataSource: HikariDataSource
        get() = envDataSource


    private fun createConnection(env: Map<String, String?>): HikariDataSource {
        requireNotNull(env["dbUrl"]) { "database url mangler" }
        requireNotNull(env["dbUser"]) { "database brukernavn mangler" }
        requireNotNull(env["dbPassword"]) { "database passord mangler" }

        val config = hikariCommonConfig(env).apply {
            username = env["dbUser"]
            password = env["dbPassword"]
            validate()
        }
        return HikariDataSource(config)
    }


    private fun hikariCommonConfig(env: Map<String, String?>): HikariConfig {
        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = env["dbUrl"]
            minimumIdle = 1
            maxLifetime = 30001
            maximumPoolSize = 3
            connectionTimeout = 500
            validationTimeout = 250
            idleTimeout = 10001
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
        }
        return config
    }

}
