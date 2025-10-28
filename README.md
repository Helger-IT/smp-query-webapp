# smp-query-webapp

Small web application that allows an arbitrary SMP and DNS query and returns JSON results
This is the API subset parts from https://peppol.helger.com for standalone usage.

This project is part of my Peppol solution stack. See https://github.com/phax/peppol for other components and libraries in that area.

A prebuild Docker image is available on Docker Hub as `phelger/smpqwa:latest` (for `linux/amd64`) and `phelger/smpqwa-arm64:latest` (for `linux/arm64`).

## News and Noteworthy

2025-10-20
* Replace API `/api/is-in-peppol/{smlid}/{participantID}` with `/api/ppidexistence/{smlid}/{participantID}` to be in sync with peppol.helger.com
* All the API implementations are now shared between this application and Peppol Practical 

2025-10-01
* Added new API `/api/is-in-peppol/{smlid}/{participantID}`

2025-09-17
* Updated the truststore for the latest Directory TLS certs
* Fixed a potential problem with DNS lookup results ending with '/'

2025-07-21
* Extended the truststore to contain Peppol PKI G3 CAs as well

## Supported query APIs

* SMP query all document types of a participant
    * GET `/api/smpquery/{SML-ID}/{participant-ID}[?businessCard=false&xmlSchemaValidation=true&verifySignature=true]`
* SMP query all endpoints of a participant for a certain document type
    * GET `/api/smpquery/{SML-ID}/{participant-ID}/{docType-ID}[?xmlSchemaValidation=true&verifySignature=true]`
* SMP query the Business Card of a participant
    * GET `/api/businesscard/{SML-ID}/{participant-ID}`
* Check if a participant is registered in the Peppol Network (via DNS)
    * GET `/api/ppidexistence/{SML-ID}/{participant-ID}`

See https://peppol.helger.com/public/locale-en_US/menuitem-tools-rest-api for the full API description.

Supported SML servers are `digittest` (Peppol SMK), `digitprod` (Peppol SML) and `autodetect` (automatic detection).

## Configuration parameters

Default configuration is provided in file `src/main/resources/application.properties`.
Each property can be overridden via environment variables and Java system properties - see https://github.com/phax/ph-commons/wiki/ph-config for details.

The default configuration should pretty much work out of the box.
Supported properties are:

* **`global.debug`**: global debug settings. Should always be `false`
* **`global.production`**: global production mode. Should always be `true`. This has nothing to do with the Peppol stage.
* **`webapp.datapath`**: where to store data. Does not need to be a persistent volume.
* **`webapp.checkfileaccess`**: check file access on startup. Should always be `false`.
* **`rest.log.exceptions`**: this property enables or disables the detailed logging of exceptions that occur while processing REST calls. By default the logging is disabled.
* **`rest.exceptions.payload`**: this property enables or disables the provision of HTTP response contents in case of errors from the REST API. By default this is disabled. 

SMP Client configuration properties as described on https://github.com/phax/peppol-commons/tree/master?tab=readme-ov-file#configuration

## Building from source

```
mvn clean install
```

Output: `target/*.war`

Afterwards build Docker image:

```
docker build --pull -t phelger/smpqwa .
```

## Running

Running the Docker image (deployed to Docker Hub):

```
docker run -d --name smpqwa -p 8080:8080 phelger/smpqwa
```

## Internal notes to myself

Deployment:

```
docker login -u phelger
docker push phelger/smpqwa
```

---

My personal [Coding Styleguide](https://github.com/phax/meta/blob/master/CodingStyleguide.md) |
It is appreciated if you star the GitHub project if you like it.