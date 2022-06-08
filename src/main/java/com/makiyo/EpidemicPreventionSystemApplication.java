package com.makiyo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan
public class EpidemicPreventionSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(EpidemicPreventionSystemApplication.class, args);
    }

}
