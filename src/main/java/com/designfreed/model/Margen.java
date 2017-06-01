package com.designfreed.model;

import java.util.Date;

public class Margen {
    private String articulo;
    private Integer cantidad;
    private Date fechaVta;
    private String comprobanteVta;
    private Double precioVta;
    private Date fechaCpa;
    private String comprobanteCpa;
    private Double precioCpa;

    public Margen() {
    }

    public Margen(String articulo, Integer cantidad, Date fechaVta, String comprobanteVta, Double precioVta, Date fechaCpa, String comprobanteCpa, Double precioCpa) {
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.fechaVta = fechaVta;
        this.comprobanteVta = comprobanteVta;
        this.precioVta = precioVta;
        this.fechaCpa = fechaCpa;
        this.comprobanteCpa = comprobanteCpa;
        this.precioCpa = precioCpa;
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

    public Date getFechaVta() {
        return fechaVta;
    }

    public void setFechaVta(Date fechaVta) {
        this.fechaVta = fechaVta;
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

    public Date getFechaCpa() {
        return fechaCpa;
    }

    public void setFechaCpa(Date fechaCpa) {
        this.fechaCpa = fechaCpa;
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
