package com.designfreed.entities;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "GVA07")
public class ImputacionVta implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "ID_GVA07")
    private Long id;

    @Column(name = "N_COMP_CAN")
    private String nCompCan;

    @Column(name = "N_COMP")
    private String nCompFac;

    @Column(name = "T_COMP_CAN")
    private String tCompCan;

    @Column(name = "T_COMP")
    private String tCompFac;

    public ImputacionVta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getnCompCan() {
        return nCompCan;
    }

    public void setnCompCan(String nCompCan) {
        this.nCompCan = nCompCan;
    }

    public String getnCompFac() {
        return nCompFac;
    }

    public void setnCompFac(String nCompFac) {
        this.nCompFac = nCompFac;
    }

    public String gettCompCan() {
        return tCompCan;
    }

    public void settCompCan(String tCompCan) {
        this.tCompCan = tCompCan;
    }

    public String gettCompFac() {
        return tCompFac;
    }

    public void settCompFac(String tCompFac) {
        this.tCompFac = tCompFac;
    }
}
