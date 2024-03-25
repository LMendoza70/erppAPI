package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A Bitacora.
 */
@Entity
@Table(name = "bitacora")
public class Bitacora implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bitacoraGenerator")
    @SequenceGenerator(allocationSize = 1, name = "bitacoraGenerator", sequenceName="bitacora_seq")
    private Long id;

    @Column(name = "fecha")
    private Date fecha;

    @Size(max = 4000)
    @Column(name = "observaciones", length = 4000)
    private String observaciones;

    @ManyToOne
    private Motivo motivo;

    @ManyToOne
    private TipoMotivo  tipoMotivo;

    @ManyToOne
    private TipoRechazo tipoRechazo;

    @ManyToOne
    private TipoAclaracion tipoAclaracion;

    @Size(max = 1000)
    @Column(name = "aclaraciones", length = 1000)
    private String aclaraciones;

    @ManyToOne(optional = false)
    @NotNull
    private Prelacion prelacion;

    @ManyToOne(optional = false)
    @NotNull
    private Status status;

    @ManyToOne(optional = false)
    @NotNull
    private Usuario usuario;

    @ManyToOne(optional = true)
    private Usuario registradorAnterior;

    @ManyToOne(optional = true)
    private Usuario registradorNuevo;

    @ManyToOne(optional = true)
    private Prioridad prioridadAnterior;

    @ManyToOne(optional = true)
    private Prioridad prioridadNuevo;
    
    @Column(name = "reproceso")
    private Boolean reproceso;
    
    @ManyToOne
    private Acto acto;


    public Bitacora(){}    

    public Bitacora(Date fecha, String observaciones, Prelacion prelacion, Status status, Usuario usuario) {
		super();
		this.fecha = fecha;
		this.observaciones = observaciones;
		this.prelacion = prelacion;
		this.status = status;
		this.usuario = usuario;
	}

	
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public Bitacora fecha(Date fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public Bitacora observaciones(String observaciones) {
        this.observaciones = observaciones;
        return this;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public Motivo getMotivo() {
        return motivo;
    }

    public Bitacora motivo(Motivo motivo) {
        this.motivo = motivo;
        return this;
    }

    public void setMotivo(Motivo motivo) {
        this.motivo = motivo;
    }


    public TipoMotivo getTipoMotivo() {
        return tipoMotivo;
    }

    public Bitacora tipoMotivo(TipoMotivo tipoMotivo) {
        this.tipoMotivo = tipoMotivo;
        return this;
    }

    public void setTipoMotivo(TipoMotivo tipoMotivo) {
        this.tipoMotivo = tipoMotivo;
    }
    

    public TipoRechazo getTipoRechazo() {
        return tipoRechazo;
    }

    public Bitacora tipoRechazo(TipoRechazo tipoRechazo) {
        this.tipoRechazo = tipoRechazo;
        return this;
    }

    public void setTipoRechazo(TipoRechazo tipoRechazo) {
        this.tipoRechazo = tipoRechazo;
    }


    public TipoAclaracion getTipoAclaracion() {
        return tipoAclaracion;
    }

    public Bitacora tipoAclaracion(TipoAclaracion tipoAclaracion) {
        this.tipoAclaracion = tipoAclaracion;
        return this;
    }

    public void setTipoAclaracion(TipoAclaracion tipoAclaracion) {
        this.tipoAclaracion = tipoAclaracion;
    }    


    public String getAclaraciones() {
        return observaciones;
    }

    public Bitacora aclaraciones(String aclaraciones) {
        this.aclaraciones = aclaraciones;
        return this;
    }

    public void setAclaraciones(String aclaraciones) {
        this.aclaraciones = aclaraciones;
    }



    public Prelacion getPrelacion() {
        return prelacion;
    }

    public Bitacora prelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
        return this;
    }

    public void setPrelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
    }

    public Status getStatus() {
        return status;
    }

    public Bitacora status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Bitacora usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Usuario getRegistradorAnterior() {
        return registradorAnterior;
    }

    public void setRegistradorAnterior(Usuario registradorAnterior) {
        this.registradorAnterior = registradorAnterior;
    }

    public Usuario getRegistradorNuevo() {
        return registradorNuevo;
    }

    public void setRegistradorNuevo(Usuario registradorNuevo) {
        this.registradorNuevo = registradorNuevo;
    }

    public Prioridad getPrioridadAnterior() {
        return prioridadAnterior;
    }

    public void setPrioridadAnterior(Prioridad prioridadAnterior) {
        this.prioridadAnterior = prioridadAnterior;
    }

    public Prioridad getPrioridadNuevo() {
        return prioridadNuevo;
    }

    public void setPrioridadNuevo(Prioridad prioridadNuevo) {
        this.prioridadNuevo = prioridadNuevo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bitacora bitacora = (Bitacora) o;
        if (bitacora.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bitacora.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Bitacora{" +
            "id=" + getId() +
            ", fecha='" + getFecha() + "'" +
            ", observaciones='" + getObservaciones() + "'" +
            "}";
    }

	public Boolean getReproceso() {
		return reproceso;
	}

	public void setReproceso(Boolean reproceso) {
		this.reproceso = reproceso;
	}

	public Acto getActo() {
		return acto;
	}

	public void setActo(Acto acto) {
		this.acto = acto;
	}
}
