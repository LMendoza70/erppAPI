package com.gisnet.erpp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "predio_bitacora")
public class PredioBitacora implements Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "predioBitacoraGenerator")
	@SequenceGenerator(allocationSize = 1, name = "predioBitacoraGenerator", sequenceName = "predio_bitacora_seq")
	private Long id;

	@Column(name = "fecha_act")
	private Date fechaAct;

	@Size(max = 40)
	@Column(name = "num_int", length = 40)
	private String numInt;

	@Column(name = "edificio")
	private String edificio;

	@Size(max = 64)
	@Column(name = "localidad_sector", length = 64)
	private String localidadSector;

	@Size(max = 50)
	@Column(name = "no_lote", length = 50)
	private String noLote;

	@Column(name = "fraccion", length = 20)
	private String fraccion;

	@Size(max = 50)
	@Column(name = "manzana", length = 50)
	private String manzana;

	@Column(name = "macro_manzana", length = 20)
	private String macroManzana;

	@Size(max = 2000)
	@Column(name = "vialidad", length = 2000)
	private String vialidad;

	@Size(max = 40)
	@Column(name = "NUM_EXT", length = 40)
	private String numExt;

	@Size(max = 2000)
	@Column(name = "vialidad2", length = 2000)
	private String vialidad2;

	@Size(max = 40)
	@Column(name = "NUM_EXT2", length = 40)
	private String numExt2;

	@Size(max = 2000)
	@Column(name = "asentamiento", length = 2000)
	private String asentamiento;

	@Size(max = 50)
	@Column(name = "zona", length = 50)
	private String zona;

	@Size(max = 11)
	@Column(name = "clave_catastral", length = 11)
	private String claveCatastral;

	@Size(max = 40)
	@Column(name = "cuenta_catastral", length = 40)
	private String cuentaCatastral;

	@Size(max = 31)
	@Column(name = "clave_catastral_estandard", length = 31)
	private String claveCatastralEstandard;

	@Column(name = "codigo_postal", length = 5)
	private String codigoPostal;

	@Size(max = 20)
	@Column(name = "superficie", length = 20)
	private String superficie;
	
    @Column(name = "procede_de_folio", length = 200)
    private String procedeDeFolio;
	
    @Size(max = 4)
    @Column(name = "documento", length = 4)
    private String documento;


	@ManyToOne(optional = false)
	private UnidadMedida unidadMedida;

	@ManyToOne(optional = false)
	private UsoSuelo usoSuelo;

	@ManyToOne(optional = false)
	private Predio predio;
	
	@ManyToOne
	private Usuario usuarioAct;

	@ManyToOne
	private Oficina oficina;

	@ManyToOne
	private TipoInmueble tipoInmueble;

	@ManyToOne
	private Nivel nivel;

	@ManyToOne
	private TipoVialidad tipoVialidad;

	@ManyToOne
	private EnlaceVialidad enlaceVialidad;

	@ManyToOne
	private TipoVialidad tipoVialidad2;

	@ManyToOne
	private TipoAsent tipoAsent;

	@ManyToOne
	private FracOCondo fracOCondo;

	@ManyToOne
	private EtapaOSeccion etapaOSeccion;

	@ManyToOne
	private Municipio municipio;

	@ManyToOne
	private Libro libro;
	
	@Column(name = "superficie_m2")
    private Double superficieM2;
	


	@ManyToOne
	private Acto actoRectificacion;
  
	@OneToMany(mappedBy = "predioBitacora")
    @JsonIgnore
    private Set<PredioBitacoraColindancia> colindancias = new HashSet<>();
	

	public Set<PredioBitacoraColindancia> getColindancias() {
		return colindancias;
	}

	public void setColindancias(Set<PredioBitacoraColindancia> colindancias) {
		this.colindancias = colindancias;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setFechaAct(Date fechaAct) {
		this.fechaAct = fechaAct;
	}

	public Usuario getUsuarioAct() {
		return usuarioAct;
	}

	public void setNumInt(String numInt) {
		this.numInt = numInt;
	}

	public void setEdificio(String edificio) {
		this.edificio = edificio;
	}

	public void setLocalidadSector(String localidadSector) {
		this.localidadSector = localidadSector;
	}

	public void setNoLote(String noLote) {
		this.noLote = noLote;
	}

	public void setFraccion(String fraccion) {
		this.fraccion = fraccion;
	}

	public void setManzana(String manzana) {
		this.manzana = manzana;
	}

	public void setMacroManzana(String macroManzana) {
		this.macroManzana = macroManzana;
	}

	public void setVialidad(String vialidad) {
		this.vialidad = vialidad;
	}

	public void setNumExt(String numExt) {
		this.numExt = numExt;
	}

	public void setVialidad2(String vialidad2) {
		this.vialidad2 = vialidad2;
	}

	public void setNumExt2(String numExt2) {
		this.numExt2 = numExt2;
	}

	public void setAsentamiento(String asentamiento) {
		this.asentamiento = asentamiento;
	}

	public void setZona(String zona) {
		this.zona = zona;
	}

	public void setClaveCatastral(String claveCatastral) {
		this.claveCatastral = claveCatastral;
	}

	public void setCuentaCatastral(String cuentaCatastral) {
		this.cuentaCatastral = cuentaCatastral;
	}

	public void setClaveCatastralEstandard(String claveCatastralEstandard) {
		this.claveCatastralEstandard = claveCatastralEstandard;
	}

	public void setCodigoPostal(String codigoPostal) {
		this.codigoPostal = codigoPostal;
	}

	public void setSuperficie(String superficie) {
		this.superficie = superficie;
	}

	public void setUnidadMedida(UnidadMedida unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	public void setUsoSuelo(UsoSuelo usoSuelo) {
		this.usoSuelo = usoSuelo;
	}

	public void setPredio(Predio predio) {
		this.predio = predio;
	}

	public void setUsuarioAct(Usuario usuarioAct) {
		this.usuarioAct = usuarioAct;
	}

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

	public void setTipoInmueble(TipoInmueble tipoInmueble) {
		this.tipoInmueble = tipoInmueble;
	}

	public void setNivel(Nivel nivel) {
		this.nivel = nivel;
	}

	public void setTipoVialidad(TipoVialidad tipoVialidad) {
		this.tipoVialidad = tipoVialidad;
	}

	public void setEnlaceVialidad(EnlaceVialidad enlaceVialidad) {
		this.enlaceVialidad = enlaceVialidad;
	}

	public void setTipoVialidad2(TipoVialidad tipoVialidad2) {
		this.tipoVialidad2 = tipoVialidad2;
	}

	public void setTipoAsent(TipoAsent tipoAsent) {
		this.tipoAsent = tipoAsent;
	}

	public void setFracOCondo(FracOCondo fracOCondo) {
		this.fracOCondo = fracOCondo;
	}

	public void setEtapaOSeccion(EtapaOSeccion etapaOSeccion) {
		this.etapaOSeccion = etapaOSeccion;
	}

	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	
/*
	@ManyToOne
    @JsonIgnore
    private Set<PredioBitacoraColindancia> predioBitacoraColindancia = new HashSet<>();

	
	
	public Set<PredioBitacoraColindancia> getPredioBitacoraColindancia() {
		return predioBitacoraColindancia;
	}

	public void setPredioBitacoraColindancia(Set<PredioBitacoraColindancia> predioBitacoraColindancia) {
		this.predioBitacoraColindancia = predioBitacoraColindancia;
	}
*/
	public Long getId() {
		return id;
	}

	public Date getFechaAct() {
		return fechaAct;
	}

	public String getNumInt() {
		return numInt;
	}

	public String getEdificio() {
		return edificio;
	}

	public String getLocalidadSector() {
		return localidadSector;
	}

	public String getNoLote() {
		return noLote;
	}

	public String getFraccion() {
		return fraccion;
	}

	public String getManzana() {
		return manzana;
	}

	public String getMacroManzana() {
		return macroManzana;
	}

	public String getVialidad() {
		return vialidad;
	}

	public String getNumExt() {
		return numExt;
	}

	public String getVialidad2() {
		return vialidad2;
	}

	public String getNumExt2() {
		return numExt2;
	}

	public String getAsentamiento() {
		return asentamiento;
	}

	public String getZona() {
		return zona;
	}

	public String getClaveCatastral() {
		return claveCatastral;
	}

	public String getCuentaCatastral() {
		return cuentaCatastral;
	}

	public String getClaveCatastralEstandard() {
		return claveCatastralEstandard;
	}

	public String getCodigoPostal() {
		return codigoPostal;
	}

	public String getSuperficie() {
		return superficie;
	}

	public UnidadMedida getUnidadMedida() {
		return unidadMedida;
	}

	public UsoSuelo getUsoSuelo() {
		return usoSuelo;
	}

	public TipoInmueble getTipoInmueble() {
		return tipoInmueble;
	}

	public Nivel getNivel() {
		return nivel;
	}

	public TipoVialidad getTipoVialidad() {
		return tipoVialidad;
	}

	public TipoVialidad getTipoVialidad2() {
		return tipoVialidad2;
	}

	public TipoAsent getTipoAsent() {
		return tipoAsent;
	}

	public EtapaOSeccion getEtapaOSeccion() {
		return etapaOSeccion;
	}

	public Municipio getMunicipio() {
		return municipio;
	}
	public String getProcedeDeFolio() {
        return procedeDeFolio;
    }

    public void setProcedeDeFolio(String procedeDeFolio) {
        this.procedeDeFolio = procedeDeFolio;
	}
	
	public Libro getLibro() {
		return libro;
	}

	public void setLibro(Libro libro) {
		this.libro = libro;
	}
	
	public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}
	public Double getSuperficieM2() {
		return superficieM2;
	}

	public void setSuperficieM2(Double superficieM2) {
		this.superficieM2 = superficieM2;
	}
	
	public Acto getActoRectificacion() {
		return actoRectificacion;
	}

	public void setActoRectificacion(Acto actoRectificacion) {
		this.actoRectificacion = actoRectificacion;
	}

}
