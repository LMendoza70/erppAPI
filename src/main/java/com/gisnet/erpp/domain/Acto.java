package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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


/**
 * A Acto.
 */
@Entity
@Table(name = "acto")
public class Acto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "actoGenerator", sequenceName="acto_seq")
    private Long id;
    
    @Column(name = "fecha_registro")
    private Date fechaRegistro;

    @Size(max = 80)
    @Column(name = "hash_fila", length = 80)
    private String hashFila;

    @Column(name = "orden")
    private Integer orden;

    @Column(name = "procesado")
    private Boolean procesado;
    
    @Column(name = "copiado_modificado")
    private Boolean copiadoModificado;
    
    @Column(name = "version", length = 3, nullable = false)
    private Integer version;

    @Size(max = 10000)
    @Column(name="observaciones_motivo")
    private String observacionesMotivo;

    @Column(name="fecha_rechazo")
    private Date fechaRechazo;
    
    @Column(name = "modificable")
    private Boolean modificable;

    @Column(name = "primer_registro")
    private Boolean primerRegistro;
    
    @Column(name = "vf")
    private Boolean vf;

    /**
     * True cuando se ha generado un acto copia con nuevos valores en la materializacion
     */
    @Column(name = "remplazado")
    private Boolean remplazado;
    
    @Column(name="contenido_xml")
    private String contenidoXml;
    
    @Column(name="id_asiento")
    private Long idAsiento;
    
    @Column(name="id_entrada")
    private Long id_entrada;
    
    @Column(name = "id_aaj")
    private Integer id_aaj;

    @OneToMany(mappedBy = "acto")
    @JsonIgnore
    private Set<BitacoraActoRechazo> bitacoraActoRechazos = new HashSet<>();

    @OneToMany(mappedBy = "acto")
    @JsonIgnore
    private Set<ActoAnte> actoAntesParaActos = new HashSet<>();

    //@OneToMany(mappedBy = "acto", cascade = CascadeType.REMOVE, orphanRemoval = true)
    @OneToMany(mappedBy = "acto")
    @JsonIgnore
    private Set<ActoDocumento> actoDocumentosParaActos = new HashSet<>();

    @OneToMany(mappedBy = "acto")
    @JsonIgnore
    private Set<ActoFirma> actoFirmasParaActos = new HashSet<>();

    @OneToMany(mappedBy = "acto")
    @JsonIgnore
    private Set<ActoModulo> actoModulosParaActos = new HashSet<>();

    @OneToMany(mappedBy = "acto", fetch=FetchType.LAZY)
    @SortNatural
    @OrderBy("version DESC")
    @JsonIgnore
    private SortedSet<ActoPredio> actoPrediosParaActos = new TreeSet<ActoPredio>();

    @OneToMany(mappedBy = "acto")
    @JsonIgnore
    private Set<FoliosFracCond> foliosFracCondParaActos = new HashSet<>();

    @OneToMany(mappedBy = "acto")
    @JsonIgnore
    private Set<ActoRequisito> actoRequisitosParaActos = new HashSet<>();
    
    @OneToMany(mappedBy = "actoSig")
    @JsonIgnore
    private Set<ActoRel> actoRelesParaActoSiguientes = new HashSet<>();

    @OneToMany(mappedBy = "acto")
    @JsonIgnore
    private Set<ActoRel> actoRelesParaActos = new HashSet<>();
    //CV
    @Column(name = "archivado")
    private Boolean archivado;
    //CV
    @Size(max=200)
	@Column(name="path_archivado",length=200)
    private String pathArchivado;
    //CV
	@Column(name="clon")
	private Boolean clon;

    @ManyToOne(optional = false)
    @NotNull
    private StatusActo statusActo;

    @ManyToOne(fetch= FetchType.LAZY)
    private Prelacion prelacion;

    @ManyToOne
    private TipoActo tipoActo;

    @ManyToOne(fetch= FetchType.LAZY)
    private Motivo motivo;

    @ManyToOne
    private TipoRechazo tipoRechazo;


    @OneToMany(mappedBy = "acto")
    @JsonIgnore
    private Set<ActoPublicitario> actosPublicitariosParaActo = new HashSet<>();
    
    @Column(name="hist")
    private Boolean hist;
    
	public Boolean getVf() {
		return vf;
	}

	public void setVf(Boolean vf) {
		this.vf = vf;
	}

	public Boolean getCopiadoModificado() {
		return copiadoModificado;
	}

	public void setCopiadoModificado(Boolean copiadoModificado) {
		this.copiadoModificado = copiadoModificado;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Date getFechaRegistro() {
		return fechaRegistro;
	}

	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	public String getHashFila() {
        return hashFila;
    }

    public Acto hashFila(String hashFila) {
        this.hashFila = hashFila;
        return this;
    }

    public void setHashFila(String hashFila) {
        this.hashFila = hashFila;
    }

    public Integer getOrden() {
        return orden;
    }

    public Acto orden(Integer orden) {
        this.orden = orden;
        return this;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Boolean isProcesado() {
        return procesado;
    }

    public Acto procesado(Boolean procesado) {
        this.procesado = procesado;
        return this;
    }

    public void setProcesado(Boolean procesado) {
        this.procesado = procesado;
    }

    public Set<ActoAnte> getActoAntesParaActos() {
        return actoAntesParaActos;
    }

    public Acto actoAntesParaActos(Set<ActoAnte> actoAntes) {
        this.actoAntesParaActos = actoAntes;
        return this;
    }

    public Acto addActoAntesParaActo(ActoAnte actoAnte) {
        this.actoAntesParaActos.add(actoAnte);
        actoAnte.setActo(this);
        return this;
    }

    public Acto removeActoAntesParaActo(ActoAnte actoAnte) {
        this.actoAntesParaActos.remove(actoAnte);
        actoAnte.setActo(null);
        return this;
    }

    public void setActoAntesParaActos(Set<ActoAnte> actoAntes) {
        this.actoAntesParaActos = actoAntes;
    }

    public Set<ActoDocumento> getActoDocumentosParaActos() {
        return actoDocumentosParaActos;
    }

    public Acto actoDocumentosParaActos(Set<ActoDocumento> actoDocumentos) {
        this.actoDocumentosParaActos = actoDocumentos;
        return this;
    }

    public Acto addActoDocumentosParaActo(ActoDocumento actoDocumento) {
        this.actoDocumentosParaActos.add(actoDocumento);
        actoDocumento.setActo(this);
        return this;
    }

    public Acto removeActoDocumentosParaActo(ActoDocumento actoDocumento) {
        this.actoDocumentosParaActos.remove(actoDocumento);
        actoDocumento.setActo(null);
        return this;
    }

    public void setActoDocumentosParaActos(Set<ActoDocumento> actoDocumentos) {
        this.actoDocumentosParaActos = actoDocumentos;
    }

    public Set<ActoFirma> getActoFirmasParaActos() {
        return actoFirmasParaActos;
    }

    public Acto actoFirmasParaActos(Set<ActoFirma> actoFirmas) {
        this.actoFirmasParaActos = actoFirmas;
        return this;
    }

    public Acto addActoFirmasParaActo(ActoFirma actoFirma) {
        this.actoFirmasParaActos.add(actoFirma);
        actoFirma.setActo(this);
        return this;
    }

    public Acto removeActoFirmasParaActo(ActoFirma actoFirma) {
        this.actoFirmasParaActos.remove(actoFirma);
        actoFirma.setActo(null);
        return this;
    }

    public void setActoFirmasParaActos(Set<ActoFirma> actoFirmas) {
        this.actoFirmasParaActos = actoFirmas;
    }

    public Set<ActoModulo> getActoModulosParaActos() {
        return actoModulosParaActos;
    }

    public Acto actoModulosParaActos(Set<ActoModulo> actoModulos) {
        this.actoModulosParaActos = actoModulos;
        return this;
    }

    public Acto addActoModulosParaActo(ActoModulo actoModulo) {
        this.actoModulosParaActos.add(actoModulo);
        actoModulo.setActo(this);
        return this;
    }

    public Acto removeActoModulosParaActo(ActoModulo actoModulo) {
        this.actoModulosParaActos.remove(actoModulo);
        actoModulo.setActo(null);
        return this;
    }

    public void setActoModulosParaActos(Set<ActoModulo> actoModulos) {
        this.actoModulosParaActos = actoModulos;
    }

    public Set<ActoPredio> getActoPrediosParaActos() {
        return (Set<ActoPredio>)this.actoPrediosParaActos;
    }
    @JsonIgnore
    public SortedSet<ActoPredio> getActoPrediosParaActosOrderedByVersion() {
        return this.actoPrediosParaActos;
    }

    public Acto actoPrediosParaActos(Set<ActoPredio> actoPredios) {
    	actoPredios.clear();
    	actoPredios.forEach(x -> this.actoPrediosParaActos.add(x));
        return this;
    }

    public Acto addActoPrediosParaActo(ActoPredio actoPredio) {
        this.actoPrediosParaActos.add(actoPredio);
        actoPredio.setActo(this);
        return this;
    }

    public Acto removeActoPrediosParaActo(ActoPredio actoPredio) {
        this.actoPrediosParaActos.remove(actoPredio);
        actoPredio.setActo(null);
        return this;
    }

    public void setActoPrediosParaActos(Set<ActoPredio> actoPredios) {
    	actoPredios.clear();
    	actoPredios.forEach(x -> this.actoPrediosParaActos.add(x));
    }
    
    public void setActoPredios(SortedSet<ActoPredio> actoPredios) {
    	this.actoPrediosParaActos=actoPredios;
    }


    public Set<FoliosFracCond> getFoliosFracCondParaActos() {
        return foliosFracCondParaActos;
    }

    public Acto foliosFracCondParaActos(Set<FoliosFracCond> foliosFracCond) {
        this.foliosFracCondParaActos = foliosFracCond;
        return this;
    }

    public Acto addFoliosFracCondParaActo(FoliosFracCond foliosFracCond) {
        this.foliosFracCondParaActos.add(foliosFracCond);
        foliosFracCond.setActo(this);
        return this;
    }

    public Acto removeFoliosFracCondParaActo(FoliosFracCond foliosFracCond) {
        this.foliosFracCondParaActos.remove(foliosFracCond);
        foliosFracCond.setActo(null);
        return this;
    }

    public void setFoliosFracCondParaActos(Set<FoliosFracCond> foliosFracCond) {
        this.foliosFracCondParaActos = foliosFracCond;
    }

    public Set<ActoRequisito> getActoRequisitosParaActos() {
        return actoRequisitosParaActos;
    }

    public Acto actoRequisitosParaActos(Set<ActoRequisito> actoRequisitos) {
        this.actoRequisitosParaActos = actoRequisitos;
        return this;
    }

    public Acto addActoRequisitosParaActo(ActoRequisito actoRequisito) {
        this.actoRequisitosParaActos.add(actoRequisito);
        actoRequisito.setActo(this);
        return this;
    }

    public Acto removeActoRequisitosParaActo(ActoRequisito actoRequisito) {
        this.actoRequisitosParaActos.remove(actoRequisito);
        actoRequisito.setActo(null);
        return this;
    }

    public void setActoRequisitosParaActos(Set<ActoRequisito> actoRequisitos) {
        this.actoRequisitosParaActos = actoRequisitos;
    }
    
    public Set<ActoRel> getActoRelesParaActoSiguientes() {
		return actoRelesParaActoSiguientes;
	}

	public void setActoRelesParaActoSiguientes(Set<ActoRel> actoRelesParaActoSiguientes) {
		this.actoRelesParaActoSiguientes = actoRelesParaActoSiguientes;
	}

	public Set<ActoRel> getActoRelesParaActos() {
		return actoRelesParaActos;
	}

	public void setActoRelesParaActos(Set<ActoRel> actoRelesParaActos) {
		this.actoRelesParaActos = actoRelesParaActos;
	}

	public StatusActo getStatusActo() {
        return statusActo;
    }

    public Acto statusActo(StatusActo statusActo) {
        this.statusActo = statusActo;
        return this;
    }

    public void setStatusActo(StatusActo statusActo) {
        this.statusActo = statusActo;
    }

    public Prelacion getPrelacion() {
        return prelacion;
    }

    public Acto prelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
        return this;
    }

    public void setPrelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
    }

    public TipoActo getTipoActo() {
        return tipoActo;
    }

    public Acto tipoActo(TipoActo tipoActo) {
        this.tipoActo = tipoActo;
        return this;
    }

    public void setTipoActo(TipoActo tipoActo) {
        this.tipoActo = tipoActo;
    }
    
    public Integer getVersion() {
		return version;
	}

	public void setVersion(Integer version) {
		this.version = version;
	}

	
    public String getObservacionesMotivo() {
        return observacionesMotivo;
    }

    public void setObservacionesMotivo(String observacionesMotivo) {
        this.observacionesMotivo = observacionesMotivo;
    }

    public Acto observacionesMotivo(String observacionesMotivo){
        this.observacionesMotivo = observacionesMotivo;
        return this;
    }

    public Motivo getMotivo() {
        return this.motivo;
    }

    public void setMotivo (Motivo motivo){
        this.motivo = motivo;
    }

    public Acto motivo(Motivo motivo) {
        this.motivo = motivo;
        return this;
    }


    
    public TipoRechazo getTipoRechazo() {
        return this.tipoRechazo;
    }

    public void setTipoRechazo (TipoRechazo tipoRechazo){
        this.tipoRechazo = tipoRechazo;
    }

    public Acto tipoRechazo (TipoRechazo tipoRechazo) {
        this.tipoRechazo = tipoRechazo;
        return this;
    }
    


    public Date getFechaRechazo() {
        return this.fechaRechazo;
    }

    public void setFechaRechazo(Date fechaRechazo){
        this.fechaRechazo = fechaRechazo;
    }

    public Acto fechaRechazo(Date fechaRechazo) {
        this.fechaRechazo = fechaRechazo;
        return this;
    }

    

    public Boolean getModificable() {
		return modificable;
	}

	public void setModificable(Boolean modificable) {
		this.modificable = modificable;
	}
	
	public Acto modificable(Boolean modificable){
        this.modificable = modificable;
        return this;
    }
	
    public Boolean getPrimerRegistro() {
    	
    	if(primerRegistro != null) {
    		return primerRegistro;
    	}else {
    		return false;
    	}
    	
	}

	public void setPrimerRegistro(Boolean primerRegistro) {
		this.primerRegistro = primerRegistro;
	}
	
	public Boolean getRemplazado() {
		return remplazado;
	}

	public void setRemplazado(Boolean remplazado) {
		this.remplazado = remplazado;
	}
	@JsonIgnore
	public Set<ActoPublicitario> getActosPublicitarosParaActo(){
		return this.actosPublicitariosParaActo;
	}
	
	public Acto actosPublicitariosParaActo(Set<ActoPublicitario> actosPublicitarios) {
		this.actosPublicitariosParaActo=actosPublicitarios;
		return this;
	}
	public Acto addActosPublicitarosParaActo(ActoPublicitario actoPublicitario) {
		this.actosPublicitariosParaActo.add(actoPublicitario);
		actoPublicitario.setActo(this);
		return this;
	}
	
	public Acto removeActosPublicitarosParaActo(ActoPublicitario actoPublicitario) {
    	this.actosPublicitariosParaActo.remove(actoPublicitario);
    	actoPublicitario.setActo(null);
    	return this;
    }
	public void setActosPublicitarosParaActo(Set<ActoPublicitario> actosPublicitaros) {
		this.actosPublicitariosParaActo = actosPublicitaros;
	}
	
	public String getContenidoXml() {
		return contenidoXml;
	}

	public void setContenidoXml(String contenidoXml) {
		this.contenidoXml = contenidoXml;
	}

	public Integer getId_aaj() {
        return id_aaj;
    }
	
	    public void setId_aaj(Integer id_aaj) {
        this.id_aaj = id_aaj;
    }

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Acto acto = (Acto) o;
        if (acto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), acto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Acto{" +
            "id=" + getId() +
            ", hashFila='" + getHashFila() + "'" +
            ", orden='" + getOrden() + "'" +
            ", procesado='" + isProcesado() + "'" +
            ", fechaRegistro='" + getFechaRegistro() + "'" +
            ", tipoActo='" + getTipoActo() + "'" +
            ", version='" + getVersion() + "'" +
            ", vf='" + getVf() + "'" +
            ", primerRegistro='" + getPrimerRegistro() + "'" +
            ", statusActo='" + getStatusActo() + "'" +
            ", copiadoModificado='" + getCopiadoModificado() + "'" +
            "}";
    }
    
    
    @JsonIgnore
	public String getCadenaHash() {
		return "||" + id + "|" + fechaRegistro + "|" + orden + "|" + procesado
				+ "|" + version + "|" + (statusActo.getId()==5L? observacionesMotivo:" ") + "|"
				+ fechaRechazo + "|" + modificable + "|" + statusActo.getId() + "|"
				+ prelacion.getId() + "|" + tipoActo.getId() + "|" + (motivo!=null? motivo.getId():" ") + "||";
    }
    
    //CV
        
    public String getPathArchivado() {
		return pathArchivado;
	}

	public void setPathArchivado(String pathArchivado) {
		this.pathArchivado = pathArchivado;
    }
    
    public Boolean getArchivado() {
		return archivado;
	}

	public void setArchivado(Boolean archivado) {
		this.archivado = archivado;
    }
    public Boolean getClon() {
		return clon;
	}

	public void setClon(Boolean clon) {
		this.clon = clon;
	}

	public Long getIdAsiento() {
		return idAsiento;
	}

	public void setIdAsiento(Long idAsiento) {
		this.idAsiento = idAsiento;
	}
	
	

	public Long getId_entrada() {
		return id_entrada;
	}

	public void setId_entrada(Long id_entrada) {
		this.id_entrada = id_entrada;
	}

	public Boolean getHist() {
		return hist;
	}

	public void setHist(Boolean hist) {
		this.hist = hist;
	}

    public Set<BitacoraActoRechazo> getBitacoraActoRechazos() {
        return bitacoraActoRechazos;
    }

    public void setBitacoraActoRechazos(Set<BitacoraActoRechazo> bitacoraActoRechazos) {
        this.bitacoraActoRechazos = bitacoraActoRechazos;
    }
}
