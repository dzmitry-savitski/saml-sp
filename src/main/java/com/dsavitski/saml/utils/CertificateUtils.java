package com.dsavitski.saml.utils;

import com.dsavitski.saml.configuration.IDPProperties;
import com.dsavitski.saml.configuration.SPProperties;
import org.springframework.core.io.Resource;
import org.springframework.security.converter.RsaKeyConverters;
import org.springframework.security.saml2.core.Saml2X509Credential;
import org.springframework.util.Assert;

import java.io.InputStream;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;

public class CertificateUtils {
    public static Saml2X509Credential getSigningCredential(SPProperties.Credentials credentials) {
        RSAPrivateKey privateKey = readPrivateKey(credentials.getPrivateKeyLocation());
        X509Certificate certificate = readCertificate(credentials.getCertificateLocation());
        return new Saml2X509Credential(privateKey, certificate, Saml2X509Credential.Saml2X509CredentialType.SIGNING);
    }

    public static Saml2X509Credential getEncryptionCredential(SPProperties.Credentials credentials) {
        RSAPrivateKey privateKey = readPrivateKey(credentials.getPrivateKeyLocation());
        X509Certificate certificate = readCertificate(credentials.getCertificateLocation());
        return new Saml2X509Credential(privateKey, certificate, Saml2X509Credential.Saml2X509CredentialType.DECRYPTION);
    }

    public static Saml2X509Credential getVerificationCredential(IDPProperties.Certificate credentials) {
        X509Certificate certificate = readCertificate(credentials.getCertificateLocation());
        return new Saml2X509Credential(certificate, Saml2X509Credential.Saml2X509CredentialType.ENCRYPTION, Saml2X509Credential.Saml2X509CredentialType.VERIFICATION);
    }

    private static RSAPrivateKey readPrivateKey(Resource location) {
        Assert.state(location != null, "No private key location specified");
        Assert.state(location.exists(), () -> {
            return "Private key location '" + location + "' does not exist";
        });

        try {
            InputStream inputStream = location.getInputStream();
            RSAPrivateKey priv;
            try {
                priv = RsaKeyConverters.pkcs8().convert(inputStream);
            } catch (Throwable thr) {
                try {
                    inputStream.close();
                } catch (Throwable ex) {
                    thr.addSuppressed(ex);
                }
                throw thr;
            }

            inputStream.close();
            return priv;
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private static X509Certificate readCertificate(Resource location) {
        Assert.state(location != null, "No certificate location specified");
        Assert.state(location.exists(), () -> {
            return "Certificate  location '" + location + "' does not exist";
        });

        try {
            InputStream inputStream = location.getInputStream();

            X509Certificate cert;
            try {
                cert = (X509Certificate) CertificateFactory.getInstance("X.509").generateCertificate(inputStream);
            } catch (Throwable thr) {
                try {
                    inputStream.close();
                } catch (Throwable ex) {
                    thr.addSuppressed(ex);
                }

                throw thr;
            }

            inputStream.close();

            return cert;
        } catch (Exception ex) {
            throw new IllegalArgumentException(ex);
        }
    }
}
