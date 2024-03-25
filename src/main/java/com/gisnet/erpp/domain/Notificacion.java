package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;


/**
 * A Notification. 
 */
@Entity
@Table(name = "notificacion")
public class Notificacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notificacionGenerator")
    @SequenceGenerator(allocationSize = 1, name = "notificacionGenerator", sequenceName="notificacion_seq")
    private Long id;
    
    @ManyToOne
    private Usuario usuarioRemitente;
    
    @ManyToOne
    private Usuario usuarioDestinatario;
    
    @ManyToOne
    private TipoNotificacion tipoNotificacion;
    
    @ManyToOne(optional = false)
    @NotNull
    private StatusNotificacion statusNotificacion;
    
    @Size(max = 1000)
    @Column(name = "notificacion", length = 1000)
    private String notificacion;
    
    @Column(name = "fecha_envio")
    private Date fechaEnvio;

    @Column(name = "fecha_recepcion")
    private Date fechaRecepcion;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

	public Usuario getUsuarioRemitente() {
		return usuarioRemitente;
	}

	public void setUsuarioRemitente(Usuario usuarioRemitente) {
		this.usuarioRemitente = usuarioRemitente;
	}

	public Usuario getUsuarioDestinatario() {
		return usuarioDestinatario;
	}

	public void setUsuarioDestinatario(Usuario usuarioDestinatario) {
		this.usuarioDestinatario = usuarioDestinatario;
	}

	public TipoNotificacion getTipoNotificacion() {
		return tipoNotificacion;
	}

	public void setTipoNotificacion(TipoNotificacion tipoNotificacion) {
		this.tipoNotificacion = tipoNotificacion;
	}

	public StatusNotificacion getStatusNotificacion() {
		return statusNotificacion;
	}

	public void setStatusNotificacion(StatusNotificacion statusNotificacion) {
		this.statusNotificacion = statusNotificacion;
	}

	public String getNotificacion() {
		return notificacion;
	}

	public void setNotificacion(String notificacion) {
		this.notificacion = notificacion;
	}

	public Date getFechaEnvio() {
		return fechaEnvio;
	}

	public void setFechaEnvio(Date fechaEnvio) {
		this.fechaEnvio = fechaEnvio;
	}

	public Date getFechaRecepcion() {
		return fechaRecepcion;
	}

	public void setFechaRecepcion(Date fechaRecepcion) {
		this.fechaRecepcion = fechaRecepcion;
	}

}
