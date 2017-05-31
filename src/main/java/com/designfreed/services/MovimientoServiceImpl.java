package com.designfreed.services;

import com.designfreed.entities.*;
import com.designfreed.model.ItemMovimiento;
import com.designfreed.model.Margen;
import com.designfreed.model.Movimiento;
import com.designfreed.repository.ImputacionCpaRepository;
import com.designfreed.repository.ImputacionVtaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MovimientoServiceImpl implements MovimientoService {
    private List<Movimiento> movimientos = new ArrayList<>();
    private List<Margen> margenes = new ArrayList<>();

    private ImputacionCpaRepository imputacionCpaRepository;
    private ImputacionVtaRepository imputacionVtaRepository;

    @Autowired
    public void setImputacionCpaRepository(ImputacionCpaRepository imputacionCpaRepository) {
        this.imputacionCpaRepository = imputacionCpaRepository;
    }

    @Autowired
    public void setImputacionVtaRepository(ImputacionVtaRepository imputacionVtaRepository) {
        this.imputacionVtaRepository = imputacionVtaRepository;
    }

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
    public void addSaldosIniciales() {
        try {
            Movimiento[] movs = new ObjectMapper().readValue(new File("stock.json"), Movimiento[].class);

            for (Movimiento mov: movs) {
                mov.getItems().forEach((i) -> i.setCantidadDisponible(i.getCantidad()));
            }

            movimientos.addAll(Arrays.asList(movs));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addMovimientosCpa(ComprobanteCpa cpa) {
        String tipo = getTipoCpa(cpa);
        String entidad = cpa.getCodProvee();
        Date fechaIngreso = cpa.getFecha();
        String horaIngreso = cpa.getHora();
        String comprobante = cpa.getnComp();

        if (tipo != null) {
            Movimiento mov = new Movimiento("CPA", tipo, entidad, fechaIngreso, horaIngreso, comprobante);

            if (tipo.equals("N/C")) {
                for (ImputacionCpa imp: imputacionCpaRepository.findByNCompCanAndTCompCan(comprobante, tipo)) {
                    mov.getImputaciones().add(imp.getnCompFac());
                }
            }

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
        Date fechaIngreso = vta.getFecha();
        String horaIngreso = vta.getHora();
        String comprobante = vta.getnComp();

        if (tipo != null) {
            Movimiento mov = new Movimiento("VTA", tipo, entidad, fechaIngreso, horaIngreso, comprobante);

            if (tipo.equals("N/C") || tipo.equals("N/D")) {
                String tip = "";

                if (tipo.equals("N/C")) {
                    tip = "NCR";
                }

                if (tipo.equals("N/D")) {
                    tip = "NDB";
                }

                for (ImputacionVta imp: imputacionVtaRepository.findByNCompCanAndTCompCan(comprobante, tip)) {
                    mov.getImputaciones().add(imp.getnCompFac());
                }
            }

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
                                    Date fecha = mov1.getFecha();
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

            if (mov1.getModulo().equals("VTA") && mov1.getTipo().equals("N/C")) {
//                List<Margen> mar = new ArrayList<>();
//
//                for (String imp: mov1.getImputaciones()) {
//                    mar = margenes.stream()
//                            .filter(m -> m.getComprobanteVta().equals("N/C" + imp))
//                            .collect(Collectors.toList());
//                }
//
//                for (ItemMovimiento item1: mov1.getItems()) {
//                    for (String imp: mov1.getImputaciones()) {
//                        Movimiento mov2 = movimientos.stream()
//                                .filter(m -> imp.equals(m.getComprobante()))
//                                .findFirst()
//                                .orElse(null);
//
//                        if (mov2 != null) {
//                            ItemMovimiento item2 = mov2.getItems().stream()
//                                    .filter(i -> item1.getArticulo().equals(i.getArticulo()))
//                                    .findFirst()
//                                    .orElse(null);
//
//                            if (item2 != null) {
//                                item2.setCantidadDisponible(item2.getCantidadDisponible() + item1.getCantidad());
//                            }
//                        }
//                    }
//                }
//
//                if (!mar.isEmpty()) {
//                    mar.forEach(m -> {
//                        Date fecha = m.getFecha();
//                        String articulo = m.getArticulo();
//                        Integer cantidad = m.getCantidad() * -1;
//                        String comprobanteVta = m.getComprobanteVta();
//                        Double precioVta = m.getPrecioVta();
//                        String comprobanteCpa = m.getComprobanteCpa();
//                        Double precioCpa = m.getPrecioCpa();
//
//                        margenes.add(new Margen(fecha, articulo, cantidad, comprobanteVta, precioVta, comprobanteCpa, precioCpa));
//                    });
//                }
            }

            if (mov1.getModulo().equals("VTA") && mov1.getTipo().equals("N/D")) {

            }

            if (mov1.getModulo().equals("VTA") && mov1.getTipo().equals("NCC")) {
                for (ItemMovimiento item: mov1.getItems()) {
                    Date fecha = mov1.getFecha();
                    String articulo = item.getArticulo();
                    Integer cantidad = item.getCantidad();
                    String comprobanteVta = mov1.getTipo() + mov1.getComprobante();
                    Double precioVta = item.getPrecio() * -1;
                    String comprobanteCpa = null;
                    Double precioCpa = 0D;

                    margenes.add(new Margen(fecha, articulo, cantidad, comprobanteVta, precioVta, comprobanteCpa, precioCpa));
                }
            }

            if (mov1.getModulo().equals("VTA") && mov1.getTipo().equals("NDC")) {
                for (ItemMovimiento item: mov1.getItems()) {
                    Date fecha = mov1.getFecha();
                    String articulo = item.getArticulo();
                    Integer cantidad = item.getCantidad();
                    String comprobanteVta = mov1.getTipo() + mov1.getComprobante();
                    Double precioVta = item.getPrecio() * 1;
                    String comprobanteCpa = null;
                    Double precioCpa = 0D;

                    margenes.add(new Margen(fecha, articulo, cantidad, comprobanteVta, precioVta, comprobanteCpa, precioCpa));
                }
            }

            if (mov1.getModulo().equals("CPA") && mov1.getTipo().equals("N/C")) {
                for (ItemMovimiento item1: mov1.getItems()) {
                    for (String imp: mov1.getImputaciones()) {
                        Movimiento mov2 = movimientos.stream()
                                .filter(m -> imp.equals(m.getComprobante()))
                                .findFirst()
                                .orElse(null);

                        if (mov2 != null) {
                            ItemMovimiento item2 = mov2.getItems().stream()
                                    .filter(i -> item1.getArticulo().equals(i.getArticulo()))
                                    .findFirst()
                                    .orElse(null);

                            if (item2 != null) {
                                item2.setCantidadDisponible(item2.getCantidadDisponible() - item1.getCantidad());
                            }
                        } else {
                            
                        }
                    }
                }
            }

            if (mov1.getModulo().equals("CPA") && mov1.getTipo().equals("NCC")) {
                for (ItemMovimiento item: mov1.getItems()) {
                    Date fecha = mov1.getFecha();
                    String articulo = item.getArticulo();
                    Integer cantidad = item.getCantidad();
                    String comprobanteVta = null;
                    Double precioVta = 0D;
                    String comprobanteCpa = mov1.getTipo() + mov1.getComprobante();
                    Double precioCpa = item.getPrecio() * -1;

                    margenes.add(new Margen(fecha, articulo, cantidad, comprobanteVta, precioVta, comprobanteCpa, precioCpa));
                }
            }

            if (mov1.getModulo().equals("CPA") && mov1.getTipo().equals("NDC")) {
                for (ItemMovimiento item: mov1.getItems()) {
                    Date fecha = mov1.getFecha();
                    String articulo = item.getArticulo();
                    Integer cantidad = item.getCantidad();
                    String comprobanteVta = null;
                    Double precioVta = 0D;
                    String comprobanteCpa = mov1.getTipo() + mov1.getComprobante();
                    Double precioCpa = item.getPrecio() * 1;

                    margenes.add(new Margen(fecha, articulo, cantidad, comprobanteVta, precioVta, comprobanteCpa, precioCpa));
                }
            }
        }

        Float ventas = 0F;
        Float compras = 0F;

        for (Margen margen: margenes) {
            ventas = ventas + Float.valueOf(String.valueOf(margen.getCantidad() * margen.getPrecioVta()));
            compras = compras + Float.valueOf(String.valueOf(margen.getCantidad() * margen.getPrecioCpa()));
        }

        System.out.println(String.format("%f", ventas));
        System.out.println(String.format("%f", compras));
        System.out.println(ventas - compras);
        System.out.println((ventas / compras) - 1);
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
                Comparator
                        .comparing(Movimiento::getModulo)
                        .thenComparing(Movimiento::getTipo)
                        .thenComparing(Movimiento::getFecha)
                        .thenComparing(Movimiento::getHora)
        );
    }
}
