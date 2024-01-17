package com.example.weatherapi.modelo;

import java.awt.Point;

public abstract class Planeta {

    private String name;
    protected double distanciaAlSol;
    protected double velocidadAngular;
    protected Point posicionInicial;

    public Planeta(String name, double distanciaAlSol, double velocidadAngular, Point unaPosicionInicial) {
        this.name = name;
        this.distanciaAlSol = distanciaAlSol;
        this.velocidadAngular = velocidadAngular;
        this.posicionInicial = unaPosicionInicial;
    }

    public String getName() {
        return name;
    }

    public double getDistanciaAlSol() {
        return distanciaAlSol;
    }

    public Point calcularPosicion(int cantidadDeDias) {

        // Calcular ángulo en radianes
        double angle = Math.toRadians(velocidadAngular * cantidadDeDias);

        // Calcular nueva coordenada
        double x = distanciaAlSol * Math.cos(angle);
        double y = distanciaAlSol * Math.sin(angle);

        Point point = new Point();
        point.setLocation(x, y);
        return point;
    }

    public Point getPosicionInicial() {
        return posicionInicial;
    }

}
