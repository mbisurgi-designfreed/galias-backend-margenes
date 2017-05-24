package com.designfreed.model;

public class ItemMovimiento {
    private String articulo;
    private Integer cantidad;
    private Double precio;
    private Integer cantidadDisponible;

    public ItemMovimiento() {
    }

    public ItemMovimiento(String articulo, Integer cantidad, Double precio, Integer cantidadDisponible) {
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.precio = precio;
        this.cantidadDisponible = cantidadDisponible;
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

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Integer getCantidadDisponible() {
        return cantidadDisponible;
    }

    public void setCantidadDisponible(Integer cantidadDisponible) {
        this.cantidadDisponible = cantidadDisponible;
    }
}
