package com.example.Preu_TopEducation_Ti.service;

import com.example.Preu_TopEducation_Ti.entities.CuotaEntity;
import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;
import com.example.Preu_TopEducation_Ti.repositories.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;

@Service
public class CuotaService {
    @Autowired
    CuotaRepository cuotaRepository;
    @Autowired
    EstudianteService estudianteService;

    // Crea un cuota //
    public CuotaEntity creacuota(int numero, EstudianteEntity estudiante){
        CuotaEntity cuota = new CuotaEntity();
        cuota.setEstudiante(estudiante);
        cuota.setEstado("Pendiente");
        cuota.setArancel(1500000);
        cuota.setNumeroCuota(numero);
        cuota.setFechaEmision(LocalDate.now());
        calcularcuotamensuales(cuota, estudiante);
         return cuota;
    }

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

    // Calcula el arancel mensual que deberia pagar dependiendo de las cuotas escogidas //
    public double calcularcuotamensuales(CuotaEntity cuota, EstudianteEntity estudiante){
        double arancel  = calculararancel(cuota, estudiante);
        double arancelMensual = 0;
        if (arancel>0 && estudiante.getCantidad()>0){
            arancelMensual = arancel / estudiante.getCantidad();
        }
        return Math.round(arancelMensual); // redondea el valor decimal al numero entero mas cercano //
    }

    //Guarda el arancel y su arancel mensual del estudiante //
    public CuotaEntity guardarcuota(CuotaEntity cuota, EstudianteEntity estudiante){
        double arancelMensual = calcularcuotamensuales(cuota, estudiante);
        cuota.setArancelMensual(arancelMensual);
        cuota.setEstudiante(estudiante);
        return cuotaRepository.save(cuota);
    }

    // Genera las cuotas mediante a la cantidad asociada que solicia la persona y las guarda  //
    public void cuotasxEstudiante(EstudianteEntity estudiante ) {
        EstudianteEntity estudiante1 = estudianteService.buscarRut(estudiante.getRut());
        estudiante1.setCantidad(estudiante.getCantidad());
        for (int i = 0; i < estudiante.getCantidad(); i++) {
            CuotaEntity cuota = creacuota(i + 1, estudiante1);
            guardarcuota(cuota, estudiante1);
        }
    }

    // Obtener las cuotas asociadas por el rut //
    public ArrayList<CuotaEntity> obtenerPorRut(String rut){
     return cuotaRepository.findByEstudianteRut(rut);
    }

    //Genera que al apretar el boton pagar se cambie el estado de su cuota de pendiente a pagado //
    public void pagarCuota(CuotaEntity cuota){
        if ("Pendiente".equals(cuota.getEstado())){
            cuota.setEstado("Pagado");
            cuotaRepository.cambiarEstado(cuota.getIdCuota(), "Pagado");
        }
    }
}
