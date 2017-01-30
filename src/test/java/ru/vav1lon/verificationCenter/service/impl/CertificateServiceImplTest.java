package ru.vav1lon.verificationCenter.service.impl;

import org.apache.commons.io.FileUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.vav1lon.verificationCenter.AbstractUnitTest;
import ru.vav1lon.verificationCenter.common.CertificateUtils;
import ru.vav1lon.verificationCenter.model.CertificateRequestModel;
import ru.vav1lon.verificationCenter.service.CertificateService;
import sun.security.x509.X509CertImpl;

import java.io.File;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class CertificateServiceImplTest extends AbstractUnitTest {

    @Autowired
    private CertificateService certificateService;

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
        FileUtils.cleanDirectory(new File(filePath));
    }

    @Test
    public void create() throws Exception {
        String subject = "CN=Kuzmin,O=Vavilon";
        X500Name issuer = new X500Name(RFC4519Style.INSTANCE, "CN=MyCompany CA, O=MyCompany");
        X509CertImpl cer = (X509CertImpl) certificateService.create(CertificateRequestModel.builder()
                .subject(subject)
                .storePassword("123")
                .storeName(234L)
                .userId(123L)
                .build(), issuer);

        Assert.assertNotNull(cer.getSerialNumberObject());
        Assert.assertEquals("ECGOST3410", cer.getPublicKey().getAlgorithm());
        Assert.assertEquals("X.509", cer.getType());
        Assert.assertEquals(3, cer.getVersion());
        Assert.assertEquals("1.2.643.2.2.3", cer.getSigAlgOID());
        Assert.assertEquals("1.2.643.2.2.3", cer.getSigAlgName());
        Assert.assertEquals(1, Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant()).compareTo(cer.getNotBefore()));
        Assert.assertEquals(1, Date.from(LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toInstant()).compareTo(cer.getNotAfter()));
        Assert.assertTrue(CertificateUtils.DNmatches(new X500Name(RFC4519Style.INSTANCE, cer.getSubjectDN().getName()).getRDNs(), new X500Name(RFC4519Style.INSTANCE, subject).getRDNs()));
        Assert.assertTrue(CertificateUtils.DNmatches(new X500Name(RFC4519Style.INSTANCE, cer.getIssuerDN().getName()).getRDNs(), issuer.getRDNs()));
    }

    @Test
    public void getInformation() throws Exception {
    }

    @Test
    public void delete() throws Exception {
    }

}