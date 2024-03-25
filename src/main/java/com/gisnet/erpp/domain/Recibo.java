package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.gisnet.erpp.util.UtilFecha;

import javax.persistence.*;
//import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigInteger;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Recibo.
 */
@Entity
@Table(name = "recibo")
public class Recibo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reciboGenerator")
    @SequenceGenerator(allocationSize = 1, name = "reciboGenerator", sequenceName="recibo_seq")
    private Long id;

    @Column(name = "no_recibo")
    private Integer noRecibo;

    @Column(name = "monto", precision=10, scale=2)
    private BigDecimal monto;

    @Column(name = "fecha")
    private Date fecha;

    @Column(name = "cuenta")
    private String cuenta;

    @Column(name = "referencia")
    private String referencia;

    @OneToMany(mappedBy = "recibo")
    private Set<ReciboConcepto> reciboConceptosParaRecibos = new HashSet<>();

    @OneToMany(mappedBy = "recibo")
    @JsonIgnore
    private Set<ReciboServicio> reciboServiciosParaRecibos = new HashSet<>();
    
    @OneToMany(mappedBy = "recibo")
    @JsonIgnore
    private Set<ActoFolioRecibo> actoFolioRecibosParaRecibos = new HashSet<>();

    @ManyToOne
    private Prelacion prelacion;
    
    @ManyToOne
    private SolicitudDevolucion solicitudDevolucion;
    
    @Column(name = "generado_en_linea")
    private Boolean generadoEnLinea;
    
    @Column(name = "pagado_en_linea")
    private Boolean pagadoEnLinea;
    
    public String getDescripcion() {
    	return this.toString();
    }
    
    public Set<ActoFolioRecibo> getActoFolioRecibosParaRecibos() {
		return actoFolioRecibosParaRecibos;
	}

	public void setActoFolioRecibosParaRecibos(Set<ActoFolioRecibo> actoFolioRecibosParaRecibos) {
		this.actoFolioRecibosParaRecibos = actoFolioRecibosParaRecibos;
	}

	public Boolean getGeneradoEnLinea() {
		return generadoEnLinea;
	}

	public void setGeneradoEnLinea(Boolean generadoEnLinea) {
		this.generadoEnLinea = generadoEnLinea;
	}

	public Boolean getPagadoEnLinea() {
		return pagadoEnLinea;
	}

	public void setPagadoEnLinea(Boolean pagadoEnLinea) {
		this.pagadoEnLinea = pagadoEnLinea;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNoRecibo() {
        return noRecibo;
    }

    public Recibo noRecibo(Integer noRecibo) {
        this.noRecibo = noRecibo;
        return this;
    }

    public void setNoRecibo(Integer noRecibo) {
        this.noRecibo = noRecibo;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public Recibo monto(BigDecimal monto) {
        this.monto = monto;
        return this;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Date getFecha() {
        return fecha;
    }

    public Recibo fecha(Date fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public String getCuenta() {
        return cuenta;
    }

    public void setCuenta(String cuenta) {
        this.cuenta = cuenta;
    }

    public String getReferencia() {
        return referencia;
    }

    /*public Recibo referencia(Integer referencia) {
        this.referencia = referencia;
        return this;
    } */

    public void setReferencia(String referencia) {
        this.referencia = referencia;
    }

    public Set<ReciboConcepto> getReciboConceptosParaRecibos() {
        return reciboConceptosParaRecibos;
    }

    public Recibo reciboConceptosParaRecibos(Set<ReciboConcepto> reciboConceptos) {
        this.reciboConceptosParaRecibos = reciboConceptos;
        return this;
    }

    public Recibo addReciboConceptosParaRecibo(ReciboConcepto reciboConcepto) {
        this.reciboConceptosParaRecibos.add(reciboConcepto);
        reciboConcepto.setRecibo(this);
        return this;
    }

    public Recibo removeReciboConceptosParaRecibo(ReciboConcepto reciboConcepto) {
        this.reciboConceptosParaRecibos.remove(reciboConcepto);
        reciboConcepto.setRecibo(null);
        return this;
    }

    public void setReciboConceptosParaRecibos(Set<ReciboConcepto> reciboConceptos) {
        this.reciboConceptosParaRecibos = reciboConceptos;
    }

    public Set<ReciboServicio> getReciboServiciosParaRecibos() {
        return reciboServiciosParaRecibos;
    }

    public Recibo reciboServiciosParaRecibos(Set<ReciboServicio> reciboServicios) {
        this.reciboServiciosParaRecibos = reciboServicios;
        return this;
    }

    public Recibo addReciboServiciosParaRecibo(ReciboServicio reciboServicio) {
        this.reciboServiciosParaRecibos.add(reciboServicio);
        reciboServicio.setRecibo(this);
        return this;
    }

    public Recibo removeReciboServiciosParaRecibo(ReciboServicio reciboServicio) {
        this.reciboServiciosParaRecibos.remove(reciboServicio);
        reciboServicio.setRecibo(null);
        return this;
    }

    public void setReciboServiciosParaRecibos(Set<ReciboServicio> reciboServicios) {
        this.reciboServiciosParaRecibos = reciboServicios;
    }

    public Prelacion getPrelacion() {
        return prelacion;
    }

    public Recibo prelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
        return this;
    }

    public void setPrelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Recibo recibo = (Recibo) o;
        if (recibo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), recibo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
    	StringBuilder sb = new StringBuilder();
    	sb.append("noRecibo= ").append(noRecibo);
    	sb.append(", monto=").append(monto);
    	sb.append(", fecha=").append(UtilFecha.formatToDatePattern(fecha));
    	sb.append(", referencia=").append(referencia);
        return sb.toString();
    }

	public SolicitudDevolucion getSolicitudDevolucion() {
		return solicitudDevolucion;
	}

	public void setSolicitudDevolucion(SolicitudDevolucion solicitudDevolucion) {
		this.solicitudDevolucion = solicitudDevolucion;
	}
}
