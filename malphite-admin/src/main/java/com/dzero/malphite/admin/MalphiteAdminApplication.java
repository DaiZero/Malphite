package com.dzero.malphite.admin;

import de.codecentric.boot.admin.server.config.EnableAdminServer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@EnableAdminServer
@SpringBootApplication
public class MalphiteAdminApplication {

    public static void main(String[] args) {
        SpringApplication.run(MalphiteAdminApplication.class, args);
    }

}
