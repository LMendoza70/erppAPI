package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A ActoFirma.
 */
@Entity
@Table(name = "acto_firma")
public class ActoFirma implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "actoFirmaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "actoFirmaGenerator", sequenceName="acto_firma_seq")
    private Long id;

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

    @ManyToOne(optional = false)
    @NotNull
    private Usuario usuario;

    @ManyToOne(optional = false)
    @NotNull
    private Acto acto;

    @ManyToOne
    private Archivo archivo;
    
    @Column(name = "es_acto")
    	private Boolean esActo;
    	
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

    public ActoFirma pkcs7(String pkcs7) {
        this.pkcs7 = pkcs7;
        return this;
    }

    public void setPkcs7(String pkcs7) {
        this.pkcs7 = pkcs7;
    }

    public Integer getSecuencia() {
        return secuencia;
    }

    public ActoFirma secuencia(Integer secuencia) {
        this.secuencia = secuencia;
        return this;
    }

    public void setSecuencia(Integer secuencia) {
        this.secuencia = secuencia;
    }

    public String getPolitica() {
        return politica;
    }

    public ActoFirma politica(String politica) {
        this.politica = politica;
        return this;
    }

    public void setPolitica(String politica) {
        this.politica = politica;
    }

    public String getDigestion() {
        return digestion;
    }

    public ActoFirma digestion(String digestion) {
        this.digestion = digestion;
        return this;
    }

    public void setDigestion(String digestion) {
        this.digestion = digestion;
    }

    public Integer getSecuenciaTS() {
        return secuenciaTS;
    }

    public ActoFirma secuenciaTS(Integer secuenciaTS) {
        this.secuenciaTS = secuenciaTS;
        return this;
    }

    public void setSecuenciaTS(Integer secuenciaTS) {
        this.secuenciaTS = secuenciaTS;
    }

    public Date getEstampilla() {
        return estampilla;
    }

    public ActoFirma estampilla(Date estampilla) {
        this.estampilla = estampilla;
        return this;
    }

    public void setEstampilla(Date estampilla) {
        this.estampilla = estampilla;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public ActoFirma usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Acto getActo() {
        return acto;
    }

    public ActoFirma acto(Acto acto) {
        this.acto = acto;
        return this;
    }

    public void setActo(Acto acto) {
        this.acto = acto;
    }

    public Archivo getArchivo() {
        return archivo;
    }

    public ActoFirma archivo(Archivo archivo) {
        this.archivo = archivo;
        return this;
    }

    public void setArchivo(Archivo archivo) {
        this.archivo = archivo;
    }

    
    
    public Boolean getEsActo() {
		return esActo;
	}
    
    public ActoFirma esActo(Boolean esActo) {
    		this.esActo = esActo;
        return this;
    }
    

	public void setEsActo(Boolean esActo) {
		this.esActo = esActo;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ActoFirma actoFirma = (ActoFirma) o;
        if (actoFirma.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), actoFirma.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ActoFirma{" +
            "id=" + getId() +
            ", pkcs7='" + getPkcs7() + "'" +
            ", secuencia='" + getSecuencia() + "'" +
            ", politica='" + getPolitica() + "'" +
            ", digestion='" + getDigestion() + "'" +
            ", secuenciaTS='" + getSecuenciaTS() + "'" +
            ", estampilla='" + getEstampilla() + "'" +
            "}";
    }
}
