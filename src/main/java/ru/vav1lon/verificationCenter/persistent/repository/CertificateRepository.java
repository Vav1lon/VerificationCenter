package ru.vav1lon.verificationCenter.persistent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vav1lon.verificationCenter.persistent.entity.Certificate;

public interface CertificateRepository extends JpaRepository<Certificate, Long> {
}
