package com.designfreed.model;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "GVA53")
public class ItemComprobanteVta implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "ID_GVA53")
    private Long id;

    @Column(name = "COD_ARTICU")
    private String codArticu;

    @Column(name = "CANTIDAD")
    private Integer cantidad;

    @Column(name = "PRECIO_NET")
    private Double precioNet;

    public ItemComprobanteVta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodArticu() {
        return codArticu;
    }

    public void setCodArticu(String codArticu) {
        this.codArticu = codArticu;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioNet() {
        return precioNet;
    }

    public void setPrecioNet(Double precioNet) {
        this.precioNet = precioNet;
    }
}
