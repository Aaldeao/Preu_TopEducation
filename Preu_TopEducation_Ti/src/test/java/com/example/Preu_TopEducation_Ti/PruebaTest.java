package com.example.Preu_TopEducation_Ti;

import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;
import com.example.Preu_TopEducation_Ti.entities.PruebaEntity;
import com.example.Preu_TopEducation_Ti.repositories.EstudianteRepository;
import com.example.Preu_TopEducation_Ti.repositories.PruebaRepository;
import com.example.Preu_TopEducation_Ti.service.PruebaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
class PruebaTest {

    @InjectMocks
    PruebaService pruebaService;
    @Mock //simula el Pruebarepository//
    PruebaRepository pruebaRepository;
    @Mock
    EstudianteRepository estudianteRepository;
    @BeforeEach
    public void verifica(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void guardarcsvTest() throws IOException { // se simula un archivo .csv  //

        String csv="22525698-0;2022-12-04;949";
        byte[] csvbytes = csv.getBytes(); // combierte la cadena en un archivo.csv //

        MultipartFile mockCsv = new MockMultipartFile("test.csv", new ByteArrayInputStream(csvbytes)); // simulamos la subida del archivo //
        String archivoSubido = pruebaService.cargarCsv(mockCsv);
        assertEquals("csv cargado", archivoSubido);

    }

    @Test
    void cantidadDePruebas() { // simula una cantidad de pruebas que tenga un estudiante //
        String rut = "20731198-7";
        ArrayList<PruebaEntity> pruebas = new ArrayList<>();
        pruebas.add(new PruebaEntity());
        pruebas.add(new PruebaEntity());
        when(pruebaRepository.findByEstudianteRut(rut)).thenReturn(pruebas);
        int cantidadPrueba = pruebaService.calcularCantidadprueba(rut); // la cual se revisa cuantas veces se repita el rut //
        assertEquals(2, cantidadPrueba);
    }

    @Test
    void calcularpromedioTest(){ // se calcula un promedio de puntajes de un estudiante que tiene 2 pruebas //
        String rut = "20731198-7";
        ArrayList<PruebaEntity> pruebas = new ArrayList<>();
        PruebaEntity prueba1 = new PruebaEntity();
        prueba1.setEstudiante(new EstudianteEntity());
        prueba1.setFechaExamen("2023-10-30");
        prueba1.setPuntaje(465L);

        PruebaEntity prueba2 = new PruebaEntity();
        prueba2.setEstudiante(new EstudianteEntity());
        prueba2.setFechaExamen("2023-10-30");
        prueba2.setPuntaje(520L);

        pruebas.add(prueba1);
        pruebas.add(prueba2);

        when(pruebaRepository.findByEstudianteRut(rut)).thenReturn(pruebas);
        double promedio = pruebaService.calcularpromediopuntaje(rut);
        double promedioesperado = ((465.0 + 520.0)/2.0);
        assertEquals(promedioesperado, promedio);
    }

}
