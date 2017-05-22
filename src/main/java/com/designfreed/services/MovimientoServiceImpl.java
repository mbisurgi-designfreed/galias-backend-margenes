package com.designfreed.services;

import com.designfreed.entities.*;
import com.designfreed.model.ItemMovimiento;
import com.designfreed.model.Movimiento;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class MovimientoServiceImpl implements MovimientoService {
    private List<Movimiento> movimientos = new ArrayList<>();

    @Override
    public List<Movimiento> getMovimientos() {
        orderMovimientos();

        return movimientos;
    }

    @Override
    public void addMovimientosCpa(ComprobanteCpa cpa) {
        String tipo = getTipoCpa(cpa);
        String entidad = cpa.getCodProvee();
        Date fechaIngreso = cpa.getFechaEmis();
        String horaIngreso = cpa.getHoraIngreso();
        String comprobante = cpa.getnComp();

        if (tipo != null) {
            Movimiento mov = new Movimiento("CPA", tipo, entidad, fechaIngreso, horaIngreso, comprobante);

            for (ItemComprobanteCpa item: cpa.getItems()) {
                String articulo = item.getCodArticu();
                Integer cantidad = item.getCantidad();
                Double precio = item.getPrecioNet();

                mov.getItems().add(new ItemMovimiento(articulo, cantidad, precio));
            }

            movimientos.add(mov);
        }
    }

    @Override
    public void addMovimientosVta(ComprobanteVta vta) {
        String tipo = getTipoVta(vta);
        String entidad = vta.getCodClient();
        Date fechaIngreso = vta.getFechaEmis();
        String horaIngreso = vta.getHoraIngreso();
        String comprobante = vta.getnComp();

        if (tipo != null) {
            Movimiento mov = new Movimiento("VTA", tipo, entidad, fechaIngreso, horaIngreso, comprobante);

            for (ItemComprobanteVta item: vta.getItems()) {
                String articulo = item.getCodArticu();
                Integer cantidad = item.getCantidad();
                Double precio = item.getPrecioNet();

                mov.getItems().add(new ItemMovimiento(articulo, cantidad, precio));
            }

            movimientos.add(mov);
        }
    }

    private String getTipoCpa(ComprobanteCpa cpa) {
        if (cpa instanceof ComprobanteCpaFac) {
            return "FAC";
        }

        if (cpa instanceof ComprobanteCpaNc) {
            return "N/C";
        }

        if (cpa instanceof ComprobanteCpaNcc) {
            return "NCC";
        }

        if (cpa instanceof ComprobanteCpaNd) {
            return "N/D";
        }

        return null;
    }

    private String getTipoVta(ComprobanteVta vta) {
        if (vta instanceof ComprobanteVtaFac) {
            return "FAC";
        }

        if (vta instanceof ComprobanteVtaNc) {
            return "N/C";
        }

        if (vta instanceof ComprobanteVtaNcc) {
            return "NCC";
        }

        if (vta instanceof ComprobanteVtaNd) {
            return "N/D";
        }

        if (vta instanceof ComprobanteVtaNdc) {
            return "NDC";
        }

        return null;
    }

    private void orderMovimientos() {
        movimientos.sort(
                Comparator.comparing(Movimiento::getFechaIngreso).thenComparing(Movimiento::getHoraIngreso)
        );
    }
}
