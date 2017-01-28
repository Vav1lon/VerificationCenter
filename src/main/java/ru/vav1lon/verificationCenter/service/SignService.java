package ru.vav1lon.verificationCenter.service;

import ru.vav1lon.verificationCenter.model.SignModel;

public interface SignService {

    byte[] sign(SignModel cert, byte[] file, byte[] sig) throws Exception;

}
