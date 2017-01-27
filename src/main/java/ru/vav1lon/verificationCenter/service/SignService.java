package ru.vav1lon.verificationCenter.service;

import ru.vav1lon.verificationCenter.model.CertificateInternalModel;

public interface SignService {

    byte[] sign(CertificateInternalModel cert, byte[] file, byte[] sig) throws Exception;


}
