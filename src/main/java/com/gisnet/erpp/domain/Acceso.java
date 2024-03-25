package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A Acceso.
 */
@Entity
@Table(name = "acceso")
public class Acceso implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "accesoGenerator")
    @SequenceGenerator(allocationSize = 1,name = "accesoGenerator", sequenceName="acceso_seq")
    private Long id;

    @Column(name = "fecha_ini")
    private Date fechaIni;

    @Column(name = "fecha_fin")
    private Date fechaFin;
    
    @Column(name = "logout_error")
    private Boolean logoutError; 

    public Boolean getLogoutError() {
		return logoutError;
	}

	public void setLogoutError(Boolean logoutError) {
		this.logoutError = logoutError;
	}

	@ManyToOne(optional = false)
    @NotNull
    private Usuario usuario;
    
    @Size(max = 100)
    @Column(name = "session_id", length = 40)
    @NotNull
    private String sessionId;
    
    @Column(name = "timeout")
    private Boolean timeout;
    
    public String getSessionId() {
		return sessionId;
	}

	public void setSessionId(String sessionId) {
		this.sessionId = sessionId;
	}

	public Boolean getTimeout() {
		return timeout;
	}

	public void setTimeout(Boolean timeout) {
		this.timeout = timeout;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFechaIni() {
        return fechaIni;
    }

    public Acceso fechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
        return this;
    }

    public void setFechaIni(Date fechaIni) {
        this.fechaIni = fechaIni;
    }

    public Date getFechaFin() {
        return fechaFin;
    }

    public Acceso fechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
        return this;
    }

    public void setFechaFin(Date fechaFin) {
        this.fechaFin = fechaFin;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Acceso usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Acceso acceso = (Acceso) o;
        if (acceso.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), acceso.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Acceso{" +
            "id=" + getId() +
            ", fechaIni='" + getFechaIni() + "'" +
            ", fechaFin='" + getFechaFin() + "'" +
            "}";
    }
}
