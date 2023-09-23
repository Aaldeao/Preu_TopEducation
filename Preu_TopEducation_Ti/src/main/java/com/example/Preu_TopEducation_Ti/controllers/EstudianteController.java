package com.example.Preu_TopEducation_Ti.controllers;

import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;
import com.example.Preu_TopEducation_Ti.repositories.EstudianteRepository;
import com.example.Preu_TopEducation_Ti.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class EstudianteController {

    @Autowired
    EstudianteService estudianteService;

    @GetMapping("/Inicio")
    public String Inicio(){
        return "index";
    }

    @GetMapping("/Formulario")
    public String IngresarEstudiante(Model model){
        model.addAttribute("estudiante", new EstudianteEntity());
        return "Formulario";
    }

    @PostMapping("/Guardar_Estudiante")
    public String GuardarEstudiante(EstudianteEntity estudiante, Model model){
        estudianteService.ingresarestudiante(estudiante);
        model.addAttribute("mensaje","Estudiante Guardado Correctamente");
        return "index";
    }

}
