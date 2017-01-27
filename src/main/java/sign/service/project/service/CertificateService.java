package sign.service.project.service;

import org.bouncycastle.asn1.x500.X500Name;
import sign.service.project.model.CertificateInternalModel;
import sign.service.project.model.CertificateRequestModel;

import java.security.cert.Certificate;

public interface CertificateService {

    Certificate create(CertificateRequestModel request, X500Name issuer);

    CertificateInternalModel getInformation();

    void delete(CertificateInternalModel model);

}
