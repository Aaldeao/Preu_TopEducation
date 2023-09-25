package com.example.Preu_TopEducation_Ti.service;

import com.example.Preu_TopEducation_Ti.entities.CuotaEntity;
import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;
import com.example.Preu_TopEducation_Ti.repositories.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
public class CuotaService {
    @Autowired
    CuotaRepository cuotaRepository;
    public double calculararancel(CuotaEntity cuota, EstudianteEntity estudiante){
        double arancel = 1500000;
        int egreso= LocalDate.now().getYear() - estudiante.getAnoEgreso();

        if ("Municipal".equalsIgnoreCase(estudiante.getTipoColegio())){
            if(egreso < 1){
                arancel = arancel * 0.65;
            } else if (egreso <= 2) {
                arancel = arancel * 0.72;

            } else if (egreso <= 4) {
                arancel = arancel * 0.76;

            }
        }else if ("Subvencionado".equalsIgnoreCase(estudiante.getTipoColegio())){

            if(egreso<1){
                arancel = arancel * 0.75;
            } else if (egreso <= 2) {
                arancel = arancel * 0.82;

            } else if (egreso <= 4) {
                arancel = arancel * 0.86;

            }
        }
        cuota.setArancel(arancel);
        return arancel;
    }

    public double calcularcuotamensuales(CuotaEntity cuota, EstudianteEntity estudiante){
        double arancel  = calculararancel(cuota, estudiante);
        double arancelMensual = 0;
        if (arancel>0 && cuota.getCantidad()>0){
            arancelMensual = arancel / cuota.getCantidad();
        }
        return arancelMensual;
    }
    public CuotaEntity guardarcuota(CuotaEntity cuota, EstudianteEntity estudiante){
        double arancelMensual = calcularcuotamensuales(cuota, estudiante);
        cuota.setArancelMensual(arancelMensual);
        cuota.setEstudiante(estudiante);
        return cuotaRepository.save(cuota);
    }
}
