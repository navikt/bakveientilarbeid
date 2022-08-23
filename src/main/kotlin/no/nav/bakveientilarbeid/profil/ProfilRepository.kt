package no.nav.bakveientilarbeid.profil

interface ProfilRepository {
    fun hentProfil(brukerId: String): ProfilEntity?
    fun lagreProfil(brukerId: String, profil: ProfilJson)
}
