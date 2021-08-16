package com.monoplus.mcd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@SpringBootApplication
@EnableWebSecurity
public class Sample2FAEmail {

    public static void main(String[] args) {

        SpringApplication sa = new SpringApplication(Sample2FAEmail.class);
        sa.run(args);
    }
}
