package com.gisnet.erpp.domain;



import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

/**
 * @author Juan Duarte
 *
 */
@Entity
@Table(name = "predio_bitacora_colindancia")
public class PredioBitacoraColindancia  implements Serializable {

		private static final long serialVersionUID = 1L;

		@Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "predioBitacoraColindanciaGenerator")
	    @SequenceGenerator(allocationSize = 1, name = "predioBitacoraColindanciaGenerator", sequenceName="bitacora_colindancia_seq")
	    private Long id;
	
	
	    @Column(name = "nombre")
	    private String nombre;

	    
	    
	    @ManyToOne
	    private TipoColin tipoColin;

	    @ManyToOne
	    private Vialidad vialidad;

	    

	    @ManyToOne(optional = false)
	    @NotNull
	    private Orientacion orientacion;
	
	    
	    @ManyToOne
	    private PredioBitacora predioBitacora;

		public PredioBitacora getPredioBitacora() {
			return predioBitacora;
		}

		public void setPredioBitacora(PredioBitacora predioBitacora) {
			this.predioBitacora = predioBitacora;
		}

		public Long getId() {
			return id;
		}

		public String getNombre() {
			return nombre;
		}


		public TipoColin getTipoColin() {
			return tipoColin;
		}

		public Vialidad getVialidad() {
			return vialidad;
		}


		public Orientacion getOrientacion() {
			return orientacion;
		}

		public void setId(Long id) {
		this.id = id;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

		

		public void setTipoColin(TipoColin tipoColin) {
			this.tipoColin = tipoColin;
		}

		public void setVialidad(Vialidad vialidad) {
			this.vialidad = vialidad;
		}

		public void setOrientacion(Orientacion orientacion) {
			this.orientacion = orientacion;
		}
	
	
}
