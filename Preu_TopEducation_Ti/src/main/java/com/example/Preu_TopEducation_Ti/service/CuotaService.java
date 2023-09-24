package com.example.Preu_TopEducation_Ti.service;

import com.example.Preu_TopEducation_Ti.entities.CuotaEntity;
import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;
import com.example.Preu_TopEducation_Ti.repositories.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class CuotaService {
    @Autowired
    CuotaRepository cuotaRepository;

    public double calcularcuotamensuales(CuotaEntity cuota){
        double arancelMensual = 0;
        if (cuota.getArancel()>0 && cuota.getCantidad()>0){
            arancelMensual = cuota.getArancel() / cuota.getCantidad();
        }
        return arancelMensual;
    }
    public CuotaEntity guardarcuota(CuotaEntity cuota, EstudianteEntity estudiante){
        double arancel = 1500000;
        cuota.setArancel(arancel);
        double arancelMensual = calcularcuotamensuales(cuota);
        cuota.setArancelMensual(arancelMensual);
        cuota.setEstudiante(estudiante);
        return cuotaRepository.save(cuota);
    }
}
