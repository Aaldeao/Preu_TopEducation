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
public class EstudianteEntity {
    @Id
    private String rut;
    private String Apellidos;
    private String Nombres;
    private Date Fecha_Nacimiento;
    private String Tipo_Colegio;
    private String Nombre_Colegio;
    private Date Año_Egreso;

}
