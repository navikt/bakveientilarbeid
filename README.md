# bakveientilarbeid
Enkel backend for [veientilarbeid](https://github.com/navikt/veientilarbeid).

Proxyer kall til tjenester som krever tokenx.
Foretar tokenx utveksling mot tokendings for å veksle loginservice-token on the fly.

## Autentisering
Benytter `implementation(Tms.KtorTokenSupport.<modul>)`

Se [navikt/tms-ktor-token-support](https://github.com/navikt/tms-ktor-token-support) for mer info om bruk.

# Kom i gang
1. Bygg bakveientilarbeid ved å kjøre `mvn clean install`
2. Start appen lokalt ved å kjøre `BootstrapLocalKT` fra IDEA
3. Appen nås på `http://localhost:8101/bakveientilarbeid`
   * F.eks. via `curl http://localhost:8101/bakveientilarbeid/internal/isAlive`
4. For å kjøre database-tester lokalt må du ha en kjørende postgresql docker container som testcontainer kan koble seg til
## For NAV-ansatte
Interne henvendelser kan sendes via Slack i kanalen #team-paw-dev.
