package com.example.Preu_TopEducation_Ti.service;

import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;
import com.example.Preu_TopEducation_Ti.entities.PruebaEntity;
import com.example.Preu_TopEducation_Ti.repositories.EstudianteRepository;
import com.example.Preu_TopEducation_Ti.repositories.PruebaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import lombok.Generated;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;
import java.nio.file.Paths;

@Service
public class PruebaService {
    @Autowired
    PruebaRepository pruebaRepository;
    @Autowired
    EstudianteRepository estudianteRepository;


    // Guarda el archivo subido en mi carpeta raiz //
    private final Logger logg = LoggerFactory.getLogger(PruebaService.class);
    @Generated
    public String guardar(MultipartFile file){
        String filename = file.getOriginalFilename();
        if(filename != null){
            if(!file.isEmpty()){
                try{
                    byte [] bytes = file.getBytes();
                    Path path  = Paths.get(file.getOriginalFilename());
                    Files.write(path, bytes);
                    logg.info("Archivo guardado");
                }
                catch (IOException e){
                    logg.error("ERROR", e);
                }
            }
            return "Archivo guardado con exito!";
        }
        else{
            return "No se pudo guardar el archivo";
        }
    }

    // lee un archivo .csv el cual valida si el formato es el indicado(rut,fecha,puntaje) y luego lo guarda en la base de datos //
    public String cargarCsv(MultipartFile archivoCSV) {
        try {
            Scanner scanner = new Scanner(archivoCSV.getInputStream());
            scanner.useDelimiter(";");

            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                String[] parts = line.split(";");

                if (parts.length == 3) { // comprueba si el array tiene un largo 3 //
                    String rut = parts[0];
                    String fechaExamen = parts[1];
                    String puntajeStr = parts[2].trim();// El puntaje debe ser el tercero y elimina el espacio en blanco //
                    Long puntaje = Long.parseLong(puntajeStr); // convierte el puntaje del alumno en Long //

                    EstudianteEntity estudiante2 = estudianteRepository.findByRut(rut);
                    PruebaEntity pruebaEntity = new PruebaEntity();
                    pruebaEntity.setEstudiante(estudiante2);
                    pruebaEntity.setFechaExamen(fechaExamen);
                    pruebaEntity.setPuntaje(puntaje);
                    pruebaRepository.save(pruebaEntity);
                }
            }
            scanner.close();
            return "csv cargado";
        } catch (IOException e) {
            e.printStackTrace();
            return "error con el archivo.csv";
        }
    }

    public void calcularpromediopuntaje(PruebaEntity pruebaEntity){

    }
}
