package com.example.Preu_TopEducation_Ti.service;

import com.example.Preu_TopEducation_Ti.entities.CuotaEntity;
import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;
import com.example.Preu_TopEducation_Ti.repositories.CuotaRepository;
import com.example.Preu_TopEducation_Ti.repositories.EstudianteRepository;
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
    EstudianteRepository estudianteRepository;
    @Autowired
    PruebaService pruebaService;

    // Crea un cuota //
    public CuotaEntity creacuota(int numero, EstudianteEntity estudiante){
        CuotaEntity cuota = new CuotaEntity();
        cuota.setEstudiante(estudiante);
        cuota.setEstado("Pendiente");
        cuota.setArancel(calculararancel(estudiante));
        cuota.setNumeroCuota(numero);
        cuota.setFechaEmision(LocalDate.now());
        calcularcuotamensuales(estudiante);
        cuota.setFechaPago(cuota.getFechaEmision().plusMonths(1).withDayOfMonth(5));
        cuota.setFechaVencimiento(cuota.getFechaEmision().plusMonths(1).withDayOfMonth(11));
        return cuota;
    }

    // Calcula el arancel con los descuentos correspondientes sobre su tipo de colegio y los años de egreso//
    public double calculararancel(EstudianteEntity estudiante){
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
        return arancel;
    }

    // Calcula el arancel mensual que deberia pagar dependiendo de las cuotas escogidas //
    public double calcularcuotamensuales( EstudianteEntity estudiante){
        double arancel  = calculararancel(estudiante);
        double arancelMensual = 0;
        if (arancel>0 && estudiante.getCantidad()>0){
            arancelMensual = arancel / estudiante.getCantidad();
        }
        return Math.round(arancelMensual); // redondea el valor decimal al numero entero mas cercano //
    }

    // Guarda el arancel y su arancel mensual del estudiante //
    public CuotaEntity guardarcuota(CuotaEntity cuota, EstudianteEntity estudiante){
        double arancelMensual = calcularcuotamensuales(estudiante);
        cuota.setArancelMensual(arancelMensual);
        cuota.setEstudiante(estudiante);
        return cuotaRepository.save(cuota);
    }

    // Genera las cuotas mediante a la cantidad asociada que solicia la persona y las guarda  //
    public void cuotasxEstudiante(EstudianteEntity estudiante ) {
        EstudianteEntity estudiante1 = estudianteRepository.findByRut(estudiante.getRut());
        estudiante1.setCantidad(estudiante.getCantidad());
        LocalDate fechaEmision=LocalDate.now();
        for (int i = 0; i < estudiante.getCantidad(); i++) {
            CuotaEntity cuota = creacuota(i + 1, estudiante1);
            cuota.setFechaEmision(fechaEmision);
            cuota.setFechaPago(cuota.getFechaEmision().plusMonths(1).withDayOfMonth(5));
            cuota.setFechaVencimiento(cuota.getFechaEmision().plusMonths(1).withDayOfMonth(11));
            guardarcuota(cuota, estudiante1);
            fechaEmision=cuota.getFechaEmision().plusMonths(1);
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
    public void pagarCuotaAtrasadas(CuotaEntity cuota) {
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

    // Se realiza el descuento a las cuotas mensuales pendientes que debe pagar el estudiantes gracias a su promedio de los puntajes de las pruebas //
    public void descuentoPrueba(CuotaEntity cuota , String rutEstudiante){
        LocalDate fechaLocal = LocalDate.now();
        LocalDate fechaPago = cuota.getFechaPago();
        if ("Pendiente".equals(cuota.getEstado()) && !cuota.isDescuentoPrueba() && fechaLocal.getMonth() == fechaPago.getMonth()) {
            double promedio = pruebaService.calcularpromediopuntaje(rutEstudiante);
            double arancelmensual= cuota.getArancelMensual();
            double descuento = 0;
            if (promedio >= 950 && promedio <= 1000){
                descuento = arancelmensual * 0.10;

            } else if (promedio >= 900 && promedio <= 949){
                descuento = arancelmensual * 0.05;

            } else if (promedio >= 850 && promedio <= 899){
                descuento = arancelmensual * 0.02;

            }
            cuota.setArancelMensual(arancelmensual - descuento);
            cuota.setDescuentoPrueba(true);
            cuotaRepository.save(cuota);

            List<CuotaEntity> cuotasPendientes = cuotaRepository.findCuotasPendintes();
            for (CuotaEntity cuotaPendientes : cuotasPendientes) {
                if (!cuotaPendientes.equals(cuota)) {
                    double nuevoArancelMensualPendiente2 = cuotaPendientes.getArancelMensual() - descuento;
                    cuotaPendientes.setArancelMensual(nuevoArancelMensualPendiente2);
                }
            }
            cuotaRepository.saveAll(cuotasPendientes);
        }
    }

    // Obtiene cuantas cuotas pagadas tienen el rut asociado //
    public int registrarPagada(String rut){
        int cuotaPagadas = cuotaRepository.cuotaspagadasRut(rut);
        return cuotaPagadas;
    }

    // Obtiene cuantas cuotas atrasadas tienen el rut asociado //
    public int registrarAtrasadas(String rut){
        int cuotasAtrasada = cuotaRepository.cuotasAtrasadaRut(rut);
        return cuotasAtrasada;
    }

    // Obtenemos el monto total que ha pagado el rut asociado //
    public double montoCuotasPagadas (String rut){
        double montototal = 0;
        ArrayList<CuotaEntity> cuotasPagadas = cuotaRepository.findCuotasPagadas(rut);
        for (CuotaEntity cuota : cuotasPagadas){
            montototal = montototal + cuota.getArancelMensual();
        }
        return montototal;
    }

    // Obtenemos el monto total que le falta por pagar estudiante(rut) asociado //
    public double montoApagar (String rut){
        double montototalatrasado = 0;
        ArrayList<CuotaEntity> cuotasPendiente = cuotaRepository.findCuotasPendienterut(rut);
        for (CuotaEntity cuota : cuotasPendiente){
            montototalatrasado = montototalatrasado + cuota.getArancelMensual();
        }
        return montototalatrasado;
    }

    // Obtenemos la ultima fecha de pago de la cuota del rut asociado //
    public LocalDate obtenerFechaultimaCuota (String rut){
        ArrayList<CuotaEntity> cuotas = cuotaRepository.findUltimafechadepago(rut);
        if (!cuotas.isEmpty()){
            CuotaEntity ultimacuota= cuotas.get(0);
            return ultimacuota.getFechaPago();
        }else {
            return null;
        }
    }

}
