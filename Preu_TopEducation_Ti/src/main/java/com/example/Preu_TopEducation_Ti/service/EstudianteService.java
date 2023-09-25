package com.example.Preu_TopEducation_Ti.service;


import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;
import com.example.Preu_TopEducation_Ti.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstudianteService {

    @Autowired
    EstudianteRepository estudianteRepository;

    public EstudianteEntity ingresarestudiante(EstudianteEntity estudiante){
        return estudianteRepository.save(estudiante);
    }
    public EstudianteEntity buscarRut(String rut){
        return estudianteRepository.findByRut(rut);
    }
}
