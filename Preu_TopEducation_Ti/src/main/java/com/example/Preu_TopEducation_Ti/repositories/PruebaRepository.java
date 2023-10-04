package com.example.Preu_TopEducation_Ti.repositories;

import com.example.Preu_TopEducation_Ti.entities.PruebaEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.ArrayList;

public interface PruebaRepository extends JpaRepository<PruebaEntity, Long> {
    ArrayList<PruebaEntity> findByEstudianteRut(String rut);// La busqueda del rut en la base de datos //
}
