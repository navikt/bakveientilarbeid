package no.nav.bakveientilarbeid.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import no.nav.bakveientilarbeid.config.Environment

class PostgreSqlDatabase(env: Environment) : Database {
    private val envDataSource: HikariDataSource

    init {
        envDataSource = createConnection(env)
    }

    override val dataSource: HikariDataSource
        get() = envDataSource


    private fun createConnection(env: Environment): HikariDataSource {
        val config = hikariCommonConfig(env).apply {
            username = env.dbUser
            password = env.dbPassword
            validate()
        }
        return HikariDataSource(config)
    }


    private fun hikariCommonConfig(env: Environment): HikariConfig {
        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl = env.dbUrl
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
