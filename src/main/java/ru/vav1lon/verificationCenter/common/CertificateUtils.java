package ru.vav1lon.verificationCenter.common;

import org.bouncycastle.asn1.x500.RDN;

import javax.naming.InvalidNameException;
import javax.naming.ldap.LdapName;
import javax.naming.ldap.Rdn;
import javax.security.auth.x500.X500Principal;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

public class CertificateUtils {

    public static String x509toPem(X509Certificate cert) throws CertificateEncodingException {
        String cert_begin = "-----BEGIN CERTIFICATE-----\n";
        String end_cert = "-----END CERTIFICATE-----";

        String pemCertPre = new String(Base64.getEncoder().encode(cert.getEncoded()));
        return cert_begin + pemCertPre + end_cert;
    }

    public static String x509toDer(X509Certificate cert) throws CertificateEncodingException {
        return new String(cert.getEncoded());
    }

    public static boolean DNmatches(X500Principal p1, X500Principal p2) throws InvalidNameException {
        List<Rdn> rdn1 = new LdapName(p1.getName()).getRdns();
        List<Rdn> rdn2 = new LdapName(p2.getName()).getRdns();

        if (rdn1.size() != rdn2.size())
            return false;

        return rdn1.containsAll(rdn2);
    }

    public static boolean DNmatches(RDN[] rdn, X500Principal p2) throws InvalidNameException {
        List<Rdn> rdn2 = new LdapName(p2.getName()).getRdns();

        if (rdn.length != rdn2.size())
            return false;

        return Stream.of(rdn).allMatch(rdn2::contains);
    }

}
