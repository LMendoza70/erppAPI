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
import javax.validation.constraints.Size;

@Entity
@Table(name = "bitacora_gestor")
public class BitacoraGestor implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2585100233805469856L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bitacoraGestorGenerator")
	@SequenceGenerator(allocationSize = 1, name = "bitacoraGestorGenerator", sequenceName = "bitacora_gestor_seq")
	private Long id;

	@ManyToOne
	private Usuario usuarioGestor;

	@Column(name = "fecha")
	private Date fecha;

	@Size(max = 50)
	@Column(name = "codigo_libro", length = 50)
	private String codigoLibro;

	@Column(name = "libro")
	private String libro;

	@Column(name = "libro_bis")
	private String libroBis;
	
	@Column(name = "anio")
	private Integer anio;
	
	@Column(name = "volumen")
	private String volumen;

	//@Column(name = "tipo_doc")
	//private Boolean tipoDoc;

	@Size(max = 100)
	@Column(name = "documento", length = 100)
	private String documento;

	//@Size(max = 100)
	//@Column(name = "documento_bis", length = 100)
	//private String documentoBis;

	@Column(name = "no_folio")
	private Integer noFolio;

	@ManyToOne
	private Oficina oficina;

	@ManyToOne
	private Seccion seccion;
	
    //@ManyToOne
    //private NombreLibro nombreLibro;
    
    //@ManyToOne
    //private TipoLibro tipoLibro;


	@Column(name = "pagina_ini")
	private Integer paginaIni;

	@Column(name = "paginaFin")
	private Integer paginaFin;

	@ManyToOne
	private TipoFolio tipoFolio;
	
	/*public NombreLibro getNombreLibro() {
		return nombreLibro;
	}*/

	/*public void setNombreLibro(NombreLibro nombreLibro) {
		this.nombreLibro = nombreLibro;
	}*/

	/*public TipoLibro getTipoLibro() {
		return tipoLibro;
	}*/

	/*public void setTipoLibro(TipoLibro tipoLibro) {
		this.tipoLibro = tipoLibro;
	}*/

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Usuario getUsuarioGestor() {
		return usuarioGestor;
	}

	public void setUsuarioGestor(Usuario usuarioGestor) {
		this.usuarioGestor = usuarioGestor;
	}

	public Date getFecha() {
		return fecha;
	}

	public void setFecha(Date fecha) {
		this.fecha = fecha;
	}

	public String getCodigoLibro() {
		return codigoLibro;
	}

	public void setCodigoLibro(String codigoLibro) {
		this.codigoLibro = codigoLibro;
	}

	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public Seccion getSeccion() {
		return seccion;
	}

	public void setSeccion(Seccion seccion) {
		this.seccion = seccion;
	}

	public Oficina getOficina() {
		return oficina;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

	public TipoFolio getTipoFolio() {
		return tipoFolio;
	}

	public void setTipoFolio(TipoFolio tipoFolio) {
		this.tipoFolio = tipoFolio;
	}

	public String getLibroBis() {
		return libroBis;
	}

	public void setLibroBis(String libroBis) {
		this.libroBis = libroBis;
	}

	/*public Boolean getTipoDoc() {
		return tipoDoc;
	}*/

	/*public void setTipoDoc(Boolean tipoDoc) {
		this.tipoDoc = tipoDoc;
	}*/

	/*public String getDocumentoBis() {
		return documentoBis;
	}*/

	/*public void setDocumentoBis(String documentoBis) {
		this.documentoBis = documentoBis;
	}*/

	public Integer getNoFolio() {
		return noFolio;
	}

	public void setNoFolio(Integer noFolio) {
		this.noFolio = noFolio;
	}

	public Integer getPaginaIni() {
		return paginaIni;
	}

	public void setPaginaIni(Integer paginaIni) {
		this.paginaIni = paginaIni;
	}

	public Integer getPaginaFin() {
		return paginaFin;
	}

	public void setPaginaFin(Integer paginaFin) {
		this.paginaFin = paginaFin;
	}

	public String getLibro() {
		return libro;
	}

	public void setLibro(String libro) {
		this.libro = libro;
	}
	
	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}
	
	public String getVolumen() {
		return volumen;
	}

	public void setVolumen(String volumen) {
		this.volumen = volumen;
	}	

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("  Libro: ").append(libro);
		sb.append(", Libro Bis: ").append(libroBis);
		sb.append(", Sección:").append(getSeccion() == null ? "" : getSeccion().getNombre());
		sb.append(", Oficina:").append(getOficina() == null ? "" : getOficina().getNombre());
		sb.append(", año:").append(getAnio());
		sb.append(", volumen:").append(getVolumen());
		//sb.append(", Tipo Doc:").append(getTipoDoc());
		sb.append("  Documento: ").append(documento);
		//sb.append(", documentoBis: ").append(documentoBis);
		sb.append("  paginaIni: ").append(paginaIni);
		sb.append(", paginaFin: ").append(paginaFin);
		sb.append("  noFolio: ").append(noFolio);
		sb.append(", tipoFolio: ").append(tipoFolio);

		return sb.toString();
	}

}
