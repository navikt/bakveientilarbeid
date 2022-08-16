package no.nav.bakveientilarbeid.profil

import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

interface ProfilRepository {
    fun hentProfil(bruker: AuthenticatedUser): ProfilEntity?
    fun lagreProfil(bruker: AuthenticatedUser, profil: ProfilJson)
}
