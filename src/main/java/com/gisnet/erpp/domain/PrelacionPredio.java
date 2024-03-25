package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PrelacionPredio.
 */
@Entity
@Table(name = "prelacion_predio")
public class PrelacionPredio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prelacionPredioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "prelacionPredioGenerator", sequenceName="prelacion_predio_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Prelacion prelacion;

    @ManyToOne
    private Predio predio;

    @ManyToOne
    private TipoFolio tipoFolio;

    @ManyToOne
    private Mueble mueble;

    @ManyToOne
    private PersonaJuridica personaJuridica;

    @ManyToOne
    private FolioSeccionAuxiliar folioSeccionAuxiliar;
    
    @Column (name="validado_cyv")
    private Boolean validadoCyv;

    private String idVersion;

    private Integer version;

    private Integer estatus;

    private Boolean valido= false;

        @Column(name = "requiere_validacion")
    private Boolean requiereValidacion;    
    
	public Boolean getRequiereValidacion() {
		return requiereValidacion;
	}

	public void setRequiereValidacion(Boolean requiereValidacion) {
		this.requiereValidacion = requiereValidacion;
	}
    public Boolean getValidadoCyv() {
		return validadoCyv;
	}

	public void setValidadoCyv(Boolean validadoCyv) {
		this.validadoCyv = validadoCyv;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prelacion getPrelacion() {
        return prelacion;
    }

    public PrelacionPredio prelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
        return this;
    }

    public void setPrelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
    }

    public Predio getPredio() {
        return predio;
    }

    public PrelacionPredio predio(Predio predio) {
        this.predio = predio;
        return this;
    }

    public void setPredio(Predio predio) {
        this.predio = predio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrelacionPredio prelacionPredio = (PrelacionPredio) o;
        if (prelacionPredio.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), prelacionPredio.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PrelacionPredio{" +
            "id=" + getId() +
            " prelacionId= " + getPrelacion() +
            " tipoFolioId= " + getTipoFolio()+
            " Predio= " + getPredio() +
            " Mueble= " + getMueble() +
            " PersonaJuridica= " + getPersonaJuridica() +
            " FolioSeccionAuxiliar= " + getFolioSeccionAuxiliar()+
            " validadoCyv= " + getValidadoCyv() +
            " version= " + getVersion() +
            " estatus= " + getEstatus() +
            "}";
    }

	public PersonaJuridica getPersonaJuridica() {
		return this.personaJuridica;
	}

	public void setPersonaJuridica(PersonaJuridica personaJuridica) {
		this.personaJuridica = personaJuridica;
	}

	public TipoFolio getTipoFolio() {
		return this.tipoFolio;
	}

	public void setTipoFolio(TipoFolio tipoFolio) {
		this.tipoFolio = tipoFolio;
	}

  public Mueble getMueble(){
    return this.mueble;
  }


  public void setMueble(Mueble mueble){
    this.mueble = mueble;
  }

  public FolioSeccionAuxiliar getFolioSeccionAuxiliar(){
    return this.folioSeccionAuxiliar;
  }

  public void setFolioSeccionAuxiliar(FolioSeccionAuxiliar folioSeccionAuxiliar){
    this.folioSeccionAuxiliar = folioSeccionAuxiliar;
  }

  public Integer getVersion() {
      return this.version;
  }

  public void setVersion(Integer version) {
      this.version = version;
  }

  public String getIdVersion() {
      return this.idVersion;
  }

  public void setIdVersion(String idVersion) {
      this.idVersion = idVersion;
  }

  public Integer getEstatus() {
      return this.estatus;
  }

  public void setEstatus(Integer estatus) {
      this.estatus = estatus;
  }

    public Boolean getValido() {
        return valido;
    }

    public void setValido(Boolean valido) {
        this.valido = valido;
    }
}
