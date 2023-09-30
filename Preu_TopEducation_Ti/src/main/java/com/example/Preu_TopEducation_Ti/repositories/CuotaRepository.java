package com.example.Preu_TopEducation_Ti.repositories;

import com.example.Preu_TopEducation_Ti.entities.CuotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;
import java.util.ArrayList;

@Repository
public interface CuotaRepository extends JpaRepository<CuotaEntity, Long> {
     ArrayList<CuotaEntity> findByEstudianteRut(String rut); // La busqueda del rut en la base de datos //
     Optional<CuotaEntity> findById(Long id); // Busca la cuota mediante su ID en la base de datos //
}
