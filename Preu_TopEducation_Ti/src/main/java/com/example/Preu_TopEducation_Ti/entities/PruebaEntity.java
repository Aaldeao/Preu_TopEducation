package com.example.Preu_TopEducation_Ti.entities;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "Prueba")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PruebaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idPrueba;
    private String fechaExamen;
    private Long puntaje;

    // relacion mucho a uno del estudiante prueba//
    @ManyToOne
    @JoinColumn(name="rut")
    private EstudianteEntity estudiante;
}
