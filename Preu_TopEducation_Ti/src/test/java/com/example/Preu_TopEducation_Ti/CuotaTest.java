package com.example.Preu_TopEducation_Ti;

import com.example.Preu_TopEducation_Ti.entities.CuotaEntity;
import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;
import com.example.Preu_TopEducation_Ti.repositories.CuotaRepository;
import com.example.Preu_TopEducation_Ti.repositories.EstudianteRepository;
import com.example.Preu_TopEducation_Ti.service.CuotaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;


@SpringBootTest
class CuotaTest {

    @Autowired
    CuotaService cuotaService;
    @Mock
    CuotaRepository cuotaRepository;
    @Mock
    EstudianteRepository estudianteRepository;

    @BeforeEach
    void setUp(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void crearcuotaTest(){
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setAnoEgreso(2023);
        estudiante.setTipoColegio("Municipal");
        int cantidadCuota = 10;
        double arancel = cuotaService.calculararancel(estudiante);
        CuotaEntity cuota = cuotaService.creacuota(cantidadCuota,estudiante);
        assertEquals(estudiante, cuota.getEstudiante());
        assertEquals("Pendiente", cuota.getEstado());
        assertEquals(arancel,cuota.getArancel());
        assertEquals(cantidadCuota, cuota.getNumeroCuota());
    }

    @Test
    void calculararancelTest(){
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setAnoEgreso(2023);
        estudiante.setTipoColegio("Municipal");
        double arancel = cuotaService.calculararancel(estudiante);
        double arancelprueba = 1500000;
        int egreso= LocalDate.now().getYear() - estudiante.getAnoEgreso();
        if(estudiante.getTipoColegio().equalsIgnoreCase("Municipal")){
            if(egreso < 1){
                arancelprueba = arancelprueba * 0.65; // 20% + 15% //
            } else if (egreso <= 2) {
                arancelprueba = arancelprueba * 0.72; // 20% + 8% //
            } else if (egreso <= 4) {
                arancelprueba = arancelprueba * 0.76; // 20% + 4% //
            }
        }
        assertEquals(arancelprueba,arancel);
    }

    @Test
    void calculararancelmensualTest(){
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setAnoEgreso(2021);
        estudiante.setTipoColegio("Municipal");
        estudiante.setCantidad(5);
        double arancel = (cuotaService.calculararancel(estudiante)/estudiante.getCantidad());
        double arancelmensual = cuotaService.calcularcuotamensuales(estudiante);
        assertEquals(arancel,arancelmensual);
    }

    @Test
    void pagarCuotaTest(){
        CuotaEntity cuota = new CuotaEntity();
        cuota.setArancel(1080000.0);
        cuota.setArancelMensual(108000.0);
        cuota.setEstado("Pendiente");
        cuota.setFechaPago(LocalDate.now());
        cuotaService.pagarCuota(cuota);
        assertEquals("Pagado", cuota.getEstado());
    }

    @Test
    void pagarCuotaAtrasadaTest(){
        CuotaEntity cuota = new CuotaEntity();
        cuota.setEstado("Pendiente");
        cuota.setArancel(1080000.0);
        cuota.setArancelMensual(108000.0);
        cuota.setFechaPago(LocalDate.now().minusMonths(2));
        cuotaService.pagarCuotaAtrasadas(cuota);
        assertEquals("Atrasada", cuota.getEstado());
    }
}
