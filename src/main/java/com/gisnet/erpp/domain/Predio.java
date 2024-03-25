package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Predio.
 */
@Entity
@Table(name = "predio")
public class Predio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "predioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "predioGenerator", sequenceName = "predio_seq")
    private Long id;

    @Column(name = "no_folio")
    private Integer noFolio;

    @Size(max = 64)
    @Column(name = "localidad_sector", length = 64)
    private String localidadSector;

    @Size(max = 50)
    @Column(name = "no_lote", length = 50)
    private String noLote;

    @Size(max = 50)
    @Column(name = "manzana", length = 50)
    private String manzana;

    @Size(max = 20)
    @Column(name = "superficie", length = 20)
    private String superficie;

    @Column(name = "superficie_m2")
    private Double superficieM2;

    public Libro getLibro() {
		return libro;
	}

	public void setLibro(Libro libro) {
		this.libro = libro;
	}

	@Size(max = 11)
    @Column(name = "clave_catastral", length = 11)
    private String claveCatastral;

    @Size(max = 40)
    @Column(name = "num_int", length = 40)
    private String numInt;

    @Size(max = 40)
    @Column(name = "cuenta_catastral", length = 40)
    private String cuentaCatastral;

    @Size(max = 31)
    @Column(name = "clave_catastral_estandard", length = 31)
    private String claveCatastralEstandard;

    @Column(name = "fecha_apertura")
    private Date fechaApertura;

    @Column(name = "folio_calidad")
    private Boolean folioCalidad;

    @Column(name = "migrado")
    private Boolean migrado;

    @Size(max = 80)
    @Column(name = "hash_fila", length = 80)
    private String hashFila;

    @Column(name = "edificio", length = 64)
    private String edificio;

    @Column(name = "fraccion", length = 20)
    private String fraccion;

    @Column(name = "macro_manzana", length = 20)
    private String macroManzana;

    @ManyToOne
    private FracOCondo fracOCondo;

    @Size(max = 200)
    @Column(name = "nombre_frac_o_condo", length = 200)
    private String nombreFracOCondo;

    @ManyToOne
    private EtapaOSeccion etapaOSeccion;

    @Size(max = 50)
    @Column(name = "zona", length = 50)
    private String zona;

    @Column(name = "bloqueado")
    private Boolean bloqueado;

    @Column(name = "cerrado")
    private String cerrado;

    @Column(name = "caratula_actualizada")
    private Boolean caratulaActualizada;

    @Column(name = "codigo_postal", length = 5)
    private String codigoPostal;

    @Column(name = "primer_registro", length = 1)
    private Integer primerRegistro;

    @Column(name = "procede_de_folio", length = 200)
    private String procedeDeFolio;
    
    @Column(name = "hereda_acto")
    private Boolean heredaActo;
    
    @Column(name = "ind_matriz")
    private Boolean indMatriz;

    @OneToMany(mappedBy = "predio")
    @JsonIgnore
    private Set<ActoPredio> actoPrediosParaPredios = new HashSet<>();

    @OneToMany(mappedBy = "predio")
    @JsonIgnore
    private Set<FoliosFracCond> foliosFracCondParaPredios = new HashSet<>();

    @OneToMany(mappedBy = "predioRel")
    @JsonIgnore
    private Set<Colindancia> colindanciasParaPredioRels = new HashSet<>();

    @OneToMany(mappedBy = "predio")
    @JsonIgnore
    @NotFound(action = NotFoundAction.IGNORE)
    private Set<Colindancia> colindanciasParaPredios = new HashSet<>();

    @OneToMany(mappedBy = "predio", cascade = CascadeType.PERSIST)
    @JsonIgnore
    private Set<PredioAnte> predioAntesParaPredios = new HashSet<>();

    @OneToMany(mappedBy = "predio")
    @JsonIgnore
    private Set<PredioMigrado> predioMigradosParaPredios = new HashSet<>();

    @OneToMany(mappedBy = "predio")
    @JsonIgnore
    private Set<PrelacionPredio> prelacionPrediosParaPredios = new HashSet<>();

    @OneToMany(mappedBy = "predioSig")
    @JsonIgnore
    private Set<PredioRel> predioRelesParaPredioSiguientes = new HashSet<>();

    @OneToMany(mappedBy = "predio")
    @JsonIgnore
    private Set<PredioRel> predioRelesParaPredios = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private StatusActo statusActo;

    @ManyToOne(optional = false)
    @NotNull
    private UsoSuelo usoSuelo;

    @ManyToOne(optional = false)
    @NotNull
    private UnidadMedida unidadMedida;

    @ManyToOne
    private ErrorMigra errorMigra;

    @ManyToOne
    private TipoVialidad tipoVialidad;

    @Size(max = 2000)
    @Column(name = "vialidad", length = 2000)
    private String vialidad;

    @Size(max = 40)
    @Column(name = "NUM_EXT", length = 40)
    private String numExt;

    @ManyToOne
    private TipoVialidad tipoVialidad2;

    @Size(max = 2000)
    @Column(name = "vialidad2", length = 2000)
    private String vialidad2;

    @Size(max = 40)
    @Column(name = "NUM_EXT2", length = 40)
    private String numExt2;

    @ManyToOne
    private TipoAsent tipoAsent;

    @Size(max = 2000)
    @Column(name = "asentamiento", length = 2000)
    private String asentamiento;

    @ManyToOne
    private TipoInmueble tipoInmueble;

    @ManyToOne
    private Nivel nivel;

    @ManyToOne
    private EnlaceVialidad enlaceVialidad;

    @ManyToOne
    private Oficina oficina;

    @ManyToOne
    private Municipio municipio;

    @ManyToOne
    private Libro libro;
    
    @Size(max = 4)
    @Column(name = "documento", length = 4)
    private String documento;

    @Column(name = "numero_folio_real")
    private Integer numeroFolioReal;

    @Column(name = "auxiliar")
    private Integer auxiliar;
    
    @Column(name = "id_folio_real")
    private Integer id_folio_real;


    public Double getSuperficieM2() {
        return superficieM2;
    }

    public void setSuperficieM2(Double superficieM2) {
        this.superficieM2 = superficieM2;
    }

    public String getNumExt2() {
        return numExt2;
    }

    public void setNumExt2(String numExt2) {
        this.numExt2 = numExt2;
    }

    public String getDocumento() {
		return documento;
	}

	public void setDocumento(String documento) {
		this.documento = documento;
	}

	public EnlaceVialidad getEnlaceVialidad() {
        return enlaceVialidad;
    }

    public void setEnlaceVialidad(EnlaceVialidad enlaceVialidad) {
        this.enlaceVialidad = enlaceVialidad;
    }

    public FracOCondo getFracOCondo() {
        return fracOCondo;
    }

    public void setFracOCondo(FracOCondo fracOCondo) {
        this.fracOCondo = fracOCondo;
    }

    public void setNivel(Nivel nivel) {
        this.nivel = nivel;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNoFolio() {
        return noFolio;
    }

    public Predio noFolio(Integer noFolio) {
        this.noFolio = noFolio;
        return this;
    }

    public void setNoFolio(Integer noFolio) {
        this.noFolio = noFolio;
    }

    public String getLocalidadSector() {
        return localidadSector;
    }

    public Predio localidadSector(String localidadSector) {
        this.localidadSector = localidadSector;
        return this;
    }

    public void setLocalidadSector(String localidadSector) {
        this.localidadSector = localidadSector;
    }

    public String getNoLote() {
        return noLote;
    }

    public Predio noLote(String noLote) {
        this.noLote = noLote;
        return this;
    }

    public void setNoLote(String noLote) {
        this.noLote = noLote;
    }

    public String getCodigoPostal() {
        return codigoPostal;
    }

    public void setCodigoPostal(String codigoPostal) {
        this.codigoPostal = codigoPostal;
    }

    public String getManzana() {
        return manzana;
    }

    public Predio manzana(String manzana) {
        this.manzana = manzana;
        return this;
    }

    public void setManzana(String manzana) {
        this.manzana = manzana;
    }

    public String getSuperficie() {
        return superficie;
    }

    public Predio superficie(String superficie) {
        this.superficie = superficie;
        return this;
    }

    public void setSuperficie(String superficie) {
        this.superficie = superficie;
    }

    public String getClaveCatastral() {
        return claveCatastral;
    }

    public Predio claveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
        return this;
    }

    public void setClaveCatastral(String claveCatastral) {
        this.claveCatastral = claveCatastral;
    }

    public String getNumExt() {
        return numExt;
    }

    public Predio numExt(String numExt) {
        this.numExt = numExt;
        return this;
    }

    public void setNumExt(String numExt) {
        this.numExt = numExt;
    }

    public String getNumInt() {
        return numInt;
    }

    public Predio numInt(String numInt) {
        this.numInt = numInt;
        return this;
    }

    public void setNumInt(String numInt) {
        this.numInt = numInt;
    }

    public String getCuentaCatastral() {
        return cuentaCatastral;
    }

    public Predio cuentaCatastral(String cuentaCatastral) {
        this.cuentaCatastral = cuentaCatastral;
        return this;
    }

    public void setCuentaCatastral(String cuentaCatastral) {
        this.cuentaCatastral = cuentaCatastral;
    }

    public String getClaveCatastralEstandard() {
        return claveCatastralEstandard;
    }

    public Predio claveCatastralEstandard(String claveCatastralEstandard) {
        this.claveCatastralEstandard = claveCatastralEstandard;
        return this;
    }

    public void setClaveCatastralEstandard(String claveCatastralEstandard) {
        this.claveCatastralEstandard = claveCatastralEstandard;
    }

    public Date getFechaApertura() {
        return fechaApertura;
    }

    public Predio fechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
        return this;
    }

    public void setFechaApertura(Date fechaApertura) {
        this.fechaApertura = fechaApertura;
    }

    public Boolean isFolioCalidad() {
        return folioCalidad;
    }

    public Predio folioCalidad(Boolean folioCalidad) {
        this.folioCalidad = folioCalidad;
        return this;
    }

    public void setFolioCalidad(Boolean folioCalidad) {
        this.folioCalidad = folioCalidad;
    }

    public Boolean isMigrado() {
        return migrado;
    }

    public Predio migrado(Boolean migrado) {
        this.migrado = migrado;
        return this;
    }

    public void setMigrado(Boolean migrado) {
        this.migrado = migrado;
    }

    public String getHashFila() {
        return hashFila;
    }

    public Predio hashFila(String hashFila) {
        this.hashFila = hashFila;
        return this;
    }

    public void setHashFila(String hashFila) {
        this.hashFila = hashFila;
    }

    public String getEdificio() {
        return edificio;
    }

    public void setEdificio(String edificio) {
        this.edificio = edificio;
    }

    public String getFraccion() {
        return fraccion;
    }

    public void setFraccion(String fraccion) {
        this.fraccion = fraccion;
    }

    public String getMacroManzana() {
        return macroManzana;
    }

    public void setMacroManzana(String macroManzana) {
        this.macroManzana = macroManzana;
    }

    public String getNombreFracOCondo() {
        return nombreFracOCondo;
    }

    public Predio nombreFracOCondo(String nombreFracOCondo) {
        this.nombreFracOCondo = nombreFracOCondo;
        return this;
    }

    public void setNombreFracOCondo(String nombreFracOCondo) {
        this.nombreFracOCondo = nombreFracOCondo;
    }

    public EtapaOSeccion getEtapaOSeccion() {
        return etapaOSeccion;
    }

    public void setEtapaOSeccion(EtapaOSeccion etapaOSeccion) {
        this.etapaOSeccion = etapaOSeccion;
    }

    public String getZona() {
        return zona;
    }

    public Predio zona(String zona) {
        this.zona = zona;
        return this;
    }

    public void setZona(String zona) {
        this.zona = zona;
    }

    public Boolean getBloqueado() {
        return bloqueado;
    }

    public Predio bloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
        return this;
    }

    public void setBloqueado(Boolean bloqueado) {
        this.bloqueado = bloqueado;
    }

    public String getCerrado() {
        return cerrado;
    }

    public Predio cerrado(String cerrado) {
        this.cerrado = cerrado;
        return this;
    }

    public void setCerrado(String cerrado) {
        this.cerrado = cerrado;
    }

    public Boolean getCaratulaActualizada() {
        return caratulaActualizada;
    }

    public Predio caratulaActualizada(Boolean caratulaActualizada) {
        this.caratulaActualizada = caratulaActualizada;
        return this;
    }

    public void setCaratulaActualizada(Boolean caratulaActualizada) {
        this.caratulaActualizada = caratulaActualizada;
    }

    public Set<ActoPredio> getActoPrediosParaPredios() {
        return actoPrediosParaPredios;
    }

    public Predio actoPrediosParaPredios(Set<ActoPredio> actoPredios) {
        this.actoPrediosParaPredios = actoPredios;
        return this;
    }

    public Predio addActoPrediosParaPredio(ActoPredio actoPredio) {
        this.actoPrediosParaPredios.add(actoPredio);
        actoPredio.setPredio(this);
        return this;
    }

    public Predio removeActoPrediosParaPredio(ActoPredio actoPredio) {
        this.actoPrediosParaPredios.remove(actoPredio);
        actoPredio.setPredio(null);
        return this;
    }

    public void setActoPrediosParaPredios(Set<ActoPredio> actoPredios) {
        this.actoPrediosParaPredios = actoPredios;
    }

    public Set<FoliosFracCond> getFoliosFracCondParaPredios() {
        return foliosFracCondParaPredios;
    }

    public Predio foliosFracCondParaPredios(Set<FoliosFracCond> foliosFracCond) {
        this.foliosFracCondParaPredios = foliosFracCond;
        return this;
    }

    public Predio addFoliosFracCondParaPredio(FoliosFracCond foliosFracCond) {
        this.foliosFracCondParaPredios.add(foliosFracCond);
        foliosFracCond.setPredio(this);
        return this;
    }

    public Predio removeFoliosFracCondParaPredio(FoliosFracCond foliosFracCond) {
        this.foliosFracCondParaPredios.remove(foliosFracCond);
        foliosFracCond.setPredio(null);
        return this;
    }

    public void setFoliosFracCondParaPredios(Set<FoliosFracCond> foliosFracCond) {
        this.foliosFracCondParaPredios = foliosFracCond;
    }

    public Set<Colindancia> getColindanciasParaPredioRels() {
        return colindanciasParaPredioRels;
    }

    public Predio colindanciasParaPredioRels(Set<Colindancia> colindancias) {
        this.colindanciasParaPredioRels = colindancias;
        return this;
    }

    public Predio addColindanciasParaPredioRel(Colindancia colindancia) {
        this.colindanciasParaPredioRels.add(colindancia);
        colindancia.setPredioRel(this);
        return this;
    }

    public Predio removeColindanciasParaPredioRel(Colindancia colindancia) {
        this.colindanciasParaPredioRels.remove(colindancia);
        colindancia.setPredioRel(null);
        return this;
    }

    public void setColindanciasParaPredioRels(Set<Colindancia> colindancias) {
        this.colindanciasParaPredioRels = colindancias;
    }

    public Set<Colindancia> getColindanciasParaPredios() {
        return colindanciasParaPredios;
    }

    public Predio colindanciasParaPredios(Set<Colindancia> colindancias) {
        this.colindanciasParaPredios = colindancias;
        return this;
    }

    public Predio addColindanciasParaPredio(Colindancia colindancia) {
        this.colindanciasParaPredios.add(colindancia);
        colindancia.setPredio(this);
        return this;
    }

    public Predio removeColindanciasParaPredio(Colindancia colindancia) {
        this.colindanciasParaPredios.remove(colindancia);
        colindancia.setPredio(null);
        return this;
    }

    public void setColindanciasParaPredios(Set<Colindancia> colindancias) {
        this.colindanciasParaPredios = colindancias;
    }

    public Set<PredioAnte> getPredioAntesParaPredios() {
        return predioAntesParaPredios;
    }

    public Predio predioAntesParaPredios(Set<PredioAnte> predioAntes) {
        this.predioAntesParaPredios = predioAntes;
        return this;
    }

    public Predio addPredioAntesParaPredio(PredioAnte predioAnte) {
        this.predioAntesParaPredios.add(predioAnte);
        predioAnte.setPredio(this);
        return this;
    }

    public Predio removePredioAntesParaPredio(PredioAnte predioAnte) {
        this.predioAntesParaPredios.remove(predioAnte);
        predioAnte.setPredio(null);
        return this;
    }

    public void setPredioAntesParaPredios(Set<PredioAnte> predioAntes) {
        this.predioAntesParaPredios = predioAntes;
    }

    public Set<PredioMigrado> getPredioMigradosParaPredios() {
        return predioMigradosParaPredios;
    }

    public Predio predioMigradosParaPredios(Set<PredioMigrado> predioMigrados) {
        this.predioMigradosParaPredios = predioMigrados;
        return this;
    }

    public Predio addPredioMigradosParaPredio(PredioMigrado predioMigrado) {
        this.predioMigradosParaPredios.add(predioMigrado);
        predioMigrado.setPredio(this);
        return this;
    }

    public Predio removePredioMigradosParaPredio(PredioMigrado predioMigrado) {
        this.predioMigradosParaPredios.remove(predioMigrado);
        predioMigrado.setPredio(null);
        return this;
    }

    public void setPredioMigradosParaPredios(Set<PredioMigrado> predioMigrados) {
        this.predioMigradosParaPredios = predioMigrados;
    }

    public Set<PrelacionPredio> getPrelacionPrediosParaPredios() {
        return prelacionPrediosParaPredios;
    }

    public Predio prelacionPrediosParaPredios(Set<PrelacionPredio> prelacionPredios) {
        this.prelacionPrediosParaPredios = prelacionPredios;
        return this;
    }

    public Predio addPrelacionPrediosParaPredio(PrelacionPredio prelacionPredio) {
        this.prelacionPrediosParaPredios.add(prelacionPredio);
        prelacionPredio.setPredio(this);
        return this;
    }

    public Predio removePrelacionPrediosParaPredio(PrelacionPredio prelacionPredio) {
        this.prelacionPrediosParaPredios.remove(prelacionPredio);
        prelacionPredio.setPredio(null);
        return this;
    }

    public void setPrelacionPrediosParaPredios(Set<PrelacionPredio> prelacionPredios) {
        this.prelacionPrediosParaPredios = prelacionPredios;
    }

    public StatusActo getStatusActo() {
        return statusActo;
    }

    public void setStatusActo(StatusActo statusActo) {
        this.statusActo = statusActo;
    }

    public UsoSuelo getUsoSuelo() {
        return usoSuelo;
    }

    public Predio usoSuelo(UsoSuelo usoSuelo) {
        this.usoSuelo = usoSuelo;
        return this;
    }

    public void setUsoSuelo(UsoSuelo usoSuelo) {
        this.usoSuelo = usoSuelo;
    }

    public UnidadMedida getUnidadMedida() {
        return unidadMedida;
    }

    public Predio unidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
        return this;
    }

    public void setUnidadMedida(UnidadMedida unidadMedida) {
        this.unidadMedida = unidadMedida;
    }

    public ErrorMigra getErrorMigra() {
        return errorMigra;
    }

    public Predio errorMigra(ErrorMigra errorMigra) {
        this.errorMigra = errorMigra;
        return this;
    }

    public void setErrorMigra(ErrorMigra errorMigra) {
        this.errorMigra = errorMigra;
    }

    public TipoInmueble getTipoInmueble() {
        return tipoInmueble;
    }

    public Predio tipoInmueble(TipoInmueble tipoInmueble) {
        this.tipoInmueble = tipoInmueble;
        return this;
    }

    public void setTipoInmueble(TipoInmueble tipoInmueble) {
        this.tipoInmueble = tipoInmueble;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public Integer getNumeroFolioReal() {
        return numeroFolioReal;
    }

    public Predio numeroFolioReal(Integer numeroFolioReal) {
        this.numeroFolioReal = numeroFolioReal;
        return this;
    }

    public void setNumeroFolioReal(Integer numeroFolioReal) {
        this.numeroFolioReal = numeroFolioReal;
    }

    public Integer getAuxiliar() {
        return auxiliar;
    }

    public void setAuxiliar(Integer auxiliar) {
        this.auxiliar = auxiliar;
    }

    
    public Integer getId_folio_real() {
        return id_folio_real;
    }

    public void setId_folio_real(Integer id_folio_real) {
        this.id_folio_real = id_folio_real;
    }
    
    @JsonIgnore
    public String getUbicacion() {
        StringBuilder sb = new StringBuilder();
        if (vialidad != null) {
            sb.append("VIALIDAD: " + vialidad);
        }

        if (numExt != null) {
            sb.append(", NÚMERO EXTERIOR: ").append(numExt);
        }
        if (numInt != null) {
            sb.append(", NÚMERO INTERIOR: ").append(numInt);
        }

        if (tipoVialidad != null) {
            sb.append(", TIPO VIALIDAD: " + tipoVialidad.getNombre());
        }

        if (tipoAsent != null){
            sb.append(",TIPO DE ASENTAMIENTO: "+ tipoAsent.getNombre());
        }

        if (asentamiento != null) {
            sb.append(", ASENTAMIENTO: " + asentamiento);
        }

        if (tipoInmueble != null) {
            sb.append(", TIPO DE INMUEBLE: "+ tipoInmueble.getNombre());
        }

        if(noLote != null){
            sb.append(", LOTE: "+ noLote);
        }

        if(manzana != null) {
            sb.append(", MANZANA: "+ manzana);
        }

        if (municipio != null) {
            sb.append(", MUNICIPIO: " + municipio.getNombre());
            sb.append(", ESTADO: " + municipio.getEstado().getNombre());
        }

        return sb.toString();
    }

    public String getDireccion() {
        StringBuilder sb = new StringBuilder();       
        if (vialidad != null) {
            sb.append(vialidad.trim());
        }

        if (numExt != null) {
            sb.append(" ").append(numExt.trim());
        }   
        if (numInt != null) {
            sb.append(" ").append(numInt.trim());
        }

        if (asentamiento != null) {
            sb.append(", " + asentamiento.trim());
        }

        if (municipio != null) {
            sb.append(", " + municipio.getNombre().trim());
            sb.append(", " + municipio.getEstado().getNombre().trim());
        }
        
        return sb.toString();
    }

    public Integer getPrimerRegistro() {
        return primerRegistro;
    }

    public void setPrimerRegistro(Integer primerRegistro) {
        this.primerRegistro = primerRegistro;
    }

    public String getProcedeDeFolio() {
        return procedeDeFolio;
    }

    public void setProcedeDeFolio(String procedeDeFolio) {
        this.procedeDeFolio = procedeDeFolio;
    }

    public Nivel getNivel() {
        return nivel;
    }

    public TipoVialidad getTipoVialidad() {
        return tipoVialidad;
    }

    public void setTipoVialidad(TipoVialidad tipoVialidad) {
        this.tipoVialidad = tipoVialidad;
    }

    public String getVialidad() {
        return vialidad;
    }

    public void setVialidad(String vialidad) {
        this.vialidad = vialidad;
    }

    public TipoVialidad getTipoVialidad2() {
        return tipoVialidad2;
    }

    public void setTipoVialidad2(TipoVialidad tipoVialidad2) {
        this.tipoVialidad2 = tipoVialidad2;
    }

    public String getVialidad2() {
        return vialidad2;
    }

    public void setVialidad2(String vialidad2) {
        this.vialidad2 = vialidad2;
    }

    public TipoAsent getTipoAsent() {
        return tipoAsent;
    }

    public void setTipoAsent(TipoAsent tipoAsent) {
        this.tipoAsent = tipoAsent;
    }

    public String getAsentamiento() {
        return asentamiento;
    }

    public void setAsentamiento(String asentamiento) {
        this.asentamiento = asentamiento;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Boolean getFolioCalidad() {
        return folioCalidad;
    }

    public Boolean getMigrado() {
        return migrado;
    }

    public Set<PredioRel> getPredioRelesParaPredioSiguientes() {
        return predioRelesParaPredioSiguientes;
    }

    public void setPredioRelesParaPredioSiguientes(Set<PredioRel> predioRelesParaPredioSiguientes) {
        this.predioRelesParaPredioSiguientes = predioRelesParaPredioSiguientes;
    }

    public Set<PredioRel> getPredioRelesParaPredios() {
        return predioRelesParaPredios;
    }

    public void setPredioRelesParaPredios(Set<PredioRel> predioRelesParaPredios) {
        this.predioRelesParaPredios = predioRelesParaPredios;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Predio predio = (Predio) o;
        if (predio.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), predio.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Predio{" + "id=" + getId() + ", noFolio='" + getNoFolio() + "'" + ", noLote='" + getNoLote() + "'"
                + ", manzana='" + getManzana() + "'" + ", superficie='" + getSuperficie() + "'" + ", claveCatastral='"
                + getClaveCatastral() + "'" + ", numExt='" + getNumExt() + "'" + ", numInt='" + getNumInt() + "'"
                + ", cuentaCatastral='" + getCuentaCatastral() + "'" + ", claveCatastralEstandard='"
                + getClaveCatastralEstandard() + "'" + ", fechaApertura='" + getFechaApertura() + "'"
                + ", folioCalidad='" + isFolioCalidad() + "'" + ", migrado='" + isMigrado() + "'" + ", hashFila='"
                + getHashFila() + "'" + ", nivel='" + getNivel() + "'" + ", edificio='" + getEdificio() + "'"
                + ", localidadSector='" + getLocalidadSector() + "'" + ", fraccion='" + getFraccion() + "'"
                + ", macroManzana='" + getMacroManzana() + "'" + ", fracOCondo='" + getFracOCondo() + "'"
                + ", nombreFracOCondo='" + getNombreFracOCondo() + "'" + ", etapaOSeccion='" + getEtapaOSeccion() + "'"
                + ", zona='" + getZona() + "'" + "}";
    }

    @JsonIgnore
    public String getDireccionComplete() {
        StringBuilder sb = new StringBuilder();
        if (tipoInmueble != null) {
            if (tipoInmueble.getId() != 28) {
                sb.length();
                sb.append(" TIPO INMUEBLE O PREDIO: " + tipoInmueble.getNombre());
            }
        }

        if (validaCadena(numInt) != null) {
            sb.append(", N° INTERIOR: ").append(numInt.trim());
        }
        if (nivel != null) {
            if (nivel.getId() != 97) {
                sb.append(", NIVEL: " + nivel.getNombre());
            }
        }
        if (validaCadena(edificio) != null) {
            sb.append(", EDIFICIO: ").append(edificio);
        }
        if (validaCadena(localidadSector) != null) {
            sb.append(", LOCALIDAD O SECTOR: " + localidadSector);
        }
        if (validaCadena(noLote) != null) {
            sb.append(", LOTE O PARCELA: ").append(noLote.trim());
        }
        if (validaCadena(fraccion) != null) {
            sb.append(", FRACCIÓN: ").append(fraccion.trim());
        }
        if (validaCadena(manzana) != null) {
            sb.append(", MANZANA: ").append(manzana.trim());
        }
        if (tipoVialidad != null) {
            if (tipoVialidad.getId() != 31) {
                sb.append(", TIPO VIALIDAD: " + tipoVialidad.getNombre());
            }
        }
        if (validaCadena(vialidad) != null) {
            sb.append(", VIALIDAD: " + vialidad.toUpperCase().trim());
        }
        if (validaCadena(numExt) != null) {
            if (numExt.trim().length() > 0) {
                sb.append(", N° EXTERIOR: " + numExt.trim());
            }

        }
        if (enlaceVialidad != null) {
            if (enlaceVialidad.getId() != 4) {
                sb.append(", ENLACE VIALIDAD: " + enlaceVialidad.getNombre());
            }
        }
        if (tipoVialidad2 != null) {
            if (tipoVialidad2.getId() != 31) {
                sb.append(", TIPO VIALIDAD 2: " + tipoVialidad2.getNombre());
            }
        }
        if (validaCadena(vialidad2) != null) {
            sb.append(", VIALIDAD 2: " + vialidad2.trim());
        }
        if (validaCadena(numExt2) != null) {
            sb.append(", N° EXTERIOR 2: " + numExt2.trim());
        }
        if (tipoAsent != null) {
            if (tipoAsent.getId() != 42) {
                sb.append(", TIPO ASENTAMIENTO: " + tipoAsent.getNombre());
            }
        }
        if (validaCadena(asentamiento) != null) {
            sb.append(", NOMBRE DE ASENTAMIENTO: " + asentamiento.toUpperCase().trim());
        }
        if (etapaOSeccion != null) {
            if (etapaOSeccion.getId() != 31) {
                sb.append(", ETAPA O SECCIÓN: " + etapaOSeccion.getNombre());
            }
        }
        if (validaCadena(zona) != null) {
            sb.append(", ZONA: " + zona.trim());
        }

        if (validaCadena(nombreFracOCondo) != null) {
            sb.append(", NOMBRE DE PREDIO: " + nombreFracOCondo.trim());
        }
        if (validaCadena(cuentaCatastral) != null) {
            sb.append(", CUENTA PREDIAL: " + cuentaCatastral.trim());
        }
        if (validaCadena(claveCatastralEstandard) != null) {
            sb.append(", CLAVE ESTÁNDAR: " + claveCatastralEstandard.trim());
        }
        if (validaCadena(claveCatastral) != null) {
            sb.append(", CLAVE CATASTRAL: " + claveCatastral.trim());
        }
        if (municipio != null) {
            sb.append(", ESTADO: " + municipio.getEstado().getNombre().trim());
            sb.append(", MUNICIPIO:  " + municipio.getNombre().trim());

        }
        if (validaCadena(codigoPostal) != null) {
            sb.append(", C.P.: " + codigoPostal.trim());
        }

        if (validaCadena(superficie) != null) {
            sb.append(", SUPERFICIE: " + superficie.trim());
        }
        if (unidadMedida != null) {
            if (unidadMedida.getId() != 6) {
                sb.append(", UNIDAD DE MEDIDA: " + unidadMedida.getNombre());
            }
        }
        if (usoSuelo != null) {
            if (usoSuelo.getId() != 155450) {
                sb.append(", USO DE SUELO: " + usoSuelo.getNombre());
            }
        }
        return sb.toString();
    }

    public String validaCadena(String valor) {
        String valorValidado = null;
        if (valor != null) {
            if (valor.trim().length() > 0) {
                valorValidado = valor;
            }
        }
        return valorValidado;
    }

	public Boolean getHeredaActo() {
		return heredaActo;
	}

	public void setHeredaActo(Boolean heredaActo) {
		this.heredaActo = heredaActo;
	}

	public Boolean getIndMatriz() {
		return indMatriz;
	}

	public void setIndMatriz(Boolean indMatriz) {
		this.indMatriz = indMatriz;
	}

}
