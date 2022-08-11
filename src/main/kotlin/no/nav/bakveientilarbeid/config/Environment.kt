package no.nav.bakveientilarbeid.config


data class Environment(
    val corsAllowedOrigins: String = requireProperty("CORS_ALLOWED_ORIGINS"),
    val corsAllowedSchemes: String = requireProperty("CORS_ALLOWED_SCHEMES"),
    val ptoProxyUrl: String = requireProperty("PTO_PROXY_URL"),
    val unleashApiUrl: String = requireProperty("UNLEASH_API_URL"),
    val dbUser: String? = getPropertyOrNull("${databaseEnvProperty()}_USERNAME"),
    val dbPassword: String? = getPropertyOrNull("${databaseEnvProperty()}_PASSWORD"),
    val dbUrl: String? = getPropertyOrNull("${databaseEnvProperty()}_URL")
)

fun requireProperty(property: String) =
    getPropertyOrNull(property) ?: throw IllegalStateException("Missing required property $property")

fun getPropertyOrNull(property: String): String? =
    System.getProperty(property, System.getenv(property))

fun requireClusterName() =
    requireProperty("NAIS_CLUSTER_NAME")

fun requireApplicationName() =
    requireProperty("NAIS_APP_NAME")

fun applicationNameOrNull() =
    getPropertyOrNull("NAIS_APP_NAME")

fun isDevelopment(): Boolean =
    requireClusterName().startsWith("dev")

fun databaseEnvProperty(): String = "NAIS_DATABASE_BAKVEIENTILARBEID_BAKVEIENTILARBEID${if (isDevelopment()) "_DEV" else ""}"

fun ifDevelopment(block: () -> Any) {
    if (isDevelopment()) {
        run(block)
    }
}

fun requireMeldekortAppName() =
    requireProperty("MELDEKORT_APP_NAME")
