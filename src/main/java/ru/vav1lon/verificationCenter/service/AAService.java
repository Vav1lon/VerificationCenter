package ru.vav1lon.verificationCenter.service;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(propagation = Propagation.MANDATORY)
public class AAService {

    public void aa() {
        System.out.println("aa");
    }
}
