package com.dsavitski.saml.configuration;

import jakarta.servlet.http.HttpSession;
import org.opensaml.saml.saml2.core.NameIDPolicy;
import org.opensaml.saml.saml2.core.impl.NameIDPolicyBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.web.DefaultRelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.RelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.authentication.OpenSaml4AuthenticationRequestResolver;
import org.springframework.security.saml2.provider.service.web.authentication.Saml2AuthenticationRequestResolver;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;


@Configuration
public class AppConfig {
    private static final String[] AUTH_WHITELIST = {"/webjars/**", "/favicon.ico", "/", "/metadata", "/force", "/session", "/re-login"};
    @Value("${spring.security.saml2.relyingparty.registration.sp.nameidformat:}")
    private String nameIdFormat;

    // https://spring.io/blog/2022/02/21/spring-security-without-the-websecurityconfigureradapter
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(AUTH_WHITELIST);
    }

    @Bean
    Saml2AuthenticationRequestResolver authenticationRequestResolver(RelyingPartyRegistrationRepository registrations) {
        RelyingPartyRegistrationResolver registrationResolver = new DefaultRelyingPartyRegistrationResolver(registrations);
        OpenSaml4AuthenticationRequestResolver authenticationRequestResolver = new OpenSaml4AuthenticationRequestResolver(registrationResolver);
        authenticationRequestResolver.setAuthnRequestCustomizer((context) -> {
            // 1. Set NameId
            if (!nameIdFormat.isEmpty()) {
                NameIDPolicy policy = new NameIDPolicyBuilder().buildObject();
                policy.setFormat(nameIdFormat);
                context.getAuthnRequest().setNameIDPolicy(policy);
            }

            // 2. Set forceAuntN if needed
            HttpSession session = context.getRequest().getSession();
            if (session.getAttribute("force") != null) {
                context.getAuthnRequest().setForceAuthn(true);
                session.removeAttribute("force");
            }
        });
        return authenticationRequestResolver;
    }

    @Bean
    AuthenticationFailureHandler errorhandler() {
        SimpleUrlAuthenticationFailureHandler handler = new SimpleUrlAuthenticationFailureHandler();
        handler.setDefaultFailureUrl("/error");
        return handler;
    }

//    @Bean
//    public Filter saml2MetadataFilter(RelyingPartyRegistrationRepository repository) {
//        Saml2MetadataFilter saml2MetadataFilter = new Saml2MetadataFilter(new DefaultRelyingPartyRegistrationResolver(repository), new OpenSamlMetadataResolver());
////        saml2MetadataFilter.setRequestMatcher(new AntPathRequestMatcher("/metadata", "GET"));
//        return saml2MetadataFilter;
//    }
}
