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
 * A TipoUsuario.
 */
@Entity
@Table(name = "tipo_usuario")
@Cacheable
@org.hibernate.annotations.Cache(usage = CacheConcurrencyStrategy.READ_ONLY)
public class TipoUsuario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoUsuarioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoUsuarioGenerator", sequenceName="tipo_usuario_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;
    
    @Column(name = "registro")
    private Boolean registro;
    
    @ManyToOne
    private Rol rol;

    
    @OneToMany(mappedBy = "tipoUsuario")
    @JsonIgnore
    private Set<Usuario> usuariosParaTipoUsuarios = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public TipoUsuario nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Boolean getRegistro() {
		return registro;
	}

	public void setRegistro(Boolean registro) {
		this.registro = registro;
	}

	public Set<Usuario> getUsuariosParaTipoUsuarios() {
        return usuariosParaTipoUsuarios;
    }

    public TipoUsuario usuariosParaTipoUsuarios(Set<Usuario> usuarios) {
        this.usuariosParaTipoUsuarios = usuarios;
        return this;
    }

    public TipoUsuario addUsuariosParaTipoUsuario(Usuario usuario) {
        this.usuariosParaTipoUsuarios.add(usuario);
        usuario.setTipoUsuario(this);
        return this;
    }

    public TipoUsuario removeUsuariosParaTipoUsuario(Usuario usuario) {
        this.usuariosParaTipoUsuarios.remove(usuario);
        usuario.setTipoUsuario(null);
        return this;
    }

    public void setUsuariosParaTipoUsuarios(Set<Usuario> usuarios) {
        this.usuariosParaTipoUsuarios = usuarios;
    }
    
    public Rol getRol() {
		return rol;
	}

	public void setRol(Rol rol) {
		this.rol = rol;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TipoUsuario tipoUsuario = (TipoUsuario) o;
        if (tipoUsuario.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), tipoUsuario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "TipoUsuario{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            "}";
    }
}
