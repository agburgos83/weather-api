package com.example.weatherapi.responses;

import java.util.Map;

public class RespuestaPronostico {

    private Map<String, Map<String, String>> resumenClimatico;

    public RespuestaPronostico(Map<String, Map<String, String>> respuesta) {
        this.resumenClimatico = respuesta;
    }

    public Map<String, Map<String, String>> getResumenClimatico() {
        return resumenClimatico;
    }
}