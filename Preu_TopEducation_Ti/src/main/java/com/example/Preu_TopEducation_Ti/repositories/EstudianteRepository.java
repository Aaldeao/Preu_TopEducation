package com.example.Preu_TopEducation_Ti.repositories;


import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EstudianteRepository extends JpaRepository<EstudianteEntity, Long> {
public EstudianteEntity findByRut(String rut);
}
