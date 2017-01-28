package ru.vav1lon.verificationCenter.common;

import org.bouncycastle.asn1.x500.RDN;

import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Base64;
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


    public static boolean DNmatches(RDN[] rdNs, RDN[] rdNs1) {
        return Stream.of(rdNs).allMatch(rdn -> Arrays.asList(rdNs1).contains(rdn));
    }

}
