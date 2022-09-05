package no.nav.bakveientilarbeid.arbeidssoker

@kotlinx.serialization.Serializable
data class Underoppfolging(val underOppfolging: Boolean)

@kotlinx.serialization.Serializable
data class Arbeidssokerperioder(val arbeidssokerperioder: List<Arbeidssokerperiode>)

@kotlinx.serialization.Serializable
data class Arbeidssokerperiode(val fraOgMedDato: String, val tilOgMedDato: String?)

@kotlinx.serialization.Serializable
data class UnderoppfolgingMedStatus(val status: Int, val underoppfolging: Boolean?)

@kotlinx.serialization.Serializable
data class ArbeidssokerperiodeMedStatus(val status: Int, val arbeidssokerperioder: List<Arbeidssokerperiode>)

@kotlinx.serialization.Serializable
data class Arbeidssoker(
    val underoppfolging: UnderoppfolgingMedStatus,
    val arbeidssokerperioder: ArbeidssokerperiodeMedStatus
)
