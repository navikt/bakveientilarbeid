package no.nav.bakveientilarbeid.profil

import no.nav.personbruker.dittnav.common.security.AuthenticatedUser

class ProfilService(val profilRepository: ProfilRepository) {
    fun hentProfil(bruker: AuthenticatedUser): ProfilEntity? {
        return profilRepository.hentProfil(bruker.ident)
    }
    fun lagreProfil(bruker: AuthenticatedUser, profil: ProfilJson) {
        profilRepository.lagreProfil(bruker.ident, profil)
    }
}
