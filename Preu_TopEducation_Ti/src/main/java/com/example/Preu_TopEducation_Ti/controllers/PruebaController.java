package com.example.Preu_TopEducation_Ti.controllers;

import com.example.Preu_TopEducation_Ti.service.PruebaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequestMapping
public class PruebaController {
    @Autowired
    PruebaService pruebaService;

    @GetMapping("/subirArchivoExcel") // Devuelve la vista para subir el archivo excel //
    public String subirArchivo(){
        return "subirArchivoExcel";
    }

    // recibe el archivo .csv para guardarlo en la base de datos //
    @PostMapping("/SubirExcel")
    public String subirExcel(@RequestParam("archivo_excel") MultipartFile pruebaExcel, Model model) {
        pruebaService.guardar(pruebaExcel);
        String resultado = pruebaService.cargarCsv(pruebaExcel);
        model.addAttribute("resultado", resultado);
        return "index";
    }
}
