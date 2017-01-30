package ru.vav1lon.verificationCenter.persistent.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.vav1lon.verificationCenter.persistent.entity.Person;

public interface PersonRepository extends JpaRepository<Person, Long> {

    Person findById(Long id);

}
