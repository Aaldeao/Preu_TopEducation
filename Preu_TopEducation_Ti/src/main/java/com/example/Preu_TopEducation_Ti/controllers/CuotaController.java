package com.example.Preu_TopEducation_Ti.controllers;

import com.example.Preu_TopEducation_Ti.entities.CuotaEntity;
import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;
import com.example.Preu_TopEducation_Ti.service.CuotaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;

@Controller
public class CuotaController {

    @Autowired
    CuotaService cuotaService;

    @GetMapping("/GeneradorCuotas")
    public String GeneradorCuotas(Model model){
        model.addAttribute("cuota",new CuotaEntity());
        return "GeneradorCuotas";
    }

    @PostMapping("/guardarCuota")
    public String GuardarCuota(@ModelAttribute("cuota") CuotaEntity cuota , EstudianteEntity estudiante){
        cuotaService.guardarcuota(cuota,estudiante );
        return "index";
    }
    @GetMapping("/Mostrar")
    public String mostrar(){
        return "Mostrar";
    }

}
