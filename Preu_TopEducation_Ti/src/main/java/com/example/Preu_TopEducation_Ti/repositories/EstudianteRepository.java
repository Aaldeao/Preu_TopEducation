package com.example.Preu_TopEducation_Ti.repositories;


import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;

@Repository
public interface EstudianteRepository extends JpaRepository<EstudianteEntity, Long> {
    EstudianteEntity findByRut(String rut); // Permite buscar por el rut en la base de datos //

    ArrayList<EstudianteEntity>findAll(); //Obtiene lo del estudiante //

}
