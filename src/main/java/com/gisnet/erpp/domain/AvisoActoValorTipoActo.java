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
@Table(name = "AVISOS_ACTO_VALOR_TIPO_ACTO")
public class AvisoActoValorTipoActo implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "avisoActoValorTipoActoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "avisoActoValorTipoActoGenerator", sequenceName="avisos_act_val_t_a_seq")
    private Long id;

	@ManyToOne
	private TipoActo tipoActo;

	@ManyToOne
	private CampoValores campoValores;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public TipoActo getTipoActo() {
		return tipoActo;
	}

	public void setTipoActo(TipoActo tipoActo) {
		this.tipoActo = tipoActo;
	}

	public CampoValores getCampoValores() {
		return campoValores;
	}

	public void setCampoValores(CampoValores campoValores) {
		this.campoValores = campoValores;
	}

}
