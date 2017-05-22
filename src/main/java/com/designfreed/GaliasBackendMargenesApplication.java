package com.designfreed;

import com.designfreed.entities.*;
import com.designfreed.model.Movimiento;
import com.designfreed.repository.*;
import com.designfreed.services.MovimientoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.util.List;

@SpringBootApplication
@ComponentScan("com.designfreed")
public class GaliasBackendMargenesApplication {

	public static void main(String[] args) {
		ApplicationContext app = SpringApplication.run(GaliasBackendMargenesApplication.class, args);

		ComprobanteCpaFacRepository comprobanteCpaFacRepository = app.getBean(ComprobanteCpaFacRepository.class);
		ComprobanteCpaNcRepository comprobanteCpaNcRepository = app.getBean(ComprobanteCpaNcRepository.class);
		ComprobanteCpaNdRepository comprobanteCpaNdRepository = app.getBean(ComprobanteCpaNdRepository.class);

		ComprobanteVtaFacRepository comprobanteVtaFacRepository = app.getBean(ComprobanteVtaFacRepository.class);
		ComprobanteVtaNcRepository comprobanteVtaNcRepository = app.getBean(ComprobanteVtaNcRepository.class);
		ComprobanteVtaNdRepository comprobanteVtaNdRepository = app.getBean(ComprobanteVtaNdRepository.class);

		List<ComprobanteCpaFac> cpaFacturas = comprobanteCpaFacRepository.findByCodProvee("100001");
		List<ComprobanteCpaNc> cpaCreditos = comprobanteCpaNcRepository.findByCodProvee("100001");
		List<ComprobanteCpaNd> cpaDebitos = comprobanteCpaNdRepository.findByCodProvee("100001");

		List<ComprobanteVtaFac> vtaFacturas = (List<ComprobanteVtaFac>) comprobanteVtaFacRepository.findAll();
		List<ComprobanteVtaNc> vtaCreditos = (List<ComprobanteVtaNc>) comprobanteVtaNcRepository.findAll();
		List<ComprobanteVtaNd> vtaDebitos = (List<ComprobanteVtaNd>) comprobanteVtaNdRepository.findAll();

//		System.out.println(facturas);
//		System.out.println(creditos);
//

		MovimientoService movimientoService = app.getBean(MovimientoService.class);

		for (ComprobanteCpa cpa: cpaFacturas) {
			movimientoService.addMovimientosCpa(cpa);
		}

		for (ComprobanteCpa cpa: cpaCreditos) {
			movimientoService.addMovimientosCpa(cpa);
		}

		for (ComprobanteCpa cpa: cpaDebitos) {
			movimientoService.addMovimientosCpa(cpa);
		}

		for (ComprobanteVta vta: vtaFacturas) {
			movimientoService.addMovimientosVta(vta);
		}

		for (ComprobanteVta vta: vtaCreditos) {
			movimientoService.addMovimientosVta(vta);
		}

		for (ComprobanteVta vta: vtaDebitos) {
			movimientoService.addMovimientosVta(vta);
		}

		List<Movimiento> movimientos = movimientoService.getMovimientos();

		System.out.println(movimientos);
	}

}
