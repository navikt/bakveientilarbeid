package no.nav.bakveientilarbeid.database

import com.zaxxer.hikari.HikariDataSource
import no.nav.bakveientilarbeid.config.Flyway

class H2Database(val dbname: String) : Database {
    override val dataSource: HikariDataSource = HikariDataSource().apply {
        jdbcUrl = "jdbc:h2:mem:$dbname"
        username = "sa"
        password = ""

        validate()
    }

    init {
        Flyway.configure(dataSource).load().migrate()
    }

    fun closeConnection() {
        dataSource.close()
    }
}
