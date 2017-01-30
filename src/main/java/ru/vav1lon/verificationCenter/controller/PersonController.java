package ru.vav1lon.verificationCenter.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.vav1lon.verificationCenter.model.PersonDTO;
import ru.vav1lon.verificationCenter.persistent.repository.PersonRepository;
import ru.vav1lon.verificationCenter.service.AAService;
import ru.vav1lon.verificationCenter.transform.PersonTransform;

@RestController
@Transactional(readOnly = true)
public class PersonController extends AbstractController {

    @Autowired
    private PersonRepository repository;

    @Autowired
    private PersonTransform personTransform;

    @Autowired
    private AAService aaService;

    @Autowired
    private ApplicationContext applicationContext;

    @ResponseBody
    @Transactional
    @PostMapping(path = "/create")
    public PersonDTO create(PersonDTO request) {
        return personTransform.toModel(repository.save(personTransform.toEntity(request)));
    }

    @ResponseBody
    @GetMapping(path = "/findById")
    public PersonDTO findById(@RequestParam("id") Long id) {
        aaService.aa();
        return personTransform.toModel(repository.findById(id));
    }

}
