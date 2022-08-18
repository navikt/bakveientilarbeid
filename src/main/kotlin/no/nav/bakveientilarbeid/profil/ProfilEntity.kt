package no.nav.bakveientilarbeid.profil

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Feedback(@Contextual val updated: LocalDateTime, val  state: Boolean)

@Serializable
data class ProfilJson(
    val aiaHarMottattEgenvurderingKvittering: Boolean? = null,
    val aiaFeedbackMeldekortForklaring: Feedback? = null,
    val aiaFeedbackHjelpOgStotteForklaring: Feedback? = null,
    val aiaFeedbackHjelpOgStotteForklaringUngdom: Feedback? = null,
    val aiaFeedbackSvarFraRegistreringen: Feedback? = null,
    val aiaAvslaattEgenvurdering: LocalDateTime? = null,
    val aiaAvslaattEgenvurderingUke12: LocalDateTime? = null,
    val aiaValgtPengestotteVisning: String? = null
)

data class ProfilEntity(
    val id: String,
    val brukerId: String,
    val profil: ProfilJson,
    val createdAt: LocalDateTime,
)
