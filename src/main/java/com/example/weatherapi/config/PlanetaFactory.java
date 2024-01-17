package com.example.weatherapi.config;

import java.awt.Point;

import org.springframework.context.annotation.Bean;

import com.example.weatherapi.modelo.Betasoide;
import com.example.weatherapi.modelo.Ferengi;
import com.example.weatherapi.modelo.Vulcano;

public class PlanetaFactory {

    @Bean
    public static Betasoide creaBetasoide() {
        Point posB = new Point(2000, 1000);
        return new Betasoide("Betasoide", 2000, 3, posB);
    }

    @Bean
    public static Ferengi creaFerengi() {
        Point posF = new Point(500, 1000);
        return new Ferengi("Ferengi", 500, 1, posF);
    }

    @Bean
    public static Vulcano creaVulcano() {
        Point posV = new Point(-1000, 1000);
        return new Vulcano("Vulcano", 1000, -5, posV);
    }

}
