package com.example.Preu_TopEducation_Ti;

import com.example.Preu_TopEducation_Ti.entities.CuotaEntity;
import com.example.Preu_TopEducation_Ti.entities.EstudianteEntity;
import com.example.Preu_TopEducation_Ti.repositories.CuotaRepository;
import com.example.Preu_TopEducation_Ti.repositories.EstudianteRepository;
import com.example.Preu_TopEducation_Ti.service.CuotaService;
import com.example.Preu_TopEducation_Ti.service.PruebaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;


@SpringBootTest
class CuotaTest {

    @InjectMocks
    CuotaService cuotaService;
    @Mock
    CuotaRepository cuotaRepository;
    @Mock
    EstudianteRepository estudianteRepository;
    @Mock
    PruebaService pruebaService;
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
    void cuotasxEstudianteTest(){
        EstudianteEntity estudiante = new EstudianteEntity();
        estudiante.setRut("20567417-9");
        estudiante.setCantidad(4);
        when(estudianteRepository.findByRut(estudiante.getRut())).thenReturn(estudiante);
        ArgumentCaptor<CuotaEntity> cuotasC = ArgumentCaptor.forClass(CuotaEntity.class);
        cuotaService.cuotasxEstudiante(estudiante);
        List<CuotaEntity> cuotas = cuotasC.getAllValues();
        LocalDate fechaEmision = LocalDate.now();
        for (CuotaEntity cuota : cuotas) {
            assertEquals(fechaEmision, cuota.getFechaEmision());
            fechaEmision = fechaEmision.plusMonths(1);
        }
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

    @Test
    void descuentoPruebaTest(){
        CuotaEntity cuota = new CuotaEntity();
        cuota.setArancel(1080000.0);
        cuota.setArancelMensual(108000.0);
        cuota.setEstado("Pendiente");
        cuota.setFechaPago(LocalDate.now());
        cuota.setDescuentoPrueba(false);
        String rut = "20567417-9";
        when(pruebaService.calcularpromediopuntaje(rut)).thenReturn(950.0);
        cuotaService.descuentoPrueba(cuota, rut);
        assertEquals(97200, cuota.getArancelMensual());
        assertTrue(cuota.isDescuentoPrueba());
    }

    @Test
    void registrarPagadaTest(){
        String rut = "20567417-9";
        when(cuotaRepository.cuotaspagadasRut(rut)).thenReturn(5);
        int cuotasPagadas = cuotaService.registrarPagada(rut);
        assertEquals(5,cuotasPagadas);
    }

    @Test
    void registrarAtrasadasTest(){
        String rut = "20567417-9";
        when(cuotaRepository.cuotasAtrasadaRut(rut)).thenReturn(10);
        int cuotasAtrasadas = cuotaService.registrarAtrasadas(rut);
        assertEquals(10,cuotasAtrasadas);
    }

    @Test
    void montoCuotasPagadasTest(){
        String rut = "20567417-9";
        ArrayList<CuotaEntity> cuotaspagadas = new ArrayList<>();
        CuotaEntity cuota = new CuotaEntity();
        cuota.setArancelMensual(108000.0);
        cuotaspagadas.add(cuota);
        when(cuotaRepository.findCuotasPagadas(rut)).thenReturn(cuotaspagadas);
        double monto = cuotaService.montoCuotasPagadas(rut);
        assertEquals(108000, monto );
    }

    @Test
    void montoAPagarTest(){
        String rut = "20565417-9";
        ArrayList<CuotaEntity> cuotaspendientes = new ArrayList<>();
        CuotaEntity cuota1 = new CuotaEntity();
        cuota1.setArancelMensual(108000.0);
        cuotaspendientes.add(cuota1);
        CuotaEntity cuota2 = new CuotaEntity();
        cuota2.setArancelMensual(108000.0);
        cuotaspendientes.add(cuota2);
        when(cuotaRepository.findCuotasPendienterut(rut)).thenReturn(cuotaspendientes);
        double monto = cuotaService.montoApagar(rut);
        assertEquals(216000, monto );
    }

    @Test
    void obtenerFechaultimacuotaTest(){
        String rut = "20565417-9";
        ArrayList<CuotaEntity> cuotaas = new ArrayList<>();
        CuotaEntity cuota = new CuotaEntity();
        cuota.setArancelMensual(108000.0);
        cuota.setFechaPago(LocalDate.of(2023,04, 05));
        cuotaas.add(cuota);
        when(cuotaRepository.findUltimafechadepago(rut)).thenReturn(cuotaas);
        LocalDate ultimafecha = cuotaService.obtenerFechaultimaCuota(rut);
        assertEquals(LocalDate.of(2023,04, 05),ultimafecha);
    }



}
