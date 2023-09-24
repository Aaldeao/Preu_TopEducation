package com.example.Preu_TopEducation_Ti;

import com.example.Preu_TopEducation_Ti.entities.CuotaEntity;
import com.example.Preu_TopEducation_Ti.service.CuotaService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class PreuTopEducationTiApplication {

	public static void main(String[] args) {
		SpringApplication.run(PreuTopEducationTiApplication.class, args);
		/*
		CuotaEntity cuota = new CuotaEntity();
		cuota.setCantidad(4);;
		cuota.setArancel(1500000);

		CuotaService calcular = new CuotaService();
		double arancelMensual = calcular.calcularcuotamensuales(cuota);
		System.out.println("Arancel mensual: " + arancelMensual);

		 */
	}


}
