# bakveientilarbeid

Enkel backend for veientilarbeid. Proxyer kall til tjenester som krever tokenx. Foretar tokenx utveksling mot tokendings for å veksle loginservice-token on the fly. 


## Autentisering

Benytter `implementation(Tms.KtorTokenSupport.<modul>)` 

Referer til repo [navikt/tms-ktor-token-support](https://github.com/navikt/tms-ktor-token-support) for mer info om bruk.

# Kom i gang
1. Bygg bakveientilarbeid ved å kjøre `gradle build`
1. Start appen lokalt ved å kjøre `gradle runServer`
1. Appen nås på `http://localhost:8101/bakveientilarbeid`
   * F.eks. via `curl http://localhost:8101/bakveientilarbeid/internal/isAlive`
   
## For NAV-ansatte

Interne henvendelser kan sendes via Slack i kanalen #team-paw-dev.
