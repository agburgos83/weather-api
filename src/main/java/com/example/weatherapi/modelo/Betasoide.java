package com.example.weatherapi.modelo;

import java.awt.Point;

import org.springframework.stereotype.Component;

@Component
public class Betasoide extends Planeta {

    public Betasoide(String name, double distanciaAlSol, double velocidadAngular, Point posicionInicial) {
        super(name, distanciaAlSol, velocidadAngular, posicionInicial);
    }

}
