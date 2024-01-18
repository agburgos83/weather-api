package com.example.weatherapi.servicios;

import java.awt.Point;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.weatherapi.modelo.Betasoide;
import com.example.weatherapi.modelo.Ferengi;
import com.example.weatherapi.modelo.Vulcano;
import com.example.weatherapi.responses.RespuestaPronostico;

@Service
public class PronosticoService {

    private static final Point centroSolar = new Point(0, 0);
    private static final double tolerancia = 1;

    private Betasoide betasoide;
    private Ferengi ferengi;
    private Vulcano vulcano;

    private LocalDate fechaAConsultar;

    public PronosticoService(Betasoide betasoide, Ferengi ferengi, Vulcano vulcano) {
        this.betasoide = betasoide;
        this.ferengi = ferengi;
        this.vulcano = vulcano;
    }

    public void setFecha(LocalDate fechaAConsultar) {
        this.fechaAConsultar = fechaAConsultar;
    }

    private Line2D lineaMasLarga(Point posB, Point posF, Point posV) {
        double dis1 = Point2D.distance(posB.getX(), posB.getY(), posF.getX(), posF.getY());
        double dis2 = Point2D.distance(posF.getX(), posF.getY(), posV.getX(), posV.getY());
        double dis3 = Point2D.distance(posV.getX(), posV.getY(), posB.getX(), posB.getY());
        Line2D lineaMaslarga = new Line2D.Double();
        if (dis1 > dis2 && dis1 > dis3) {
            lineaMaslarga.setLine(posB.getX(), posB.getY(), posF.getX(), posF.getY());
        } else if (dis2 > dis1 && dis2 > dis3) {
            lineaMaslarga.setLine(posF.getX(), posF.getY(), posV.getX(), posV.getY());
        }
        lineaMaslarga.setLine(posV.getX(), posV.getY(), posB.getX(), posB.getY());
        return lineaMaslarga;
    }

    private double areaDelTriangulo(Point posB, Point posF, Point posV, double perimetro) {
        double semiperimetro = perimetro / 2;
        return Math.sqrt(semiperimetro * (semiperimetro - (posB.distance(posF)))
                * (semiperimetro - (posF.distance(posV))) * (semiperimetro - (posV.distance(posB))));
    }

    private double perimetroDelTriangulo(Point posB, Point posF, Point posV) {
        return posB.distance(posF) + posF.distance(posV) + posV.distance(posB);
    }

    private boolean solEnTriangulo(Point posB, Point posF, Point posV, double perimetro) {
        double area0 = areaDelTriangulo(posB, posF, posV, perimetro);

        double perimetro1 = this.perimetroDelTriangulo(posB, posF, centroSolar);
        double area1 = areaDelTriangulo(posB, posF, centroSolar, perimetro1);

        double perimetro2 = this.perimetroDelTriangulo(posF, posV, centroSolar);
        double area2 = areaDelTriangulo(posF, posV, centroSolar, perimetro2);

        double perimetro3 = this.perimetroDelTriangulo(posV, posB, centroSolar);
        double area3 = areaDelTriangulo(posV, posB, centroSolar, perimetro3);

        return Math.abs(area0 - (area1 + area2 + area3)) < tolerancia;
    }

    private boolean alineadosConElSol(Line2D lineaPlanetas) {
        return (lineaPlanetas.getBounds2D().contains(centroSolar)
                || lineaPlanetas.ptLineDist(centroSolar) < tolerancia
                || lineaPlanetas.relativeCCW(centroSolar) == 0);
    }

    @Scheduled(cron = "0 * * * * *")
    // @Scheduled(cron = "0 0 5 * * *")
    public RespuestaPronostico pronosticadorADiezAños() {

        String climaActual = "";
        String climaPrevio = "";

        int periodosDeSequia = 0;
        int periodosDeLluvia = 0;
        int periodosDeClimaOptimo = 0;
        double perimetroActual = 0;
        double perimetroLluviaMax = -1;
        LocalDate diaLluviaMax = LocalDate.now();
        int numDiaAConsultar = 4000;
        String climaFechaConsultada = "";

        if (fechaAConsultar != null) {
            numDiaAConsultar = (int) ChronoUnit.DAYS.between(LocalDate.now(), fechaAConsultar);
        }

        for (int i = 0; i < 3650; i++) {

            Point posBetasoide = betasoide.calcularPosicion(i);
            Point posFerengi = ferengi.calcularPosicion(i);
            Point posVulcano = vulcano.calcularPosicion(i);
            perimetroActual = this.perimetroDelTriangulo(posBetasoide, posFerengi, posVulcano);

            // entra acá si están alineados los planetas
            if (this.areaDelTriangulo(posBetasoide, posFerengi, posVulcano, perimetroActual) == 0) {
                Line2D lineaPlanetas = this.lineaMasLarga(posBetasoide, posFerengi, posVulcano);
                if (this.alineadosConElSol(lineaPlanetas)) {
                    climaActual = "seco";
                } else {
                    climaActual = "óptimo";
                }
            } else {
                if (this.solEnTriangulo(posBetasoide, posFerengi, posVulcano, perimetroActual) == true) {
                    // entra si hay período de lluvia
                    climaActual = "lluvioso";
                    if (perimetroActual > perimetroLluviaMax) {
                        perimetroLluviaMax = perimetroActual;
                        diaLluviaMax = LocalDate.now().plusDays(i);
                    }
                } else {
                    climaActual = "clima no válido";
                }
            }

            if (numDiaAConsultar == i) {
                climaFechaConsultada = climaActual;
            }

            // cambia el período climático
            if (climaActual != climaPrevio && climaPrevio != "") {
                if (climaActual == "seco") {
                    periodosDeSequia++;
                } else if (climaActual == "óptimo") {
                    periodosDeClimaOptimo++;
                } else {
                    periodosDeLluvia++;
                }
            }
            climaPrevio = climaActual;
        }

        // informar
        Map<String, String> respuestaDecada = new HashMap<>();
        respuestaDecada.put("periodosClimaSeco", "" + periodosDeSequia);
        respuestaDecada.put("periodosClimaOptimo", "" + periodosDeClimaOptimo);
        respuestaDecada.put("periodosClimaLluvioso", "" + periodosDeLluvia);
        respuestaDecada.put("diaMasLluvioso", "" + diaLluviaMax);

        Map<String, Map<String, String>> respuesta = new HashMap<>();
        respuesta.put("climaDecada", respuestaDecada);

        if (fechaAConsultar != null) {
            Map<String, String> respuestaFechaConsultada = new HashMap<>();
            respuestaFechaConsultada.put(fechaAConsultar.toString(), "" + climaFechaConsultada);
            respuesta.put("climaFecha", respuestaFechaConsultada);
        }

        System.out.println(respuesta.toString());

        RespuestaPronostico respuestaPronostico = new RespuestaPronostico(respuesta);
        return respuestaPronostico;
    }

}
