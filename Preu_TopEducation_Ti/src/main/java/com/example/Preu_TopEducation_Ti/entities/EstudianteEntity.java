package com.example.Preu_TopEducation_Ti.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;


@Entity
@Table(name = "Estudiante")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int rut;

    private String Apellidos;
    private String Nombre;

    @Temporal(TemporalType.DATE)
    private Date Fecha_Nacimiento;

    private String Tipo_Colegio;
    private String Nombre_Colegio;

    @Temporal(TemporalType.DATE)
    private Date Año_Egreso;

}
