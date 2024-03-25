package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Date;
import java.util.Objects;

/**
 * A Direccion.
 */
@Entity
@Table(name = "dependencia")


public class Dependencia implements Serializable {

    private static final long serialVersionUID = 1L;
    
    
    

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "dependenciaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "dependenciaGenerator", sequenceName="dependencia_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private TipoDependencia tipoDependencia;

    @NotNull
    @Column(name = "nombre", nullable = false)
    private String nombre;
    
    @ManyToOne
    private Ambito ambito;

    @NotNull
    @Column(name = "cargo", nullable = false)
    private String cargo;
    
    @NotNull
    @Column(name = "nombre_titular", nullable = false)
    private String nombreTitular;
    
    @NotNull
    @Column(name = "paterno_titular", nullable = false)
    private String paternoTitular;
    
    
    @Column(name = "materno_titular", nullable = true)
    private String maternoTitular;
    
    @NotNull
    @Column(name = "email", nullable = false)
    private String email;
    
    @Size(max = 13)
    @Column(name = "rfc", nullable = true)
    private String rfc;
    
    @OneToMany(mappedBy = "dependencia")
    @JsonIgnore
    private Set<Usuario> usuariosParaDependencias = new HashSet<>();
    
    @ManyToOne
    private Estado estado;
    
    
    @OneToMany(mappedBy = "dependencia")
    @JsonIgnore
    private Set<Telefono> telefonosParaDependencias = new HashSet<>();
        
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public TipoDependencia getTipoDependencia() {
        return this.tipoDependencia;
    }

    public void setTipoDependencia(TipoDependencia tipoDependencia) {
        this.tipoDependencia = tipoDependencia;
    }
    
    public String getNombre() {
        return this.nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    
    public Ambito getAmbito() {
        return this.ambito;
    }

    public void setAmbito(Ambito ambito) {
        this.ambito = ambito;
    }
    
    public String getCargo() {
        return this.cargo;
    }

    public void setCargo(String cargo) {
        this.cargo = cargo;
    }
    
    public String getNombreTitular() {
        return this.nombreTitular;
    }

    public void setNombreTitular(String nombreTitular) {
        this.nombreTitular = nombreTitular;
    }
    
    public String getPaternoTitular() {
        return this.paternoTitular;
    }

    public void setPaternoTitular(String paternoTitular) {
        this.paternoTitular = paternoTitular;
    }
    
    public String getMaternoTitular() {
        return this.maternoTitular;
    }

    public void setMaternoTitular(String maternoTitular) {
        this.maternoTitular = maternoTitular;
    }
    
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public String getRfc() {
        return this.rfc;
    }

    public void setRfc(String rfc) {
        this.rfc = rfc;
    }
    
    public Estado getEstado(){
        return estado;
    }
        
    public void setEstado(Estado estado){
        this.estado = estado;
    }
    
    
    public Set<Telefono> getTelefonosParaDependencias() {
        return telefonosParaDependencias;
    }

    public Dependencia telefonosParaDependencias(Set<Telefono> telefonos) {
        this.telefonosParaDependencias = telefonos;
        return this;
    }

    public Dependencia addTelefonosParaDependencia(Telefono telefono) {
        this.telefonosParaDependencias.add(telefono);
        telefono.setDependencia(this);
        return this;
    }

    public Dependencia removeTelefonosParaDependencia(Telefono telefono) {
        this.telefonosParaDependencias.remove(telefono);
        telefono.setNotario(null);
        return this;
    }

    public void setTelefonosParaDependencias(Set<Telefono> telefonos) {
        this.telefonosParaDependencias = telefonos;
    }
    
    public Set<Usuario> getUsuariosParaDependencias() {
        return usuariosParaDependencias;
    }

    public Dependencia usuariosParaDependencias(Set<Usuario> usuariosParaDependencias) {
        this.usuariosParaDependencias= usuariosParaDependencias;
        return this;
    }

    public Dependencia addUsuariosParaDependencias(Usuario usuariosParaDependencia) {
        this.usuariosParaDependencias.add(usuariosParaDependencia);
        usuariosParaDependencia.setDependencia(this);
        return this;
    }

    public Dependencia removeUsuariosParaDependencias(Usuario usuariosParaDependencia) {
        this.usuariosParaDependencias.remove(usuariosParaDependencia);
        usuariosParaDependencia.setDependencia(null);
        return this;
    }

    public void setUsuariosParaDependencias(Set<Usuario> usuariosParaDependencias) {
        this.usuariosParaDependencias = usuariosParaDependencias;
    }
    

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Dependencia direccion = (Dependencia) o;
        if (direccion.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), direccion.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Depenencia{" +
            "id=" + getId() +
            "tipoDependencia={"+this.getTipoDependencia().getId()+" "+this.getTipoDependencia().getNombre()+"}"+
            " nombre= "+this.getNombre()+
            " email= "+this.getEmail()+
            "}";
    }
}
