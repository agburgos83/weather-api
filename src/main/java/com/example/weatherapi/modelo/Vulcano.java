package com.example.weatherapi.modelo;

import java.awt.Point;

import org.springframework.stereotype.Component;

@Component
public class Vulcano extends Planeta {

    public Vulcano(String name, double distanciaAlSol, double velocidadAngular, Point posicionInicial) {
        super(name, distanciaAlSol, velocidadAngular, posicionInicial);
    }

    public void avanzarUnDia() {
    };

}
