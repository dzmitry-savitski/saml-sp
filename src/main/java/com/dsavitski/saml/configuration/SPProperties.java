package com.dsavitski.saml.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.security.saml2.provider.service.registration.Saml2MessageBinding;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("saml.sp")
public class SPProperties {
    private String registrationId;
    private String entityId = "{baseUrl}/{registrationId}";
    private String nameIdFormat;
    private String[] authEndpoints;
    private Credentials signing;
    private Credentials encryption;
    private Boolean signAuthnRequest;
    private Boolean allowCreate;

    private Acs acs;

    public String getRegistrationId() {
        return registrationId;
    }

    public void setRegistrationId(String registrationId) {
        this.registrationId = registrationId;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public String getNameIdFormat() {
        return nameIdFormat;
    }

    public void setNameIdFormat(String nameIdFormat) {
        this.nameIdFormat = nameIdFormat;
    }


    public String[] getAuthEndpoints() {
        return authEndpoints;
    }

    public void setAuthEndpoints(String[] authEndpoints) {
        this.authEndpoints = authEndpoints;
    }

    public Credentials getSigning() {
        return signing;
    }

    public void setSigning(Credentials signing) {
        this.signing = signing;
    }

    public Credentials getEncryption() {
        return encryption;
    }

    public void setEncryption(Credentials encryption) {
        this.encryption = encryption;
    }

    public Acs getAcs() {
        return acs;
    }

    public void setAcs(Acs acs) {
        this.acs = acs;
    }

    public Boolean isSignAuthnRequest() {
        return signAuthnRequest;
    }

    public void setSignAuthnRequest(Boolean signAuthnRequest) {
        this.signAuthnRequest = signAuthnRequest;
    }

    public Boolean getAllowCreate() {
        return allowCreate;
    }
    public void setAllowCreate(Boolean allowCreate) {
        this.allowCreate = allowCreate;
    }


    public static class Credentials {
        private Resource privateKeyLocation;
        private Resource certificateLocation;

        public Resource getPrivateKeyLocation() {
            return privateKeyLocation;
        }

        public void setPrivateKeyLocation(Resource privateKeyLocation) {
            this.privateKeyLocation = privateKeyLocation;
        }

        public Resource getCertificateLocation() {
            return certificateLocation;
        }

        public void setCertificateLocation(Resource certificateLocation) {
            this.certificateLocation = certificateLocation;
        }
    }

    public static class Acs {
        private String location = "{baseUrl}/sso/{registrationId}";
        private Saml2MessageBinding binding;

        public Acs() {
            this.binding = Saml2MessageBinding.POST;
        }

        public String getLocation() {
            return this.location;
        }

        public void setLocation(String location) {
            this.location = location;
        }

        public Saml2MessageBinding getBinding() {
            return this.binding;
        }

        public void setBinding(Saml2MessageBinding binding) {
            this.binding = binding;
        }
    }
}
