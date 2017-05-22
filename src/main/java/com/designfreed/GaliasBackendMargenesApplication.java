package com.designfreed;

import com.designfreed.model.*;
import com.designfreed.repository.*;
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

//		List<ComprobanteCpaFac> facturas = comprobanteCpaFacRepository.findByCodProvee("100001");
//		List<ComprobanteCpaNc> creditos = comprobanteCpaNcRepository.findByCodProvee("100001");
//		List<ComprobanteCpaNd> debitos = comprobanteCpaNdRepository.findByCodProvee("100001");

//		List<ComprobanteVtaFac> facturas = (List<ComprobanteVtaFac>) comprobanteVtaFacRepository.findAll();
//		List<ComprobanteVtaNc> creditos = (List<ComprobanteVtaNc>) comprobanteVtaNcRepository.findAll();
		List<ComprobanteVtaNd> debitos = (List<ComprobanteVtaNd>) comprobanteVtaNdRepository.findAll();

//		System.out.println(facturas);
//		System.out.println(creditos);
		System.out.println(debitos);
	}

}
