package no.nav.bakveientilarbeid.config

import org.flywaydb.core.api.configuration.FluentConfiguration
import javax.sql.DataSource

object Flyway {
    fun configure(dataSource: DataSource): FluentConfiguration {
        return org.flywaydb.core.Flyway.configure().dataSource(dataSource)
    }
}
