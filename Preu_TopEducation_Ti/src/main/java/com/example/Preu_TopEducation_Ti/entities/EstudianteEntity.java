package com.example.Preu_TopEducation_Ti.entities;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
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
    private String apellidos;
    private String nombres;
    private String fechaNacimiento;
    private String tipoColegio;
    private String nombreColegio;
    private String anoEgreso;

}
