package org.kea.easyscope;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EasyScopeApplication {

    // Logger til at se resultater i terminalen ...
    // private Logger logger = LoggerFactory.getLogger(EasyScopeApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(EasyScopeApplication.class, args);
    }

}
