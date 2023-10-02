package com.example.Preu_TopEducation_Ti.service;

import com.example.Preu_TopEducation_Ti.entities.PruebaEntity;
import com.example.Preu_TopEducation_Ti.repositories.PruebaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Scanner;

@Service
public class PruebaService {
    @Autowired
    PruebaRepository pruebaRepository;

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
                    Long puntaje = Long.parseLong(puntajeStr); // convierte el puntaje en Long //

                    PruebaEntity pruebaEntity = new PruebaEntity();
                    pruebaEntity.setRut(rut);
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
}
