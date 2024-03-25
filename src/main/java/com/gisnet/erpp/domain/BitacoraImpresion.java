package com.gisnet.erpp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "bitacora_impresion")
public class BitacoraImpresion implements Serializable {
	
	 private static final long serialVersionUID = 1L;


	    @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bitacoraImpresionGenerator")
	    @SequenceGenerator(allocationSize = 1, name = "bitacoraImpresionGenerator", sequenceName="bitacora_impresion_seq")
	    private Long id;

	    @ManyToOne
	    private Prelacion prelacion;

	    @Column(name = "fecha_impresion")
	    private Date fechaImpresion;

	    @ManyToOne
	    private Usuario usuarioLogeado;

	    @ManyToOne(optional = false)
	    @NotNull
	    private StatusImpresion status_impresion;

	    @Column(name = "version", length = 3, nullable = false)
	    private Integer version;

	    @Column(name = "inactivo")
	    private Boolean inactivo;

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

		public Date getFechaImpresion() {
			return fechaImpresion;
		}

		public void setFechaImpresion(Date fechaImpresion) {
			this.fechaImpresion = fechaImpresion;
		}

		public Usuario getUsuarioLogeado() {
			return usuarioLogeado;
		}

		public void setUsuarioLogeado(Usuario usuarioLogeado) {
			this.usuarioLogeado = usuarioLogeado;
		}

		public StatusImpresion getStatus_impresion() {
			return status_impresion;
		}

		public void setStatus_impresion(StatusImpresion status_impresion) {
			this.status_impresion = status_impresion;
		}

		public Integer getVersion() {
			return version;
		}

		public void setVersion(Integer version) {
			this.version = version;
		}

		public Boolean getInactivo() {
			return inactivo;
		}

		public void setInactivo(Boolean inactivo) {
			this.inactivo = inactivo;
		}

	    
}
