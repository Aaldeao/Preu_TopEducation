package com.example.Preu_TopEducation_Ti.repositories;

import com.example.Preu_TopEducation_Ti.entities.CuotaEntity;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface CuotaRepository extends JpaRepository<CuotaEntity, Long> {
     ArrayList<CuotaEntity> findByEstudianteRut(String rut); // La busqueda del rut en la base de datos //
}
