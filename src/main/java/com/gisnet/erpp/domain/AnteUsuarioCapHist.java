package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Antecedente.
 */
@Entity
@Table(name = "ante_usua_cap_hist")
public class AnteUsuarioCapHist implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "anteUsuarioCapHistGenerator")
    @SequenceGenerator(allocationSize = 1, name = "anteUsuarioCapHistGenerator", sequenceName="ante_usua_cap_hist_seq")
    private Long id;
    
    private String statusAnte;
    
    @ManyToOne
    private Usuario usuario;
    
    @ManyToOne(optional = true, fetch = FetchType.LAZY)
    private Usuario usuarioValidacion;
    
    @ManyToOne
    private PrelacionAnteCapHist prelacionAnteCapHist;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getStatusAnte() {
        return statusAnte;
    }

    public void setStatusAnte(String statusAnte) {
        this.statusAnte = statusAnte;
    }
    
    public Usuario getUsuario() {
        return this.usuario;
    }

    public AnteUsuarioCapHist usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
    
    public Usuario getUsuarioValidacion () {
        return this.usuarioValidacion;
    }

    public AnteUsuarioCapHist usuarioValidacion(Usuario usuarioValidacion) {
        this.usuarioValidacion = usuarioValidacion;
        return this;
    }

    public void setUsuarioValidacion(Usuario usuarioValidacion) {
        this.usuarioValidacion = usuarioValidacion;
    }
    
    public PrelacionAnteCapHist getPrelacionAnteCapHist() {
        return this.prelacionAnteCapHist;
    }

    public AnteUsuarioCapHist prelacionAnteCapHist(PrelacionAnteCapHist prelacionAnteCapHist) {
        this.prelacionAnteCapHist = prelacionAnteCapHist;
        return this;
    }

    public void setPrelacionAnteCapHist(PrelacionAnteCapHist prelacionAnteCapHist) {
        this.prelacionAnteCapHist = prelacionAnteCapHist;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AnteUsuarioCapHist anteUsuarioCapHist = (AnteUsuarioCapHist) o;
        if (anteUsuarioCapHist.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), anteUsuarioCapHist.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Antecedente{" +
            "id=" + getId() +
            ", usuario='" + getUsuario().getUserName() + "'" +
            ", statusAnte= '" + getStatusAnte() + "'" +
            ", usuarioValidacion= '" + getUsuarioValidacion().getUserName() + "'" +

            "}";
    }
}
