package com.dkha.communication;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * @Author Spring
 * @Since 2019/11/11 13:41
 * @Description
 */
@SpringBootApplication(exclude = {
        org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration.class, DataSourceAutoConfiguration.class},
        scanBasePackages = {"com.dkha.common", "com.dkha.communication"})
public class CommunicationApplication {

    public static void main(String[] args) {
        /**
         * 防止netty的bug
         * java.lang.IllegalStateException: availableProcessors is already set to [4], rejecting [4]
         */
        System.setProperty("es.set.netty.runtime.available.processors", "false");
        SpringApplication.run(CommunicationApplication.class, args);
    }

}
