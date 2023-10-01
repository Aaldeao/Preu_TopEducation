package com.example.Preu_TopEducation_Ti.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDate;


@Entity
@Table(name = "Cuota")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuotaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCuota;
    private int numeroCuota;
    private double arancel;
    private double arancelMensual;
    private LocalDate fechaEmision;
    private LocalDate fechaPago;
    private LocalDate fechaVencimiento;
    private String estado;

    // relacion mucho a uno del estudiante cuota //
    @ManyToOne
    @JoinColumn(name="rut")
    private EstudianteEntity estudiante;

}
