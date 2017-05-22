package com.designfreed.model;

public class ItemMovimiento {
    private String articulo;
    private Integer cantidad;
    private Double precio;

    public ItemMovimiento() {
    }

    public ItemMovimiento(String articulo, Integer cantidad, Double precio) {
        this.articulo = articulo;
        this.cantidad = cantidad;
        this.precio = precio;
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
}
