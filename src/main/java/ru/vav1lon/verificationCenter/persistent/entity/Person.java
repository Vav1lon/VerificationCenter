package ru.vav1lon.verificationCenter.persistent.entity;

import lombok.Builder;
import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Table
@Entity
@Builder
public class Person {

    @Id
    private Long id;
    @Column
    private String firstName;
    @Column
    private String lastName;
    @Column
    private String patronymic;
    @Column
    private String email;
    @Column
    private String phone;
    @Column
    private String inn;

}
