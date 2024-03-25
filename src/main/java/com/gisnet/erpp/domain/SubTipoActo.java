package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A SubTipoActo.
 */
@Entity
@Table(name = "sub_tipo_acto")
public class SubTipoActo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "subTipoActoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "subTipoActoGenerator", sequenceName="sub_tipo_acto_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;

    @Size(max = 100)
    @Column(name = "formula", length = 100)
    private String formula;

    @OneToMany(mappedBy = "subTipoActo")
    @JsonIgnore
    private Set<ReciboServicio> reciboServiciosParaSubTipoActos = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private TipoActo tipoActo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public SubTipoActo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getFormula() {
        return formula;
    }

    public SubTipoActo formula(String formula) {
        this.formula = formula;
        return this;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    public Set<ReciboServicio> getReciboServiciosParaSubTipoActos() {
        return reciboServiciosParaSubTipoActos;
    }

    public SubTipoActo reciboServiciosParaSubTipoActos(Set<ReciboServicio> reciboServicios) {
        this.reciboServiciosParaSubTipoActos = reciboServicios;
        return this;
    }

    public SubTipoActo addReciboServiciosParaSubTipoActo(ReciboServicio reciboServicio) {
        this.reciboServiciosParaSubTipoActos.add(reciboServicio);
        reciboServicio.setSubTipoActo(this);
        return this;
    }

    public SubTipoActo removeReciboServiciosParaSubTipoActo(ReciboServicio reciboServicio) {
        this.reciboServiciosParaSubTipoActos.remove(reciboServicio);
        reciboServicio.setSubTipoActo(null);
        return this;
    }

    public void setReciboServiciosParaSubTipoActos(Set<ReciboServicio> reciboServicios) {
        this.reciboServiciosParaSubTipoActos = reciboServicios;
    }

    public TipoActo getTipoActo() {
        return tipoActo;
    }

    public SubTipoActo tipoActo(TipoActo tipoActo) {
        this.tipoActo = tipoActo;
        return this;
    }

    public void setTipoActo(TipoActo tipoActo) {
        this.tipoActo = tipoActo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SubTipoActo subTipoActo = (SubTipoActo) o;
        if (subTipoActo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), subTipoActo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "SubTipoActo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", formula='" + getFormula() + "'" +
            "}";
    }
}
