package com.example.weatherapi.modelo;

import java.awt.Point;

import org.springframework.stereotype.Component;

@Component
public class Vulcano extends Planeta {

    public Vulcano(String name, double distanciaAlSol, double velocidadAngular, Point unaPosicionInicial) {
        super(name, distanciaAlSol, velocidadAngular, unaPosicionInicial);
    }

    public void avanzarUnDia() {
    };

}
