package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;

import org.hibernate.annotations.CacheConcurrencyStrategy;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Oficina.
 */
@Entity
@Table(name = "oficina")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Oficina implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "oficinaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "oficinaGenerator", sequenceName="oficina_seq")
    private Long id;

    @Size(max = 50)
    @Column(name = "nombre", length = 50)
    private String nombre;

    @Size(max = 4)
    @Column(name = "num_oficina", length = 4)
    private String numOficina;

    @Column(name="nu_region", length=2)
    private Integer nuRegion;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "req_digitalizar")
    private Boolean req_digitalizar;

    @OneToMany(mappedBy = "oficina")
    @JsonIgnore
    private Set<Municipio> municipiosParaOficinas = new HashSet<>();

    @OneToMany(mappedBy = "oficina")
    @JsonIgnore
    private Set<Parametro> parametrosParaOficinas = new HashSet<>();

    @OneToMany(mappedBy = "oficina")
    @JsonIgnore
    private Set<Prelacion> prelacionesParaOficinas = new HashSet<>();

    @OneToMany(mappedBy = "oficina")
    @JsonIgnore
    private Set<Telefono> telefonosParaOficinas = new HashSet<>();

    @OneToMany(mappedBy = "oficina")
    @JsonIgnore
    private Set<Usuario> usuariosParaOficinas = new HashSet<>();
    
    @OneToMany(mappedBy = "oficina")
    @JsonIgnore
    private Set<SeccionPorOficina> seccionPorOficinasParaOficinas = new HashSet<>();
    
    @OneToMany(mappedBy = "oficina")
    @JsonIgnore
    private Set<Predio> prediosParaOficinas = new HashSet<>();
    
    @OneToMany(mappedBy = "oficina")
    @JsonIgnore
    private Set<Mueble> mueblesParaOficinas = new HashSet<>();


    @ManyToOne(optional = false)
    @NotNull
    private Estado estado;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Oficina nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getNumOficina() {
        return numOficina;
    }

    public Oficina numOficina(String numOficina) {
        this.numOficina = numOficina;
        return this;
    }

    public void setNumOficina(String numOficina) {
        this.numOficina = numOficina;
    }

    public Integer getNuRegion() {
        return nuRegion;
    }

    public Oficina nuRegion(Integer nuRegion) {
        this.nuRegion = nuRegion;
        return this;
    }

    public void setNuRegion(Integer nuRegion) {
        this.nuRegion = nuRegion;
    }

    public Boolean isActivo() {
        return activo;
    }

    public Oficina activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Boolean getReq_digitalizar() {
		return req_digitalizar;
	}

	public void setReq_digitalizar(Boolean req_digitalizar) {
		this.req_digitalizar = req_digitalizar;
	}

	public Set<Municipio> getMunicipiosParaOficinas() {
        return municipiosParaOficinas;
    }

    public Oficina municipiosParaOficinas(Set<Municipio> municipios) {
        this.municipiosParaOficinas = municipios;
        return this;
    }

    public Oficina addMunicipiosParaOficina(Municipio municipio) {
        this.municipiosParaOficinas.add(municipio);
        municipio.setOficina(this);
        return this;
    }

    public Oficina removeMunicipiosParaOficina(Municipio municipio) {
        this.municipiosParaOficinas.remove(municipio);
        municipio.setOficina(null);
        return this;
    }

    public void setMunicipiosParaOficinas(Set<Municipio> municipios) {
        this.municipiosParaOficinas = municipios;
    }

    public Set<Parametro> getParametrosParaOficinas() {
        return parametrosParaOficinas;
    }

    public Oficina parametrosParaOficinas(Set<Parametro> parametros) {
        this.parametrosParaOficinas = parametros;
        return this;
    }

    public Oficina addParametrosParaOficina(Parametro parametro) {
        this.parametrosParaOficinas.add(parametro);
        parametro.setOficina(this);
        return this;
    }

    public Oficina removeParametrosParaOficina(Parametro parametro) {
        this.parametrosParaOficinas.remove(parametro);
        parametro.setOficina(null);
        return this;
    }

    public void setParametrosParaOficinas(Set<Parametro> parametros) {
        this.parametrosParaOficinas = parametros;
    }

    public Set<Prelacion> getPrelacionesParaOficinas() {
        return prelacionesParaOficinas;
    }

    public Oficina prelacionesParaOficinas(Set<Prelacion> prelacions) {
        this.prelacionesParaOficinas = prelacions;
        return this;
    }

    public Oficina addPrelacionesParaOficina(Prelacion prelacion) {
        this.prelacionesParaOficinas.add(prelacion);
        prelacion.setOficina(this);
        return this;
    }

    public Oficina removePrelacionesParaOficina(Prelacion prelacion) {
        this.prelacionesParaOficinas.remove(prelacion);
        prelacion.setOficina(null);
        return this;
    }

    public void setPrelacionesParaOficinas(Set<Prelacion> prelacions) {
        this.prelacionesParaOficinas = prelacions;
    }

    public Set<Telefono> getTelefonosParaOficinas() {
        return telefonosParaOficinas;
    }

    public Oficina telefonosParaOficinas(Set<Telefono> telefonos) {
        this.telefonosParaOficinas = telefonos;
        return this;
    }

    public Oficina addTelefonosParaOficina(Telefono telefono) {
        this.telefonosParaOficinas.add(telefono);
        telefono.setOficina(this);
        return this;
    }

    public Oficina removeTelefonosParaOficina(Telefono telefono) {
        this.telefonosParaOficinas.remove(telefono);
        telefono.setOficina(null);
        return this;
    }

    public void setTelefonosParaOficinas(Set<Telefono> telefonos) {
        this.telefonosParaOficinas = telefonos;
    }

    public Set<Usuario> getUsuariosParaOficinas() {
        return usuariosParaOficinas;
    }

    public Oficina usuariosParaOficinas(Set<Usuario> usuarios) {
        this.usuariosParaOficinas = usuarios;
        return this;
    }

    public Oficina addUsuariosParaOficina(Usuario usuario) {
        this.usuariosParaOficinas.add(usuario);
        usuario.setOficina(this);
        return this;
    }

    public Oficina removeUsuariosParaOficina(Usuario usuario) {
        this.usuariosParaOficinas.remove(usuario);
        usuario.setOficina(null);
        return this;
    }

    public void setUsuariosParaOficinas(Set<Usuario> usuarios) {
        this.usuariosParaOficinas = usuarios;
    }
    
    public Set<Predio> getPrediosParaOficinas() {
		return prediosParaOficinas;
	}

	public void setPrediosParaOficinas(Set<Predio> prediosParaOficinas) {
		this.prediosParaOficinas = prediosParaOficinas;
	}

	public Set<Mueble> getMueblesParaOficinas() {
		return mueblesParaOficinas;
	}

	public void setMueblesParaOficinas(Set<Mueble> mueblesParaOficinas) {
		this.mueblesParaOficinas = mueblesParaOficinas;
	}

	public Estado getEstado() {
        return estado;
    }

    public Oficina estado(Estado estado) {
        this.estado = estado;
        return this;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Oficina oficina = (Oficina) o;
        if (oficina.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), oficina.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Oficina{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", numOficina='" + getNumOficina() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }

	public Set<SeccionPorOficina> getSeccionPorOficinasParaOficinas() {
		return seccionPorOficinasParaOficinas;
	}

	public void setSeccionPorOficinasParaOficinas(Set<SeccionPorOficina> seccionPorOficinasParaOficinas) {
		this.seccionPorOficinasParaOficinas = seccionPorOficinasParaOficinas;
	}
}
