package ru.vav1lon.verificationCenter.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509v3CertificateBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.jcajce.JcaContentSignerBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import ru.vav1lon.verificationCenter.model.CertificateInternalModel;
import ru.vav1lon.verificationCenter.model.CertificateRequestModel;
import ru.vav1lon.verificationCenter.service.CertificateService;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.security.spec.ECGenParameterSpec;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Random;

@Slf4j
@Service
public class CertificateServiceImpl implements CertificateService {

    private final static String signatureAlgorithm = "GOST3411WITHECGOST3410";
    private final static String KEY_STORE_TYPE = "JKS";
    private final static String ECGOST_3410_ALG = "ECGOST3410";
    private final static String EC_SPEC = "GostR3410-2001-CryptoPro-A";
    private final static String SECURITY_PROVIDER = "BC";

    @Value("${signature.alias}")
    private String signatureAlias;

    @Value("${local.filePath}")
    private String filePath;

    @Override
    public Certificate create(CertificateRequestModel request, X500Name issuer) {

        KeyPairGenerator keyPairGenerator = buildKeyPairGenerator();
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        X500Name subject = new X500Name(request.getSubject());
        BigInteger serial = nextRandomBigInteger();
        Date notBefore = Date.from(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant());
        Date notAfter = Date.from(LocalDateTime.now().plusYears(1).atZone(ZoneId.systemDefault()).toInstant());

        X509CertificateHolder certificateHolder = getX509CertificateHolder(issuer, keyPair, subject, serial, notBefore, notAfter);
        X509Certificate certificate = getX509Certificate(certificateHolder);

        buildAndSaveInKeyStore(request, keyPair, serial, certificate);

        return certificate;
    }

    @Override
    public CertificateInternalModel getInformation() {
        return null;
    }

    @Override
    public void delete(CertificateInternalModel model) {

    }

    private BigInteger nextRandomBigInteger() {
        BigInteger max = BigInteger.probablePrime(128, new Random()); //("e4fbb05ff0c2b79e741d20fc0476133f2b277505");
        Random rnd = new Random();
        do {
            BigInteger i = new BigInteger(max.bitLength(), rnd);
            if (i.compareTo(max) <= 0)
                return i;
        } while (true);
    }

    private X509CertificateHolder getX509CertificateHolder(X500Name issuer, KeyPair keyPair, X500Name subject, BigInteger serial, Date notBefore, Date notAfter) {
        X509v3CertificateBuilder certificateBuilder = new JcaX509v3CertificateBuilder(
                issuer,
                serial,
                notBefore,
                notAfter,
                subject,
                keyPair.getPublic()
        );

        X509CertificateHolder certificateHolder;
        try {
            certificateHolder = certificateBuilder.build(new JcaContentSignerBuilder(signatureAlgorithm).build(keyPair.getPrivate()));
        } catch (OperatorCreationException e) {
            String msg = "X509v3CertificateBuilder create error";
            log.error(msg, e);
            throw new RuntimeException(msg);
        }
        return certificateHolder;
    }

    private KeyPairGenerator buildKeyPairGenerator() {
        KeyPairGenerator keyPairGenerator;
        try {
            keyPairGenerator = KeyPairGenerator.getInstance(ECGOST_3410_ALG, SECURITY_PROVIDER);
            keyPairGenerator.initialize(new ECGenParameterSpec(EC_SPEC));
        } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException e) {
            String msg = "KeyPairGenerator create error";
            log.error(msg, e);
            throw new RuntimeException(msg);
        }
        return keyPairGenerator;
    }

    private X509Certificate getX509Certificate(X509CertificateHolder certificateHolder) {
        org.bouncycastle.cert.jcajce.JcaX509CertificateConverter certificateConverter = new org.bouncycastle.cert.jcajce.JcaX509CertificateConverter();
        X509Certificate certificate;
        try {
            certificate = certificateConverter.getCertificate(certificateHolder);

        } catch (CertificateException e) {
            String msg = "certificateConverter.getCertificate error";
            log.error(msg, e);
            throw new RuntimeException(msg);
        }
        return certificate;
    }

    private void buildAndSaveInKeyStore(CertificateRequestModel request, KeyPair keyPair, BigInteger serial, X509Certificate certificate) {
        KeyStore keyStore;
        try {
            keyStore = KeyStore.getInstance(KEY_STORE_TYPE);
            keyStore.load(null, null);
        } catch (KeyStoreException | CertificateException | IOException | NoSuchAlgorithmException e) {
            String msg = "KeyStore init error";
            log.error(msg, e);
            throw new RuntimeException(msg);
        }
        try {
            keyStore.setEntry(
                    signatureAlias,
                    new KeyStore.PrivateKeyEntry(
                            keyPair.getPrivate(),
                            new Certificate[]{certificate}
                    ),
                    new KeyStore.PasswordProtection(request.getPassword().toCharArray())
            );
        } catch (KeyStoreException e) {
            String msg = "Write key to KeyStore error";
            log.error(msg, e);
            throw new RuntimeException(msg);
        }
        try {
            keyStore.store(new FileOutputStream(buildFullPath(filePath, request.getUserId().toString(), serial.toString())), request.getPassword().toCharArray());
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException e) {
            String msg = "Write KeyStore to HDD error";
            log.error(msg, e);
            throw new RuntimeException(msg);
        }
    }

    private String buildFullPath(String filePath, String userId, String fileName) {

        if (StringUtils.isEmpty(filePath) || StringUtils.isEmpty(userId) || StringUtils.isEmpty(fileName)) {
            String msg = "Incorrect params for build path";
            log.error(msg);
            throw new IllegalArgumentException(msg);
        }

        StringBuilder result = new StringBuilder(filePath);

        if (!filePath.endsWith(File.separator)) {
            result.append(File.separator);
        }

        String nameWithExt = fileName.concat(".jks");

        result.append(userId).append(File.separator);

        if (!Files.exists(Paths.get(result.toString()))) {
            new File(result.toString()).mkdirs();
        }

        return result.append(nameWithExt).toString();
    }
}
