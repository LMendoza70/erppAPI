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
@Table(name = "folio_seccion_auxiliar")
public class FolioSeccionAuxiliar implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "folioSeccionAuxiliarGenerator")
    @SequenceGenerator(allocationSize = 1, name = "folioSeccionAuxiliarGenerator", sequenceName="folio_seccion_auxiliar_seq")
    private Long id;

    @Column(name = "no_folio", length = 10)
    private Integer noFolio;

    @Column(name = "fecha_apertura")
    private Date fechaApertura;
	
    @Column(name = "primer_registro", length = 1)
    private Integer primerRegistro;
    
    @Size(max = 80)
    @Column(name = "hash_fila", length = 80)
    private String hashFila;
    
    @Column(name = "bloqueado")
    private Boolean bloqueado;
    
    @ManyToOne
    private Oficina oficina;
    
    @Column(name = "caratula_actualizada")
    private Boolean caratulaActualizada;
    
    @OneToMany(mappedBy = "folioSeccionAuxiliar")
    @JsonIgnore
    private Set<ActoPredio> actoPrediosParaFolioSeccionAuxiliares = new HashSet<>();
    
    @OneToMany(mappedBy = "folioSeccionAuxiliar")
    @JsonIgnore
    private Set<AuxiliarAnte> auxiliarAnteParaFolioSeccionAuxiliares = new HashSet<>();
    
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

    public Integer getNoFolio() {
        return noFolio;
    }

    public void setNoFolio(Integer noFolio) {
        this.noFolio = noFolio;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }


    public Set<ActoPredio> getActoPrediosParaFolioSeccionAuxiliares() {
        return actoPrediosParaFolioSeccionAuxiliares;
    }	
    
    public Oficina getOficina(){
        return oficina;
    }
    
    public void setOficina(Oficina oficina){
        this.oficina = oficina;
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
        return "FolioSeccionAuxiliar{" +
            "id=" + getId() +
            "}";
    }

	public Set<AuxiliarAnte> getAuxiliarAnteParaFolioSeccionAuxiliares() {
		return auxiliarAnteParaFolioSeccionAuxiliares;
	}

	public void setAuxiliarAnteParaFolioSeccionAuxiliares(Set<AuxiliarAnte> auxiliarAnteParaFolioSeccionAuxiliares) {
		this.auxiliarAnteParaFolioSeccionAuxiliares = auxiliarAnteParaFolioSeccionAuxiliares;
	}

	public void setActoPrediosParaFolioSeccionAuxiliares(Set<ActoPredio> actoPrediosParaFolioSeccionAuxiliares) {
		this.actoPrediosParaFolioSeccionAuxiliares = actoPrediosParaFolioSeccionAuxiliares;
	}

	public Boolean getCaratulaActualizada() {
		return caratulaActualizada;
	}

	public void setCaratulaActualizada(Boolean caratulaActualizada) {
		this.caratulaActualizada = caratulaActualizada;
	}
	
    public String getDescripcion(){
    	StringBuilder sb = new StringBuilder();
    	sb.append(" Fecha Apertura: ").append(fechaApertura);
    	
    	return sb.toString();    	
    }

	public Boolean getBloqueado() {
		return bloqueado;
	}

	public void setBloqueado(Boolean bloqueado) {
		this.bloqueado = bloqueado;
	}

}
