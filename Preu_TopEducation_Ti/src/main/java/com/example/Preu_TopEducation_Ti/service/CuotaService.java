package com.example.Preu_TopEducation_Ti.service;

import com.example.Preu_TopEducation_Ti.entities.CuotaEntity;
import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;
import com.example.Preu_TopEducation_Ti.repositories.CuotaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;
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
        cuota.setFechaPago(cuota.getFechaEmision().plusMonths(1).withDayOfMonth(5));
        cuota.setFechaVencimiento(cuota.getFechaEmision().plusMonths(1).withDayOfMonth(11));
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
            cuota.setFechaPago(cuota.getFechaEmision().plusMonths(i + 2).withDayOfMonth(5));
            cuota.setFechaVencimiento(cuota.getFechaEmision().plusMonths(i + 2).withDayOfMonth(11));
        }
    }

    // Obtener las cuotas asociadas por el rut //
    public ArrayList<CuotaEntity> obtenerPorRut(String rut){
     return cuotaRepository.findByEstudianteRut(rut);
    }

    // Busca la cuota mediante su idCuota //
    public CuotaEntity obteneridCuota(Long idCuota) {
        Optional<CuotaEntity> optionalCuota = cuotaRepository.findById(idCuota);
        return optionalCuota.orElse(null); // El cual retorna la cuota o null si es que no la encuentra //
    }

    // Al apretar el boton pagar se cambie el estado de la cuota de pendiente a pagado o atrasada dependiendo de la fecha local //
    public void pagarCuota(CuotaEntity cuota){
        LocalDate fechaLocal = LocalDate.now();
        LocalDate fechaPago = cuota.getFechaPago();
        //LocalDate fechaVencimiento = cuota.getFechaVencimiento();
        if ("Pendiente".equals(cuota.getEstado()) ) {
            int diaDelmes = fechaLocal.getDayOfMonth();
            int mesDeldia = fechaPago.getMonthValue();
            if (fechaLocal.equals(fechaPago) || (fechaLocal.getMonthValue() == mesDeldia && diaDelmes >= 6 && diaDelmes <= 10)) {
                cuota.setEstado("Pagado");
                cuotaRepository.save(cuota);
            }
        }
    }

    // Calcula los meses atrasados y aplica los intereses según la cantidad de meses //
    public void pagarCuotaAtrasada(CuotaEntity cuota) {
        LocalDate fechaLocal = LocalDate.now();
        long mesesAtrasados = ChronoUnit.MONTHS.between(cuota.getFechaPago(), fechaLocal);// calcula la cantidad de meses entre la fecha de pago con la fecha local //
        if ("Pendiente".equals(cuota.getEstado())) {
            double interes = 0;
            if ( mesesAtrasados == 1) {
                interes = cuota.getArancelMensual() * 0.03;
                cuota.setEstado("Atrasada");
            } else if (mesesAtrasados == 2) {
                interes = cuota.getArancelMensual() * 0.06;
                cuota.setEstado("Atrasada");
            } else if (mesesAtrasados == 3) {
                interes = cuota.getArancelMensual() * 0.09;
                cuota.setEstado("Atrasada");
            } else if (mesesAtrasados > 3) {
                interes = cuota.getArancelMensual() * 0.15;
                cuota.setEstado("Atrasada");
            }
            double nuevoArancelMensual = cuota.getArancelMensual() + interes;
            cuota.setArancelMensual(nuevoArancelMensual);

            List<CuotaEntity> cuotasPendientes = cuotaRepository.findCuotasPendintes(); // Obtengo una lista de las cuotas pendientes que quedan y le aplico el interes que corresponde //
            for (CuotaEntity cuotaPendientes : cuotasPendientes) {
                if (!cuotaPendientes.equals(cuota)) {
                    double nuevoArancelMensualPendiente = cuotaPendientes.getArancelMensual() + interes;
                    cuotaPendientes.setArancelMensual(nuevoArancelMensualPendiente);
                }
            }
            cuotaRepository.save(cuota);
        }
    }

}
