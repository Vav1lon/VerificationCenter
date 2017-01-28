package ru.vav1lon.verificationCenter.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SignModel {

    private Long userId;
    private Long certificateId;
    private String certificatePassword;

}
