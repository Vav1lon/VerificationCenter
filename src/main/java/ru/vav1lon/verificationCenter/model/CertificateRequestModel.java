package ru.vav1lon.verificationCenter.model;

import lombok.Builder;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
@Builder
public class CertificateRequestModel {

    @NotNull
    private String subject;
    @NotNull
    private Long userId;
    @NotNull
    private Long storeName;
    @NotNull
    private String storePassword;

}
