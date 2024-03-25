package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Motivo.
 */
@Entity
@Table(name = "motivo")
public class Motivo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "motivoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "motivoGenerator", sequenceName="motivo_seq")
    private Long id;

    @Size(max = 300)
    @Column(name = "nombre", length = 300)
    private String nombre;

    @Column(name = "activo")
    private Boolean activo;

    @OneToMany(mappedBy = "motivo")
    @JsonIgnore
    private Set<Bitacora> bitacorasParaMotivos = new HashSet<>();

    @OneToMany(mappedBy = "motivo")
    @JsonIgnore
    private Set<Prelacion> prelacionesParaMotivos = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private TipoMotivo tipoMotivo;
    
    @OneToOne
    private Motivo motivoDesbloqueo;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Motivo nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isActivo() {
        return activo;
    }

    public Motivo activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Bitacora> getBitacorasParaMotivos() {
        return bitacorasParaMotivos;
    }

    public Motivo bitacorasParaMotivos(Set<Bitacora> bitacoras) {
        this.bitacorasParaMotivos = bitacoras;
        return this;
    }

    public Motivo addBitacorasParaMotivo(Bitacora bitacora) {
        this.bitacorasParaMotivos.add(bitacora);
        bitacora.setMotivo(this);
        return this;
    }

    public Motivo removeBitacorasParaMotivo(Bitacora bitacora) {
        this.bitacorasParaMotivos.remove(bitacora);
        bitacora.setMotivo(null);
        return this;
    }

    public void setBitacorasParaMotivos(Set<Bitacora> bitacoras) {
        this.bitacorasParaMotivos = bitacoras;
    }

    public Set<Prelacion> getPrelacionesParaMotivos() {
        return prelacionesParaMotivos;
    }

    public Motivo prelacionesParaMotivos(Set<Prelacion> prelacions) {
        this.prelacionesParaMotivos = prelacions;
        return this;
    }

    public Motivo addPrelacionesParaMotivo(Prelacion prelacion) {
        this.prelacionesParaMotivos.add(prelacion);
        prelacion.setMotivo(this);
        return this;
    }

    public Motivo removePrelacionesParaMotivo(Prelacion prelacion) {
        this.prelacionesParaMotivos.remove(prelacion);
        prelacion.setMotivo(null);
        return this;
    }

    public void setPrelacionesParaMotivos(Set<Prelacion> prelacions) {
        this.prelacionesParaMotivos = prelacions;
    }

    public TipoMotivo getTipoMotivo() {
        return tipoMotivo;
    }

    public Motivo tipoMotivo(TipoMotivo tipoMotivo) {
        this.tipoMotivo = tipoMotivo;
        return this;
    }

    public void setTipoMotivo(TipoMotivo tipoMotivo) {
        this.tipoMotivo = tipoMotivo;
    }

    public Motivo getMotivoDesbloqueo() {
		return motivoDesbloqueo;
	}

	public void setMotivoDesbloqueo(Motivo motivoDesbloqueo) {
		this.motivoDesbloqueo = motivoDesbloqueo;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Motivo motivo = (Motivo) o;
        if (motivo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), motivo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Motivo{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
