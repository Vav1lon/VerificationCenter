package ru.vav1lon.verificationCenter.service.impl;

import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSProcessableByteArray;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.CMSSignedDataGenerator;
import org.bouncycastle.cms.CMSTypedData;
import org.bouncycastle.cms.jcajce.JcaSignerInfoGeneratorBuilder;
import org.bouncycastle.operator.ContentSigner;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.bouncycastle.operator.jcajce.JcaDigestCalculatorProviderBuilder;
import org.bouncycastle.util.Store;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import ru.vav1lon.verificationCenter.model.SignModel;
import ru.vav1lon.verificationCenter.service.SignService;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.PrivateKey;
import java.security.cert.Certificate;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;

@Service
public class SignServiceImpl implements SignService {

    private final static String SECURITY_PROVIDER = "BC";
    private final static String KEY_STORE_TYPE = "JKS";

    @Value("${signature.algo}")
    private String signatureAlg;

    @Value("${signature.alias}")
    private String signatureAlias;

    @Value("${local.filePath}")
    private String filePath;

    @Override
    public byte[] sign(SignModel cert, byte[] content, byte[] sig) throws Exception {

        KeyStore keyStore = loadKeyStore(cert.getUserId(), cert.getCertificateId(), cert.getCertificatePassword());

        CMSSignedDataGenerator signatureGenerator = setUpProvider(keyStore, cert.getCertificatePassword());

        return signPkcs7(content, signatureGenerator);
    }

    private KeyStore loadKeyStore(Long userId, Long storeName, String password) throws Exception {
        KeyStore keystore = KeyStore.getInstance(KEY_STORE_TYPE);
        InputStream is = new FileInputStream(filePath + File.separator + userId + File.separator + storeName + ".jks");
        keystore.load(is, password.toCharArray());
        return keystore;
    }

    private CMSSignedDataGenerator setUpProvider(final KeyStore keystore, String password) throws Exception {

        Certificate[] certChain = keystore.getCertificateChain(signatureAlias);

        final List<Certificate> certList = new ArrayList<>();

        for (int i = 0, length = certChain == null ? 0 : certChain.length; i < length; i++) {
            certList.add(certChain[i]);
        }

        Store certStore = new JcaCertStore(certList);

        Certificate cert = keystore.getCertificate(signatureAlias);

        PrivateKey key = (PrivateKey) keystore.getKey(signatureAlias, password.toCharArray());

        ContentSigner signer = new JcaContentSignerBuilder(signatureAlg)
                .setProvider(SECURITY_PROVIDER)
                .build(key);

        CMSSignedDataGenerator generator = new CMSSignedDataGenerator();

        generator.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder()
                .setProvider(SECURITY_PROVIDER)
                .build()).build(signer, (X509Certificate) cert));

        generator.addCertificates(certStore);

        return generator;
    }

    private byte[] signPkcs7(final byte[] content, final CMSSignedDataGenerator generator) throws Exception {
        CMSTypedData cmsdata = new CMSProcessableByteArray(content);
        CMSSignedData signeddata = generator.generate(cmsdata, true);
        return signeddata.getEncoded();
    }

}
