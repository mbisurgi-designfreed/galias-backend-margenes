package com.designfreed;

import com.designfreed.entities.*;
import com.designfreed.model.Margen;
import com.designfreed.model.Movimiento;
import com.designfreed.repository.*;
import com.designfreed.services.MovimientoService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
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

		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		Date desde = null;
		Date hasta = null;

		try {
			desde = formatter.parse("01/01/2017");
			hasta = formatter.parse("30/04/2017");
		} catch (ParseException e) {
			e.printStackTrace();
		}

		List<ComprobanteCpaFac> cpaFacturas = comprobanteCpaFacRepository.findByCodProveeAndFechaBetween("100001", desde, hasta);
		List<ComprobanteCpaNc> cpaCreditos = comprobanteCpaNcRepository.findByCodProveeAndFechaBetween("100001", desde, hasta);
		List<ComprobanteCpaNd> cpaDebitos = comprobanteCpaNdRepository.findByCodProveeAndFechaBetween("100001", desde, hasta);

		List<ComprobanteVtaFac> vtaFacturas = comprobanteVtaFacRepository.findByFechaBetween(desde, hasta);
		List<ComprobanteVtaNc> vtaCreditos = comprobanteVtaNcRepository.findByFechaBetween(desde, hasta);
		List<ComprobanteVtaNd> vtaDebitos = comprobanteVtaNdRepository.findByFechaBetween(desde, hasta);

		System.out.println(cpaFacturas);
//		System.out.println(cpaCreditos);


		MovimientoService movimientoService = app.getBean(MovimientoService.class);

		movimientoService.addSaldosIniciales();

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

		movimientoService.generarMargenes();

		List<Margen> margenes = movimientoService.getMargenes();

		System.out.println(margenes);
	}

}
