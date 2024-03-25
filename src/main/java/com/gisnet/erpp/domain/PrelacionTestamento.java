package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;
import com.gisnet.erpp.vo.PrelacionVO;

/**
 * A Recibo.
 */
@Entity
@Table(name = "prelacion_testamento")
public class PrelacionTestamento implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prelacionTestamentoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "prelacionTestamentoGenerator", sequenceName="prelacion_testamento_seq")
    private Long id;

    @OneToOne(optional = false)
    @NotNull
    private Prelacion prelacion;

    @Column(name = "individual")
    @NotNull
    private Boolean individual;

    @Column(name = "grupal")
    @NotNull
    private Boolean grupal;

    @Column(name = "ya_depositado")
    @NotNull
    private Boolean yaDepositado;

    @Column(name = "numero_personas")
    @NotNull
    private Integer numeroPersonas;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Prelacion getPrelacion() {
        return prelacion;
    }

    public PrelacionTestamento prelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
        return this;
    }

    public void setPrelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
    }

    public Boolean getIndividual() {
        return individual;
    }

    public PrelacionTestamento individual(Boolean individual) {
        this.individual = individual;
        return this;
    }

    public void setIndividual(Boolean individual) {
        this.individual = individual;
    }

    public Boolean getGrupal() {
        return grupal;
    }

    public PrelacionTestamento grupal(Boolean grupal) {
        this.grupal = grupal;
        return this;
    }

    public void setGrupal(Boolean grupal) {
        this.grupal = grupal;
    }

    public Boolean getYaDepositado() {
        return yaDepositado;
    }

    public PrelacionTestamento yaDepositado(Boolean yaDepositado) {
        this.yaDepositado = yaDepositado;
        return this;
    }

    public void setYaDepositado(Boolean yaDepositado) {
        this.yaDepositado = yaDepositado;
    }
    
    public Integer getNumeroPersonas() {
        return numeroPersonas;
    }

    public PrelacionTestamento numeroPersonas(Integer numeroPersonas) {
        this.numeroPersonas = numeroPersonas;
        return this;
    }

    public void setNumeroPersonas(Integer numeroPersonas) {
        this.numeroPersonas = numeroPersonas;
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
        return "PrelacionTestamento{" +
            "id=" + getId() +
            "}";
    }
}
