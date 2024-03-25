package com.gisnet.erpp.domain;

import java.io.Serializable;

import javax.persistence.*;
import javax.validation.constraints.*;

@Entity
@Table(name = "status_impresion")
public class StatusImpresion implements Serializable{
	
	private static final long serialVersionUID = 1L;


	    @Id
	    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statusImpresionGenerator")
	    @SequenceGenerator(allocationSize = 1, name = "statusImpresionGenerator", sequenceName="status_impresion_seq")
	    private Long id;

	    @Size(max = 1000)
	    @Column(name = "nombre", length = 1000)
	    private String nombre;

		public Long getId() {
			return id;
		}

		public void setId(Long id) {
			this.id = id;
		}

		public String getNombre() {
			return nombre;
		}

		public void setNombre(String nombre) {
			this.nombre = nombre;
		}

	    
}
