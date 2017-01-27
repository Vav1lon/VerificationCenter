package ru.vav1lon.verificationCenter.service;

import org.bouncycastle.asn1.x500.X500Name;
import ru.vav1lon.verificationCenter.model.CertificateInternalModel;
import ru.vav1lon.verificationCenter.model.CertificateRequestModel;

import java.security.cert.Certificate;

public interface CertificateService {

    Certificate create(CertificateRequestModel request, X500Name issuer);

    CertificateInternalModel getInformation();

    void delete(CertificateInternalModel model);

}
