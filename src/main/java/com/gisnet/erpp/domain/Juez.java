package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Juez.
 */
@Entity
@Table(name = "juez")
public class Juez implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonIgnore
    public String getNombreCompleto(){
		StringBuilder sb = new StringBuilder();
		if (getNombre()!=null){
			sb.append(getNombre());
		} 
		if (getPaterno()!=null){
			sb.append(" ").append(getPaterno());
		}
		if (getMaterno()!=null){
			sb.append(" ").append(getMaterno());
		}
		return sb.toString().toUpperCase();
	}    
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "juezGenerator")
    @SequenceGenerator(allocationSize = 1, name = "juezGenerator", sequenceName="juez_seq")
    private Long id;

    @Column(name = "no_juez")
    private Integer noJuez;

    @Size(max = 50)
    @Column(name = "email", length = 50)
    private String email;
    
    @NotNull
    @Size(max = 70)
    @Column(name = "nombre", length = 70)
    private String nombre;
    
    @NotNull
    @Size(max = 60)
    @Column(name = "paterno", length = 60)
    private String paterno;
    
    @Size(max = 60)
    @Column(name = "materno", length = 60)
    private String materno;
    
    @Size(max = 100)
    @Column(name = "juzgado", length = 100)
    private String juzgado;
    
    @Column(name = "no_juzgado")
    private Integer noJuzgado;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "juez")
    @JsonIgnore
    private Set<Documento> documentosParaJuezs = new HashSet<>();

    @OneToMany(mappedBy = "juez")
    @JsonIgnore
    private Set<Telefono> telefonosParaJuezs = new HashSet<>();

    
    @ManyToOne(optional = false)
    @NotNull
    private Municipio municipio;
    
    /*@ManyToOne(optional = false)
    @NotNull
    private Estado estado;*/

    /*@ManyToOne(optional = false)
    @NotNull
    private Materia materia;*/

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNoJuez() {
        return noJuez;
    }

    public Juez noJuez(Integer noJuez) {
        this.noJuez = noJuez;
        return this;
    }

    public void setNoJuez(Integer noJuez) {
        this.noJuez = noJuez;
    }

    public String getEmail() {
        return email;
    }

    public Juez email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getNombre() {
        return nombre;
    }

    public Juez nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public String getPaterno() {
        return paterno;
    }

    public Juez paterno(String paterno) {
        this.paterno = paterno;
        return this;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }
    
    public String getMaterno() {
        return materno;
    }

    public Juez materno(String materno) {
        this.materno = materno;
        return this;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }
    
    public String getJuzgado() {
        return juzgado;
    }

    public Juez juzgado(String juzgado) {
        this.juzgado = juzgado;
        return this;
    }

    public void setJuzgado(String juzgado) {
        this.juzgado = juzgado;
    }
    
    public Integer getNoJuzgado() {
        return noJuzgado;
    }

    public Juez noJuzgado(Integer noJuzgado) {
        this.noJuzgado = noJuzgado;
        return this;
    }

    public void setNoJuzgado(Integer noJuzgado) {
        this.noJuzgado = noJuzgado;
    }
    

    public Boolean isActivo() {
        return activo;
    }

    public Juez activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Documento> getDocumentosParaJuezs() {
        return documentosParaJuezs;
    }

    public Juez documentosParaJuezs(Set<Documento> documentos) {
        this.documentosParaJuezs = documentos;
        return this;
    }

    public Juez addDocumentosParaJuez(Documento documento) {
        this.documentosParaJuezs.add(documento);
        documento.setJuez(this);
        return this;
    }

    public Juez removeDocumentosParaJuez(Documento documento) {
        this.documentosParaJuezs.remove(documento);
        documento.setJuez(null);
        return this;
    }

    public void setDocumentosParaJuezs(Set<Documento> documentos) {
        this.documentosParaJuezs = documentos;
    }

    public Set<Telefono> getTelefonosParaJuezs() {
        return telefonosParaJuezs;
    }

    public Juez telefonosParaJuezs(Set<Telefono> telefonos) {
        this.telefonosParaJuezs = telefonos;
        return this;
    }

    public Juez addTelefonosParaJuez(Telefono telefono) {
        this.telefonosParaJuezs.add(telefono);
        telefono.setJuez(this);
        return this;
    }

    public Juez removeTelefonosParaJuez(Telefono telefono) {
        this.telefonosParaJuezs.remove(telefono);
        telefono.setJuez(null);
        return this;
    }

    public void setTelefonosParaJuezs(Set<Telefono> telefonos) {
        this.telefonosParaJuezs = telefonos;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public Juez municipio(Municipio municipio) {
        this.municipio = municipio;
        return this;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }
    
/*    public Materia getMateria() {
        return materia;
    }

    public Juez materia(Materia materia) {
        this.materia = materia;
        return this;
    }

    public void setMateria(Materia materia) {
        this.materia = materia;
    }*/

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Juez juez = (Juez) o;
        if (juez.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), juez.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Juez{" +
            "id=" + getId() +
            ", noJuez='" + getNoJuez() + "'" +
            ", email='" + getEmail() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", paterno='" + getPaterno() + "'" +
            ", materno='" + getMaterno() + "'" +
            ", juzgado='" + getJuzgado() + "'" +
            ", noJuzgado='" + getNoJuzgado() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
