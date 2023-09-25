package com.example.Preu_TopEducation_Ti.entities;

import lombok.*;

import javax.persistence.*;
import java.util.List;


@Entity
@Table(name = "Estudiante")
@NoArgsConstructor
@Data
@AllArgsConstructor

public class EstudianteEntity {
    @Id
    private String rut;
    private String apellidos;
    private String nombres;
    private String fechaNacimiento;
    private String tipoColegio;
    private String nombreColegio;
    private int anoEgreso;

}
