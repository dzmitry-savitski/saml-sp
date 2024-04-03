# Spring boot SAML SP (service provider)
Use for testing SAML

## Usage
1. Put application.properties in the same folder with the saml-sp.jar. 
2. Run the app:
```
java -jar saml-sp.jar
```


## Options
IDP can be configured using the metadata or manually.
Properties file example:
```
# SP configuration
saml.sp.auth-endpoints=/profile/**
saml.sp.registrationid=test-sp
saml.sp.entityid={baseUrl}/{registrationId}
saml.sp.nameid-format=urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified
saml.sp.signing.privatekey-location=classpath:certificates/sp.key
saml.sp.signing.certificate-location=classpath:certificates/sp.crt
saml.sp.encryption.privatekey-location=classpath:certificates/sp.key
saml.sp.encryption.certificate-location=classpath:certificates/sp.crt
#saml.sp.encryption.privatekey-location=file:/tmp/sp.key
#saml.sp.encryption.certificate-location=file:/tmp/sp.crt
saml.sp.sign-authn-request=false
saml.sp.acs.location={baseUrl}/sso/{registrationId}
saml.sp.acs.binding=POST

# IDP configuration for SP
saml.idp.metadata-location=https://idp/sso/saml/metadata
#saml.idp.entityid=idp:test
#saml.idp.singlesignon.url=https://idp/sso/saml
#saml.idp.singlesignon.binding=POST
#saml.idp.singlesignon.want-request-signed=false
#saml.idp.verification.certificate-location=file:/tmp/idp.pem

# Application configuration

# login URL should match (!) ACS location
sso.login-processing-url=/sso/test-sp
```

## TODO:
 - ADD SLO support