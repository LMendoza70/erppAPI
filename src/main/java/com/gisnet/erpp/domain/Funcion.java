package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Funcion.
 */
@Entity
@Table(name = "funcion")
public class Funcion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "funcionGenerator")
    @SequenceGenerator(allocationSize = 1, name = "funcionGenerator", sequenceName="funcion_seq")
    private Long id;

    @NotNull
    @Size(max = 500)
    @Column(name = "nombre", length = 500, nullable = false)
    private String nombre;

    @Size(max = 100)
    @Column(name = "url", length = 100)
    private String url;
    
    @Size(max = 15)
    @Column(name = "param", length = 15)
    private String param;
    
    @Size(max = 200)
    @Column(name = "url_div", length = 200)
    private String urlDiv;

    @Column(name = "orden")
    private Integer orden;
    
    @Size(max = 50)
    @Column(name = "icono", length = 50)
    private String icono;

    @OneToMany(mappedBy = "funcionPadre")
    @JsonIgnore
    private Set<Funcion> funcionesParaFuncionPadres = new HashSet<>();

    @OneToMany(mappedBy = "funcion")
    @JsonIgnore
    private Set<FuncionRol> funcionRolesParaFuncions = new HashSet<>();

    @ManyToOne
    private Funcion funcionPadre;
    
	public String getParam() {
		return param;
	}

	public void setParam(String param) {
		this.param = param;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public Funcion nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getUrl() {
        return url;
    }

    public Funcion url(String url) {
        this.url = url;
        return this;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Integer getOrden() {
        return orden;
    }

    public Funcion orden(Integer orden) {
        this.orden = orden;
        return this;
    }

    public void setOrden(Integer orden) {
        this.orden = orden;
    }

    public Set<Funcion> getFuncionesParaFuncionPadres() {
        return funcionesParaFuncionPadres;
    }

    public Funcion funcionesParaFuncionPadres(Set<Funcion> funcions) {
        this.funcionesParaFuncionPadres = funcions;
        return this;
    }

    public Funcion addFuncionesParaFuncionPadre(Funcion funcion) {
        this.funcionesParaFuncionPadres.add(funcion);
        funcion.setFuncionPadre(this);
        return this;
    }

    public Funcion removeFuncionesParaFuncionPadre(Funcion funcion) {
        this.funcionesParaFuncionPadres.remove(funcion);
        funcion.setFuncionPadre(null);
        return this;
    }

    public void setFuncionesParaFuncionPadres(Set<Funcion> funcions) {
        this.funcionesParaFuncionPadres = funcions;
    }

    public Set<FuncionRol> getFuncionRolesParaFuncions() {
        return funcionRolesParaFuncions;
    }

    public Funcion funcionRolesParaFuncions(Set<FuncionRol> funcionRols) {
        this.funcionRolesParaFuncions = funcionRols;
        return this;
    }

    public Funcion addFuncionRolesParaFuncion(FuncionRol funcionRol) {
        this.funcionRolesParaFuncions.add(funcionRol);
        funcionRol.setFuncion(this);
        return this;
    }

    public Funcion removeFuncionRolesParaFuncion(FuncionRol funcionRol) {
        this.funcionRolesParaFuncions.remove(funcionRol);
        funcionRol.setFuncion(null);
        return this;
    }

    public void setFuncionRolesParaFuncions(Set<FuncionRol> funcionRols) {
        this.funcionRolesParaFuncions = funcionRols;
    }

    public Funcion getFuncionPadre() {
        return funcionPadre;
    }

    public Funcion funcionPadre(Funcion funcion) {
        this.funcionPadre = funcion;
        return this;
    }

    public void setFuncionPadre(Funcion funcion) {
        this.funcionPadre = funcion;
    }
        

    public String getIcono() {
		return icono;
	}

	public void setIcono(String icono) {
		this.icono = icono;
	}
	
	

	public String getUrlDiv() {
		return urlDiv;
	}

	public void setUrlDiv(String urlDiv) {
		this.urlDiv = urlDiv;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Funcion funcion = (Funcion) o;
        if (funcion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), funcion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Funcion{" +
            "id=" + getId() +
            ", nombre='" + getNombre() + "'" +
            ", url='" + getUrl() + "'" +
            ", orden='" + getOrden() + "'" +
            ", icono='" + getIcono() + "'" +
            "}";
    }
}
