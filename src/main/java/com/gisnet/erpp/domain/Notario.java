package com.gisnet.erpp.domain;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * A Notario.
 */
@Entity
@Table(name = "notario")
public class Notario implements Serializable {

    private static final long serialVersionUID = 1L;

    @Transient
    @JsonProperty
    public String getNombreCompleto(){
		StringBuilder sb = new StringBuilder();
		if (persona!=null){
			if (persona.getNombre()!=null){
				sb.append(persona.getNombre());
			} 
			if (persona.getPaterno()!=null){
				sb.append(" ").append(persona.getPaterno());
			}
			if (persona.getMaterno()!=null){
				sb.append(" ").append(persona.getMaterno());
			}
		}
		return sb.toString().toUpperCase();
	}
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "notarioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "notarioGenerator", sequenceName="notario_seq")
    private Long id;

    @Column(name = "no_notario")
    private Integer noNotario;

    @Size(max = 50)
    @Column(name = "email", length = 50)
    private String email;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @OneToMany(mappedBy = "notario")
    @JsonIgnore
    private Set<Direccion> direccionesParaNotarios = new HashSet<>();

    @OneToMany(mappedBy = "notario")
    @JsonIgnore
    private Set<Documento> documentosParaNotarios = new HashSet<>();

    @OneToMany(mappedBy = "notario")
    @JsonIgnore
    private Set<Prelacion> prelacionesParaNotarios = new HashSet<>();

    @OneToMany(mappedBy = "notario")
    @JsonIgnore
    private Set<Telefono> telefonosParaNotarios = new HashSet<>();

    @OneToMany(mappedBy = "notario")
    @JsonIgnore
    private Set<UsuNotario> usuNotariosParaNotarios = new HashSet<>();

    @OneToMany(mappedBy = "notario")
    @JsonIgnore
    private Set<Usuario> usuariosParaNotarios = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private TipoNotario tipoNotario;

    @ManyToOne(optional = false)
    @NotNull
    private Municipio municipio;

    @ManyToOne(optional = false)
    @NotNull
    private Persona persona;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNoNotario() {
        return noNotario;
    }

    public Notario noNotario(Integer noNotario) {
        this.noNotario = noNotario;
        return this;
    }

    public void setNoNotario(Integer noNotario) {
        this.noNotario = noNotario;
    }

    public String getEmail() {
        return email;
    }

    public Notario email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isActivo() {
        return activo;
    }

    public Notario activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Direccion> getDireccionesParaNotarios() {
        return direccionesParaNotarios;
    }

    public Notario direccionesParaNotarios(Set<Direccion> direccions) {
        this.direccionesParaNotarios = direccions;
        return this;
    }

    public Notario addDireccionesParaNotario(Direccion direccion) {
        this.direccionesParaNotarios.add(direccion);
        direccion.setNotario(this);
        return this;
    }

    public Notario removeDireccionesParaNotario(Direccion direccion) {
        this.direccionesParaNotarios.remove(direccion);
        direccion.setNotario(null);
        return this;
    }

    public void setDireccionesParaNotarios(Set<Direccion> direccions) {
        this.direccionesParaNotarios = direccions;
    }

    public Set<Documento> getDocumentosParaNotarios() {
        return documentosParaNotarios;
    }

    public Notario documentosParaNotarios(Set<Documento> documentos) {
        this.documentosParaNotarios = documentos;
        return this;
    }

    public Notario addDocumentosParaNotario(Documento documento) {
        this.documentosParaNotarios.add(documento);
        documento.setNotario(this);
        return this;
    }

    public Notario removeDocumentosParaNotario(Documento documento) {
        this.documentosParaNotarios.remove(documento);
        documento.setNotario(null);
        return this;
    }

    public void setDocumentosParaNotarios(Set<Documento> documentos) {
        this.documentosParaNotarios = documentos;
    }

    public Set<Prelacion> getPrelacionesParaNotarios() {
        return prelacionesParaNotarios;
    }

