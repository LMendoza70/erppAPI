package com.gisnet.erpp.domain;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

 import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

 /**
 * A SuspensionFirma.
 */
@Entity
@Table(name = "suspension_firma")
public class SuspensionFirma implements Serializable {

     private static final long serialVersionUID = 1L;

     @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "suspensionFirmaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "suspensionFirmaGenerator", sequenceName="suspension_firma_seq")
    private Long id;

     
    @Size(max = 12288)     
    @Column(name = "pkcs7", length = 12288)
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

    @Column(name = "fecha_firma")
	private Date fechaFirma;

    @ManyToOne(optional = false)
    @NotNull
    private Usuario usuario;

    @ManyToOne(optional = false)
    @NotNull
    private Suspension suspension;


    public String getFirma() {
		return firma;
	}

 	public void setFirma(String firma) {
		this.firma = firma;
	}

 	public Long getId() {
        return id;
    }

     public void setId(Long id) {
        this.id = id;
    }

     public String getPkcs7() {
        return pkcs7;
    }

     public SuspensionFirma pkcs7(String pkcs7) {
        this.pkcs7 = pkcs7;
        return this;
    }

     public void setPkcs7(String pkcs7) {
        this.pkcs7 = pkcs7;
    }

     public Integer getSecuencia() {
        return secuencia;
    }

     public SuspensionFirma secuencia(Integer secuencia) {
        this.secuencia = secuencia;
        return this;
    }

     public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

     public String getPolitica() {
        return politica;
    }

     public SuspensionFirma politica(String politica) {
        this.politica = politica;
        return this;
    }

     public void setPolitica(String politica) {
        this.politica = politica;
    }

     public String getDigestion() {
        return digestion;
    }

     public SuspensionFirma digestion(String digestion) {
        this.digestion = digestion;
        return this;
    }

     public void setDigestion(String digestion) {
        this.digestion = digestion;
    }

     public Integer getSecuenciaTS() {
        return secuenciaTS;
    }

     public SuspensionFirma secuenciaTS(Integer secuenciaTS) {
        this.secuenciaTS = secuenciaTS;
        return this;
    }

     public void setSecuenciaTS(Integer secuenciaTS) {
        this.secuenciaTS = secuenciaTS;
    }

     public Date getEstampilla() {
        return estampilla;
    }

     public SuspensionFirma estampilla(Date estampilla) {
        this.estampilla = estampilla;
        return this;
    }

     public void setEstampilla(Date estampilla) {
        this.estampilla = estampilla;
    }

    public Date getFechaFirma() {
        return fechaFirma;
    }

    public void setFechaFirma(Date fechaFirma) {
        this.fechaFirma = fechaFirma;
    }

     public Usuario getUsuario() {
        return usuario;
    }

     public SuspensionFirma usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

     public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
     
 	public Suspension getSuspension() {
		return suspension;
	}

 	public void setSuspension(Suspension suspension) {
		this.suspension = suspension;
	}

 	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SuspensionFirma suspensionFirma = (SuspensionFirma) o;
        if (suspensionFirma.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), suspensionFirma.getId());
    }

     @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

     @Override
    public String toString() {
        return "SuspensionFirma{" +
            "id=" + getId() +
            ", pkcs7='" + getPkcs7() + "'" +
            ", secuencia='" + getSecuencia() + "'" +
            ", politica='" + getPolitica() + "'" +
            ", digestion='" + getDigestion() + "'" +
            ", secuenciaTS='" + getSecuenciaTS() + "'" +
            ", estampilla='" + getEstampilla() + "'" +
            ", suspension='"+ getSuspension() + "'" +
            "}";
    }
}