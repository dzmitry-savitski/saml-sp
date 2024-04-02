# Spring boot SAML SP (service provider)
Use for testing SAML

## Usage
1. Create application.yml file to configure the sp side. Example:
```
spring:
  security:
    saml2:
      relyingparty:
        registration:
          sp:
            ################################# SP CONFIGURATION #############################################################
            entity-id: "sp:entity:id"
            signing:
              credentials:
                - private-key-location: "classpath:certificates/sp.key"
                  certificate-location: "classpath:certificates/sp.crt"
            decryption:
              credentials:
                - private-key-location: "classpath:certificates/sp.key"
                  certificate-location: "classpath:certificates/sp.crt"
            acs:
              location: "{baseUrl}/login/saml2/sso/{registrationId}"
              binding: POST
            nameidformat: "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified"
            ################################# IDP CONFIGURATION #############################################################
            assertingparty:
              metadata-uri: https://idp/metadata
```
2. Run the app:
```
java -jar saml-sp.jar
```

## Options
IDP can be configured manually. Example:
```
spring:
  security:
    saml2:
      relyingparty:
        registration:
          sp:
            assertingparty:
              entity-id: "idp:entity:id"
              singlesignon:
                url: "https://idp/sso/saml"
                binding: REDIRECT
                sign-request: true
              verification:
                credentials:
                  - certificate-location: "file:/home/user/cert.pem"
```