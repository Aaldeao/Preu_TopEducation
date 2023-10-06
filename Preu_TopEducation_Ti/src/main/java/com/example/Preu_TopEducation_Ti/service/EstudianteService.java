package com.example.Preu_TopEducation_Ti.service;


import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;
import com.example.Preu_TopEducation_Ti.repositories.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EstudianteService {

    @Autowired // Es una instancia de EstudianteRepository //
    EstudianteRepository estudianteRepository;

    public EstudianteEntity ingresarestudiante(EstudianteEntity estudiante){ // Guarda en la base de datos los datos del estudiante //
        return estudianteRepository.save(estudiante);
    }
    public EstudianteEntity buscarRut(String rut){ // Permite buscar un estudiante por el rut //
        return estudianteRepository.findByRut(rut);
    }


}
