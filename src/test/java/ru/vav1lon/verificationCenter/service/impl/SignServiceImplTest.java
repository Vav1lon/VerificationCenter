package ru.vav1lon.verificationCenter.service.impl;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.style.RFC4519Style;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import ru.vav1lon.verificationCenter.AbstractUnitTest;
import ru.vav1lon.verificationCenter.model.CertificateRequestModel;
import ru.vav1lon.verificationCenter.model.SignModel;
import ru.vav1lon.verificationCenter.service.CertificateService;
import ru.vav1lon.verificationCenter.service.SignService;

import java.io.File;

public class SignServiceImplTest extends AbstractUnitTest {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private SignService signService;

    private Long userId = 19216801L;
    private Long storeName = 6121986L;
    private String password = "123321";

    @Before
    public void setUp() throws Exception {

        String subject = "CN=Kuzmin,O=Vavilon";
        X500Name issuer = new X500Name(RFC4519Style.INSTANCE, "CN=MyCompany CA, O=MyCompany");
        certificateService.create(CertificateRequestModel.builder()
                .subject(subject)
                .storePassword(password)
                .storeName(storeName)
                .userId(userId)
                .build(), issuer);

    }

    @After
    public void tearDown() throws Exception {
        FileUtils.cleanDirectory(new File(filePath));
    }

    @Test
    public void sign() throws Exception {

        ClassPathResource res = new ClassPathResource("origin.txt");

        SignModel signModel = SignModel.builder()
                .certificateId(storeName)
                .certificatePassword(password)
                .userId(userId)
                .build();

        byte[] sign = signService.sign(signModel, IOUtils.toByteArray(res.getInputStream()), null);

        Assert.assertNotNull(sign);
    }
}