package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import org.exolab.castor.types.DateDescriptor;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Date;
import java.util.Objects;

/**
 * A Certificado.
 */
@Entity
@Table(name = "certificado")
public class Certificado implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "certificadoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "certificadoGenerator", sequenceName="certificado_seq")
    private Long id;

    @NotNull
    @Column(name = "datos", nullable = false)
    private String datos;

    @Size(max = 4000)
    @Column(name = "pkcs7", length = 4000)
    private String pkcs7;
    
    @Size(max = 4000)
    @Column(name = "firma", length = 4000)
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
    private LocalDate estampilla;

    @ManyToOne(optional = false)
    @NotNull
    private Usuario usuario;

    @ManyToOne(optional = false)
    @NotNull
    private PrelacionServicio prelacionServicio;

    @Size(max = 4000)
    @Column(name = "historial", length = 4000)
    private String historial;


    @Size(max = 200)
    @Column(name = "a_favor_de", length = 200)
    private String aFavorDe;

    @Size(max = 400)
    @Column(name = "direccion", length = 400)
    private String direccion;

    @Size(max = 100)
    @Column(name = "director", length = 100)
    private String director;

    
    @Size(max = 100)
    @Column(name = "directorCatastro", length = 100)
    private String directorCatastro;

    @Size(max = 100)
    @Column(name = "propietarios", length = 100)
    private String propietarios;

  
    @Column(name = "fecha_plano_cartografico")
    private Date fechaPlanoCartografico;


    @Column(name = "tipoRespuesta")
    private Integer tipoRespuesta;

    

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDatos() {
        return datos;
    }

    public Certificado datos(String datos) {
        this.datos = datos;
        return this;
    }

    public void setDatos(String datos) {
        this.datos = datos;
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

    public Certificado secuencia(Integer secuencia) {
        this.secuencia = secuencia;
        return this;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

    public String getPolitica() {
        return politica;
    }

    public Certificado politica(String politica) {
        this.politica = politica;
        return this;
    }

    public void setPolitica(String politica) {
        this.politica = politica;
    }

    public String getDigestion() {
        return digestion;
    }

    public Certificado digestion(String digestion) {
        this.digestion = digestion;
        return this;
    }

    public void setDigestion(String digestion) {
        this.digestion = digestion;
    }

    public Integer getSecuenciaTS() {
        return secuenciaTS;
    }

    public Certificado secuenciaTS(Integer secuenciaTS) {
        this.secuenciaTS = secuenciaTS;
        return this;
    }

    public void setSecuenciaTS(Integer secuenciaTS) {
        this.secuenciaTS = secuenciaTS;
    }

    public LocalDate getEstampilla() {
        return estampilla;
    }

    public Certificado estampilla(LocalDate estampilla) {
        this.estampilla = estampilla;
        return this;
    }

    public void setEstampilla(LocalDate estampilla) {
        this.estampilla = estampilla;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public Certificado usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public PrelacionServicio getPrelacionServicio() {
        return prelacionServicio;
    }

    public Certificado prelacionServicio(PrelacionServicio prelacionServicio) {
        this.prelacionServicio = prelacionServicio;
        return this;
    }

    public void setPrelacionServicio(PrelacionServicio prelacionServicio) {
        this.prelacionServicio = prelacionServicio;
    }
    


    public String getHistorial() {
        return historial;
    }

    public Certificado historial(String historial) {
        this.historial = historial;
        return this;
    }

    public void setHistorial(String historial) {
        this.historial = historial;
    }

    

    public String getFirma() {
		return firma;
	}

	public void setFirma(String firma) {
		this.firma = firma;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Certificado certificado = (Certificado) o;
        if (certificado.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), certificado.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Certificado{" +
            "id=" + getId() +
            ", datos='" + getDatos() + "'" +
            ", pkcs7='" + getPkcs7() + "'" +
            ", firma='" + getFirma() + "'" +
            ", secuencia='" + getSecuencia() + "'" +
            ", politica='" + getPolitica() + "'" +
            ", digestion='" + getDigestion() + "'" +
            ", secuenciaTS='" + getSecuenciaTS() + "'" +
            ", estampilla='" + getEstampilla() + "'" +
            "}";
    }

	/**
	 * @return the fechaPlanoCartografico
	 */
	public Date getFechaPlanoCartografico() {
		return fechaPlanoCartografico;
	}

	/**
	 * @param fechaPlanoCartografico the fechaPlanoCartografico to set
	 */
	public void setFechaPlanoCartografico(Date fechaPlanoCartografico) {
		this.fechaPlanoCartografico = fechaPlanoCartografico;
	}

	/**
	 * @return the propietarios
	 */
	public String getPropietarios() {
		return propietarios;
	}

	/**
	 * @param propietarios the propietarios to set
	 */
	public void setPropietarios(String propietarios) {
		this.propietarios = propietarios;
	}

	/**
	 * @return the director
	 */
	public String getDirector() {
		return director;
	}

	/**
	 * @param director the director to set
	 */
	public void setDirector(String director) {
		this.director = director;
	}

	/**
	 * @return the direccion
	 */
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion the direccion to set
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
    }
    

    	/**
	 * @return the direccion
	 */
	public String getDirectorCatastro() {
		return direccion;
	}

	/**
	 * @param direccion the direccion to set
	 */
	public void setDirectorCatastro(String direccion) {
		this.direccion = direccion;
    }
    


	/**
	 * @return the aFavorDe
	 */
	public String getaFavorDe() {
		return aFavorDe;
	}

	/**
	 * @param aFavorDe the aFavorDe to set
	 */
	public void setaFavorDe(String aFavorDe) {
		this.aFavorDe = aFavorDe;
	}

	/**
	 * @return the tipoRespuesta
	 */
	public Integer getTipoRespuesta() {
		return tipoRespuesta;
	}

	/**
	 * @param tipoRespuesta the tipoRespuesta to set
	 */
	public void setTipoRespuesta(Integer tipoRespuesta) {
		this.tipoRespuesta = tipoRespuesta;
	}
}
