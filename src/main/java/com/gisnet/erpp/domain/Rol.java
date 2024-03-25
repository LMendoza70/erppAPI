package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Rol.
 */
@Entity
@Table(name = "rol")
public class Rol implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rolGenerator")
    @SequenceGenerator(allocationSize = 1, name = "rolGenerator", sequenceName="rol_seq")
    private Long id;

    @Size(max = 100)
    @Column(name = "nombre", length = 100)
    private String nombre;
    
    @Size(max = 100)
    @Column(name = "cve", length = 100)
    private String cve;

    @Size(max = 50)
    @Column(name = "icono", length = 50)
    private String icono;
    
    @Column(name = "orden")
    private Integer orden;
	
    @Column(name = "nivel", length = 2)
    private Integer nivel;
    
    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "rol")
    @JsonIgnore
    private Set<FuncionRol> funcionRolesParaRols = new HashSet<>();

    @OneToMany(mappedBy = "rol")
    @JsonIgnore
    private Set<UsuarioRol> usuarioRolesParaRols = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
	
	public Integer getNivel(){
		return nivel;
	}
	
	public void setNivel(Integer nivel){
		this.nivel = nivel;
	}

    public String getNombre() {
        return nombre;
    }

    public Rol nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Boolean isActivo() {
        return activo;
    }

    public Rol activo(Boolean activo) {
        this.activo = activo;
        return this;
    }
    
    public String getCve() {
		return cve;
	}

	public void setCve(String cve) {
		this.cve = cve;
	}

	public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<FuncionRol> getFuncionRolesParaRols() {
        return this.funcionRolesParaRols;
    }

    public Rol funcionRolesParaRols(Set<FuncionRol> funcionRols) {
        this.funcionRolesParaRols = funcionRols;
        return this;
    }

    public Rol addFuncionRolesParaRol(FuncionRol funcionRol) {
    	System.out.println("//here on addServiceParaRol");
        this.funcionRolesParaRols.add(funcionRol);
        funcionRol.setRol(this);
        return this;
    }

    public Rol removeFuncionRolesParaRol(FuncionRol funcionRol) {
        this.funcionRolesParaRols.remove(funcionRol);
        funcionRol.setRol(null);
        return this;
    }

    public void setFuncionRolesParaRols(Set<FuncionRol> funcionRols) {
    	funcionRols.forEach(x -> { x.setRol(this); funcionRolesParaRols.add(x);});    	
    }

    public Set<UsuarioRol> getUsuarioRolesParaRols() {
        return this.usuarioRolesParaRols;
    }

    public Rol usuarioRolesParaRols(Set<UsuarioRol> usuarioRols) {
        this.usuarioRolesParaRols = usuarioRols;
        return this;
    }

    public Rol addUsuarioRolesParaRol(UsuarioRol usuarioRol) {
        this.usuarioRolesParaRols.add(usuarioRol);
        usuarioRol.setRol(this);
        return this;
    }

    public Rol removeUsuarioRolesParaRol(UsuarioRol usuarioRol) {
        this.usuarioRolesParaRols.remove(usuarioRol);
        usuarioRol.setRol(null);
        return this;
    }

    public void setUsuarioRolesParaRols(Set<UsuarioRol> usuarioRols) {
        this.usuarioRolesParaRols = usuarioRols;
    }
    
    public String getIcono() {
		return icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}
	
	

	public Integer getOrden() {
		return orden;
	}

	public void setOrden(Integer orden) {
		this.orden = orden;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Rol rol = (Rol) o;
        if (rol.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), rol.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Rol{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", activo='" + isActivo() + "'" +
            ", icono='" + getIcono() + "'" +
            ", orden='" + getOrden() + "'" +
            "}";
    }
}
