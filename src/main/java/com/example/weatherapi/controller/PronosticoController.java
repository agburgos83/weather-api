package com.example.weatherapi.controller;

import java.time.LocalDate;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.weatherapi.responses.RespuestaPronostico;
import com.example.weatherapi.servicios.PronosticoService;

@RestController
@RequestMapping("/api/v1/pronostico")
public class PronosticoController {

    private final PronosticoService pronosticoService;

    @Autowired
    public PronosticoController(PronosticoService pronostico) {
        this.pronosticoService = pronostico;
    }

    @GetMapping("/ParaFecha")
    public ResponseEntity<?> getPronosticoParaUnaFecha(
            @RequestParam("date") @DateTimeFormat(pattern = "dd.MM.yyyy") LocalDate unaFecha) {

        if (isValidDate(unaFecha)) {
            pronosticoService.setFecha(unaFecha);
            RespuestaPronostico respuestaPronostico = pronosticoService.pronosticadorADiezAños();
            return ResponseEntity.ok(respuestaPronostico);
        } else {
            // return ResponseEntity.badRequest().body("La fecha provista no es válida");
            return ResponseEntity.badRequest().body(Map.of("error", "La fecha provista no es válida"));

        }
    }

    private boolean isValidDate(LocalDate date) {
        return date != null && (date.equals(LocalDate.now()) || date.isAfter(LocalDate.now()))
                && date.isBefore(LocalDate.now().plusDays(3651));
    }

}