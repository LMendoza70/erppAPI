package com.gisnet.erpp.domain;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
//JADV-SUSPENSION
@Entity
@Table(name = "dias_solv_susp")
public class DiasSolventacionSuspension {

	private static final long serialVersionUID = 1L;

	 	@Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dias_solv_suspGenerator")
	    @SequenceGenerator(allocationSize = 1, name = "dias_solv_suspGenerator", sequenceName="dias_solv_susp_seq")
	    private Long id;

	 	@ManyToOne(optional = false)
	    @NotNull
	    private Suspension suspension;
	 	
	 	@Column(name = "dias_adicionales")
	 	private Long diasAdicionales;
	 	
	 	@Column(name = "fecha_modificacion")
	    private Date fechaModificacion;
	 	
	 	@ManyToOne(optional = false)
	    @NotNull
	    private Usuario usuario;

	 	@Column(name = "observaciones")
	    private String observaciones;
	 	
	 	
		public String getObservaciones() {
			return observaciones;
		}

		public void setObservaciones(String observaciones) {
			this.observaciones = observaciones;
		}

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Suspension getSuspension() {
			return suspension;
		}

		public void setSuspension(Suspension suspension) {
			this.suspension = suspension;
		}

		public Long getDiasAdicionales() {
			return diasAdicionales;
		}

		public void setDiasAdicionales(Long diasAdicionales) {
			this.diasAdicionales = diasAdicionales;
		}

		public Date getFechaModificacion() {
			return fechaModificacion;
		}

		public void setFechaModificacion(Date fechaModificacion) {
			this.fechaModificacion = fechaModificacion;
		}

		public Usuario getUsuario() {
			return usuario;
		}

		public void setUsuario(Usuario usuario) {
			this.usuario = usuario;
		}
	 	
	 	
		
	
	    
}
