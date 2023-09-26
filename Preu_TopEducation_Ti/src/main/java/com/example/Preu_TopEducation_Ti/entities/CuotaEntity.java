package com.example.Preu_TopEducation_Ti.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;


@Entity
@Table(name = "Cuota")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CuotaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCuota;
    private int cantidad;
    private double arancel;
    private double arancelMensual;
    private String fechaEmision;
    //private String fechaPago;
    private String estado;

    // relacion mucho a uno del estudiante cuota //
    @ManyToOne
    @JoinColumn(name="rut")
    private EstudianteEntity estudiante;


}
