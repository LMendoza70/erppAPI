package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A Busqueda.
 */
@Entity
@Table(name = "busqueda_resultado")
public class BusquedaResultado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "busquedaResultadoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "busquedaResultadoGenerator", sequenceName="busqueda_resultado_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Prelacion prelacion;

    @ManyToOne(optional = true)
    private Predio predio;

    // Para copias e impresiones
    @ManyToOne(optional = true)
    private FolioSeccionAuxiliar folioSeccionAuxiliar;

    @ManyToOne(optional = true)
    private PersonaJuridica personaJuridica;

    @ManyToOne(optional = true)
    private Mueble mueble;

    private String observaciones;

    private Integer folio;

    @ManyToOne(optional = true)
    private FoliosrDigital foliosrDigital;

    private Boolean esImpresion;

    private String escritura;

    private Boolean caratulaGenerada;

    // Para copias e impresiones
    @Size(max = 100)
    @Column(name = "documento", length = 100)
    private String documento;

    @ManyToOne
    private Libro libro;

    private String hojasSeleccionadas;

    @OneToMany(mappedBy = "busquedaResultado")
    private Set<BusquedaResultadoTipoActo> listaTiposActo = new HashSet<>();
    
    @OneToMany(mappedBy = "busquedaResultado")
    private Set<BusquedaResultadoActo> actosParaBusqueda = new HashSet<>();
    
   

	@ManyToOne
    private Prelacion prelacionHistorica;
    
    @ManyToOne
    private ActoPublicitario actoPublicitario;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prelacion getPrelacion() {
        return prelacion;
    }

    public void setPrelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
    }

    public Predio getPredio() {
        return predio;
    }

    public void setPredio(Predio predio) {
        this.predio = predio;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }


    public FolioSeccionAuxiliar getFolioSeccionAuxiliar() {
        return folioSeccionAuxiliar;
    }

    public void setFolioSeccionAuxiliar(FolioSeccionAuxiliar folioSeccionAuxiliar) {
        this.folioSeccionAuxiliar = folioSeccionAuxiliar;
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

    public Integer getFolio() {
        return folio;
    }

    public void setFolio(Integer folio) {
        this.folio = folio;
    }

    public FoliosrDigital getFoliosrDigital() {
        return foliosrDigital;
    }

    public void setFoliosrDigital(FoliosrDigital foliosrDigital) {
        this.foliosrDigital = foliosrDigital;
    }

    public Boolean getEsImpresion() {
        return esImpresion;
    }

    public void setEsImpresion(Boolean esImpresion) {
        this.esImpresion = esImpresion;
    }

    public String getEscritura() {
        return escritura;
    }

    public void setEscritura(String escritura) {
        this.escritura = escritura;
    }

    public Boolean getCaratulaGenerada() {
        return caratulaGenerada;
    }

    public void setCaratulaGenerada(Boolean caratulaGenerada) {
        this.caratulaGenerada = caratulaGenerada;
    }




    public Set<BusquedaResultadoTipoActo> getListaTiposActo() {
        return listaTiposActo;
    }

    public void setListaTiposActo(Set<BusquedaResultadoTipoActo> listaTiposActo) {
        this.listaTiposActo = listaTiposActo;
    }

    /** Copias e impresion de folios **/
    public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public Libro getLibro() {
		return libro;
	}

	public void setLibro(Libro libro) {
		this.libro = libro;
	}

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    public String getHojasSeleccionadas() {
        return hojasSeleccionadas;
    }

    public void setHojasSeleccionadas(String hojasSeleccionadas) {
        this.hojasSeleccionadas = hojasSeleccionadas;
    }

	public Prelacion getPrelacionHistorica() {
		return prelacionHistorica;
	}

	public void setPrelacionHistorica(Prelacion prelacionHistorica) {
		this.prelacionHistorica = prelacionHistorica;
	}

	public ActoPublicitario getActoPublicitario() {
		return actoPublicitario;
	}

	public void setActoPublicitario(ActoPublicitario actoPublicitario) {
		this.actoPublicitario = actoPublicitario;
	}
	
	public Set<BusquedaResultadoActo> getActosParaBusqueda() {
			return actosParaBusqueda;
	}

	public void setActosParaBusqueda(Set<BusquedaResultadoActo> actosParaBusqueda) {
			this.actosParaBusqueda = actosParaBusqueda;
	}
    	
}
