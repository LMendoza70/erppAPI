package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.SortNatural;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.Set;
import java.util.Objects;


@Entity
@Table(name = "persona_juridica")
public class PersonaJuridica implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personaJuridicaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "personaJuridicaGenerator", sequenceName="persona_juridica_seq")
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

    @Column(name = "duracion")
    private String duracion;

    @Column(name="fecha_apertura")
    private Date fechaApertura;
	
    @Column(name = "primer_registro", length = 1)
    private Integer primerRegistro;
    
    @Size(max = 80)
    @Column(name = "hash_fila", length = 80)
    private String hashFila;

    @Column(name = "bloqueado")
    private Boolean bloqueado;

    @ManyToOne
    private Municipio municipio;

    @ManyToOne
    private TipoSociedad tipoSociedad;
	
    @ManyToOne
    private Oficina oficina;

    @Column(name = "caratula_actualizada")
    private Boolean caratulaActualizada;
    
    @Column(name = "id_folio_real")
    private Integer id_folio_real;
    
    @Column(name = "numero_folio_real")
    private Integer numeroFolioReal;
    
  

	@Column(name = "id_dato_persona_moral")
    private Integer id_dato_persona_moral;
    
    /*@ManyToOne
    private Participacion participacion;*/

    @ManyToOne
    private CampoValores organo;
    
    @ManyToOne
    private CampoValores tipoColectivo;
    
    
    @OneToMany(mappedBy = "personaJuridica")
    @JsonIgnore
    private Set<ActoPredio> actoPrediosParaPersonasJuridicas = new HashSet<>();
    
    @OneToMany(mappedBy = "personaJuridica")
    @JsonIgnore
    private Set<PjAnte> pjAnteParaPersonasJuridicas = new HashSet<>();
    
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
    
    
    public Integer getId_folio_real() {
        return id_folio_real;
    }

    public void setId_folio_real(Integer id_folio_real) {
        this.id_folio_real = id_folio_real;
    }

    
    

    
    public Integer getId_dato_persona_moral() {
        return id_dato_persona_moral;
    }

    public void setId_dato_persona_moral(Integer id_dato_persona_moral) {
        this.id_dato_persona_moral = id_dato_persona_moral;
    }
  

    /*public Participacion getParticipacion() {
        return participacion;
    }

    public void setParticipacion(Participacion participacion) {
        this.participacion = participacion;
    }*/
    
    
    public Set<ActoPredio> getActoPrediosParaPersonasJuridicas() {
        return actoPrediosParaPersonasJuridicas;
    }
	
    public Integer getPrimerRegistro() {
        return primerRegistro;
    }

    public void setPrimerRegistro(Integer primerRegistro) {
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
            "}";
    }

	public Set<PjAnte> getPjAnteParaPersonasJuridicas() {
		return pjAnteParaPersonasJuridicas;
	}

	public void setPjAnteParaPersonasJuridicas(Set<PjAnte> pjAnteParaPersonasJuridicas) {
		this.pjAnteParaPersonasJuridicas = pjAnteParaPersonasJuridicas;
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
	
	public Integer getNumeroFolioReal() {
		return numeroFolioReal;
	}

   public void setNumeroFolioReal(Integer numeroFolioReal) {
	    this.numeroFolioReal = numeroFolioReal;
   }

}
