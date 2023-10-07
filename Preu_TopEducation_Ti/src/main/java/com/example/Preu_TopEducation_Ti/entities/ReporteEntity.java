package com.example.Preu_TopEducation_Ti.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;


@NoArgsConstructor
@AllArgsConstructor
@Data
public class ReporteEntity {
    private String rut;
    private String Nombre;
    private int examenesRendidos;
    private double promedioPuntaje;
    private double arancel;
    private String tipoPago;
    private int cuotasPactadas;
    private int cuotasPagadas;
    private double montoPagado;
    private LocalDate fechaUltimo;
    private double saldoaPagar;
    private int cuotasAtrasadas;

}



