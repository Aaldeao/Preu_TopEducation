package com.example.Preu_TopEducation_Ti.controllers;

import com.example.Preu_TopEducation_Ti.service.PruebaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping
public class PruebaController {
    @Autowired
    PruebaService pruebaService;

    // recibe el archivo .csv para guardarlo en la base de datos //
    @PostMapping("/SubirExcel")
    public String subirExcel(@RequestParam("archivo_excel") MultipartFile pruebaExcel, Model model) {
        String resultado = pruebaService.cargarCsv(pruebaExcel);
        model.addAttribute("resultado", resultado);
        return "index";
    }
}
