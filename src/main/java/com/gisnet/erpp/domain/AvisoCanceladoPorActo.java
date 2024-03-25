package com.gisnet.erpp.domain;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

@Entity
@Table(name = "avisos_cancelado_porActo")
public class AvisoCanceladoPorActo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "avisoCanceladoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "avisoCanceladoGenerator", sequenceName="avisosavisoCancelados_seq")
    private Long id;

    @Column
	private Date fechaCan;

	@ManyToOne
	private TipoActo tipoActo;

	@ManyToOne
	private Acto acto;

	@ManyToOne
	private Prelacion prelacion;
	
	@ManyToOne
	private Aviso aviso;

	@ManyToOne
	private Usuario usuario;

	@Column
	private Boolean cancelacionActiva;
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}


	public Date getFechaCan() {
		return fechaCan;
	}

	public void setFechaCan(Date fechaCan) {
		this.fechaCan = fechaCan;
	}

	public Aviso getAviso() {
		return aviso;
	}

	public void setAviso(Aviso aviso) {
		this.aviso = aviso;
	}

	public TipoActo getTipoActo() {
		return tipoActo;
	}

	public void setTipoActo(TipoActo tipoActo) {
		this.tipoActo = tipoActo;
	}

	public Acto getActo() {
		return acto;
	}

	public void setActo(Acto acto) {
		this.acto = acto;
	}

	public Prelacion getPrelacion() {
		return prelacion;
	}

	public void setPrelacion(Prelacion prelacion) {
		this.prelacion = prelacion;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Boolean getCancelacionActiva() {
		return cancelacionActiva;
	}

	public void setCancelacionActiva(Boolean cancelacionActiva) {
		this.cancelacionActiva = cancelacionActiva;
	}

}
