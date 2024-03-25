package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Prelacion. 
 */
@Entity
@Table(name = "prelacion")
public class Prelacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prelacionGenerator")
    @SequenceGenerator(allocationSize = 1, name = "prelacionGenerator", sequenceName="prelacion_seq")
    private Long id;

    @Column(name = "consecutivo")
    private Integer consecutivo;

    @Column(name = "anio")
    private Integer anio;

    @Column(name = "email_enviado")
    private Boolean emailEnviado;

    @Size(max = 1000)
    @Column(name = "motivo_rechazo", length = 1000)
    private String motivoRechazo;

    @Size(max = 1000)
    @Column(name = "motivo_reasignacion", length = 1000)
    private String motivoReasignacion;

    @Column(name = "reingresado")
    private Integer reingresado;

    @Column(name = "reproceso")
    private Integer reproceso;

    @Column(name = "reproceso_calificador")
    private Integer reprocesoCalificador;

    @Size(max = 1000)
    @Column(name = "observaciones_motivo", length = 1000)
    private String observacionesMotivo;

    @Size(max = 1000)
    @Column(name = "observaciones_aclaracion", length = 1000)
    private String observacionesAclaracion;

    @Size(max = 1000)
    @Column(name = "observaciones", length = 1000)
    private String observaciones;

    @ManyToOne
    private Observacion observacion;

    @Size(max = 80)
    @Column(name = "hash_fila", length = 80)
    private String hashFila;

    @Column(name = "fecha_ingreso")
    private Date fechaIngreso;

    @Column(name = "fecha_envio_firma")
    private Date fechaEnvioFirma;

    @Column(name = "fecha_baja")
    private Date fechaBaja;
    
    @Size(max = 60)
    @Column(name = "numero_boleta", length = 60)
    private String numeroBoleta;
    
    @Column(name = "id_pago_electronico")
    private Integer idPagoElectronico;
    
    @Column(name = "url_formato")
    private String urlFormato;
    
    @Size(max = 6)
    @Column(name = "clave_consulta", length = 60)
    private String claveConsulta;
    
    @OneToMany(mappedBy = "prelacion")
    @JsonIgnore
    private Set<Acto> actosParaPrelacions = new HashSet<>();

    @OneToMany(mappedBy = "prelacion")
    @JsonIgnore
    private Set<Bitacora> bitacorasParaPrelacions = new HashSet<>();

    //JADV-SUSPENSION
    @OneToMany(mappedBy = "prelacion")
    @JsonIgnore
    private Set<Suspension> suspension = new HashSet<>();
    
    @OneToMany(mappedBy = "prelacion")
    @JsonIgnore
    private Set<PrelacionAnte> prelacionAntesParaPrelacions = new HashSet<>();

    @OneToMany(mappedBy = "prelacion")
    @JsonIgnore
    private Set<PrelacionPredio> prelacionPrediosParaPrelacions = new HashSet<>();

    @OneToMany(mappedBy = "prelacion")
    @JsonIgnore
    private Set<Recibo> recibosParaPrelacions = new HashSet<>();

    @OneToMany(mappedBy = "prelacion")
    @JsonIgnore
    private Set<PrelacionServicio> prelacionServiciosParaPrelacions = new HashSet<>();

    @OneToMany(mappedBy = "prelacion")
    @JsonIgnore
    private Set<PrelacionContratante> prelacionContratantesParaPrelacions = new HashSet<>();
    
    @ManyToOne
    private Municipio municipio;

    @ManyToOne
    private Motivo motivo;

    @ManyToOne(optional = false)
    @NotNull
    private Usuario usuarioSolicitan;

    @ManyToOne
    private Usuario usuarioValPredios;

    @ManyToOne(optional = false)
    @NotNull
    private Prioridad prioridad;

    @ManyToOne
    private Notario notario;

    @ManyToOne(optional = false)
    @NotNull
    private Oficina oficina;

    @ManyToOne
    private Resultado resultado;

    @ManyToOne(optional = false)
    @NotNull
    private Status status;
    
    @ManyToOne
    private Status statusAnterior;

    @ManyToOne
    private Usuario usuarioNotario;

    @ManyToOne
    private Usuario usuarioVentanilla;

    @ManyToOne
    private Usuario usuarioCoordinador;

    @ManyToOne
    private Usuario usuarioCalificador;

    @ManyToOne
    private Usuario usuarioAnalista;
    
    @ManyToOne
    private Usuario usuarioCapVal;
    
    @ManyToOne
    private TipoAclaracion tipoAclaracion;


    @ManyToOne
    private Area area;

    @OneToMany(mappedBy = "prelacion")
    @JsonIgnore
    private Set<Suspension> suspencionesParaPrelacion = new HashSet<>();

    @Column (name="tipo_busqueda")
    private String tipoBusqueda ;

    @OneToMany(mappedBy = "prelacion")
    @JsonIgnore
    private Set<PrelacionServicio> prelacionServicioParaPrelacion = new HashSet<>();

    @Column(name = "fecha_programada_entrega")
    private Date fechaProgramadaEntrega;

    @Column(name = "tipo_ante", length = 10)
    private Integer tipoAnte;

    @Column(name = "tipo_entrega")
    private Integer	tipoEntrega;

    @Size(max = 4096)
    @Column(name = "pkcs_7", length = 4096)
    private String pkcs7;

    @Size(max = 512)
    @Column(name = "firma", length = 512)
    private String firma;

    @Column(name = "secuencia")
    private Integer secuencia;

    @Size(max = 100)
    @Column(name = "politica", length = 100)
    private String politica;

    @Size(max = 100)
    @Column(name = "digestion", length = 100)
    private String digestion;

    @Column(name = "secuencia_ts")
    private Integer secuenciaTS;

    @Column(name = "estampilla")
    private Date estampilla;

    @Column(name = "tiene_termino")
    private Boolean tieneTermino;

    @Column(name = "dias")
    private Integer dias;

    @Column(name = "horas")
    private Integer horas;
    
    @Column(name = "num_capturas")
    private Integer numCapturas;
    
    @Column(name = "num_por_sistema")
    private Integer numPorSistema;


    @Column(name = "exento_pago")
    private Boolean exentoPago;

    @Column(name = "primer_registro", length = 1)
    private Boolean primerRegistro = false;

    @Column(name = "anterio_abril81", length = 1)
    private Boolean anteriorAbril81;

    @Column(name = "marcar_suspensivo")
    private Boolean marcarSuspensivo = false;
    
    @Column(name = "es_digitalizado")
    private Boolean es_digitalizado; 
    
    @Column(name = "ind_entrega")
    private Boolean indEntrega; 

    @Column(name = "id_entrada")
    private Long id_entrada;
    
    @Column(name = "subindice")
    private Long subindice;
    
    @Column (name="Numero_Entrada_Completo")
    private String Numero_Entrada_Completo ;
    
    @Column(name = "fecha_reingreso")
    private Date fechaReingreso;
    
    @Column(name="vf")
    private Boolean vf;

    public Boolean getMarcarSuspensivo() {
		return marcarSuspensivo;
	}

	public void setMarcarSuspensivo(Boolean marcarSuspensivo) {
		this.marcarSuspensivo = marcarSuspensivo;
	}

	public Long getId() {

        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

    public Integer getTipoAnte(){
    	return tipoAnte;
    }

    public void setTipoAnte(Integer tipoAnte){
    	this.tipoAnte = tipoAnte;
    }

    public Integer getConsecutivo() {
        return consecutivo;
    }

    public Prelacion consecutivo(Integer consecutivo) {
        this.consecutivo = consecutivo;
        return this;
    }

    public void setConsecutivo(Integer consecutivo) {
        this.consecutivo = consecutivo;
    }

    public Integer getAnio() {
        return anio;
    }

    public Prelacion anio(Integer anio) {
        this.anio = anio;
        return this;
    }

    public void setAnio(Integer anio) {
        this.anio = anio;
    }

    public Boolean isEmailEnviado() {
        return emailEnviado;
    }

    public Prelacion emailEnviado(Boolean emailEnviado) {
        this.emailEnviado = emailEnviado;
        return this;
    }

    public void setEmailEnviado(Boolean emailEnviado) {
        this.emailEnviado = emailEnviado;
    }
    
    public Boolean isExentoPago() {
        return exentoPago;
    }

    public Prelacion exentoPago(Boolean exentoPago) {
        this.exentoPago = exentoPago;
        return this;
    }

    public void setExentoPago(Boolean exentoPago) {
        this.exentoPago = exentoPago;
    }

    public String getMotivoRechazo() {
        return motivoRechazo;
    }

    public Prelacion motivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
        return this;
    }

    public void setMotivoRechazo(String motivoRechazo) {
        this.motivoRechazo = motivoRechazo;
    }

    public String getMotivoReasignacion() {
        return motivoReasignacion;
    }

    public Prelacion motivoReasignacion(String motivoReasignacion) {
        this.motivoReasignacion = motivoReasignacion;
        return this;
    }

    public void setMotivoReasignacion(String motivoReasignacion) {
        this.motivoReasignacion = motivoReasignacion;
    }

    public Integer getReingresado() {
        return reingresado;
    }

    public Prelacion reingresado(Integer reingresado) {
        this.reingresado = reingresado;
        return this;
    }

    public void setReingresado(Integer reingresado) {
        this.reingresado = reingresado;
    }

    public Integer getReproceso() {
        return reproceso;
    }

    public Prelacion reproceso(Integer reproceso) {
        this.reproceso = reproceso;
        return this;
    }

    public void setReproceso(Integer reproceso) {
        this.reproceso = reproceso;
    }

    public Integer getReprocesoCalificador() {
        return reprocesoCalificador;
    }

    public Prelacion reprocesoCalificador(Integer reprocesoCalificador) {
        this.reprocesoCalificador = reprocesoCalificador;
        return this;
    }

    public void setReprocesoCalificador(Integer reprocesoCalificador) {
        this.reprocesoCalificador = reprocesoCalificador;
    }

    public String getObservacionesMotivo() {
        return observacionesMotivo;
    }

    public Prelacion observacionesMotivo(String observacionesMotivo) {
        this.observacionesMotivo = observacionesMotivo;
        return this;
    }

    public void setObservacionesMotivo(String observacionesMotivo) {
        this.observacionesMotivo = observacionesMotivo;
    }


    public String getObservaciones() {
        return observaciones;
    }

    public Prelacion observaciones(String observaciones) {
        this.observaciones = observaciones;
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }


    public String getHashFila() {
        return hashFila;
    }

    public Prelacion hashFila(String hashFila) {
        this.hashFila = hashFila;
        return this;
    }

    public void setHashFila(String hashFila) {
        this.hashFila = hashFila;
    }

    public Date getFechaIngreso() {
        return fechaIngreso;
    }

    public Prelacion fechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
        return this;
    }

    public void setFechaIngreso(Date fechaIngreso) {
        this.fechaIngreso = fechaIngreso;
    }

    public Date getFechaEnvioFirma() {
        return fechaEnvioFirma;
    }

    public void setFechaEnvioFirma(Date fechaEnvioFirma) {
        this.fechaEnvioFirma = fechaEnvioFirma;
    }

    public Prelacion fechaEnvioFirma (Date fechaEnvioFirma) {
        this.fechaEnvioFirma = fechaEnvioFirma;
        return this;
    }

    public Date getFechaBaja() {
        return fechaBaja;
    }

    public void setFechaBaja(Date fechaBaja) {
        this.fechaBaja = fechaBaja;
    }

    public Prelacion fechaBaja (Date fechaBaja) {
        this.fechaBaja = fechaBaja;
        return this;
    }

    public Set<Acto> getActosParaPrelacions() {
        return actosParaPrelacions;
    }

    public Prelacion actosParaPrelacions(Set<Acto> actos) {
        this.actosParaPrelacions = actos;
        return this;
    }

    public Prelacion addActosParaPrelacion(Acto acto) {
        this.actosParaPrelacions.add(acto);
        acto.setPrelacion(this);
        return this;
    }

    public Prelacion removeActosParaPrelacion(Acto acto) {
        this.actosParaPrelacions.remove(acto);
        acto.setPrelacion(null);
        return this;
    }

    public void setActosParaPrelacions(Set<Acto> actos) {
        this.actosParaPrelacions = actos;
    }

    public Set<Bitacora> getBitacorasParaPrelacions() {
        return bitacorasParaPrelacions;
    }

    public Prelacion bitacorasParaPrelacions(Set<Bitacora> bitacoras) {
        this.bitacorasParaPrelacions = bitacoras;
        return this;
    }

    public Prelacion addBitacorasParaPrelacion(Bitacora bitacora) {
        this.bitacorasParaPrelacions.add(bitacora);
        bitacora.setPrelacion(this);
        return this;
    }

    public Prelacion removeBitacorasParaPrelacion(Bitacora bitacora) {
        this.bitacorasParaPrelacions.remove(bitacora);
        bitacora.setPrelacion(null);
        return this;
    }

    public void setBitacorasParaPrelacions(Set<Bitacora> bitacoras) {
        this.bitacorasParaPrelacions = bitacoras;
    }

    public Set<PrelacionAnte> getPrelacionAntesParaPrelacions() {
        return prelacionAntesParaPrelacions;
    }

    public Prelacion prelacionAntesParaPrelacions(Set<PrelacionAnte> prelacionAntes) {
        this.prelacionAntesParaPrelacions = prelacionAntes;
        return this;
    }

    public Prelacion addPrelacionAntesParaPrelacion(PrelacionAnte prelacionAnte) {
        this.prelacionAntesParaPrelacions.add(prelacionAnte);
        prelacionAnte.setPrelacion(this);
        return this;
    }

    public Prelacion removePrelacionAntesParaPrelacion(PrelacionAnte prelacionAnte) {
        this.prelacionAntesParaPrelacions.remove(prelacionAnte);
        prelacionAnte.setPrelacion(null);
        return this;
    }

    public void setPrelacionAntesParaPrelacions(Set<PrelacionAnte> prelacionAntes) {
        this.prelacionAntesParaPrelacions = prelacionAntes;
    }

    public Set<PrelacionPredio> getPrelacionPrediosParaPrelacions() {
        return prelacionPrediosParaPrelacions;
    }

    public Prelacion prelacionPrediosParaPrelacions(Set<PrelacionPredio> prelacionPredios) {
        this.prelacionPrediosParaPrelacions = prelacionPredios;
        return this;
    }

    public Prelacion addPrelacionPrediosParaPrelacion(PrelacionPredio prelacionPredio) {
        this.prelacionPrediosParaPrelacions.add(prelacionPredio);
        prelacionPredio.setPrelacion(this);
        return this;
    }

    public Prelacion removePrelacionPrediosParaPrelacion(PrelacionPredio prelacionPredio) {
        this.prelacionPrediosParaPrelacions.remove(prelacionPredio);
        prelacionPredio.setPrelacion(null);
        return this;
    }

    public void setPrelacionPrediosParaPrelacions(Set<PrelacionPredio> prelacionPredios) {
        this.prelacionPrediosParaPrelacions = prelacionPredios;
    }

    public Set<Recibo> getRecibosParaPrelacions() {
        return recibosParaPrelacions;
    }

    public Prelacion recibosParaPrelacions(Set<Recibo> recibos) {
        this.recibosParaPrelacions = recibos;
        return this;
    }

    public Prelacion addRecibosParaPrelacion(Recibo recibo) {
        this.recibosParaPrelacions.add(recibo);
        recibo.setPrelacion(this);
        return this;
    }

    public Prelacion removeRecibosParaPrelacion(Recibo recibo) {
        this.recibosParaPrelacions.remove(recibo);
        recibo.setPrelacion(null);
        return this;
    }

    public void setRecibosParaPrelacions(Set<Recibo> recibos) {
        this.recibosParaPrelacions = recibos;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public Prelacion municipio(Municipio municipio) {
        this.municipio = municipio;
        return this;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Motivo getMotivo() {
        return motivo;
    }

    public Prelacion motivo(Motivo motivo) {
        this.motivo = motivo;
        return this;
    }

    public void setMotivo(Motivo motivo) {
        this.motivo = motivo;
    }

    public Usuario getUsuarioSolicitan() {
        return usuarioSolicitan;
    }

    public Prelacion usuarioSolicitan(Usuario usuario) {
        this.usuarioSolicitan = usuario;
        return this;
    }

    public void setUsuarioSolicitan(Usuario usuario) {
        this.usuarioSolicitan = usuario;
    }

    public Usuario getUsuarioValPredios() {
        return usuarioValPredios;
    }

    public Prelacion usuarioValPredios(Usuario usuario) {
        this.usuarioValPredios = usuario;
        return this;
    }

    public void setUsuarioValPredios(Usuario usuario) {
        this.usuarioValPredios = usuario;
    }

    public Prioridad getPrioridad() {
        return prioridad;
    }

    public Prelacion prioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
        return this;
    }

    public void setPrioridad(Prioridad prioridad) {
        this.prioridad = prioridad;
    }

    public Notario getNotario() {
        return notario;
    }

    public Prelacion notario(Notario notario) {
        this.notario = notario;
        return this;
    }

    public void setNotario(Notario notario) {
        this.notario = notario;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public Prelacion oficina(Oficina oficina) {
        this.oficina = oficina;
        return this;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    public Resultado getResultado() {
        return resultado;
    }

    public Prelacion resultado(Resultado resultado) {
        this.resultado = resultado;
        return this;
    }

    public void setResultado(Resultado resultado) {
        this.resultado = resultado;
    }

    public Status getStatus() {
        return status;
    }

    public Prelacion status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }
    
    public Status getStatusAnterior() {
        return this.statusAnterior;
    }

    public Prelacion statusAnterior(Status status) {
        this.statusAnterior = status;
        return this;
    }

    public void setStatusAnterior(Status status) {
        this.statusAnterior = status;
    }

    public Usuario getUsuarioNotario() {
        return usuarioNotario;
    }

    public Prelacion usuarioNotario(Usuario usuario) {
        this.usuarioNotario = usuario;
        return this;
    }

    public void setUsuarioNotario(Usuario usuario) {
        this.usuarioNotario = usuario;
    }

    public Usuario getUsuarioVentanilla() {
        return usuarioVentanilla;
    }

    public Prelacion usuarioVentanilla(Usuario usuario) {
        this.usuarioVentanilla = usuario;
        return this;
    }

    public void setUsuarioVentanilla(Usuario usuario) {
        this.usuarioVentanilla = usuario;
    }

    public Usuario getUsuarioCoordinador() {
        return usuarioCoordinador;
    }

    public Prelacion usuarioCoordinador(Usuario usuario) {
        this.usuarioCoordinador = usuario;
        return this;
    }

    public void setUsuarioCoordinador(Usuario usuario) {
        this.usuarioCoordinador = usuario;
    }

    public Usuario getUsuarioCalificador() {
        return usuarioCalificador;
    }

    public Prelacion usuarioCalificador(Usuario usuario) {
        this.usuarioCalificador = usuario;
        return this;
    }

    public void setUsuarioCalificador(Usuario usuario) {
        this.usuarioCalificador = usuario;
    }

    public Usuario getUsuarioAnalista() {
        return usuarioAnalista;
    }

    public Prelacion usuarioAnalista(Usuario usuario) {
        this.usuarioAnalista = usuario;
        return this;
    }

    public void setUsuarioAnalista(Usuario usuario) {
        this.usuarioAnalista = usuario;
    }

    public Set<PrelacionServicio> getPrelacionServiciosParaPrelacions() {
		return prelacionServiciosParaPrelacions;
	}

	public void setPrelacionServiciosParaPrelacions(Set<PrelacionServicio> prelacionServiciosParaPrelacions) {
		this.prelacionServiciosParaPrelacions = prelacionServiciosParaPrelacions;
	}

	public Usuario getUsuarioCapVal() {
		return usuarioCapVal;
	}

	public void setUsuarioCapVal(Usuario usuarioCapVal) {
		this.usuarioCapVal = usuarioCapVal;
	}

	public Integer getTipoEntrega() {
		return tipoEntrega;
	}

	public void setTipoEntrega(Integer tipoEntrega) {
		this.tipoEntrega = tipoEntrega;
	}

	public String getPkcs7() {
		return pkcs7;
	}

	public void setPkcs7(String pkcs7) {
		this.pkcs7 = pkcs7;
	}

	public Integer getSecuencia() {
		return secuencia;
	}

	public void setSecuencia(Integer secuencia) {
		this.secuencia = secuencia;
	}

	public String getPolitica() {
		return politica;
	}

	public void setPolitica(String politica) {
		this.politica = politica;
	}

	public String getDigestion() {
		return digestion;
	}

	public void setDigestion(String digestion) {
		this.digestion = digestion;
	}

	public Integer getSecuenciaTS() {
		return secuenciaTS;
	}

	public void setSecuenciaTS(Integer secuenciaTS) {
		this.secuenciaTS = secuenciaTS;
	}

	public Date getEstampilla() {
		return estampilla;
	}

	public void setEstampilla(Date estampilla) {
		this.estampilla = estampilla;
	}

  public Boolean getTieneTermino() {
		return this.tieneTermino;
	}

	public void setTieneTermino(Boolean tieneTermino) {
		this.tieneTermino = tieneTermino;
	}

  public Integer getDias() {
    return this.dias;
  }

  public void setDias(Integer dias) {
    this.dias = dias;
  }

  public Integer getHoras() {
    return this.horas;
  }

  public void setHoras(Integer horas) {
    this.horas = horas;
  }

	public Boolean getEmailEnviado() {
		return emailEnviado;
	}
	
	public Integer getNumCapturas() {
		return numCapturas==null?0:numCapturas;
	}

	public void setNumCapturas(Integer numCapturas) {
		this.numCapturas = numCapturas;
	}
	
	public Integer getNumPorSistema() {
		return numPorSistema==null?0:numPorSistema;
	}

	public void setNumPorSistema(Integer numPorSistema) {
		this.numPorSistema = numPorSistema;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Prelacion prelacion = (Prelacion) o;
        if (prelacion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), prelacion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    

    public TipoAclaracion getTipoAclaracion() {
		return tipoAclaracion;
	}

	public void setTipoAclaracion(TipoAclaracion tipoAclaracion) {
		this.tipoAclaracion = tipoAclaracion;
	}



    public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}



	@Override
    public String toString() {
        return "Prelacion{" +
            "id=" + getId() +
            ", consecutivo='" + getConsecutivo() + "'" +
            ", anio='" + getAnio() + "'" +
            ", emailEnviado='" + isEmailEnviado() + "'" +
            ", motivoRechazo='" + getMotivoRechazo() + "'" +
            ", motivoReasignacion='" + getMotivoReasignacion() + "'" +
            ", reingresado='" + getReingresado() + "'" +
            ", reproceso='" + getReproceso() + "'" +
            ", reprocesoCalificador='" + getReprocesoCalificador() + "'" +
            ", observacionesMotivo='" + getObservacionesMotivo() + "'" +
            ", hashFila='" + getHashFila() + "'" +
            ", fechaIngreso='" + getFechaIngreso() + "'" +
            "}";
    }




    @JsonIgnore
	public String getCadenaHash() {
		return "||" + id + "|" + consecutivo + "|" + anio + "|"
				+ emailEnviado + "|" + motivoRechazo
				+ "|" + reingresado + "|" + reproceso + "|"
				+ reprocesoCalificador + "|"
				+ observacionesAclaracion + "|" + observaciones + "|" + fechaIngreso
				+ "|"+  prioridad.getId() + "|" + oficina.getNombre() + "|" + status.getNombre() +
				"|" + fechaProgramadaEntrega + "|" + tipoAnte + "|"
				+ tipoEntrega + "|" + digestion
				+ "||";
	}
    @JsonIgnore
	public Set<PrelacionServicio> getPrelacionServicioParaPrelacion() {
		return prelacionServicioParaPrelacion;
	}

	public void setPrelacionServicioParaPrelacion(Set<PrelacionServicio> prelacionServicioParaPrelacion) {
		this.prelacionServicioParaPrelacion = prelacionServicioParaPrelacion;
	}

	public Date getFechaProgramadaEntrega() {
		return fechaProgramadaEntrega;
	}

	public void setFechaProgramadaEntrega(Date fechaProgramadaEntrega) {
		this.fechaProgramadaEntrega = fechaProgramadaEntrega;
	}

    public String getObservacionesAclaracion() {
        return observacionesAclaracion;
    }

    public void setObservacionesAclaracion(String observacionesAclaracion) {
        this.observacionesAclaracion = observacionesAclaracion;
    }

	public String getTipoBusqueda() {
		return tipoBusqueda;
	}

	public void setTipoBusqueda(String tipoBusqueda) {
		this.tipoBusqueda = tipoBusqueda;
	}

    public Observacion getObservacion() {
        return observacion;
    }

    public void setObservacion(Observacion observacion) {
        this.observacion = observacion;
    }

	public Set<PrelacionContratante> getPrelacionContratantesParaPrelacions() {
		return prelacionContratantesParaPrelacions;
	}

	public void setPrelacionContratantesParaPrelacions(Set<PrelacionContratante> prelacionContratantesParaPrelacions) {
		this.prelacionContratantesParaPrelacions = prelacionContratantesParaPrelacions;
	}

    public Boolean getPrimerRegistro() {
        return primerRegistro;
    }

    public void setPrimerRegistro(Boolean primerRegistro) {
        this.primerRegistro = primerRegistro;
    }

    public Boolean getAnteriorAbril81() {
        return anteriorAbril81;
    }

    public void setAnteriorAbril81(Boolean AnteriorAbril81) {
        this.anteriorAbril81 = anteriorAbril81;
    }   
    
    public Boolean getEs_digitalizado() {
		return es_digitalizado;
	}

	public void setEs_digitalizado(Boolean es_digitalizado) {
		this.es_digitalizado = es_digitalizado;
	}

	public Set<Suspension> getPrelacionesParaSuspensiones(){
    	return this.suspencionesParaPrelacion;
    }
    
    public Prelacion suspensionesParaPrelacion(Set<Suspension> suspenciones) {
    	this.suspencionesParaPrelacion=suspenciones;
    	return this;
    }
    
    public Prelacion addSuspencionesParaPrelacion(Suspension suspension) {
    	this.suspencionesParaPrelacion.add(suspension);
    	suspension.setPrelacion(this);
    	return this;
    }
    public Prelacion removeSuspencionesParaPrelacion(Suspension suspension) {
    	this.suspencionesParaPrelacion.remove(suspension);
    	suspension.setPrelacion(null);
    	return this;
    }
    
	public void setSuspensionesParaPrelacion(Set<Suspension> suspensions) {
        this.suspencionesParaPrelacion = suspensions;
    }

	public Boolean getIndEntrega() {
		return indEntrega;
	}

	public void setIndEntrega(Boolean indEntrega) {
		this.indEntrega = indEntrega;
    }

    public Long getSubindice() {
		return subindice;
	}

	public void setSubindice(Long subindice) {
		this.subindice = subindice;
    }
	
	
	
	  public Long getId_entrada() {
			return id_entrada;
		}

		public void setId_entrada(Long id_entrada) {
			this.id_entrada = id_entrada;
	    }
		
		public String getNumero_Entrada_Completo() {
			return Numero_Entrada_Completo;
		}

		public void setNumero_Entrada_Completo(String Numero_Entrada_Completo) {
			this.Numero_Entrada_Completo = Numero_Entrada_Completo;
		}

		public Date getFechaReingreso() {
			return fechaReingreso;
		}

		public void setFechaReingreso(Date fechaReingreso) {
			this.fechaReingreso = fechaReingreso;
		}

		public Boolean getVf() {
			return vf;
		}

		public void setVf(Boolean vf) {
			this.vf = vf;
		}

		public String getClaveConsulta() {
			return claveConsulta;
		}

		public void setClaveConsulta(String claveConsulta) {
			this.claveConsulta = claveConsulta;
		}
		
		
}
