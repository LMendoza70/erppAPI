package com.gisnet.erpp.domain;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * A FoliosrDigital.
 */
@Entity
@Table(name = "foliosr_digital")
public class FoliosrDigital implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "foliosrDigitalGenerator")
    @SequenceGenerator(allocationSize = 1, name = "foliosrDigitalGenerator", sequenceName="foliosr_digital_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "num_folio_registral")
    private Integer numFolioRegistral;

    @Column(name = "acto", length = 4)
    private String acto;

    @Column(name = "descripcion", length = 80)
    private String descripcion;

    @Column(name = "fecha_prelacion")
    private Date fechaPrelacion;
    
    @Column(name = "num_cont_prelacion")
    private Integer numContPrelacion;

    @Column(name = "num_cont_registro")
    private Integer numContRegistro;
    
    @Column(name = "escritura", length = 100)
    private String escritura;

    @Column(name = "fecha_digitalizacion")
    private Date fechaDigitalizacion;

    @Column(name = "observaciones", length = 200)
    private String observaciones;

    @ManyToOne(optional = false)
    @NotNull    
    private TipoFolio tipoFolio;

    @ManyToOne()
    private TipoActo tipoActo;

    @ManyToOne ()
    private Oficina oficina;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    public Integer getNumFolioRegistral() {
        return numFolioRegistral;
    }

    public void setNumFolioRegistral(Integer numFolioRegistral) {
        this.numFolioRegistral = numFolioRegistral;
    }

    public String getActo() {
        return acto;
    }

    public void setActo(String acto) {
        this.acto = acto;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Date getFechaPrelacion() {
        return fechaPrelacion;
    }

    public void setFechaPrelacion(Date fechaPrelacion) {
        this.fechaPrelacion = fechaPrelacion;
    }

    public Integer getNumContPrelacion() {
        return numContPrelacion;
    }

    public void setNumContPrelacion(Integer numContPrelacion) {
        this.numContPrelacion = numContPrelacion;
    }

    public Integer getNumContRegistro() {
        return numContRegistro;
    }

    public void setNumContRegistro(Integer numContRegistro) {
        this.numContRegistro = numContRegistro;
    }

    public String getEscritura() {
        return escritura;
    }

    public void setEscritura(String escritura) {
        this.escritura = escritura;
    }

    public Date getFechaDigitalizacion() {
        return fechaDigitalizacion;
    }

    public void setFechaDigitalizacion(Date fechaDigitalizacion) {
        this.fechaDigitalizacion = fechaDigitalizacion;
    }

    public String getObservaciones() {
        return observaciones;
    }

    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public TipoFolio getTipoFolio() {
		return tipoFolio;
	}

	public void setTipoFolio(TipoFolio tipoFolio) {
		this.tipoFolio = tipoFolio;
	}

    public TipoActo getTipoActo() {
        return tipoActo;
    }

    public void setTipoActo(TipoActo tipoActo) {
        this.tipoActo = tipoActo;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FoliosrDigital foliosrDigital = (FoliosrDigital) o;
        if (foliosrDigital.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), foliosrDigital.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "FoliosrDigital{" +
            "id=" + getId() +
            "}";
    }

}
