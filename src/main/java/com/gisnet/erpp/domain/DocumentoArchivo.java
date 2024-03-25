package com.gisnet.erpp.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.Size;

@Entity
@Table(name = "documento_archivo")
public class DocumentoArchivo implements Serializable  {
	private static final long serialVersionUID = 1L;
	
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "documentoArchivoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "documentoArchivoGenerator", sequenceName="documento_archivo_seq")
    private Long id;
    
    @ManyToOne
    private Archivo archivo;
    
    @ManyToOne
    private Documento documento;
    
    

	@Size(max = 350)
    @Column(name = "observaciones",length = 350)
    private String observaciones;

    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Archivo getArchivo() {
		return archivo;
	}

	public void setArchivo(Archivo archivo) {
		this.archivo = archivo;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}


	public String getObservaciones() {
			return observaciones;
	}

	public void setObservaciones(String observaciones) {
	 this.observaciones = observaciones;
    }
    
}
