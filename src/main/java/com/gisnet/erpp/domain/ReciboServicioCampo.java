package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A ReciboServicioCampo.
 */
@Entity
@Table(name = "recibo_servicio_campo")
public class ReciboServicioCampo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "reciboServicioCampoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "reciboServicioCampoGenerator", sequenceName="recibo_servicio_campo_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "valor", length = 100)
    private String valor;

    @ManyToOne(optional = false)
    @NotNull
    private ConfCotizador confCotizador;

    @ManyToOne(optional = false)
    @NotNull
    private ReciboServicio reciboServicio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getValor() {
        return valor;
    }

    public ReciboServicioCampo valor(String valor) {
        this.valor = valor;
        return this;
    }

    public void setValor(String valor) {
        this.valor = valor;
    }

    public ConfCotizador getConfCotizador() {
        return confCotizador;
    }

    public ReciboServicioCampo confCotizador(ConfCotizador confCotizador) {
        this.confCotizador = confCotizador;
        return this;
    }

    public void setConfCotizador(ConfCotizador confCotizador) {
        this.confCotizador = confCotizador;
    }

    public ReciboServicio getReciboServicio() {
        return reciboServicio;
    }

    public ReciboServicioCampo reciboServicio(ReciboServicio reciboServicio) {
        this.reciboServicio = reciboServicio;
        return this;
    }

    public void setReciboServicio(ReciboServicio reciboServicio) {
        this.reciboServicio = reciboServicio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ReciboServicioCampo reciboServicioCampo = (ReciboServicioCampo) o;
        if (reciboServicioCampo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), reciboServicioCampo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ReciboServicioCampo{" +
            "id=" + getId() +
            ", valor='" + getValor() + "'" +
            "}";
    }
}
