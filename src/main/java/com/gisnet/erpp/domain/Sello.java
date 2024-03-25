package com.gisnet.erpp.domain;

import java.io.Serializable;
import javax.validation.constraints.*;
import javax.persistence.*;
import java.math.BigDecimal;
import java.sql.Timestamp;


/**
 * The persistent class for the SELLOS database table.
 * 
 */
@Entity
@Table(name = "sellos")
public class Sello implements Serializable {
	private static final long serialVersionUID = 1L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "selloGenerator")
    @SequenceGenerator(allocationSize = 1, name = "selloGenerator", sequenceName="sellos_seq")
    private Long id;

	private String activo;

	@Column(name = "detalle", length = 1000)
	private String detalle;

    @Size(max = 100)
    @Column(name = "documento", length = 100)
    private String documento;

    @Size(max = 100)
    @Column(name = "documento_bis", length = 100)
    private String documentoBis;

	@Column(name="FECHA_CAPTURA")
	private Timestamp fechaCaptura;

	@Column(name="FECHA_CIERRE")
	private Timestamp fechaCierre;

	@Column(name="FOLIO")
	private BigDecimal folio;

	@Column(name="NUM_REG_PUBLICO")
	private BigDecimal numRegPublico;

	@Column(name="PAGINA_FINAL")
	private String paginaFinal;

	@Column(name="PAGINA_INICIAL")
	private String paginaInicial;

	@Column(name="TIPO_SELLO")
	private Long tipoSello;
	
	@ManyToOne
	private Libro libro;
	
    @ManyToOne
    private Usuario usuario;

	public Sello() {
	}

	public String getActivo() {
		return this.activo;
	}

	public void setActivo(String activo) {
		this.activo = activo;
	}

	public String getDetalle() {
		return this.detalle;
	}

	public void setDetalle(String detalle) {
		this.detalle = detalle;
	}

	public String getDocumento() {
		return this.documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public Timestamp getFechaCaptura() {
		return this.fechaCaptura;
	}

	public void setFechaCaptura(Timestamp fechaCaptura) {
		this.fechaCaptura = fechaCaptura;
	}

	public Timestamp getFechaCierre() {
		return this.fechaCierre;
	}

	public void setFechaCierre(Timestamp fechaCierre) {
		this.fechaCierre = fechaCierre;
	}

	public BigDecimal getFolio() {
		return this.folio;
	}

	public void setFolio(BigDecimal folio) {
		this.folio = folio;
	}

	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public BigDecimal getNumRegPublico() {
		return this.numRegPublico;
	}

	public void setNumRegPublico(BigDecimal numRegPublico) {
		this.numRegPublico = numRegPublico;
	}

	public String getPaginaFinal() {
		return this.paginaFinal;
	}

	public void setPaginaFinal(String paginaFinal) {
		this.paginaFinal = paginaFinal;
	}

	public String getPaginaInicial() {
		return this.paginaInicial;
	}

	public void setPaginaInicial(String paginaInicial) {
		this.paginaInicial = paginaInicial;
	}

	public Libro getLibro() {
		return this.libro;
	}

	public void setLibro(Libro libro) {
		this.libro = libro;
	}

	public Long getTipoSello() {
		return tipoSello;
	}

	public void setTipoSello(Long tipoSello) {
		this.tipoSello = tipoSello;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getDocumentoBis() {
		return documentoBis;
	}

	public void setDocumentoBis(String documentoBis) {
		this.documentoBis = documentoBis;
	}
	
	

}
