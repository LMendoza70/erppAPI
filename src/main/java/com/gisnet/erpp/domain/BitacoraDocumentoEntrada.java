package com.gisnet.erpp.domain;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;


@Entity
@Table(name = "bitacora_documento_entrada")
public class BitacoraDocumentoEntrada implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bitacoraDocumentoEntradaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "bitacoraDocumentoEntradaGenerator", sequenceName="bitacora_documento_entrada_seq")
    private Long id;

    @ManyToOne(optional = false)
	@NotNull
	private Prelacion prelacion;

    @Column(name = "fecha")
    @NotNull
    private Date fecha;

    @ManyToOne(optional = false)
    @NotNull
    private Usuario usuario;

    @Size(max = 100)
    @Column(name = "accion", length = 100)
    private String accion;
    
    @ManyToOne
    @NotNull
    private Documento documento;

    @ManyToOne
    @NotNull
    private Archivo archivo;

    @Size(max = 4000)
    @Column(name = "observaciones", length = 4000)
    private String observaciones;

    @Column(name = "es_anexo")
    private Boolean es_anexo;

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

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public Documento getDocumento() {
		return documento;
	}

	public void setDocumento(Documento documento) {
		this.documento = documento;
	}

	public Archivo getArchivo() {
		return archivo;
	}

	public void setArchivo(Archivo archivo) {
		this.archivo = archivo;
	}

	public String getObservaciones() {
		return observaciones;
	}

	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	public Boolean getEs_anexo() {
		return es_anexo;
	}

	public void setEs_anexo(Boolean es_anexo) {
		this.es_anexo = es_anexo;
	}    


}