package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.SortNatural;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Set;
import java.util.Objects;


@Entity
@Table(name = "persona_juridica_bitacora")
public class PersonaJuridicaBitacora implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personaJuridicaBitacoraGenerator")
    @SequenceGenerator(allocationSize = 1, name = "personaJuridicaBitacoraGenerator", sequenceName="persona_juridica_bitacora_seq")
    private Long id;

    @Column(name = "no_folio", length = 10)
    private Integer noFolio;

    @Column(name = "denominacion_nombre", length = 250)
    private String denominacionNombre;
    
    @Column(name = "objeto", length = 10485760)
    private String objeto;
    
    @Column(name ="cerrado", length = 1)
    private String cerrado;
	
    @Column(name ="direccion", length = 400)
    private String direccion;

    @Size(max = 250)
    @Column(name = "duracion", length = 250)
    private String duracion;

    @Column(name="fecha_apertura")
    private Date fechaApertura;
	
    @Column(name = "primer_registro")
    private Boolean primerRegistro;
    
    @Size(max = 80)
    @Column(name = "hash_fila", length = 80)
    private String hashFila;

    @Column(name = "bloqueado")
    private Boolean bloqueado;
    
    @Column(name = "soc")
    private Long soc;
    
    
    @Column(name = "capital", precision=12, scale=2)
    private BigDecimal capital;

    @Column(name = "fecha_act")
    private Date fechaAct;
    
    @ManyToOne
    @JoinColumn(nullable = true)
    private Municipio municipio;
    

    @ManyToOne
    private TipoSociedad tipoSociedad;
	
    @ManyToOne
    private Oficina oficina;

    @Column(name = "caratula_actualizada")
    private Boolean caratulaActualizada;
    
    @ManyToOne
    private CampoValores organo;
    
   
	@ManyToOne
    private CampoValores tipoColectivo;
	
	@ManyToOne
	private Acto acto;
	
	@ManyToOne
	private Usuario usuario;
    
	@ManyToOne
	private PersonaJuridica personaJuridica;
   
    
    public String getHashFila() {
		return hashFila;
	}

	public void setHashFila(String hashFila) {
		this.hashFila = hashFila;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public String getObjeto() {
        return objeto;
    }
    
    public void setObjeto(String objeto){
        this.objeto = objeto;
    }
	
    public String getDireccion() {
        return direccion;
    }
    
    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

    public Integer getNoFolio() {
        return noFolio;
    }

    public void setNoFolio(Integer noFolio) {
        this.noFolio = noFolio;
    }

    public String getDenominacionNombre() {
        return denominacionNombre;
    }

    public void setDenominacionNombre(String denominacionNombre) {
        this.denominacionNombre = denominacionNombre;
    }

    public String getDuracion() {
        return duracion;
    }

    public void setDuracion(String duracion) {
        this.duracion = duracion;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }
    
	
    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    //no existen
    public TipoSociedad getTipoSociedad() {
        return tipoSociedad;
    }

    public void setTipoSociedad(TipoSociedad tipoSociedad) {
        this.tipoSociedad = tipoSociedad;
    }
    
    public String getCerrado() {
        return cerrado;
    }
    
    public void setCerrado(String cerrado){
        this.cerrado = cerrado;
    }

    /*public Participacion getParticipacion() {
        return participacion;
    }

    public void setParticipacion(Participacion participacion) {
        this.participacion = participacion;
    }*/
    
    
	
    public Boolean isPrimerRegistro() {
		return primerRegistro!=null ? primerRegistro : false;
	}

	public void setPrimerRegistro(Boolean primerRegistro) {
		this.primerRegistro = primerRegistro;
	}

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PersonaJuridica{" +
            "id=" + getId() +
            "primerRegistro=" + isPrimerRegistro() +
            "objeto=" + getObjeto() +
            "municipio = " + getMunicipio() +
            "}";
    }

	public Boolean getCaratulaActualizada() {
		return caratulaActualizada;
	}

	public void setCaratulaActualizada(Boolean caratulaActualizada) {
		this.caratulaActualizada = caratulaActualizada;
	}
	
    public String getDescripcion(){
    	StringBuilder sb = new StringBuilder();
    	sb.append(" Nombre o Denominación: ").append(denominacionNombre);
    	if (direccion!=null){
    		sb.append(", Dirección: ").append(direccion);
    	}
    	return sb.toString();    	
    }

	public Boolean getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}
	
	 public CampoValores getOrgano() {
			return organo;
		}

		public void setOrgano(CampoValores organo) {
			this.organo = organo;
		}

		public CampoValores getTipoColectivo() {
			return tipoColectivo;
		}

		public void setTipoColectivo(CampoValores tipoColectivo) {
			this.tipoColectivo = tipoColectivo;
		}

		public Long getSoc() {
			return soc;
		}

		public void setSoc(Long soc) {
			this.soc = soc;
		}

		public BigDecimal getCapital() {
			return capital;
		}

		public void setCapital(BigDecimal capital) {
			this.capital = capital;
		}

		public Acto getActo() {
			return acto;
		}

		public void setActo(Acto acto) {
			this.acto = acto;
		}

		public Usuario getUsuario() {
			return usuario;
		}

		public void setUsuario(Usuario usuario) {
			this.usuario = usuario;
		}

		public PersonaJuridica getPersonaJuridica() {
			return personaJuridica;
		}

		public void setPersonaJuridica(PersonaJuridica personaJuridica) {
			this.personaJuridica = personaJuridica;
		}

		public Date getFechaAct() {
			return fechaAct;
		}

		public void setFechaAct(Date fecha_act) {
			this.fechaAct = fecha_act;
		}


}
