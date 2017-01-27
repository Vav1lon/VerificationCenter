package ru.vav1lon.verificationCenter.service.impl;

import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import ru.vav1lon.verificationCenter.AbstractTest;
import ru.vav1lon.verificationCenter.model.CertificateRequestModel;
import ru.vav1lon.verificationCenter.service.CertificateService;
import sun.security.x509.X509CertImpl;

import java.io.File;

public class CertificateServiceImplTest extends AbstractTest {

    @Value("${local.filePath}")
    private String filePath;

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
                .password("123")
                .userId(123L)
                .build(), issuer);

        Assert.assertEquals("ECGOST3410", cer.getPublicKey().getAlgorithm());
        Assert.assertEquals("X.509", cer.getType());
        Assert.assertTrue(issuer, cer.getElements()));
//        Assert.assertEquals(subject, cer.getSubjectDN());
    }

    @Test
    public void getInformation() throws Exception {

    }

    @Test
    public void delete() throws Exception {

    }

}