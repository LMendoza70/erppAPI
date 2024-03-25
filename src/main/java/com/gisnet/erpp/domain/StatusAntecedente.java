package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A StatusActo.
 */
@Entity
@Table(name = "status_ante")
public class StatusAntecedente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "statusAnteGenerator")
    @SequenceGenerator(allocationSize = 1, name = "statusAnteGenerator", sequenceName="status_ante_seq")
    private Long id;

    @NotNull
    @Size(max = 100)
    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @OneToMany(mappedBy = "statusAntecedente")
    @JsonIgnore
    private Set<PrelacionAnteCapHist> prelacionAnteCapHistsParaStatusAntecedente = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public StatusAntecedente nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Set<PrelacionAnteCapHist> getPrelacionAnteCapHistParaStatusAntecedente() {
		return prelacionAnteCapHistsParaStatusAntecedente;
	}

	public void setPrelacionAnteCapHistParaStatusAntecedente(Set<PrelacionAnteCapHist> prelacionAnteCapHistsParaStatusAntecedente) {
		this.prelacionAnteCapHistsParaStatusAntecedente = prelacionAnteCapHistsParaStatusAntecedente;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        StatusAntecedente statusActo = (StatusAntecedente) o;
        if (statusActo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), statusActo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "StatusAntecedente{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
