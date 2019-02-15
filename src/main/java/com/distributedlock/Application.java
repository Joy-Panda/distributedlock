package com.distributedlock;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.ApplicationPidFileWriter;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class Application {

    public static void main(String[] args) {
        SpringApplication springApplication =
                new SpringApplication(Application.class);
        springApplication.addListeners(
                new ApplicationPidFileWriter("app.pid"));
        springApplication.run(args);
    }

}
