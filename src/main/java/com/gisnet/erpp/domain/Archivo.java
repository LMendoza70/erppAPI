package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import java.util.Date;

/**
 * A Archivo.
 */
@Entity
@Table(name = "archivo")
public class Archivo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "archivoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "archivoGenerator", sequenceName="archivo_seq")
    private Long id;

    @Size(max = 150)
    @Column(name = "nombre", length = 150)
    private String nombre;

    @Size(max = 100)
    @Column(name = "ruta", length = 100)
    private String ruta;

    @Size(max = 100)
    @Column(name = "tipo", length = 100)
    private String tipo;


    @Size(max = 100)
    @Column(name = "hash", length = 100)
    private String hash;
    
    
    

	@Column(name = "reingreso")
    private Boolean reingreso;


    @OneToMany(mappedBy = "archivo")
    @JsonIgnore
    private Set<ActoFirma> actoFirmasParaArchivos = new HashSet<>();

    @OneToMany(mappedBy = "archivo")
    @JsonIgnore
    private Set<ActoRequisito> actoRequisitosParaArchivos = new HashSet<>();

    @OneToMany(mappedBy = "archivo")
    @JsonIgnore
    private Set<Documento> documentosParaArchivos = new HashSet<>();
    
    
    @OneToMany(mappedBy = "archivo")
    @JsonIgnore
    private Set<DocumentoArchivo> documentosArchivoParaArchivos = new HashSet<>();


  
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Archivo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getRuta() {
        return ruta;
    }

    public Archivo ruta(String ruta) {
        this.ruta = ruta;
        return this;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }

    public String getTipo() {
        return tipo;
    }

    public Archivo tipo(String tipo) {
        this.tipo = tipo;
        return this;
    }
    
    public Boolean getReingreso() {
		return reingreso;
	}

	public void setReingreso(Boolean reingreso) {
		this.reingreso = reingreso;
	}
    public void setTipo(String tipo) {
        this.tipo = tipo;
    }


    public String getHash() {
		return hash;
	}


	public void setHash(String hash) {
		this.hash = hash;
	}




    public Set<ActoFirma> getActoFirmasParaArchivos() {
        return actoFirmasParaArchivos;
    }

    public Archivo actoFirmasParaArchivos(Set<ActoFirma> actoFirmas) {
        this.actoFirmasParaArchivos = actoFirmas;
        return this;
    }

    public Archivo addActoFirmasParaArchivo(ActoFirma actoFirma) {
        this.actoFirmasParaArchivos.add(actoFirma);
        actoFirma.setArchivo(this);
        return this;
    }

    public Archivo removeActoFirmasParaArchivo(ActoFirma actoFirma) {
        this.actoFirmasParaArchivos.remove(actoFirma);
        actoFirma.setArchivo(null);
        return this;
    }

    public void setActoFirmasParaArchivos(Set<ActoFirma> actoFirmas) {
        this.actoFirmasParaArchivos = actoFirmas;
    }

    public Set<ActoRequisito> getActoRequisitosParaArchivos() {
        return actoRequisitosParaArchivos;
    }

    public Archivo actoRequisitosParaArchivos(Set<ActoRequisito> actoRequisitos) {
        this.actoRequisitosParaArchivos = actoRequisitos;
        return this;
    }

    public Archivo addActoRequisitosParaArchivo(ActoRequisito actoRequisito) {
        this.actoRequisitosParaArchivos.add(actoRequisito);
        actoRequisito.setArchivo(this);
        return this;
    }

    public Archivo removeActoRequisitosParaArchivo(ActoRequisito actoRequisito) {
        this.actoRequisitosParaArchivos.remove(actoRequisito);
        actoRequisito.setArchivo(null);
        return this;
    }

    public void setActoRequisitosParaArchivos(Set<ActoRequisito> actoRequisitos) {
        this.actoRequisitosParaArchivos = actoRequisitos;
    }
    public Set<Documento> getDocumentosParaArchivos() {
        return documentosParaArchivos;
    }

    public Archivo documentosParaArchivos(Set<Documento> documentos) {
        this.documentosParaArchivos = documentos;
        return this;
    }

    public Archivo addDocumentosParaArchivo(Documento documento) {
        this.documentosParaArchivos.add(documento);
        documento.setArchivo(this);
        return this;
    }

    public void addDocumentoParaArchivo(Documento documento) {
        this.documentosParaArchivos.add(documento);
    }

    public Archivo removeDocumentosParaArchivo(Documento documento) {
        this.documentosParaArchivos.remove(documento);
        documento.setArchivo(null);
        return this;
    }
    
    public void setDocumentosParaArchivos(Set<Documento> documentos) {
        this.documentosParaArchivos = documentos;
    }
    
    public Set<DocumentoArchivo> getDocumentosArchivoParaArchivos() {
  		return documentosArchivoParaArchivos;
  	}

  	public void setDocumentosArchivoParaArchivos(Set<DocumentoArchivo> documentosArchivoParaArchivos) {
  		this.documentosArchivoParaArchivos = documentosArchivoParaArchivos;
  	}


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Archivo archivo = (Archivo) o;
        if (archivo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), archivo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Archivo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", ruta='" + getRuta() + "'" +
            ", tipo='" + getTipo() + "'" +
            "}";
    }
}
