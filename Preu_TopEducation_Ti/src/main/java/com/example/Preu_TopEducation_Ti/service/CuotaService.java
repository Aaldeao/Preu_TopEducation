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

    // Calcula el arancel con los descuentos correspondientes sobre su tipo de colegio y los años de egreso//
    public double calculararancel(CuotaEntity cuota, EstudianteEntity estudiante){
        double arancel = 1500000;
        int egreso= LocalDate.now().getYear() - estudiante.getAnoEgreso();

        if ("Municipal".equalsIgnoreCase(estudiante.getTipoColegio())){ // por ser un tipo de colegio municipal tiene 20% //
            if(egreso < 1){
                arancel = arancel * 0.65; // 20% + 15% //
            } else if (egreso <= 2) {
                arancel = arancel * 0.72; // 20% + 8% //

            } else if (egreso <= 4) {
                arancel = arancel * 0.76; // 20% + 4% //

            }
        }else if ("Subvencionado".equalsIgnoreCase(estudiante.getTipoColegio())){ // por ser un tipo de colegio municipal tiene 10% //

            if(egreso<1){
                arancel = arancel * 0.75; // 10% + 15% //
            } else if (egreso <= 2) {
                arancel = arancel * 0.82; // 10% + 8% //

            } else if (egreso <= 4) {
                arancel = arancel * 0.86; // 10% + 4% //

            }
        }else if ("Privado".equalsIgnoreCase(estudiante.getTipoColegio())){ // El tipo colegio Privado solo tiene el descuento por los años de egreso //
            if(egreso<1){
                arancel = arancel * 0.85; // 15% //
            } else if (egreso <= 2) {
                arancel = arancel * 0.92; // 8% //

            } else if (egreso <= 4) {
                arancel = arancel * 0.96; // 4% //

            }
        }
        cuota.setArancel(arancel);
        return arancel;
    }

    //Calcula el arancel mensual que deberia pagar dependiendo de las cuotas escogidas//
    public double calcularcuotamensuales(CuotaEntity cuota, EstudianteEntity estudiante){
        double arancel  = calculararancel(cuota, estudiante);
        double arancelMensual = 0;
        if (arancel>0 && cuota.getCantidad()>0){
            arancelMensual = arancel / cuota.getCantidad();
        }
        return arancelMensual;
    }
    //Guarda la cuota el arancel y su arancel mensual del estudiante //
    public CuotaEntity guardarcuota(CuotaEntity cuota, EstudianteEntity estudiante){
        double arancelMensual = calcularcuotamensuales(cuota, estudiante);
        cuota.setArancelMensual(arancelMensual);
        cuota.setEstudiante(estudiante);
        return cuotaRepository.save(cuota);
    }

    // Obtener la cuota por el rut //
    public CuotaEntity obtenerPorRut(String rut){
     return cuotaRepository.findByEstudianteRut(rut);
    }
}
