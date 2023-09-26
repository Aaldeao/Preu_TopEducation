package com.example.Preu_TopEducation_Ti.controllers;

import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;
import com.example.Preu_TopEducation_Ti.service.EstudianteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class EstudianteController {

    @Autowired // Es una instancia de EstudianteService //
    EstudianteService estudianteService;

    @GetMapping("/Inicio") // devuelve la vista del inicio //
    public String Inicio(){
        return "index";
    }

    @GetMapping("/Formulario") // muestra la vista del formulario y recibe los datos //
    public String IngresarEstudiante(Model model){
        model.addAttribute("estudiante", new EstudianteEntity());
        return "Formulario";
    }

    @PostMapping("/Guardar_Estudiante")// procesa y guardarlo en la base de datos //
    public String GuardarEstudiante(@ModelAttribute("estudiante") EstudianteEntity estudiante){
        estudianteService.ingresarestudiante(estudiante);
        return "index";
    }

}
