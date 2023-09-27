package com.example.Preu_TopEducation_Ti.controllers;

import com.example.Preu_TopEducation_Ti.entities.CuotaEntity;
import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;
import com.example.Preu_TopEducation_Ti.service.CuotaService;
import com.example.Preu_TopEducation_Ti.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;

@Controller
@RequestMapping
public class CuotaController {

    @Autowired
    CuotaService cuotaService;

    @Autowired
    EstudianteService estudianteService;

    @GetMapping("/GeneradorCuotas")// devuelve la vista generador de cuota y recibe los datos  //
    public String GeneradorCuotas(Model model){
        model.addAttribute("cuota",new CuotaEntity());
        return "GeneradorCuotas";
    }

    @PostMapping("/guardarCuota")// muestra el formulario del generador de cuotas y los procesa  //
    public String GuardarCuota(EstudianteEntity estudiante){
        EstudianteEntity estudiante1 = estudianteService.buscarRut(estudiante.getRut());
        estudiante1.setCantidad(estudiante.getCantidad());
        for (int i = 0; i <estudiante.getCantidad(); i++) { // genera la cantidad que aparece el esudiante  en la base de datos  mediante la cantidad de cuotas que solicito //
            CuotaEntity cuota = cuotaService.creacuota(i + 1, estudiante1);
            cuotaService.guardarcuota(cuota,estudiante1);
        }
        return "index";
    }
    @GetMapping("/Mostrar")// solicita el rut del estudiante el cual esta asociado a las cuotas //
    public String mostrar(){
        return "Mostrar";
    }

    @PostMapping("/BuscarCuota") // Mediante el rut solicitado verifica mediante condiciones si el estudiante tiene cuotas //
    public String buscarcuota(@RequestParam("rut") String rut , Model model){ // metodo @RequestParam que nos sirve para acceder al valor del parametro rut//
        ArrayList<CuotaEntity> cuota = cuotaService.obtenerPorRut(rut);
        if (cuota != null) {
            model.addAttribute("cuota", cuota);
        }
        return "BuscarCuota";
    }
}
