package com.gisnet.erpp.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name="busqueda_resultado_acto")
public class BusquedaResultadoActo implements Serializable  {
	 private static final long serialVersionUID = 1L;

	    @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "busquedaResultadoActoGenerator")
	    @SequenceGenerator(allocationSize = 1, name = "busquedaResultadoActoGenerator", sequenceName="busqueda_resultado_acto_seq")
	    private Long id;
	    
	    @ManyToOne
	    private Acto acto;
	    
	    @ManyToOne
	    private BusquedaResultado busquedaResultado;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public Acto getActo() {
			return acto;
		}

		public void setActo(Acto acto) {
			this.acto = acto;
		}

		public BusquedaResultado getBusquedaResultado() {
			return busquedaResultado;
		}

		public void setBusquedaResultado(BusquedaResultado busquedaResultado) {
			this.busquedaResultado = busquedaResultado;
		}
}