    public Notario prelacionesParaNotarios(Set<Prelacion> prelacions) {
        this.prelacionesParaNotarios = prelacions;
        return this;
    }

    public Notario addPrelacionesParaNotario(Prelacion prelacion) {
        this.prelacionesParaNotarios.add(prelacion);
        prelacion.setNotario(this);
        return this;
    }

    public Notario removePrelacionesParaNotario(Prelacion prelacion) {
        this.prelacionesParaNotarios.remove(prelacion);
        prelacion.setNotario(null);
        return this;
    }

    public void setPrelacionesParaNotarios(Set<Prelacion> prelacions) {
        this.prelacionesParaNotarios = prelacions;
    }

    public Set<Telefono> getTelefonosParaNotarios() {
        return telefonosParaNotarios;
    }

    public Notario telefonosParaNotarios(Set<Telefono> telefonos) {
        this.telefonosParaNotarios = telefonos;
        return this;
    }

    public Notario addTelefonosParaNotario(Telefono telefono) {
        this.telefonosParaNotarios.add(telefono);
        telefono.setNotario(this);
        return this;
    }

    public Notario removeTelefonosParaNotario(Telefono telefono) {
        this.telefonosParaNotarios.remove(telefono);
        telefono.setNotario(null);
        return this;
    }

    public void setTelefonosParaNotarios(Set<Telefono> telefonos) {
        this.telefonosParaNotarios = telefonos;
    }

    public Set<UsuNotario> getUsuNotariosParaNotarios() {
        return usuNotariosParaNotarios;
    }

    public Notario usuNotariosParaNotarios(Set<UsuNotario> usuNotarios) {
        this.usuNotariosParaNotarios = usuNotarios;
        return this;
    }

    public Notario addUsuNotariosParaNotario(UsuNotario usuNotario) {
        this.usuNotariosParaNotarios.add(usuNotario);
        usuNotario.setNotario(this);
        return this;
    }

    public Notario removeUsuNotariosParaNotario(UsuNotario usuNotario) {
        this.usuNotariosParaNotarios.remove(usuNotario);
        usuNotario.setNotario(null);
        return this;
    }

    public void setUsuNotariosParaNotarios(Set<UsuNotario> usuNotarios) {
        this.usuNotariosParaNotarios = usuNotarios;
    }

    public Set<Usuario> getUsuariosParaNotarios() {
        return usuariosParaNotarios;
    }

    public Notario usuariosParaNotarios(Set<Usuario> usuarios) {
        this.usuariosParaNotarios = usuarios;
        return this;
    }

    public Notario addUsuariosParaNotario(Usuario usuario) {
        this.usuariosParaNotarios.add(usuario);
        usuario.setNotario(this);
        return this;
    }

    public Notario removeUsuariosParaNotario(Usuario usuario) {
        this.usuariosParaNotarios.remove(usuario);
        usuario.setNotario(null);
        return this;
    }

    public void setUsuariosParaNotarios(Set<Usuario> usuarios) {
        this.usuariosParaNotarios = usuarios;
    }

    public TipoNotario getTipoNotario() {
        return tipoNotario;
    }

    public Notario tipoNotario(TipoNotario tipoNotario) {
        this.tipoNotario = tipoNotario;
        return this;
    }

    public void setTipoNotario(TipoNotario tipoNotario) {
        this.tipoNotario = tipoNotario;
    }

    public Municipio getMunicipio() {
        return municipio;
    }

    public Notario municipio(Municipio municipio) {
        this.municipio = municipio;
        return this;
    }

    public void setMunicipio(Municipio municipio) {
        this.municipio = municipio;
    }

    public Persona getPersona() {
        return persona;
    }

    public Notario persona(Persona persona) {
        this.persona = persona;
        return this;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Notario notario = (Notario) o;
        if (notario.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), notario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Notario{" +
            "id=" + getId() +
            ", noNotario='" + getNoNotario() + "'" +
            ", email='" + getEmail() + "'" +
            ", activo='" + isActivo() + "'" +
            "}";
    }
}
