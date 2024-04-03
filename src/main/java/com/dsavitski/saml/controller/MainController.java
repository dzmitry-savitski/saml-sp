package com.dsavitski.saml.controller;

import com.dsavitski.saml.utils.XmlUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.saml2.provider.service.authentication.Saml2AuthenticatedPrincipal;
import org.springframework.security.saml2.provider.service.metadata.OpenSamlMetadataResolver;
import org.springframework.security.saml2.provider.service.metadata.Saml2MetadataResolver;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistration;
import org.springframework.security.saml2.provider.service.registration.RelyingPartyRegistrationRepository;
import org.springframework.security.saml2.provider.service.web.DefaultRelyingPartyRegistrationResolver;
import org.springframework.security.saml2.provider.service.web.RelyingPartyRegistrationResolver;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class MainController {
    @Autowired
    private RelyingPartyRegistrationRepository repository;
    @Value("${saml.sp.registrationid}")
    private String registrationId;

    @RequestMapping("/")
    public String home(Model model) {
        model.addAttribute("name", "");
        return "main";
    }

    @RequestMapping("/profile")
    public String profile(@AuthenticationPrincipal Saml2AuthenticatedPrincipal principal, Model model) {
        model.addAttribute("name", principal.getName());
        model.addAttribute("userAttributes", principal.getAttributes());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final String samlResponse = XmlUtils.prettyXml(authentication.getCredentials().toString(), 4, false);
        model.addAttribute("samlResponse", samlResponse);

        return "profile";
    }

    @RequestMapping("/re-login")
    public String reLogin(HttpSession session) {
        session.invalidate();
        return "redirect:/profile";
    }

    @RequestMapping("/force")
    public String home(HttpSession session) {
        session.invalidate();
        return "redirect:/force-login";
    }

    @GetMapping("/force-login")
    public String session(HttpServletRequest request) {
        HttpSession session = request.getSession(true);
        session.setAttribute("force", true);
        return "redirect:/profile";
    }

    @RequestMapping(value = "/metadata", produces = {"application/xml", "text/xml"})
    @ResponseBody
    public String metadata(HttpServletRequest request) {
        RelyingPartyRegistrationResolver relyingPartyResolver = new DefaultRelyingPartyRegistrationResolver(repository);
        RelyingPartyRegistration registration = relyingPartyResolver.resolve(request, registrationId);
        registration.getNameIdFormat();
        Saml2MetadataResolver metadataResolver = new OpenSamlMetadataResolver();
        return metadataResolver.resolve(registration);
    }
}