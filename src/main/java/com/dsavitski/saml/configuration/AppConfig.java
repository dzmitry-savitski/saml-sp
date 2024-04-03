package com.dsavitski.saml.configuration;

import com.dsavitski.saml.utils.CertificateUtils;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.saml2.core.Saml2X509Credential;
import org.springframework.security.saml2.provider.service.registration.InMemoryRelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrations;
import org.springframework.security.saml2.provider.service.web.DefaultRelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.RelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.authentication.OpenSaml4AuthenticationRequestResolver;
import org.springframework.security.saml2.provider.service.web.authentication.Saml2AuthenticationRequestResolver;
import org.springframework.security.web.SecurityFilterChain;

import java.util.function.Consumer;

import static com.dsavitski.saml.utils.CertificateUtils.getVerificationCredential;


@Configuration
@EnableWebSecurity
public class AppConfig {
    @Autowired
    private SPProperties spProperties;

    @Autowired
    private IDPProperties idpProperties;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           @Value("${sso.login-processing-url}") String loginProcessingUrl) throws Exception {
        http.authorizeHttpRequests(authorize -> authorize
                .requestMatchers(spProperties.getAuthEndpoints()).authenticated()
                .anyRequest().permitAll()
        ).saml2Login((saml2) -> saml2.loginProcessingUrl(loginProcessingUrl));
        return http.build();
    }

    @Bean
    public RelyingPartyRegistrationRepository relyingPartyRegistrations() {

        RelyingPartyRegistration.Builder builder;
        if (isDefined(idpProperties.getMetadataLocation())) {
            builder = RelyingPartyRegistrations
                    .fromMetadataLocation(idpProperties.getMetadataLocation())
                    .registrationId(spProperties.getRegistrationId());
        } else {
            builder = RelyingPartyRegistration.withRegistrationId(spProperties.getRegistrationId());
        }

        Saml2X509Credential signingCert = CertificateUtils.getSigningCredential(spProperties.getSigning());
        Saml2X509Credential encryptionCert = CertificateUtils.getEncryptionCredential(spProperties.getEncryption());

        RelyingPartyRegistration registration = builder
                .entityId(spProperties.getEntityId())
                .nameIdFormat(spProperties.getNameIdFormat())
                .signingX509Credentials(saml2X509Credentials -> {
                    saml2X509Credentials.add(signingCert);
                })
                .decryptionX509Credentials(saml2X509Credentials -> {
                    saml2X509Credentials.add(encryptionCert);
                })
                .assertionConsumerServiceLocation(spProperties.getAcs().getLocation())
                .assertionConsumerServiceBinding(spProperties.getAcs().getBinding())
                .assertingPartyDetails(buildIdp())
                .authnRequestsSigned(spProperties.isSignAuthnRequest())
                .build();
        return new InMemoryRelyingPartyRegistrationRepository(registration);
    }

    private Consumer<RelyingPartyRegistration.AssertingPartyDetails.Builder> buildIdp() {
        return idp -> {
            if (isDefined(idpProperties.getEntityId())) {
                idp.entityId(idpProperties.getEntityId());
            }
            if (idpProperties.getSinglesignon() != null) {
                if (isDefined(idpProperties.getSinglesignon().getUrl())) {
                    idp.singleSignOnServiceLocation(idpProperties.getSinglesignon().getUrl());
                }
                if (idpProperties.getSinglesignon().getBinding() != null) {
                    idp.singleSignOnServiceBinding(idpProperties.getSinglesignon().getBinding());
                }
                if (idpProperties.getSinglesignon().getWantRequestSigned() != null) {
                    idp.wantAuthnRequestsSigned(idpProperties.getSinglesignon().getWantRequestSigned());
                }
            }
            if (idpProperties.getVerification() != null &&
                    idpProperties.getVerification().getCertificateLocation() != null) {
                idp.verificationX509Credentials(certificates ->
                        certificates.add(getVerificationCredential(idpProperties.getVerification())));
            }
        };
    }

    @Bean
    Saml2AuthenticationRequestResolver authenticationRequestResolver(RelyingPartyRegistrationRepository registrations) {
        RelyingPartyRegistrationResolver registrationResolver = new DefaultRelyingPartyRegistrationResolver(registrations);
        OpenSaml4AuthenticationRequestResolver authenticationRequestResolver = new OpenSaml4AuthenticationRequestResolver(registrationResolver);
        authenticationRequestResolver.setAuthnRequestCustomizer((context) -> {
            // Set forceAuntN when needed
            HttpSession session = context.getRequest().getSession();
            if (session.getAttribute("force") != null) {
                context.getAuthnRequest().setForceAuthn(true);
                session.removeAttribute("force");
            }
        });
        return authenticationRequestResolver;
    }

    private boolean isDefined(String property) {
        return property != null && !property.isBlank();
    }
}
