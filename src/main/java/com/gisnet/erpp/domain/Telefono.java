package com.gisnet.erpp.domain;


import javax.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Telefono.
 */
@Entity
@Table(name = "telefono")
public class Telefono implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "telefonoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "telefonoGenerator", sequenceName="telefono_seq")
    private Long id;

    @Column(name = "num_telefono")
    private String numTelefono;
    
    @Column(name = "extension", nullable = true)
    private String extension;

    @Column(name = "principal")
    private Boolean principal;

    @ManyToOne
    private Notario notario;

    @ManyToOne
    private Oficina oficina;

    @ManyToOne
    private Area area;

    @ManyToOne
    private Persona persona;

    @ManyToOne
    private Juez juez;
    
    @ManyToOne
    private Dependencia dependencia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNumTelefono() {
        return numTelefono;
    }

    public Telefono numTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
        return this;
    }

    public void setNumTelefono(String numTelefono) {
        this.numTelefono = numTelefono;
    }
    
    public String getExtension() {
        return extension;
    }

    public Telefono extension(String extension) {
        this.extension = extension;
        return this;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }
    
    public Boolean isPrincipal() {
        return principal;
    }

    public Telefono principal(Boolean principal) {
        this.principal = principal;
        return this;
    }

    public void setPrincipal(Boolean principal) {
        this.principal = principal;
    }

    public Notario getNotario() {
        return notario;
    }

    public Telefono notario(Notario notario) {
        this.notario = notario;
        return this;
    }

    public void setNotario(Notario notario) {
        this.notario = notario;
    }
    
    public Dependencia getDependencia() {
        return dependencia;
    }

    public Telefono dependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
        return this;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public Telefono oficina(Oficina oficina) {
        this.oficina = oficina;
        return this;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public Area getArea() {
        return area;
    }

    public Telefono area(Area area) {
        this.area = area;
        return this;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public Persona getPersona() {
        return persona;
    }

    public Telefono persona(Persona persona) {
        this.persona = persona;
        return this;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Juez getJuez() {
        return juez;
    }

    public Telefono juez(Juez juez) {
        this.juez = juez;
        return this;
    }

    public void setJuez(Juez juez) {
        this.juez = juez;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Telefono telefono = (Telefono) o;
        if (telefono.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), telefono.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Telefono{" +
            "id=" + getId() +
            ", numTelefono='" + getNumTelefono() + "'" +
            ", principal='" + isPrincipal() + "'" +
            "}";
    }
}
