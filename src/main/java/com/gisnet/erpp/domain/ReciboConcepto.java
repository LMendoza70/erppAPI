package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * A ReciboConcepto.
 */
@Entity
@Table(name = "recibo_concepto")
public class ReciboConcepto implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reciboConceptoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "reciboConceptoGenerator", sequenceName="recibo_concepto_seq")
    private Long id;

    @Column(name = "monto", precision=10, scale=2)
    private BigDecimal monto;

    @ManyToOne(optional = false)
    @NotNull
    private Recibo recibo;
    
    @ManyToOne(optional = false)
    @NotNull
    private ConceptoPago conceptoPago;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BigDecimal getMonto() {
        return monto;
    }

    public ReciboConcepto monto(BigDecimal monto) {
        this.monto = monto;
        return this;
    }

    public void setMonto(BigDecimal monto) {
        this.monto = monto;
    }

    public Recibo getRecibo() {
        return recibo;
    }

    public ReciboConcepto recibo(Recibo recibo) {
        this.recibo = recibo;
        return this;
    }

    public void setRecibo(Recibo recibo) {
        this.recibo = recibo;
    }
    
	public ConceptoPago getConceptoPago() {
		return conceptoPago;
	}


    public void setConceptoPago(ConceptoPago conceptoPago) {
		this.conceptoPago = conceptoPago;
	}
    
  
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReciboConcepto reciboConcepto = (ReciboConcepto) o;
        if (reciboConcepto.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reciboConcepto.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

	@Override
	public String toString() {
		return "ReciboConcepto [id=" + id + ", monto=" + monto + ", recibo=" + recibo + ", conceptoPago=" + conceptoPago
				+ "]";
	}

  
}
