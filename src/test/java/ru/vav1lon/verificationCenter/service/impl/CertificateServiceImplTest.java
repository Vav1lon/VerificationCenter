package ru.vav1lon.verificationCenter.service.impl;

import org.bouncycastle.asn1.x500.X500Name;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vav1lon.verificationCenter.AbstractTest;
import ru.vav1lon.verificationCenter.common.CertificateUtils;
import ru.vav1lon.verificationCenter.model.CertificateRequestModel;
import ru.vav1lon.verificationCenter.service.CertificateService;
import sun.security.x509.X509CertImpl;

public class CertificateServiceImplTest extends AbstractTest {

    @Autowired
    private CertificateService certificateService;

    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void create() throws Exception {
        String subject = "CN=Kuzmin,O=Vavilon";
        X500Name issuer = new X500Name("CN=MyCompany CA, O=MyCompany");
        X509CertImpl cer = (X509CertImpl) certificateService.create(CertificateRequestModel.builder()
                .subject(subject)
                .password("123")
                .userId(123L)
                .build(), issuer);

        Assert.assertEquals("ECGOST3410", cer.getPublicKey().getAlgorithm());
        Assert.assertEquals("X.509", cer.getType());
        Assert.assertTrue(CertificateUtils.DNmatches(issuer.getRDNs(), cer.getIssuerX500Principal()));
        Assert.assertEquals(subject, cer.getSubjectDN());
    }

    @Test
    public void getInformation() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }

}