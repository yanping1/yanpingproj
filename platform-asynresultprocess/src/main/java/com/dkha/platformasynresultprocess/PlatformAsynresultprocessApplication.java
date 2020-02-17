package com.dkha.platformasynresultprocess;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

@SpringBootApplication(scanBasePackages={"com.dkha"})
@PropertySource({"classpath:system.properties","classpath:uploadFile.properties"})
public class PlatformAsynresultprocessApplication {

    public static void main(String[] args) {
        SpringApplication.run(PlatformAsynresultprocessApplication.class, args);
    }

}
