package ru.vav1lon.verificationCenter.persistent.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Table
@Entity
public class Certificate {

    @Id
    @Column
    private Long id;

}
