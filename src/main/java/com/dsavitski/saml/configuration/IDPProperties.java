package com.dsavitski.saml.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.core.io.Resource;
import org.springframework.security.saml2.provider.service.registration.Saml2MessageBinding;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties("saml.idp")
public class IDPProperties {
    private String metadataLocation;

    private String entityId;

    private Singlesignon singlesignon;

    private Certificate verification;

    public String getMetadataLocation() {
        return metadataLocation;
    }

    public void setMetadataLocation(String metadataLocation) {
        this.metadataLocation = metadataLocation;
    }

    public String getEntityId() {
        return entityId;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public Singlesignon getSinglesignon() {
        return singlesignon;
    }

    public void setSinglesignon(Singlesignon singlesignon) {
        this.singlesignon = singlesignon;
    }

    public Certificate getVerification() {
        return verification;
    }

    public void setVerification(Certificate verification) {
        this.verification = verification;
    }

    public static class Certificate {
        private Resource certificateLocation;

        public Resource getCertificateLocation() {
            return certificateLocation;
        }

        public void setCertificateLocation(Resource certificateLocation) {
            this.certificateLocation = certificateLocation;
        }
    }

    public static class Singlesignon {
        private String url;
        private Saml2MessageBinding binding;
        private Boolean wantRequestSigned;

        public String getUrl() {
            return this.url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public Saml2MessageBinding getBinding() {
            return this.binding;
        }

        public void setBinding(Saml2MessageBinding binding) {
            this.binding = binding;
        }

        public Boolean getWantRequestSigned() {
            return wantRequestSigned;
        }

        public void setWantRequestSigned(Boolean wantRequestSigned) {
            this.wantRequestSigned = wantRequestSigned;
        }
    }
}
