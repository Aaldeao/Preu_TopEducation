package com.example.Preu_TopEducation_Ti;


import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;

import com.example.Preu_TopEducation_Ti.entities.ReporteEntity;
import com.example.Preu_TopEducation_Ti.repositories.EstudianteRepository;
import com.example.Preu_TopEducation_Ti.service.EstudianteService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
class EstudianteTest {

    @Autowired
    EstudianteService estudianteService;
    @Autowired
    EstudianteRepository estudianteRepository;

    EstudianteEntity estudiante = new EstudianteEntity();
    @Test
    void ingresarestudianteTest(){
        estudiante.setRut("20721178-k");
        estudiante.setNombres("Hernan");
        estudiante.setApellidos("Herrera");
        estudiante.setFechaNacimiento("2001-05-24");
        estudiante.setTipoColegio("Public");
        estudiante.setNombreColegio("Liceo Nacional de Maipu");
        estudiante.setAnoEgreso(2021);
        estudiante.setCantidad(6);
        estudiante.setTipoDepago("Cuotas");

        EstudianteEntity estudiante1 = estudianteService.ingresarestudiante(estudiante);
        assertNotNull(estudiante1);//se asegura que el valor no sea nulo//
    }

    @Test
    void crearReporteTest(){
        estudiante.setRut("20851458-0");
        estudiante.setNombres("Nacho");
        estudiante.setApellidos("Arias");
        estudiante.setFechaNacimiento("2000-10-15");
        estudiante.setTipoDepago("Cuotas");
        estudiante.setTipoColegio("Privado");
        estudiante.setNombreColegio("Los Agilas");
        estudiante.setAnoEgreso(2023);
        estudiante.setCantidad(4);
        estudianteRepository.save(estudiante);
        ArrayList<ReporteEntity> reportes = estudianteService.crearReporte();
        assertNotNull(reportes);//se asegura que la lista de reportes no sea nula//
        assertTrue(reportes.size()>0);//se asegura que almenos tenga algun elemento//
    }


}
