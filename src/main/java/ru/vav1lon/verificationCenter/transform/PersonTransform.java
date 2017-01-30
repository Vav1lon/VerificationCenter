package ru.vav1lon.verificationCenter.transform;

import org.springframework.stereotype.Component;
import ru.vav1lon.verificationCenter.model.PersonDTO;
import ru.vav1lon.verificationCenter.persistent.entity.Person;

@Component
public class PersonTransform {

    public PersonDTO toModel(Person entity) {
        return PersonDTO.builder()
                .id(entity.getId())
                .firstName(entity.getFirstName())
                .lastName(entity.getLastName())
                .patronymic(entity.getPatronymic())
                .email(entity.getEmail())
                .inn(entity.getInn())
                .phone(entity.getPhone())
                .build();
    }

    public Person toEntity(PersonDTO request) {
        return Person.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .patronymic(request.getPatronymic())
                .email(request.getEmail())
                .inn(request.getInn())
                .phone(request.getPhone())
                .build();
    }
}
