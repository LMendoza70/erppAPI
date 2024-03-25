package com.gisnet.erpp.domain;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A PrelacionAnte.
 */
@Entity
@Table(name = "prelacion_ante")
public class PrelacionAnte implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prelacionAnteGenerator")
    @SequenceGenerator(allocationSize = 1, name = "prelacionAnteGenerator", sequenceName="prelacion_ante_seq")
    private Long id;
  
    @ManyToOne(optional = false)
    @NotNull
    private Prelacion prelacion;

    @Column (name="libro")    
    private String libro;   
        
    @Column (name="libro_bis",length = 20)
    private String libroBis;

    @ManyToOne(optional = true)
    private Seccion seccion;

    @ManyToOne(optional = true)
    private Oficina oficina;

    @Column (name="documento")
    private String documento;
    
    @Column (name="documento_bis")
    private String documentoBis;  

    @Column (name="validado_cyv")
    private Boolean validadoCyv;
    
    private Boolean valido=false;

    @ManyToOne
    private TipoFolio tipoFolio;
    
    @ManyToOne
    private NombreLibro nombreLibro;
    
    @ManyToOne
    private TipoLibro tipoLibro;
    
    @Column (name="tipo_doc")
    private Boolean tipoDoc;
    
    @ManyToOne
    private Predio predio;

    @ManyToOne
    private Mueble mueble;

    @ManyToOne
    private PersonaJuridica personaJuridica;

    @ManyToOne
    private FolioSeccionAuxiliar folioSeccionAuxiliar;
    
    @Column(name = "anio")
    private Integer anio;
    
    @Column(name = "volumen")
    private String volumen;    
        
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public String getLibroBis() {
		return libroBis;
	}

	public void setLibroBis(String libroBis) {
		this.libroBis = libroBis;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public String getDocumentoBis() {
		return documentoBis;
	}

	public void setDocumentoBis(String documentoBis) {
		this.documentoBis = documentoBis;
	}
	
	public Boolean getValido() {
        return valido;
    }

    public void setValido(Boolean valido) {
        this.valido = valido;
    }

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	
	public Seccion getSeccion() {
		return seccion;
	}

	public void setSeccion(Seccion seccion) {
		this.seccion = seccion;
	}

	public Oficina getOficina() {
		return oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}
	
	public Boolean getValidadoCyv() {
		return validadoCyv;
	}

	public void setValidadoCyv(Boolean validadoCyv) {
		this.validadoCyv = validadoCyv;
	}
	
	public Prelacion getPrelacion() {
        return prelacion;
    }

    public PrelacionAnte prelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
        return this;
    }

    public void setPrelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
    }

	public String getLibro() {
		return libro;
	}

	public void setLibro(String libro) {
		this.libro = libro;
	}

	public Predio getPredio() {
		return predio;
	}

	public void setPredio(Predio predio) {
		this.predio = predio;
	}

	public Mueble getMueble() {
		return mueble;
	}

	public void setMueble(Mueble mueble) {
		this.mueble = mueble;
	}

	public PersonaJuridica getPersonaJuridica() {
		return personaJuridica;
	}

	public void setPersonaJuridica(PersonaJuridica personaJuridica) {
		this.personaJuridica = personaJuridica;
	}

	public FolioSeccionAuxiliar getFolioSeccionAuxiliar() {
		return folioSeccionAuxiliar;
	}

	public void setFolioSeccionAuxiliar(FolioSeccionAuxiliar folioSeccionAuxiliar) {
		this.folioSeccionAuxiliar = folioSeccionAuxiliar;
	}

	public TipoFolio getTipoFolio() {
		return tipoFolio;
	}

	public void setTipoFolio(TipoFolio tipoFolio) {
		this.tipoFolio = tipoFolio;
	}
	
    public Boolean getTipoDoc() {
		return tipoDoc==null?false:tipoDoc;
	}

	public void setTipoDoc(Boolean tipoDoc) {
		this.tipoDoc = tipoDoc;
	}
	
	public NombreLibro getNombreLibro() {
		return this.nombreLibro;
	}

	public void setNombreLibro(NombreLibro nombreLibro) {
		this.nombreLibro = nombreLibro;
	}
	
	public TipoLibro getTipoLibro() {
		return this.tipoLibro;
	}

	public void setTipoLibro(TipoLibro tipoLibro) {
		this.tipoLibro = tipoLibro;
	}
	
	public String getDescripcion(){
    	StringBuilder sb = new StringBuilder();
    	sb.append(" Tomo: ").append(libro);
    	sb.append(", Libro : ").append(libroBis);
    	sb.append(", Seccion: ").append(seccion.getNombre());
    	sb.append(", Oficina: ").append(oficina.getNombre());
    	sb.append(", Año: ").append(anio);
    	sb.append(", Volumen: ").append(volumen);
    	sb.append(", Inscripcion: ").append(documento);
    	//sb.append(", Tipo Partida: ").append(documentoBis);
    	sb.append(", Tipo Folio: ").append(tipoFolio.getNombre());    	
    	return sb.toString();
    }
	
	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PrelacionAnte prelacionAnte = (PrelacionAnte) o;
        if (prelacionAnte.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), prelacionAnte.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Id: ").append(this.getId());
		sb.append(this.getDescripcion());
		return sb.toString();
	}

	public String getVolumen() {
		return volumen;
	}

	public void setVolumen(String volumen) {
		this.volumen = volumen;
	}
		
}
