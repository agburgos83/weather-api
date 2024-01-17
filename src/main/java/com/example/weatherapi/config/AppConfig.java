package com.example.weatherapi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import com.example.weatherapi.modelo.Betasoide;
import com.example.weatherapi.modelo.Ferengi;
import com.example.weatherapi.modelo.Vulcano;

public class AppConfig {

    @Bean
    public Betasoide betasoide() {
        return PlanetaFactory.creaBetasoide();
    }

    @Bean
    public Ferengi ferengi() {
        return PlanetaFactory.creaFerengi();
    }

    @Bean
    public Vulcano vulcano() {
        return PlanetaFactory.creaVulcano();
    }

    @Bean
    public Jackson2ObjectMapperBuilder objectMapperBuilder() {
        Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        builder.indentOutput(true);
        return builder;
    }

}
