package com.designfreed.model;

import java.util.*;

public class Movimiento {
    private String modulo;
    private String tipo;
    private String entidad;
    private Date fechaIngreso;
    private String horaIngreso;
    private String comprobante;
    private List<String> imputaciones;
    private List<ItemMovimiento> items;

    public Movimiento() {
    }

    public Movimiento(String modulo, String tipo, String entidad, Date fechaIngreso, String horaIngreso, String comprobante) {
        this.modulo = modulo;
        this.tipo = tipo;
        this.entidad = entidad;
        this.fechaIngreso = fechaIngreso;
        this.horaIngreso = horaIngreso;
        this.comprobante = comprobante;
        this.imputaciones = new ArrayList<>();
        this.items = new ArrayList<>();
    }

    public String getModulo() {
        return modulo;
    }

    public void setModulo(String modulo) {
        this.modulo = modulo;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public String getEntidad() {
        return entidad;
    }

    public void setEntidad(String entidad) {
        this.entidad = entidad;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public String getHoraIngreso() {
        return horaIngreso;
    }

    public void setHoraIngreso(String horaIngreso) {
        this.horaIngreso = horaIngreso;
    }

    public String getComprobante() {
        return comprobante;
    }

    public void setComprobante(String comprobante) {
        this.comprobante = comprobante;
    }

    public List<String> getImputaciones() {
        return imputaciones;
    }

    public void setImputaciones(List<String> imputaciones) {
        this.imputaciones = imputaciones;
    }

    public List<ItemMovimiento> getItems() {
        return items;
    }

    public void setItems(List<ItemMovimiento> items) {
        this.items = items;
    }

    @Override
    public String toString() {
        return "Movimiento{" +
                "modulo='" + modulo + '\'' +
                ", tipo='" + tipo + '\'' +
                ", comprobante='" + comprobante + '\'' +
                '}';
    }
}
