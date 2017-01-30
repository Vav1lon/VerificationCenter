package ru.vav1lon.verificationCenter.model;

import lombok.Builder;
import lombok.Data;
import org.hibernate.validator.constraints.Email;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;

@Data
@Builder
public class PersonDTO {

    @NotNull
    private Long id;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String patronymic;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Max(10)
    private String phone;
    @NotNull
    @Max(10)
    private String inn;
}
