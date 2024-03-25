package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A BloqueoFolio.
 */
@Entity
@Table(name = "bloqueo_folio")
public class BloqueoFolio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bloqueoFolioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "bloqueoFolioGenerator", sequenceName="bloqueo_folio_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Motivo motivo;

    @ManyToOne
    private Predio predio;

    @Column(name = "observaciones")
    private String observaciones;

    @Column(name = "version")
    private Integer version;

    @ManyToOne
    private Usuario usuario;
    
    @ManyToOne
    private PersonaJuridica personaJuridica;
    
    @ManyToOne
    private Mueble mueble;
    
    @ManyToOne
    private FolioSeccionAuxiliar folioSeccionAuxiliar;

    public Long getId() {   
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public BloqueoFolio fecha(String observaciones) {
        this.observaciones = observaciones;
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Predio getPredio() {
        return predio;
    }

    public BloqueoFolio predio(Predio predio) {
        this.predio = predio;
        return this;
    }

    public void setPredio(Predio predio) {
        this.predio = predio;
    }

    public Motivo getMotivo() {
        return motivo;
    }

    public BloqueoFolio motivo(Motivo motivo) {
        this.motivo = motivo;
        return this;
    }

    public void setMotivo(Motivo motivo) {
        this.motivo = motivo;
    }

    public Integer getVersion() {
        return version;
    }

    public BloqueoFolio version(Integer version) {
        this.motivo = motivo;
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public PersonaJuridica getPersonaJuridica() {
		return personaJuridica;
	}

	public void setPersonaJuridica(PersonaJuridica personaJuridica) {
		this.personaJuridica = personaJuridica;
	}

	public Mueble getMueble() {
		return mueble;
	}

	public void setMueble(Mueble mueble) {
		this.mueble = mueble;
	}

	public FolioSeccionAuxiliar getFolioSeccionAuxiliar() {
		return folioSeccionAuxiliar;
	}

	public void setFolioSeccionAuxiliar(FolioSeccionAuxiliar folioSeccionAuxiliar) {
		this.folioSeccionAuxiliar = folioSeccionAuxiliar;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Recibo recibo = (Recibo) o;
        if (recibo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), recibo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Motivo{" +
            "id=" + getId() +
            "}";
    }
}
