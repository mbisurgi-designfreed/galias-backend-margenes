package com.designfreed.services;

import com.designfreed.entities.*;
import com.designfreed.model.ItemMovimiento;
import com.designfreed.model.Margen;
import com.designfreed.model.Movimiento;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

@Service
public class MovimientoServiceImpl implements MovimientoService {
    private List<Movimiento> movimientos = new ArrayList<>();
    private List<Margen> margenes = new ArrayList<>();

    @Override
    public List<Movimiento> getMovimientos() {
        orderMovimientos();

        return movimientos;
    }

    @Override
    public List<Margen> getMargenes() {
        return margenes;
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

                mov.getItems().add(new ItemMovimiento(articulo, cantidad, precio, cantidad));
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

                mov.getItems().add(new ItemMovimiento(articulo, cantidad, precio, 0));
            }

            movimientos.add(mov);
        }
    }

    @Override
    public void generarMargenes() {
        orderMovimientos();

        for (Movimiento mov1: movimientos) {
            if (mov1.getModulo().equals("VTA") && mov1.getTipo().equals("FAC")) {
                for (ItemMovimiento item1: mov1.getItems()) {
                    Integer cantidad = item1.getCantidad();

                    for (Movimiento mov2: movimientos) {
                        if (cantidad == 0) {
                            break;
                        }

                        if (mov2.getModulo().equals("CPA") && mov2.getTipo().equals("FAC")) {
                            for (ItemMovimiento item2: mov2.getItems()) {
                                if (cantidad == 0) {
                                    break;
                                }

                                if (item1.getArticulo().equals(item2.getArticulo()) && item2.getCantidadDisponible() > 0) {
                                    Date fecha = mov1.getFechaIngreso();
                                    String articulo = item1.getArticulo();
                                    String comprobanteVta = mov1.getTipo() + mov1.getComprobante();
                                    Double precioVta = item1.getPrecio();
                                    String comprobanteCpa = mov2.getTipo() + mov2.getComprobante();
                                    Double precioCpa = item2.getPrecio();

                                    Margen margen = new Margen();
                                    margen.setFecha(fecha);
                                    margen.setArticulo(articulo);
                                    margen.setComprobanteVta(comprobanteVta);
                                    margen.setPrecioVta(precioVta);
                                    margen.setComprobanteCpa(comprobanteCpa);
                                    margen.setPrecioCpa(precioCpa);

                                    if (cantidad <= item2.getCantidadDisponible()) {
                                        margen.setCantidad(cantidad);
                                        item2.setCantidadDisponible(item2.getCantidadDisponible() - cantidad);
                                        cantidad = 0;
                                    }

                                    if (cantidad > item2.getCantidadDisponible()) {
                                        margen.setCantidad(item2.getCantidadDisponible());
                                        cantidad = cantidad - item2.getCantidadDisponible();
                                        item2.setCantidadDisponible(0);
                                    }

                                    margenes.add(margen);
                                }
                            }
                        }
                    }
                }
            }

            if (mov1.getModulo().equals("VTA") && mov1.getTipo().equals("NCC")) {
                for (ItemMovimiento item: mov1.getItems()) {
                    Date fecha = mov1.getFechaIngreso();
                    String articulo = item.getArticulo();
                    Integer cantidad = item.getCantidad();
                    String comprobanteVta = mov1.getTipo() + mov1.getComprobante();
                    Double precioVta = item.getPrecio() * 1;
                    String comprobanteCpa = null;
                    Double precioCpa = 0D;

                    margenes.add(new Margen(fecha, articulo, cantidad, comprobanteVta, precioVta, comprobanteCpa, precioCpa));
                }
            }

            if (mov1.getModulo().equals("VTA") && mov1.getTipo().equals("NDC")) {
                for (ItemMovimiento item: mov1.getItems()) {
                    Date fecha = mov1.getFechaIngreso();
                    String articulo = item.getArticulo();
                    Integer cantidad = item.getCantidad();
                    String comprobanteVta = mov1.getTipo() + mov1.getComprobante();
                    Double precioVta = item.getPrecio() * -1;
                    String comprobanteCpa = null;
                    Double precioCpa = 0D;

                    margenes.add(new Margen(fecha, articulo, cantidad, comprobanteVta, precioVta, comprobanteCpa, precioCpa));
                }
            }

            if (mov1.getModulo().equals("CPA") && mov1.getTipo().equals("NCC")) {
                for (ItemMovimiento item: mov1.getItems()) {
                    Date fecha = mov1.getFechaIngreso();
                    String articulo = item.getArticulo();
                    Integer cantidad = item.getCantidad();
                    String comprobanteVta = null;
                    Double precioVta = 0D;
                    String comprobanteCpa = mov1.getTipo() + mov1.getComprobante();
                    Double precioCpa = item.getPrecio() * 1;

                    margenes.add(new Margen(fecha, articulo, cantidad, comprobanteVta, precioVta, comprobanteCpa, precioCpa));
                }
            }

            if (mov1.getModulo().equals("CPA") && mov1.getTipo().equals("NDC")) {
                for (ItemMovimiento item: mov1.getItems()) {
                    Date fecha = mov1.getFechaIngreso();
                    String articulo = item.getArticulo();
                    Integer cantidad = item.getCantidad();
                    String comprobanteVta = null;
                    Double precioVta = 0D;
                    String comprobanteCpa = mov1.getTipo() + mov1.getComprobante();
                    Double precioCpa = item.getPrecio() * -1;

                    margenes.add(new Margen(fecha, articulo, cantidad, comprobanteVta, precioVta, comprobanteCpa, precioCpa));
                }
            }
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
