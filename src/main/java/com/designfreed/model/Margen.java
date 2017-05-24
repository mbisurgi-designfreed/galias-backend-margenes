package com.designfreed.model;

import java.util.Date;

public class Margen {
    private Date fecha;
    private String articulo;
    private Integer cantidad;
    private String comprobanteVta;
    private Double precioVta;
    private String comprobanteCpa;
    private Double precioCpa;

    public Margen() {
    }

    public Margen(Date fecha, String articulo, Integer cantidad, String comprobanteVta, Double precioVta, String comprobanteCpa, Double precioCpa) {
        this.fecha = fecha;
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.comprobanteVta = comprobanteVta;
        this.precioVta = precioVta;
        this.comprobanteCpa = comprobanteCpa;
        this.precioCpa = precioCpa;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getArticulo() {
        return articulo;
    }

    public void setArticulo(String articulo) {
        this.articulo = articulo;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public String getComprobanteVta() {
        return comprobanteVta;
    }

    public void setComprobanteVta(String comprobanteVta) {
        this.comprobanteVta = comprobanteVta;
    }

    public Double getPrecioVta() {
        return precioVta;
    }

    public void setPrecioVta(Double precioVta) {
        this.precioVta = precioVta;
    }

    public String getComprobanteCpa() {
        return comprobanteCpa;
    }

    public void setComprobanteCpa(String comprobanteCpa) {
        this.comprobanteCpa = comprobanteCpa;
    }

    public Double getPrecioCpa() {
        return precioCpa;
    }

    public void setPrecioCpa(Double precioCpa) {
        this.precioCpa = precioCpa;
    }
}
