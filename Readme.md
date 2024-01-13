# Docker SAML SP
Docker SAML service provider (sp). It's a wrapper around https://github.com/mcguinness/saml-sp

## Quick start
`docker run -it -p 7070:7070 dsavitski/saml-sp --idpMetaUrl https://dev-8902837.okta.com/app/exkeg466qiHoEKxkT5d7/sso/saml/metadata`

## Usage
### Dynamic IdP Configuration from IdP Metadata (Recommended)
`docker run -it -p 7070:7070 dsavitski/saml-sp --idpMetaUrl {url}`

**Example:**\
`docker run -it -p 7070:7070 dsavitski/saml-sp --idpMetaUrl https://dev-8902837.okta.com/app/exkeg466qiHoEKxkT5d7/sso/saml/metadata`

### Static IdP Configuration with Certificate
`docker run -it -p 7070:7070 dsavitski/saml-sp --iss {issuer} --idpSsoUrl {url} --idpCert {pem}`

**Example:**\
`docker run -it -p 7070:7070 dsavitski/saml-sp --iss http://www.okta.com/exknnoOGPcwWSnKUK0g3 --idpSsoUrl https://example.okta.com/app/example_saml/exknnoOGPcwWSnKUK0g3/sso/saml --idpCert ./idp-cert.pem`

### Static IdP Configuration with SHA1 Thumbprint
`docker run -it -p 7070:7070 dsavitski/saml-sp --iss {issuer} --idpSsoUrl {url} --idpThumbprint {sha1}`

**Example:**\
`docker run -it -p 7070:7070 dsavitski/saml-sp --iss http://www.okta.com/exknnoOGPcwWSnKUK0g3 --idpSsoUrl https://example.okta.com/app/example_saml/exknnoOGPcwWSnKUK0g3/sso/saml --idpThumbprint 77:87:4A:86:18:B3:CB:44:C2:EB:68:1B:77:0B:1D:F6:4A:0E:88:E7`

### Options
`docker run -it -p 7070:7070 dsavitski/saml-sp --help`

```
Options:
  --version                      Show version number                                                                                                       [boolean]
  --settings                     Path to JSON config file
  --port, -p                     Web Server listener port                                                                        [number] [required] [default: 7070]
  --protocol                     Federation Protocol                                                                          [string] [required] [default: "samlp"]
  --idpIssuer, --iss             IdP Issuer URI                                                                                [string] [default: "urn:example:idp"]
  --idpSsoUrl                    IdP Single Sign-On Service URL (SSO URL)                                                                                   [string]
  --idpSsoBinding                IdP Single Sign-On AuthnRequest Binding         [string] [required] [default: "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect"]
  --idpSloUrl                    IdP Single Logout Service URL (SLO URL) (SAMLP)                                                                            [string]
  --idpSloBinding                IdP Single Logout Request Binding (SAMLP)       [string] [required] [default: "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-Redirect"]
  --idpCert                      IdP Public Key Signing Certificate (PEM)                                                                                   [string]
  --idpThumbprint                IdP Public Key Signing Certificate SHA1 Thumbprint                                                                         [string]
  --idpMetaUrl                   IdP SAML Metadata URL                                                                                                      [string]
  --audience, --aud              SP Audience URI / RP Realm                                                                     [string] [default: "urn:example:sp"]
  --providerName                 SP Provider Name                                                                 [string] [default: "Simple SAML Service Provider"]
  --acsUrls                      SP Assertion Consumer Service (ACS) URLs (Relative URL)                                 [array] [required] [default: ["/saml/sso"]]
  --signAuthnRequests, --signed  Sign AuthnRequest Messages (SAMLP)                                                             [boolean] [required] [default: true]
  --signatureAlgorithm           Signature Algorithm                                                                                [string] [default: "rsa-sha256"]
  --digestAlgorithm              Digest Algorithm                                                                                       [string] [default: "sha256"]
  --requestNameIDFormat          Request Subject NameID Format (SAMLP)                                                                     [boolean] [default: true]
  --validateNameIDFormat         Validate format of Assertion Subject NameID                                                               [boolean] [default: true]
  --nameIDFormat, --nameid       Assertion Subject NameID Format                        [string] [default: "urn:oasis:names:tc:SAML:1.1:nameid-format:emailAddress"]
  --requestAuthnContext          Request Authentication Context (SAMLP)                                                                    [boolean] [default: true]
  --authnContextClassRef, --acr  Authentication Context Class Reference      [string] [default: "urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport"]
  --spCert                       SP/RP Public Key Signature & Encryption Certificate (PEM)          [string] [default: "/Users/karl/src/saml-sp/config/sp-cert.pem"]
  --spKey                        SP/RP Private Key Signature & Decryption Certificate(PEM)           [string] [default: "/Users/karl/src/saml-sp/config/sp-key.pem"]
  --httpsPrivateKey              Web Server TLS/SSL Private Key (PEM)                                                                                       [string]
  --httpsCert                    Web Server TLS/SSL Certificate (PEM)                                                                                       [string]
  --https                        Enables HTTPS Listener (requires httpsPrivateKey and httpsCert)                                          [boolean] [default: false]
  --relayState, --rs             Default Relay State                                                                                                        [string]
  --help                         Show help                                                                                                                 [boolean]
```

## Author & more info
https://github.com/mcguinness/saml-sp

