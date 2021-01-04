package com.dzero.malphite.data.jpa.config;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component("malphiteJpaConfig")
@ConfigurationProperties(prefix = "malphite.jpa")
@Getter
public class MalphiteJpaConfig {
    private int batchSize = 200;
}
