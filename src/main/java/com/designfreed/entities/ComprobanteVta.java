package com.designfreed.entities;

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

    @Column(name = "FECHA_EMIS")
    private Date fecha;

    @Column(name = "HORA_INGRESO")
    private String hora;

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

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
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
