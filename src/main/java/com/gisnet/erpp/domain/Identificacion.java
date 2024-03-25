package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A Identificacion.
 */
@Entity
@Table(name = "identificacion")
public class Identificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "identificacionGenerator")
    @SequenceGenerator(allocationSize = 1, name = "identificacionGenerator", sequenceName="identificacion_seq")
    private Long id;

    @NotNull
    private String valor;

    @ManyToOne(optional = false)
    @NotNull
    private Persona persona;

    @ManyToOne(optional = false)
    @NotNull
    private TipoIden tipoIden;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Persona getPersona() {
        return persona;
    }

    public void setValor(String valor) {
    	this.valor = valor.toUpperCase();
    }

    public String getValor() {
        return this.valor;
    }

    public Identificacion persona(Persona persona) {
        this.persona = persona;
        return this;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public TipoIden getTipoIden() {
        return tipoIden;
    }

    public Identificacion tipoIden(TipoIden tipoIden) {
        this.tipoIden = tipoIden;
        return this;
    }

    public void setTipoIden(TipoIden tipoIden) {
        this.tipoIden = tipoIden;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Identificacion identificacion = (Identificacion) o;
        if (identificacion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), identificacion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Identificacion{" +
            "id=" + getId() +
            "tipo Identificacion=" + this.getTipoIden().getNombre() +
            "valor="+this.getValor()+
            "persona="+this.getPersona().toString()+
            "}";
    }
}
