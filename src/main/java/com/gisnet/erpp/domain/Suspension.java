package com.gisnet.erpp.domain;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
//JADV-SUSPENSION
@Entity
@Table(name = "suspension")
public class Suspension {

	private static final long serialVersionUID = 1L;

	 	@Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "suspensionGenerator")
	    @SequenceGenerator(allocationSize = 1, name = "suspensionGenerator", sequenceName="suspension_seq")
	    private Long id;

	 	@ManyToOne(optional = false)
		@NotNull
		@JsonIgnore
	    private Prelacion prelacion;
	 	
	 	@Column(name = "estatus_suspencion")
	 	private Long estatusSuspension;
	 	
	 	@Column(name = "fecha_suspencion")
	    private Date fechaSuspension;
	 	
	 	@Column(name = "fecha_calculo")
	    private Date fechaCalculo;

		@Column(name = "por_autoridad")
	    private Boolean porAutoridad;
	    
	    @Column(name = "dias_adicionales")
	 	private Long diasAdicionales;
		 
		@OneToOne(mappedBy = "suspension")	      
		@JsonIgnore
		private SuspensionFirma suspensionFirma;
	    
	    public Date getFechaCalculo() {
			return fechaCalculo;
		}

		public void setFechaCalculo(Date fechaCalculo) {
			this.fechaCalculo = fechaCalculo;
		}
		
	    public Long getDiasAdicionales() {
			return diasAdicionales;
		}

		public void setDiasAdicionales(Long diasAdicionales) {
			this.diasAdicionales = diasAdicionales;
		}

		public Long getId() {
			return id;
		}

		public Boolean getPorAutoridad() {
			return porAutoridad;
		}

		public void setPorAutoridad(Boolean porAutoridad) {
			this.porAutoridad = porAutoridad;
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

		public Long getEstatusSuspension() {
			return estatusSuspension;
		}

		public void setEstatusSuspension(Long estatusSuspension) {
			this.estatusSuspension = estatusSuspension;
		}

		public Date getFechaSuspension() {
			return fechaSuspension;
		}

		public void setFechaSuspension(Date fechaSuspension) {
			this.fechaSuspension = fechaSuspension;
		}

		public SuspensionFirma getSuspensionFirma() {
			return suspensionFirma;
		}

		public void setSuspensionFirma(SuspensionFirma suspensionFirma) {
			this.suspensionFirma = suspensionFirma;
		}

		@JsonIgnore
		public String getCadenaHash() {
		return "||" + id + "|" + estatusSuspension + "|" + fechaSuspension + "|"
				+ prelacion.getId() + "|" + porAutoridad + "|" + diasAdicionales
				+ "|" + fechaCalculo
				+ "||";
		}	
	    
}
