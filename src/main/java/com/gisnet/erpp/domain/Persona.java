package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Persona.
 */
@Entity
@Table(name = "persona")
public class Persona implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "personaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "personaGenerator", sequenceName="persona_seq")
    private Long id;

    @Column(name = "publica")
    private Boolean publica;

    @Size(max = 400)
    @Column(name = "paterno", length = 400)
    private String paterno;

    @Size(max = 80)
    @Column(name = "materno", length = 80)
    private String materno;

    @Size(max = 255)
    @Column(name = "nombre", length = 255)
    private String nombre;

    @Column(name = "fecha_nac")
    private Date fechaNac;

    @Size(max = 18)
    @Column(name = "rfc", length = 18)
    private String rfc;

    @Size(max = 33)
    @Column(name = "curp", length = 33)
    private String curp;

    @Size(max = 60)
    @Column(name = "email", length = 60)
    private String email;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;
    
    @Column(name = "tipo_historico", length = 1)
    private Integer tipoHistorico;

    @Size(max = 80)
    @Column(name = "hash_fila", length = 80)
    private String hashFila;
    
    @Column(name = "nu_tit", length = 400)
    private Integer nuTit;

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval=true)
    @JsonIgnore
    private Set<Direccion> direccionesParaPersonas = new HashSet<>();

    /*@OneToMany(mappedBy = "persona")
    @JsonIgnore
    private Set<Documento> documentosParaPersonas = new HashSet<>();*/

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval=true)
    @JsonIgnore
    private Set<Identificacion> identificacionesParaPersonas = new HashSet<>();

    /*@OneToMany(mappedBy = "persona")
    @JsonIgnore
    private Set<Juez> juezesParaPersonas = new HashSet<>();*/

    @OneToMany(mappedBy = "persona")
    @JsonIgnore
    private Set<Notario> notariosParaPersonas = new HashSet<>();

    @OneToMany(mappedBy = "persona")
    @JsonIgnore
    private Set<PredioPersona> predioPersonasParaPersonas = new HashSet<>();

    @OneToMany(mappedBy = "persona", cascade = CascadeType.ALL, orphanRemoval=true)
    private Set<Telefono> telefonosParaPersonas = new HashSet<>();

    @OneToMany(mappedBy = "persona")
    @JsonIgnore
    private Set<Usuario> usuariosParaPersonas = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private TipoPersona tipoPersona;

    @ManyToOne(optional = true)
    private Nacionalidad nacionalidad;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Integer getNuTit() {
        return nuTit;
    }
    
    public void setNuTit(Integer nuTit){
        this.nuTit = nuTit;
    }
    
    public Integer getTipoHistorico() {
        return tipoHistorico;
    }
    
    public void setTipoHistorico(Integer tipoHistorico){
        this.tipoHistorico = tipoHistorico;
    }

    public Boolean isPublica() {
        return publica;
    }

    public Persona publica(Boolean publica) {
        this.publica = publica;
        return this;
    }

    public void setPublica(Boolean publica) {
        this.publica = publica;
    }

    public String getPaterno() {
        return paterno;
    }

    public Persona paterno(String paterno) {
        this.paterno = paterno;
        return this;
    }

    public void setPaterno(String paterno) {
        this.paterno = paterno;
    }

    public String getMaterno() {
        return materno;
    }

    public Persona materno(String materno) {
        this.materno = materno;
        return this;
    }

    public void setMaterno(String materno) {
        this.materno = materno;
    }

    public String getNombre() {
        return nombre;
    }

    public Persona nombre(String nombre) {
        this.nombre = nombre;
        return this;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Date getFechaNac() {
        return fechaNac;
    }

    public Persona fechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
        return this;
    }

    public void setFechaNac(Date fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getRfc() {
        return rfc;
    }

    public Persona rfc(String rfc) {
        this.rfc = rfc;
        return this;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }

    public String getCurp() {
        return curp;
    }

    public Persona curp(String curp) {
        this.curp = curp;
        return this;
    }

    public void setCurp(String curp) {
        this.curp = curp;
    }

    public String getEmail() {
        return email;
    }

    public Persona email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean isActivo() {
        return activo;
    }

    public Persona activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public String getHashFila() {
        return hashFila;
    }

    public Persona hashFila(String hashFila) {
        this.hashFila = hashFila;
        return this;
    }

    public void setHashFila(String hashFila) {
        this.hashFila = hashFila;
    }
    
    public void setDireccionesParaPersonas(Set<Direccion> direcciones) {
    	direcciones.forEach((direccion) -> direccion.setPersona(this));
        this.direccionesParaPersonas = direcciones;
    }

    public Set<Direccion> getDireccionesParaPersonas() {
        return direccionesParaPersonas;
    }

    public Persona direccionesParaPersonas(Set<Direccion> direccions) {
        this.direccionesParaPersonas = direccions;
        return this;
    }

    public Persona addDireccionesParaPersona(Direccion direccion) {
        this.direccionesParaPersonas.add(direccion);
        direccion.setPersona(this);
        return this;
    }

    public Persona removeDireccionParaPersona(Direccion direccion) {
        this.direccionesParaPersonas.remove(direccion);
        direccion.setPersona(null);
        return this;
    }

    /*public Set<Documento> getDocumentosParaPersonas() {
        return documentosParaPersonas;
    }

    public Persona documentosParaPersonas(Set<Documento> documentos) {
        this.documentosParaPersonas = documentos;
        return this;
    }

    public Persona addDocumentosParaPersona(Documento documento) {
        this.documentosParaPersonas.add(documento);
        documento.setPersona(this);
        return this;
    }

    public Persona removeDocumentosParaPersona(Documento documento) {
        this.documentosParaPersonas.remove(documento);
        documento.setPersona(null);
        return this;
    }

    public void setDocumentosParaPersonas(Set<Documento> documentos) {
        this.documentosParaPersonas = documentos;
    }*/
    
    public void setIdentificacionesParaPersonas(Set<Identificacion> identificaciones) {
    	identificaciones.forEach(identificacion -> identificacion.setPersona(this));
        this.identificacionesParaPersonas = identificaciones;
    }

    public Set<Identificacion> getIdentificacionesParaPersonas() {
        return identificacionesParaPersonas;
    }

    public Persona identificacionesParaPersonas(Set<Identificacion> identificacions) {
        this.identificacionesParaPersonas = identificacions;
        return this;
    }

    public Persona addIdentificacionesParaPersona(Identificacion identificacion) {
        this.identificacionesParaPersonas.add(identificacion);
        identificacion.setPersona(this);
        return this;
    }

    public Persona removeIdentificacionParaPersona(Identificacion identificacion) {
        this.identificacionesParaPersonas.remove(identificacion);
        identificacion.setPersona(null);
        return this;
    }
    
    public void removeAllIdentificacionesParaPersona() {
        this.identificacionesParaPersonas.removeAll(this.identificacionesParaPersonas);
    }

    /*public Set<Juez> getJuezesParaPersonas() {
        return juezesParaPersonas;
    }

    public Persona juezesParaPersonas(Set<Juez> juezs) {
        this.juezesParaPersonas = juezs;
        return this;
    }

    public Persona addJuezesParaPersona(Juez juez) {
        this.juezesParaPersonas.add(juez);
        juez.setPersona(this);
        return this;
    }

    public Persona removeJuezesParaPersona(Juez juez) {
        this.juezesParaPersonas.remove(juez);
        juez.setPersona(null);
        return this;
    }

    public void setJuezesParaPersonas(Set<Juez> juezs) {
        this.juezesParaPersonas = juezs;
    }*/

    public Set<Notario> getNotariosParaPersonas() {
        return notariosParaPersonas;
    }

    public Persona notariosParaPersonas(Set<Notario> notarios) {
        this.notariosParaPersonas = notarios;
        return this;
    }

    public Persona addNotariosParaPersona(Notario notario) {
        this.notariosParaPersonas.add(notario);
        notario.setPersona(this);
        return this;
    }

    public Persona removeNotariosParaPersona(Notario notario) {
        this.notariosParaPersonas.remove(notario);
        notario.setPersona(null);
        return this;
    }

    public void setNotariosParaPersonas(Set<Notario> notarios) {
        this.notariosParaPersonas = notarios;
    }

    public Set<PredioPersona> getPredioPersonasParaPersonas() {
        return predioPersonasParaPersonas;
    }

    public Persona predioPersonasParaPersonas(Set<PredioPersona> predioPersonas) {
        this.predioPersonasParaPersonas = predioPersonas;
        return this;
    }

    public Persona addPredioPersonasParaPersona(PredioPersona predioPersona) {
        this.predioPersonasParaPersonas.add(predioPersona);
        predioPersona.setPersona(this);
        return this;
    }

    public Persona removePredioPersonasParaPersona(PredioPersona predioPersona) {
        this.predioPersonasParaPersonas.remove(predioPersona);
        predioPersona.setPersona(null);
        return this;
    }

    public void setPredioPersonasParaPersonas(Set<PredioPersona> predioPersonas) {
        this.predioPersonasParaPersonas = predioPersonas;
    }
    
    public void setTelefonosParaPersonas(Set<Telefono> telefonos) {
    	telefonos.forEach(telefono -> telefono.setPersona(this));
        this.telefonosParaPersonas = telefonos;
    }

    public Set<Telefono> getTelefonosParaPersonas() {
        return telefonosParaPersonas;
    }

    public Persona telefonosParaPersonas(Set<Telefono> telefonos) {
        this.telefonosParaPersonas = telefonos;
        return this;
    }

    public Persona addTelefonosParaPersona(Telefono telefono) {
        this.telefonosParaPersonas.add(telefono);
        telefono.setPersona(this);
        return this;
    }

    public Persona removeTelefonoParaPersona(Telefono telefono) {
        this.telefonosParaPersonas.remove(telefono);
        telefono.setPersona(null);
        return this;
    }
    
    public void removeAllTelefonosParaPersona() {
        this.telefonosParaPersonas.removeAll(this.telefonosParaPersonas);
    }

    

    
    public Set<Usuario> getUsuariosParaPersonas() {
        return usuariosParaPersonas;
    }

    public Persona usuariosParaPersonas(Set<Usuario> usuarios) {
        this.usuariosParaPersonas = usuarios;
        return this;
    }

    public Persona addUsuariosParaPersona(Usuario usuario) {
        this.usuariosParaPersonas.add(usuario);
        usuario.setPersona(this);
        return this;
    }

    public Persona removeUsuariosParaPersona(Usuario usuario) {
        this.usuariosParaPersonas.remove(usuario);
        usuario.setPersona(null);
        return this;
    }

    public void setUsuariosParaPersonas(Set<Usuario> usuarios) {
        this.usuariosParaPersonas = usuarios;
    }

    public TipoPersona getTipoPersona() {
        return tipoPersona;
    }

    public Persona tipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
        return this;
    }

    public void setTipoPersona(TipoPersona tipoPersona) {
        this.tipoPersona = tipoPersona;
    }
    
    public Nacionalidad getNacionalidad() {
        return nacionalidad;
    }

    public Persona nacionalidad(Nacionalidad nacionalidad) {
        this.nacionalidad = nacionalidad;
        return this;
    }

    public void setNacionalidad(Nacionalidad nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Persona persona = (Persona) o;
        if (persona.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), persona.getId());
    }


    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Persona{" +
            "id=" + getId() +
            ", publica='" + isPublica() + "'" +
            ", paterno='" + getPaterno() + "'" +
            ", materno='" + getMaterno() + "'" +
            ", nombre='" + getNombre() + "'" +
            ", fechaNac='" + getFechaNac() + "'" +
            ", rfc='" + getRfc() + "'" +
            ", curp='" + getCurp() + "'" +
            ", email='" + getEmail() + "'" +
            ", activo='" + isActivo() + "'" +
            ", hashFila='" + getHashFila() + "'" +
            "}";
    }
}
