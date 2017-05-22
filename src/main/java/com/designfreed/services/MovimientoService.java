package com.designfreed.services;

import com.designfreed.entities.ComprobanteCpa;
import com.designfreed.entities.ComprobanteVta;
import com.designfreed.model.Movimiento;

import java.util.List;

public interface MovimientoService {
    List<Movimiento> getMovimientos();

    void addMovimientosCpa(ComprobanteCpa cpa);

    void addMovimientosVta(ComprobanteVta vta);
}
