package com.gisnet.erpp.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
@Table(name = "prelacion_boleta")
public class PrelacionBoleta implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prelacionBoletaGenerator")
	@SequenceGenerator(allocationSize = 1, name = "prelacionBoletaGenerator", sequenceName = "prelacion_boleta_seq")
	private Long id;

	@NotNull
	@Column(name = "datos",  nullable = false)
	private String datos;

	@ManyToOne(optional = false)
	@NotNull
	private Prelacion prelacion;

	@ManyToOne(optional = true)
	private TipoBoleta tipoBoleta;
	
	@Column(name="pagina")
	private Integer pagina;

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

	public String getDatos() {
		return datos;
	}

	public void setDatos(String datos) {
		this.datos = datos;
	}

	public TipoBoleta getTipoBoleta() {
		return tipoBoleta;
	}

	public void setTipoBoleta(TipoBoleta tipoBoleta) {
		this.tipoBoleta = tipoBoleta;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "PrelacionBoleta [id=" + id + ", datos=" + datos + ", prelacion=" + prelacion + ", tipoBoleta="
				+ tipoBoleta + "]";
	}

	public Integer getPagina() {
		return pagina;
	}

	public void setPagina(Integer pagina) {
		this.pagina = pagina;
	}

	
}
