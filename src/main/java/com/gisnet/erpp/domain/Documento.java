package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Documento.
 */
@Entity
@Table(name = "documento")
@JsonIgnoreProperties(ignoreUnknown=true)
public class Documento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "documentoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "documentoGenerator", sequenceName="documento_seq")
    private Long id;

    @Column(name = "numero")
    private Integer numero;

    @Column(name = "fecha")
    private Date fecha;

    @Column(name = "ran")
    private Boolean ran;

    @Size(max = 200)
    @Column(name = "asunto", length = 200)
    private String asunto;

    @Column(name = "ratificado")
    private Boolean ratificado;

    @Column(name = "bis")
    private Boolean bis;
    
    @Column(name = "fecha2")
    private Date fecha2;

    public Date getFecha2() {
		return fecha2;
	}

	public void setFecha2(Date fecha2) {
		this.fecha2 = fecha2;
	}
	
	public Documento fecha2(Date fecha2) {
	        this.fecha2 = fecha2;
	        return this;
	}

	@Size(max = 100)
    @Column(name = "causa_utilidad", length = 100)
    private String causaUtilidad;

    @Column(name = "observaciones")
    private String observaciones;

     @Size(max = 80)
     @Column(name = "nombre", length = 80)
     private String nombre;

     @Size(max = 60)
     @Column(name = "paterno", length = 60)
     private String paterno;

     @Size(max = 60)
     @Column(name = "materno", length = 60)
     private String materno;



     @Column(name = "exhorto")
     private Boolean exhorto;

     @Size(max = 300)
     @Column(name = "derivado_de", length = 300)
     private String derivadoDe;

     @Size(max = 200)
     @Column(name = "autoridad_exhortante", length = 200)
     private String autoridadExhortante;

     @Size(max = 200)
     @Column(name = "cargo_firmante", length = 200)
     private String cargoFirmante;

     @Size(max = 200)
     @Column(name = "autoridad_local", length = 512)
     private String autoridadLocal;

     @Size(max = 2000)
     @Column(name = "objeto", length = 2000)
     private String objeto;

     @Size(max = 50)
     @Column(name = "numero2", length = 50)
     private String numero2;
     
     @Size(max = 50)
     @Column(name = "numero3", length = 50)
     private String numero3;

     @Size(max = 64)
     @Column(name = "expropiante", length = 64)
     private String expropiante;

     @Size(max = 100)
     @Column(name = "en_calidad_de", length = 100)
     private String enCalidadDe;

     @Size(max = 100)
     @Column(name = "requerido_por", length = 100)
     private String requeridoPor;

     @Column(name = "id_dato_documento")
     private Integer id_dato_documento;     
     
     

    @OneToMany(mappedBy = "documento")
    @JsonIgnore
    private Set<ActoDocumento> actoDocumentosParaDocumentos = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private TipoDocumento tipoDocumento;

    @ManyToOne
    private Municipio municipio;

    @ManyToOne
    private TipoCert tipoCert;

    @ManyToOne
    private TipoAutoridad tipoAutoridad;

    /*@ManyToOne
    private Persona persona;*/

    @ManyToOne
    private Notario notario;

    @ManyToOne
    private Archivo archivo;

    @ManyToOne
    private Juez juez;

    @Size(max = 100)
    @Column(name = "juzgado", length = 100)
    private String juzgado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNumero() {
        return numero;
    }

    public Documento numero(Integer numero) {
        this.numero = numero;
        return this;
    }

    public void setNumero(Integer numero) {
        this.numero = numero;
    }

    public Date getFecha() {
        return fecha;
    }

    public Documento fecha(Date fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Boolean isRan() {
        return ran;
    }

    public Documento ran(Boolean ran) {
        this.ran = ran;
        return this;
    }

    public void setRan(Boolean ran) {
        this.ran = ran;
    }

    public String getAsunto() {
        return asunto;
    }

    public Documento asunto(String asunto) {
        this.asunto = asunto == null ? null : asunto.toUpperCase();
        return this;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto == null ? null : asunto.toUpperCase();
    }

    public void setNombre(String nombre) {
        this.nombre = nombre == null ? null : nombre.toUpperCase();
    }


    public String getNombre() {
        return nombre;
    }

    public Documento nombre(String nombre) {
        this.nombre = nombre == null ? null : nombre.toUpperCase();
        return this;
    }


    public void setPaterno(String paterno) {
        this.paterno = paterno == null ? null : paterno.toUpperCase();
    }


    public String getPaterno() {
        return paterno;
    }

    public Documento paterno(String paterno) {
        this.paterno = paterno == null ? null : paterno.toUpperCase();
        return this;
    }

    public void setMaterno(String materno) {
        this.materno = materno == null ? null : materno.toUpperCase();
    }


    public String getMaterno() {
        return materno;
    }

    public Documento materno(String materno) {
        this.materno = materno == null ? null : materno.toUpperCase();
        return this;
    }


    public Boolean isRatificado() {
        return ratificado;
    }

    public Documento ratificado(Boolean ratificado) {
        this.ratificado = ratificado;
        return this;
    }

    public void setRatificado(Boolean ratificado) {
        this.ratificado = ratificado;
    }

    public Boolean isBis() {
        return bis;
    }

    public Documento bis(Boolean bis) {
        this.bis = bis;
        return this;
    }

    public void setBis(Boolean bis) {
        this.bis = bis;
    }

    public Boolean isExhorto() {
        return exhorto;
    }

    public Documento exhorto(Boolean exhorto) {
        this.exhorto = exhorto;
        return this;
    }

    public void setExhorto(Boolean exhorto) {
        this.exhorto = exhorto;
    }

    public String getCausaUtilidad() {
        return causaUtilidad;
    }

    public Documento causaUtilidad(String causaUtilidad) {
        this.causaUtilidad = causaUtilidad == null ? null : causaUtilidad.toUpperCase();
        return this;
    }

    public void setCausaUtilidad(String causaUtilidad) {
        this.causaUtilidad = causaUtilidad == null ? null : causaUtilidad.toUpperCase();
    }


    public String getObservaciones() {
        return observaciones;
    }

    public Documento observaciones(String observaciones) {
        this.observaciones = observaciones == null ? null : observaciones.toUpperCase();
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones == null ? null : observaciones.toUpperCase();
    }



    public Set<ActoDocumento> getActoDocumentosParaDocumentos() {
        return actoDocumentosParaDocumentos;
    }

    public Documento actoDocumentosParaDocumentos(Set<ActoDocumento> actoDocumentos) {
        this.actoDocumentosParaDocumentos = actoDocumentos;
        return this;
    }

    public Documento addActoDocumentosParaDocumento(ActoDocumento actoDocumento) {
        this.actoDocumentosParaDocumentos.add(actoDocumento);
        actoDocumento.setDocumento(this);
        return this;
    }

    public Documento removeActoDocumentosParaDocumento(ActoDocumento actoDocumento) {
        this.actoDocumentosParaDocumentos.remove(actoDocumento);
        actoDocumento.setDocumento(null);
        return this;
    }

    public void setActoDocumentosParaDocumentos(Set<ActoDocumento> actoDocumentos) {
        this.actoDocumentosParaDocumentos = actoDocumentos;
    }

    public void addActo(Acto acto) {
    	if (acto == null) {
				return;
			}
    		ActoDocumento actoDocumento = new ActoDocumento();
    		actoDocumento.setActo(acto);
    		actoDocumento.setDocumento(this);
    		this.actoDocumentosParaDocumentos.add(actoDocumento);
    }

    public TipoDocumento getTipoDocumento() {
        return tipoDocumento;
    }

    public Documento tipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
        return this;
    }

    public void setTipoDocumento(TipoDocumento tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public Documento municipio(Municipio municipio) {
        this.municipio = municipio;
        return this;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public TipoCert getTipoCert() {
        return tipoCert;
    }

    public Documento tipoCert(TipoCert tipoCert) {
        this.tipoCert = tipoCert;
        return this;
    }

    public void setTipoCert(TipoCert tipoCert) {
        this.tipoCert = tipoCert;
    }

    public TipoAutoridad getTipoAutoridad() {
        return tipoAutoridad;
    }

    public Documento tipoAutoridad(TipoAutoridad tipoAutoridad) {
        this.tipoAutoridad = tipoAutoridad;
        return this;
    }

    public void setTipoAutoridad(TipoAutoridad tipoAutoridad) {
        this.tipoAutoridad = tipoAutoridad;
    }

/*    public Persona getPersona() {
        return persona;
    }

    public Documento persona(Persona persona) {
        this.persona = persona;
        return this;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }*/

    public Notario getNotario() {
        return notario;
    }

    public Documento notario(Notario notario) {
        this.notario = notario;
        return this;
    }

    public void setNotario(Notario notario) {
        this.notario = notario;
    }

    public Archivo getArchivo() {
        return archivo;
    }

    public Documento archivo(Archivo archivo) {
   		if (archivo != null) {
        this.archivo = archivo;
			}
        return this;
    }

   	public void setArchivo(Archivo archivo) {
   		if(archivo ==null) {
   			return;
   		}
   		if (archivo.getId() != null) {

        this.archivo = archivo;
        this.archivo.addDocumentoParaArchivo(this);
			}
    }

    public Juez getJuez() {
        return juez;
    }

    public Documento juez(Juez juez) {
        this.juez = juez;
        return this;
    }

    public void setJuez(Juez juez) {
        this.juez = juez;
    }

    public String getRequeridoPor() {
		return requeridoPor;
	}

	public void setRequeridoPor(String requeridoPor) {
		this.requeridoPor = requeridoPor == null ? null : requeridoPor.toUpperCase();
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Documento documento = (Documento) o;
        if (documento.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), documento.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Documento{" +
            "id=" + getId() +
            ", numero='" + getNumero() + "'" +
            ", fecha='" + getFecha() + "'" +
            ", numero2='" + getNumero2() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            ", ran='" + isRan() + "'" +
            ", asunto='" + getAsunto() + "'" +
            ", ratificado='" + isRatificado() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", paterno='" + getPaterno() + "'" +
            ", materno='" + getMaterno() + "'" +
            ", bis='" + isBis() + "'" +
            ", causaUtilidad='" + getCausaUtilidad() + "'" +
            ", municipio='" + getMunicipio() + "'" +
            "}";
    }

    public String getDerivadoDe() {
        return derivadoDe;
    }


    public Documento derivadoDe(String derivadoDe) {
        this.derivadoDe = derivadoDe == null ? null : derivadoDe.toUpperCase();
        return this;
    }

    public void setDerivadoDe(String derivadoDe) {
        this.derivadoDe = derivadoDe == null ? null : derivadoDe.toUpperCase();
    }

    public String getAutoridadExhortante() {
        return autoridadExhortante;
    }


    public String getExpropiante() {
		return expropiante;
	}

	public void setExpropiante(String expropiante) {
		this.expropiante = expropiante == null ? null : expropiante.toUpperCase();
	}

	public String getEnCalidadDe() {
		return enCalidadDe;
	}

	public void setEnCalidadDe(String enCalidadDe) {
		this.enCalidadDe = enCalidadDe == null ? null : enCalidadDe.toUpperCase();
	}

	public Documento autoridadExhortante(String autoridadExhortante) {
        this.autoridadExhortante = autoridadExhortante == null ? null : autoridadExhortante.toUpperCase();
        return this;
    }

    public void setAutoridadExhortante(String autoridadExhortante) {
        this.autoridadExhortante = autoridadExhortante == null ? null : autoridadExhortante.toUpperCase();
    }

    public String getCargoFirmante() {
        return cargoFirmante;
    }


    public Documento cargoFirmante(String cargoFirmante) {
        this.cargoFirmante = cargoFirmante == null ? null : cargoFirmante.toUpperCase();
        return this;
    }

    public void setCargoFirmante(String cargoFirmante) {
        this.cargoFirmante = cargoFirmante == null ? null : cargoFirmante.toUpperCase();
    }

    public String getAutoridadLocal() {
        return autoridadLocal;
    }


    public Documento autoridadLocal(String autoridadLocal) {
        this.autoridadLocal = autoridadLocal == null ? null : autoridadLocal.toUpperCase();
        return this;
    }

    public void setAutoridadLocal(String autoridadLocal) {
        this.autoridadLocal = autoridadLocal == null ? null : autoridadLocal.toUpperCase();
    }

    public String getObjeto() {
        return objeto;
    }


    public Documento objeto(String objeto) {
        this.objeto = objeto == null ? null : objeto.toUpperCase();
        return this;
    }

    public void setObjeto(String objeto) {
        this.objeto = objeto == null ? null : objeto.toUpperCase();
    }

    public String getNumero2() {
        return numero2;
    }

    public Documento numero2(String numero2) {
        this.numero2 = numero2 == null ? null : numero2.toUpperCase();
        return this;
    }

    public void setNumero2(String numero2) {
        this.numero2 = numero2 == null ? null : numero2.toUpperCase();
    }
    
    public String getNumero3() {
        return numero3;
    }

    public Documento numero3(String numero3) {
        this.numero3 = numero3 == null ? null : numero3.toUpperCase();
        return this;
    }

    public void setNumero3(String numero3) {
        this.numero3 = numero3 == null ? null : numero3.toUpperCase();
    }

	public String getJuzgado() {
		return juzgado;
	}

	public void setJuzgado(String juzgado) {
		this.juzgado = juzgado;
	}

	public Integer getId_dato_documento() {
        return id_dato_documento;
    }
	
	    public void setId_dato_documento(Integer id_dato_documento) {
        this.id_dato_documento = id_dato_documento;
    }

	
}
