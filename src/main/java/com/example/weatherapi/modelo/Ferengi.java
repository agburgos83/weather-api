package com.example.weatherapi.modelo;

import java.awt.Point;

import org.springframework.stereotype.Component;

@Component
public class Ferengi extends Planeta {

    public Ferengi(String name, double distanciaAlSol, double velocidadAngular, Point unaPosicionInicial) {
        super(name, distanciaAlSol, velocidadAngular, unaPosicionInicial);
    }

}
