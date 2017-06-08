package com.designfreed.services;

import com.designfreed.entities.*;
import com.designfreed.model.ItemMovimiento;
import com.designfreed.model.Margen;
import com.designfreed.model.Movimiento;
import com.designfreed.repository.ImputacionCpaRepository;
import com.designfreed.repository.ImputacionVtaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.oracle.tools.packager.Log;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.text.SimpleDateFormat;
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
                mov.getItems().forEach((i) -> {
                    i.setArticulo(i.getArticulo().substring(1));
                    i.setCantidadDisponible(i.getCantidad());
                });
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
                String articulo = item.getCodArticu().substring(1);
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
                String articulo = item.getCodArticu().substring(1);
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
                                    String articulo = item1.getArticulo();
                                    Date fechaVta = mov1.getFecha();
                                    String comprobanteVta = mov1.getTipo() + mov1.getComprobante();
                                    Double precioVta = item1.getPrecio();
                                    Date fechaCpa = mov2.getFecha();
                                    String comprobanteCpa = mov2.getTipo() + mov2.getComprobante();
                                    Double precioCpa = item2.getPrecio();

                                    Margen margen = new Margen();
                                    margen.setFechaVta(fechaVta);
                                    margen.setArticulo(articulo);
                                    margen.setComprobanteVta(comprobanteVta);
                                    margen.setPrecioVta(precioVta);
                                    margen.setFechaCpa(fechaCpa);
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
                for (String imp: mov1.getImputaciones()) {
                    List<Margen> filtrado1 = new ArrayList<>();

                    for (Margen mar: margenes) {
                        if (mar.getComprobanteVta() != null && mar.getComprobanteVta().equals("FAC" + imp)) {
                            filtrado1.add(mar);
                        }
                    }

                    for (ItemMovimiento item1: mov1.getItems()) {
                        List<Margen> filtrado2 = filtrado1.stream()
                                .filter(m -> m.getArticulo().equals(item1.getArticulo()))
                                .collect(Collectors.toList());

                        Integer cantidad = item1.getCantidad();

                        for (Margen mar: filtrado2) {
                            if (cantidad == 0) {
                                break;
                            }

                            String articulo = mar.getArticulo();
                            Date fechaVta = mar.getFechaVta();
                            String comprobanteVta = mar.getComprobanteVta();
                            Double precioVta = mar.getPrecioVta();
                            Date fechaCpa = mar.getFechaCpa();
                            String comprobanteCpa = mar.getComprobanteCpa();
                            Double precioCpa = mar.getPrecioCpa();

                            Margen anu = new Margen();
                            anu.setFechaVta(fechaVta);
                            anu.setArticulo(articulo);
                            anu.setComprobanteVta(comprobanteVta);
                            anu.setPrecioVta(precioVta);
                            anu.setFechaCpa(fechaCpa);
                            anu.setComprobanteCpa(comprobanteCpa);
                            anu.setPrecioCpa(precioCpa);

                            if (cantidad <= mar.getCantidad()) {
                                anu.setCantidad(cantidad * -1);
                                cantidad = 0;
                            }

                            if (cantidad > mar.getCantidad()) {
                                anu.setCantidad(mar.getCantidad() * -1);
                                cantidad = cantidad - mar.getCantidad();
                            }

                            Movimiento compra = movimientos.stream()
                                    .filter(m -> m.getModulo().equals("CPA") && anu.getComprobanteCpa().equals("FAC" + m.getComprobante()))
                                    .findFirst()
                                    .orElse(null);

                            if (compra != null) {
                                ItemMovimiento item = compra.getItems().stream()
                                        .filter(i -> i.getArticulo().equals(anu.getArticulo()))
                                        .findFirst()
                                        .orElse(null);

                                if (item != null) {
                                    item.setCantidadDisponible(item.getCantidadDisponible() + anu.getCantidad() * -1);
                                }
                            }

                            margenes.add(anu);
                        }
                    }
                }
            }

            if (mov1.getModulo().equals("VTA") && mov1.getTipo().equals("N/D")) {

            }

            if (mov1.getModulo().equals("VTA") && mov1.getTipo().equals("NCC")) {
                for (ItemMovimiento item: mov1.getItems()) {
                    String articulo = item.getArticulo();
                    Integer cantidad = item.getCantidad();
                    Date fechaVta = mov1.getFecha();
                    String comprobanteVta = mov1.getTipo() + mov1.getComprobante();
                    Double precioVta = item.getPrecio() * -1;
                    Date fechaCpa = null;
                    String comprobanteCpa = null;
                    Double precioCpa = 0D;

                    margenes.add(new Margen(articulo, cantidad, fechaVta, comprobanteVta, precioVta, fechaCpa, comprobanteCpa, precioCpa));
                }
            }

            if (mov1.getModulo().equals("VTA") && mov1.getTipo().equals("NDC")) {
                for (ItemMovimiento item: mov1.getItems()) {
                    String articulo = item.getArticulo();
                    Integer cantidad = item.getCantidad();
                    Date fechaVta = mov1.getFecha();
                    String comprobanteVta = mov1.getTipo() + mov1.getComprobante();
                    Double precioVta = item.getPrecio() * 1;
                    Date fechaCpa = null;
                    String comprobanteCpa = null;
                    Double precioCpa = 0D;

                    margenes.add(new Margen(articulo, cantidad, fechaVta, comprobanteVta, precioVta, fechaCpa, comprobanteCpa, precioCpa));
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
                    String articulo = item.getArticulo();
                    Integer cantidad = item.getCantidad();
                    Date fechaVta = null;
                    String comprobanteVta = null;
                    Double precioVta = 0D;
                    Date fechaCpa = mov1.getFecha();
                    String comprobanteCpa = mov1.getTipo() + mov1.getComprobante();
                    Double precioCpa = item.getPrecio() * -1;

                    margenes.add(new Margen(articulo, cantidad, fechaVta, comprobanteVta, precioVta, fechaCpa, comprobanteCpa, precioCpa));
                }
            }

            if (mov1.getModulo().equals("CPA") && mov1.getTipo().equals("NDC")) {
                for (ItemMovimiento item: mov1.getItems()) {
                    String articulo = item.getArticulo();
                    Integer cantidad = item.getCantidad();
                    Date fechaVta = null;
                    String comprobanteVta = null;
                    Double precioVta = 0D;
                    Date fechaCpa = mov1.getFecha();
                    String comprobanteCpa = mov1.getTipo() + mov1.getComprobante();
                    Double precioCpa = item.getPrecio() * 1;

                    margenes.add(new Margen(articulo, cantidad, fechaVta, comprobanteVta, precioVta, fechaCpa, comprobanteCpa, precioCpa));
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

    @Override
    public void generarExcel() {
        HSSFWorkbook workbook = new HSSFWorkbook();
        HSSFSheet sheet = workbook.createSheet();
        workbook.setSheetName(0, "Margenes");

        String[] headers = new String[]{
                "Articulo",
                "Cantidad",
                "Fecha Vta",
                "Comprobante Vta",
                "Precio Vta",
                "Fecha Cpa",
                "Comprobante Cpa",
                "Precio Cpa"
        };

        HSSFRow headerRow = sheet.createRow(0);

        for (int i = 0; i < headers.length; ++i) {
            String header = headers[i];
            HSSFCell cell = headerRow.createCell(i);
            cell.setCellValue(header);
        }

        for (int i = 0; i < margenes.size(); ++i) {
            HSSFRow dataRow = sheet.createRow(i + 1);

            Margen mar = margenes.get(i);

            String fechaVta = "";
            String fechaCpa = "";

            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

            if (mar.getFechaVta() != null) {
                fechaVta = formatter.format(mar.getFechaVta());
            }

            if (mar.getFechaCpa() != null) {
                fechaCpa = formatter.format(mar.getFechaCpa());
            }

            dataRow.createCell(0).setCellValue(mar.getArticulo());
            dataRow.createCell(1).setCellValue(mar.getCantidad());
            dataRow.createCell(2).setCellValue(fechaVta);
            dataRow.createCell(3).setCellValue(mar.getComprobanteVta());
            dataRow.createCell(4).setCellValue(mar.getPrecioVta());
            dataRow.createCell(5).setCellValue(fechaCpa);
            dataRow.createCell(6).setCellValue(mar.getComprobanteCpa());
            dataRow.createCell(7).setCellValue(mar.getPrecioCpa());
        }

        try {
            FileOutputStream file = new FileOutputStream("margenes.xls");

            workbook.write(file);

            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
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
                Comparator
                        .comparing(Movimiento::getModulo)
                        .thenComparing(Movimiento::getTipo)
                        .thenComparing(Movimiento::getFecha)
                        .thenComparing(Movimiento::getHora)
        );
    }
}
