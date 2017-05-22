package com.designfreed.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "GVA12")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "T_COMP")
public abstract class ComprobanteVta implements Serializable {
    @Id
    @GeneratedValue
    @Column(name = "ID_GVA12")
    private Long id;

    @Column(name = "COD_CLIENT")
    private String codClient;

    @Column(name = "FECHA_INGRESO")
    private Date fechaIngreso;

    @Column(name = "HORA_INGRESO")
    private String horaIngreso;

    @Column(name = "N_COMP")
    private String nComp;

    @Column(name = "TCOMP_IN_V")
    private String tCompInV;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER, orphanRemoval = true)
    @JoinColumns({
            @JoinColumn(name = "N_COMP", referencedColumnName = "N_COMP", nullable = false, insertable = false, updatable = false),
            @JoinColumn(name = "TCOMP_IN_V", referencedColumnName = "TCOMP_IN_V", nullable = false, insertable = false, updatable = false)
    })
    private List<ItemComprobanteVta> items = new ArrayList<>();

    public ComprobanteVta() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCodClient() {
        return codClient;
    }

    public void setCodClient(String codClient) {
        this.codClient = codClient;
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

    public String gettCompInV() {
        return tCompInV;
    }

    public void settCompInV(String tCompInV) {
        this.tCompInV = tCompInV;
    }

    public String getnComp() {
        return nComp;
    }

    public void setnComp(String nComp) {
        this.nComp = nComp;
    }

    public List<ItemComprobanteVta> getItems() {
        return items;
    }

    public void setItems(List<ItemComprobanteVta> items) {
        this.items = items;
    }
}
