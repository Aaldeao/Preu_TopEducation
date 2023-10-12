package com.example.Preu_TopEducation_Ti.repositories;

import com.example.Preu_TopEducation_Ti.entities.CuotaEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.ArrayList;

@Repository
public interface CuotaRepository extends JpaRepository<CuotaEntity, Long> {
     ArrayList<CuotaEntity> findByEstudianteRut(String rut); // La busqueda del rut en la base de datos //
     Optional<CuotaEntity> findById(Long id); // Busca la cuota mediante su ID en la base de datos //
     @Query("select cuota from CuotaEntity cuota where cuota.estado = 'Pendiente'") // Obtenemos una lista de cuotas que solo tengan el estado pendiente //
     List<CuotaEntity> findCuotasPendintes();
     @Query("select count(cuota) from CuotaEntity cuota where cuota.estudiante.rut = :rut and cuota.estado = 'Pagado'") // Obtiene la cantidad de cuotas del rut que estan con estado Pagado //
     int cuotaspagadasRut (@Param("rut") String rut);

     @Query("select count(cuota) from CuotaEntity cuota where cuota.estudiante.rut = :rut and cuota.estado = 'Atrasada'") // Obtiene la cantidad de cuotas del rut que estan con estado Atrasadas //
     int cuotasAtrasadaRut (@Param("rut") String rut);

     @Query("select cuota from CuotaEntity cuota where cuota.estudiante.rut = :rut and cuota.estado = 'Pagado'") // Obtenemos las cuotas pagadas del rut asociado //
     ArrayList <CuotaEntity> findCuotasPagadas(@Param("rut") String rut);

     @Query("select cuota from CuotaEntity cuota where cuota.estudiante.rut = :rut and cuota.estado = 'Pendiente'") // Obtenemos las cuotas Pendiente del rut asociado //
     ArrayList <CuotaEntity> findCuotasPendienterut(@Param("rut") String rut);
     @Query("select cuota from CuotaEntity cuota where cuota.estudiante.rut = :rut order by cuota.fechaPago desc ") // Obtenemos las cuotas del rut asociado y las ordena descendente  //
     ArrayList <CuotaEntity> findUltimafechadepago(@Param("rut") String rut);

}
