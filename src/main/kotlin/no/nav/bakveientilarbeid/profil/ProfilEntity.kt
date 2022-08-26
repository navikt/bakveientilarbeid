package no.nav.bakveientilarbeid.profil

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable

@Serializable
data class Feedback(val updated: Instant, val valgt: String)

@Serializable
data class JaEllerNei(val oppdatert: String, val valg: String)

@Serializable
data class ProfilJson(
    val aiaHarMottattEgenvurderingKvittering: Boolean? = null,
    val aiaFeedbackMeldekortForklaring: Feedback? = null,
    val aiaFeedbackHjelpOgStotteForklaring: Feedback? = null,
    val aiaFeedbackHjelpOgStotteForklaringUngdom: Feedback? = null,
    val aiaFeedbackSvarFraRegistreringen: Feedback? = null,
    val aiaAvslaattEgenvurdering: Instant? = null,
    val aiaAvslaattEgenvurderingUke12: Instant? = null,
    val aiaValgtPengestotteVisning: String? = null,
    val aiaReaktiveringVisning: JaEllerNei? = null
)

data class ProfilEntity(
    val id: String,
    val brukerId: String,
    val profil: ProfilJson,
    val createdAt: Instant,
)
