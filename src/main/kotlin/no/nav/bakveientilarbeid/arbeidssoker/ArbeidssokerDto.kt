package no.nav.bakveientilarbeid.arbeidssoker

import kotlinx.datetime.Instant

@kotlinx.serialization.Serializable
data class Underoppfolging(val underOppfolging: Boolean)

@kotlinx.serialization.Serializable
data class Arbeidssokerperioder(val arbeidssokerperioder: List<Arbeidssokerperiode>)

@kotlinx.serialization.Serializable
data class Arbeidssokerperiode(val fraOgMedDato: Instant, val tilOgMedDato: Instant?)

@kotlinx.serialization.Serializable
data class Arbeidssoker(val underoppfolging: Boolean?, val arbeidssokerperioder: List<Arbeidssokerperiode>)
