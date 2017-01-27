package ru.vav1lon.verificationCenter.config;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.security.Security;

@Configuration
public class BaseConfig {

    @PostConstruct
    public void setUp() {
        Security.addProvider(new BouncyCastleProvider());
    }

}
