package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.gisnet.erpp.vo.PrelacionVO;

/**
 * A mantenimiento_notario.
 */
@Entity
@Table(name = "mantenimiento_notario")
public class MantenimientoNotario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mantenimientoNotarioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "mantenimientoNotarioGenerator", sequenceName="mant_notario_seq")
    private Long id;
    
    @ManyToOne
    private Usuario usuario;
    
    @ManyToOne
    private Notario notario;
    
    @Size(max = 1000)
    @Column(name = "motivo", length = 1000)
    private String motivo;
    
    @Column(name = "fecha")
    private Date fecha;
    
    @Size(max = 50)
    @Column(name = "movimiento", length = 50)
    private String movimiento;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Notario getNotario() {
		return notario;
	}

	public void setNotario(Notario notario) {
		this.notario = notario;
	}

	public String getMotivo() {
		return motivo;
	}

	public void setMotivo(String motivo) {
		this.motivo = motivo;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getMovimiento() {
		return movimiento;
	}

	public void setMovimiento(String movimiento) {
		this.movimiento = movimiento;
	}

}
