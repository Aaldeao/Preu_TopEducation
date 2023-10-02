package com.example.Preu_TopEducation_Ti.controllers;

import com.example.Preu_TopEducation_Ti.entities.CuotaEntity;
import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;
import com.example.Preu_TopEducation_Ti.service.CuotaService;
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

    @GetMapping("/GeneradorCuotas")// Devuelve la vista para generar las cuota y recibe los datos  //
    public String GeneradorCuotas(Model model){
        model.addAttribute("cuota",new CuotaEntity());
        return "GeneradorCuotas";
    }

    @PostMapping("/guardarCuota")// Guarda las cuotas del estudiante //
    public String GuardarCuota(EstudianteEntity estudiante){
        cuotaService.cuotasxEstudiante(estudiante);
        return "index";
    }

    @GetMapping("/Mostrar")// Solicita el rut del estudiante el cual esta asociado a las cuotas //
    public String mostrar(){
        return "Mostrar";
    }

    @PostMapping("/BuscarCuota") // Mediante el rut solicitado verifica si el estudiante tiene cuotas y las muestra//
    public String buscarcuota(@RequestParam("rut") String rut , Model model){ // metodo @RequestParam que nos sirve para acceder al valor del parametro rut //
        ArrayList<CuotaEntity> cuota = cuotaService.obtenerPorRut(rut);
        model.addAttribute("cuota", cuota);
        return "BuscarCuota";
    }

    @PostMapping("/Pagocuota") // Al apretar el boton de pagar en la cuota obtenemos el idCuota y se cambiamos el estado //
    public String pagoCuota(@RequestParam("idCuota") Long idCuota, Model model) {
        CuotaEntity cuota = cuotaService.obteneridCuota(idCuota);
        cuotaService.pagarCuota(cuota);
        ArrayList<CuotaEntity> cuotas = cuotaService.obtenerPorRut(cuota.getEstudiante().getRut());
        model.addAttribute("cuota", cuotas);
        return "BuscarCuota";
    }

    @PostMapping("/PagocuotaAtrasada")// Al apretar el boton de atrasada en la cuota se cambiamos el estado para luego agregar sus intereses //
    public String pagarCuotaAtrasada(@RequestParam("idCuota") Long idCuota, Model model) {
        CuotaEntity cuota = cuotaService.obteneridCuota(idCuota);
        cuotaService.pagarCuotaAtrasada(cuota);
        ArrayList<CuotaEntity> cuotas = cuotaService.obtenerPorRut(cuota.getEstudiante().getRut());
        model.addAttribute("cuota", cuotas);
        return "BuscarCuota";
    }
}
