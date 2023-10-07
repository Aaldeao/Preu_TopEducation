package com.example.Preu_TopEducation_Ti.service;


import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;
import com.example.Preu_TopEducation_Ti.entities.ReporteEntity;
import com.example.Preu_TopEducation_Ti.repositories.CuotaRepository;
import com.example.Preu_TopEducation_Ti.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class EstudianteService {
    //@Autowired
    //CuotaRepository cuotaRepository;

    @Autowired // Es una instancia de EstudianteRepository //
    EstudianteRepository estudianteRepository;
    @Autowired
    CuotaService cuotaService;
    @Autowired
    PruebaService pruebaService;

    public EstudianteEntity ingresarestudiante(EstudianteEntity estudiante){ // Guarda en la base de datos los datos del estudiante //
        return estudianteRepository.save(estudiante);
    }

    // Crea un reporte para cada estudiante con los atributos solicitados y los almacena ReporteEntity //
    public ArrayList<ReporteEntity> crearReporte(){
        ArrayList<EstudianteEntity> estudiantes = estudianteRepository.findAll();
        ArrayList <ReporteEntity> reportes = new ArrayList<>();

        for (EstudianteEntity estudiante : estudiantes){
            int examenesRendidos = pruebaService.calcularCantidadprueba(estudiante.getRut());
            double promedioPuntaje = pruebaService.calcularpromediopuntaje(estudiante.getRut());
            double arancel = cuotaService.calculararancel(estudiante);
            String tipoDePago = estudiante.getTipoDepago();
            int cuotasPactadas = estudiante.getCantidad();
            //int cuotasPagadas =
            //int montoPagao =
            //LocalDate fechaUltimo =
            //double saldoaPagar =
            //int cuotasAtrasdas =

            ReporteEntity reporte = new ReporteEntity();
            reporte.setExamenesRendidos(examenesRendidos);
            reporte.setPromedioPuntaje(promedioPuntaje);
            reporte.setRut(estudiante.getRut());
            reporte.setNombre(estudiante.getNombres());
            reporte.setArancel(arancel);
            reporte.setTipoPago(tipoDePago);
            reporte.setCuotasPactadas(cuotasPactadas);

            reportes.add(reporte);

        }
        return reportes;
    }


}
