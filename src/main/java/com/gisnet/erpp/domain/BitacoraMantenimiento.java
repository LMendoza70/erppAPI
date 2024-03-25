package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A Bitacora.
 */
@Entity
@Table(name = "bitacora_mantenimiento")
public class BitacoraMantenimiento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bitacoraMantenimientoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "bitacoraMantenimientoGenerator", sequenceName="bitacora_mantenimiento_seq")
    private Long id;

    @Column(name = "fecha")
    private Date fecha;

    @ManyToOne
    private ActoPredio actoPredio;
    
   
	@Size(max = 100)
    @Column(name = "accion", length = 100)
    private String accion;

    @Size(max = 400)
    @Column(name = "elemento", length = 400)
    private String elemento;

    @ManyToOne
    private Usuario usuario;
    
    @ManyToOne
    private Predio predio;
    
    
    public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public ActoPredio getActoPredio() {
		return actoPredio;
	}

	public void setActoPredio(ActoPredio actoPredio) {
		this.actoPredio = actoPredio;
	}

	public String getAccion() {
		return accion;
	}

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getElemento() {
		return elemento;
	}

	public void setElemento(String elemento) {
		this.elemento = elemento;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Predio getPredio() {
		return predio;
	}

	public void setPredio(Predio predio) {
		this.predio = predio;
	}

    
}
