package com.gisnet.erpp.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "prelacion_contratante")
public class PrelacionContratante {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prelacionContratanteGenerator")
    @SequenceGenerator(allocationSize = 1, name = "prelacionContratanteGenerator", sequenceName="prelacion_contratante_seq")
    private Long id;

    @ManyToOne
    private Prelacion prelacion;

    @Column(name = "nombre", length = 255)
    private String nombre;

    @Column(name = "paterno", length = 255)
    private String paterno;

    @Column(name = "materno", length = 255)
    private String materno;

    @ManyToOne
    private Acto acto;

    @ManyToOne
    private TipoActo tipoActo;

    @ManyToOne
    private Predio predio;

    @Column(name = "orden")
    private Integer orden;

    @Column(name = "dd", length = 48)
    private String dd;

    @Column(name = "uv", length = 48)
    private String uv;

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

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getPaterno() {
		return paterno;
	}

	public void setPaterno(String paterno) {
		this.paterno = paterno;
	}

	public String getMaterno() {
		return materno;
	}

	public void setMaterno(String materno) {
		this.materno = materno;
	}

	public Acto getActo() {
		return acto;
	}

	public void setActo(Acto acto) {
		this.acto = acto;
	}

	public TipoActo getTipoActo() {
		return tipoActo;
	}

	public void setTipoActo(TipoActo tipoActo) {
		this.tipoActo = tipoActo;
	}

	public Predio getPredio() {
		return predio;
	}

	public void setPredio(Predio predio) {
		this.predio = predio;
	}

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	public String getDd() {
		return dd;
	}

	public void setDd(String dd) {
		this.dd = dd;
	}

	public String getUv() {
		return uv;
	}

	public void setUv(String uv) {
		this.uv = uv;
	}

	@JsonIgnore
    public String getNombreCompleto(){
		StringBuilder sb = new StringBuilder();
		if (getNombre()!=null){
			sb.append(getNombre());
		} 
		if (getPaterno()!=null){
			sb.append(" ").append(getPaterno());
		}
		if (getMaterno()!=null){
			sb.append(" ").append(getMaterno());
		}
		return sb.toString().toUpperCase();
	}
}
