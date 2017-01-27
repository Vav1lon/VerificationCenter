package sign.service.project.service;

import sign.service.project.model.CertificateInternalModel;

public interface SignService {

    byte[] sign(CertificateInternalModel cert, byte[] file, byte[] sig) throws Exception;


}
