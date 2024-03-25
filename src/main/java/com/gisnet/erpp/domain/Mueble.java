package com.gisnet.erpp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;


@Entity
@Table(name = "mueble")
public class Mueble implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "muebleGenerator")
    @SequenceGenerator(allocationSize = 1, name = "muebleGenerator", sequenceName="mueble_seq")
    private Long id;

    @Column(name = "especificacion", length = 255)
    private String especificacion;

    @Column(name = "marca", length = 255)
    private String marca;

    @Column(name = "modelo", length = 127)
    private String modelo;

    @Column(name = "num_serie", length = 63)
    private String numSerie;

    @Column(name = "fecha_apertura")
    private Date fechaApertura;

    @Column(name = "folio_calidad")
    private Boolean folioCalidad;

    @Column(name = "hash_fila", length = 80)
    private String hashFila;

    @Column(name = "migrado")
    private Boolean migrado;

    @Column(name = "no_folio", length = 10)
    private Integer noFolio;

    @Column(name = "bloqueado")
    private Boolean bloqueado;

    @Column(name = "cerrado", length = 1)
    private String cerrado;

    @Column(name = "caratula_actualizada")
    private Boolean caratulaActualizada;

    @Column(name = "num_doc_identif", length = 15)
    private String numDocIdentif;

    @Column(name = "fecha_doc_identif")
    private Date fechaDocIdentif;
	
   @Column(name = "primer_registro", length = 1)
    private Integer primerRegistro;
   
   @OneToMany(mappedBy = "mueble")
   @JsonIgnore
   private Set<MuebleAnte> muebleAntesParaMuebles = new HashSet<>();

    @ManyToOne
    private Objeto objeto;

    @ManyToOne
    private TipoDocIdentif tipoDocIdentif;
    
    @ManyToOne
    private Oficina oficina;
    
    @OneToMany(mappedBy = "mueble")
    @JsonIgnore
    private Set<ActoPredio> actoPrediosParaMuebles = new HashSet<>();
    
    @OneToMany(mappedBy = "mueble")
    @JsonIgnore
    private Set<MuebleAnte> muebleParaMuebles = new HashSet<>();
    
    
    public Set<MuebleAnte> getMuebleAntesParaMuebles() {
		return muebleAntesParaMuebles;
	}

	public void setMuebleAntesParaMuebles(Set<MuebleAnte> muebleAntesParaMuebles) {
		this.muebleAntesParaMuebles = muebleAntesParaMuebles;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getEspecificacion() {
        return especificacion;
    }

    public void setEspecificacion(String especificacion) {
        this.especificacion = especificacion;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModel(String modelo) {
        this.modelo = modelo;
    }

    public String getNumSerie() {
        return numSerie;
    }

    public void setNumSerie(String numSerie) {
        this.numSerie = numSerie;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Boolean getFolioCalidad() {
        return folioCalidad;
    }

    public void setFolioCalidad(Boolean folioCalidad) {
        this.folioCalidad = folioCalidad;
    }

    public String getHashFila() {
        return hashFila;
    }

    public void setHashFila(String hashFila) {
        this.hashFila = hashFila;
    }

    public Boolean getMigrado() {
        return migrado;
    }

    public void setMigrado(Boolean migrado) {
        this.migrado = migrado;
    }

    public Integer getNoFolio() {
        return noFolio;
    }

    public void setNoFolio(Integer noFolio) {
        this.noFolio = noFolio;
    }

    public Boolean getBloqueado() {
        return bloqueado;
    }

    public void setBloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public String getCerrado() {
        return cerrado;
    }

    public void setCerrado(String cerrado) {
        this.cerrado = cerrado;
    }

    public Boolean getCaratulaActualizada() {
        return caratulaActualizada;
    }

    public void setCaratulaActualizada(Boolean caratulaActualizada) {
        this.caratulaActualizada = caratulaActualizada;
    }

    public String getNumDocIdentif() {
        return numDocIdentif;
    }

    public void setNumDocIdentif(String numDocIdentif) {
        this.numDocIdentif = numDocIdentif;
    }

    public Date getFechaDocIdentif() {
        return fechaDocIdentif;
    }

    public void setFechaDocIdentif(Date fechaDocIdentif) {
        this.fechaDocIdentif = fechaDocIdentif;
    }

    public Objeto getObjeto() {
        return objeto;
    }

    public void setObjeto(Objeto objeto) {
        this.objeto = objeto;
    }

    public TipoDocIdentif getTipoDocIdentif() {
        return tipoDocIdentif;
    }

    public void setTipoDocIdentif(TipoDocIdentif tipoDocIdentif) {
        this.tipoDocIdentif = tipoDocIdentif;
    }
    
    
    
    public Set<ActoPredio> getActoPrediosParaMuebles() {
        return actoPrediosParaMuebles;
    }
	
    public Integer getPrimerRegistro() {
        return primerRegistro;
    }

    public void setPrimerRegistro(Integer primerRegistro) {
        this.primerRegistro = primerRegistro;
    }
    

    public Oficina getOficina() {
		return oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

	@Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Mueble{" +
            "id=" + getId() +
            "}";
    }
    
    public String getDescripcion(){
    	StringBuilder sb = new StringBuilder();
    	sb.append(" Especificacion: ").append(especificacion);
    	if (marca!=null){
    		sb.append(", Marca: ").append(marca);
    	}
    	if (numSerie!=null){
    		sb.append(", Num. de Serie: ").append(numSerie);
    	}    	
    	return sb.toString();    	
    }

	public Set<MuebleAnte> getMuebleParaMuebles() {
		return muebleParaMuebles;
	}

	public void setMuebleParaMuebles(Set<MuebleAnte> muebleParaMuebles) {
		this.muebleParaMuebles = muebleParaMuebles;
	}
}
