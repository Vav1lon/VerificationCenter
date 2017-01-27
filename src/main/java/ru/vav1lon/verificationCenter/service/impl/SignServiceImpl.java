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
import ru.vav1lon.verificationCenter.model.CertificateInternalModel;
import ru.vav1lon.verificationCenter.service.SignService;

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


    @Override
    public byte[] sign(CertificateInternalModel cert, byte[] content, byte[] sig) throws Exception {

        KeyStore keyStore = loadKeyStore(cert.getKeyStoreName(), cert.getKeyStorePassword());

        CMSSignedDataGenerator signatureGenerator = setUpProvider(keyStore, cert.getKeyStorePassword());

        return signPkcs7(content, signatureGenerator);
    }

    private KeyStore loadKeyStore(String path, String passowrd) throws Exception {
        KeyStore keystore = KeyStore.getInstance(KEY_STORE_TYPE);
        InputStream is = new FileInputStream(path);
        keystore.load(is, passowrd.toCharArray());
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


        ContentSigner signer = new JcaContentSignerBuilder(signatureAlg).setProvider(SECURITY_PROVIDER).
                build((PrivateKey) (keystore.getKey(signatureAlias, password.toCharArray())));

        CMSSignedDataGenerator generator = new CMSSignedDataGenerator();

        generator.addSignerInfoGenerator(new JcaSignerInfoGeneratorBuilder(new JcaDigestCalculatorProviderBuilder().setProvider(SECURITY_PROVIDER).
                build()).build(signer, (X509Certificate) cert));

        generator.addCertificates(certStore);

        return generator;
    }

    private byte[] signPkcs7(final byte[] content, final CMSSignedDataGenerator generator) throws Exception {
        CMSTypedData cmsdata = new CMSProcessableByteArray(content);
        CMSSignedData signeddata = generator.generate(cmsdata, true);
        return signeddata.getEncoded();
    }

}
