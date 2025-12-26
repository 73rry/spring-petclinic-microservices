package org.springframework.samples.petclinic.customers.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.cloud.netflix.eureka.http.EurekaClientHttpRequestFactorySupplier;

@Configuration
public class RestClientConfiguration {

    @Bean
    EurekaClientHttpRequestFactorySupplier.RequestConfigCustomizer requestConfigCustomizer() {
        return builder -> builder.setProtocolUpgradeEnabled(false);
    }
}