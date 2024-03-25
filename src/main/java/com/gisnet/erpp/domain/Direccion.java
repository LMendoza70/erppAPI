package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Direccion.
 */
@Entity
@Table(name = "direccion")
public class Direccion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "direccionGenerator")
    @SequenceGenerator(allocationSize = 1, name = "direccionGenerator", sequenceName="direccion_seq")
    private Long id;

    @Size(max = 40)
    @Column(name = "num_ext", length = 40)
    private String numExt;

    @Size(max = 40)
    @Column(name = "num_int", length = 40)
    private String numInt;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Size(max = 80)
    @Column(name = "hash_fila", length = 80)
    private String hashFila; 

    @OneToMany(mappedBy = "direccion")
    @JsonIgnore
    private Set<PredioPersona> predioPersonasParaDireccions = new HashSet<>();

    @ManyToOne
    private Persona persona;

    @ManyToOne
    private Notario notario;

    @ManyToOne
    private Asentamiento asentamiento;

    @ManyToOne
    private Vialidad vialidadEntre2;

    @ManyToOne
    private Vialidad vialidadEntre1;
    
    @ManyToOne
    private Vialidad vialidad; 
    
    @Column(name = "calle", length = 200)
    private String calle;
    
    @Column(name = "colonia", length = 200)
    private String colonia;
    
    @Column(name = "codigo_postal", length = 5)
    private String cp;
    
    @Column(name = "num_telefono", length = 10)
    private Integer telefono;
    
    @Column(name = "email_2", length = 60)
    private String email2;
    
    @ManyToOne
    private Municipio municipio;
    
    @ManyToOne
    private Estado estado;
    
    @ManyToOne
    private Dependencia dependencia;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Municipio getMunicipio(){
        return municipio;
    }
    
    public void setMunicipio(Municipio municipio){
        this.municipio = municipio;
    }
    
    public Estado getEstado(){
        return estado;
    }
    
    public void setEstado(Estado estado){
        this.estado = estado;
    }
    
    public String getEmail2(){
        return email2;
    }
    
    public void setEmail2(String email2){
        this.email2 = email2;
    }
    
    public Integer getTelefono(){
        return telefono;
    }
    
    public void setTelefono(Integer telefono){
        this.telefono = telefono;
    }
    
    public String getCalle(){
        return calle;
    }
    
    public void setCalle(String calle){
        this.calle = calle.toUpperCase();
    }
    
    public String getCp(){
        return cp;
    }
    
    public void setCp(String cp){
        this.cp = cp;
    }
    
    public String getColonia(){
        return colonia;
    }
    
    public void setColonia(String colonia){
        this.colonia = colonia.toUpperCase();
    }

    public String getNumExt() {
        return numExt;
    }

    public Direccion numExt(String numExt) {
        this.numExt = numExt;
        return this;
    }

    public void setNumExt(String numExt) {
        this.numExt = numExt;
    }

    public String getNumInt() {
        return numInt;
    }

    public Direccion numInt(String numInt) {
        this.numInt = numInt;
        return this;
    }

    public void setNumInt(String numInt) {
        this.numInt = numInt;
    }

    public Boolean isActivo() {
        return activo;
    }

    public Direccion activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    public String getHashFila() {
        return hashFila;
    }

    public Direccion hashFila(String hashFila) {
        this.hashFila = hashFila;
        return this;
    }

    public void setHashFila(String hashFila) {
        this.hashFila = hashFila;
    }  

    public Set<PredioPersona> getPredioPersonasParaDireccions() {
        return predioPersonasParaDireccions;
    }

    public Direccion predioPersonasParaDireccions(Set<PredioPersona> predioPersonas) {
        this.predioPersonasParaDireccions = predioPersonas;
        return this;
    }

    public Direccion addPredioPersonasParaDireccion(PredioPersona predioPersona) {
        this.predioPersonasParaDireccions.add(predioPersona);
        predioPersona.setDireccion(this);
        return this;
    }

    public Direccion removePredioPersonasParaDireccion(PredioPersona predioPersona) {
        this.predioPersonasParaDireccions.remove(predioPersona);
        predioPersona.setDireccion(null);
        return this;
    }

    public void setPredioPersonasParaDireccions(Set<PredioPersona> predioPersonas) {
        this.predioPersonasParaDireccions = predioPersonas;
    }

    public Persona getPersona() {
        return persona;
    }

    public Direccion persona(Persona persona) {
        this.persona = persona;
        return this;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public Notario getNotario() {
        return notario;
    }

    public Direccion notario(Notario notario) {
        this.notario = notario;
        return this;
    }

    public void setNotario(Notario notario) {
        this.notario = notario;
    }
    
    public Dependencia getDependencia() {
        return dependencia;
    }

    public Direccion dependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
        return this;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }

    public Asentamiento getAsentamiento() {
        return asentamiento;
    }

    public Direccion asentamiento(Asentamiento asentamiento) {
        this.asentamiento = asentamiento;
        return this;
    }

    public void setAsentamiento(Asentamiento asentamiento) {
        this.asentamiento = asentamiento;
    }

    public Vialidad getVialidadEntre2() {
        return vialidadEntre2;
    }

    public Direccion vialidadEntre2(Vialidad vialidad) {
        this.vialidadEntre2 = vialidad;
        return this;
    }

    public void setVialidadEntre2(Vialidad vialidad) {
        this.vialidadEntre2 = vialidad;
    }

    public Vialidad getVialidadEntre1() {
        return vialidadEntre1;
    }

    public Direccion vialidadEntre1(Vialidad vialidad) {
        this.vialidadEntre1 = vialidad;
        return this;
    }

    public void setVialidadEntre1(Vialidad vialidad) {
        this.vialidadEntre1 = vialidad;
    }

    public Vialidad getVialidad() {
        return vialidad;
    }

    public Direccion vialidad(Vialidad vialidad) {
        this.vialidad = vialidad;
        return this;
    }

    public void setVialidad(Vialidad vialidad) {
        this.vialidad = vialidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Direccion direccion = (Direccion) o;
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
        return "Direccion{" +
            "id=" + getId() +
            "calle="+this.getCalle() +
            "colonia="+this.getColonia()+
            "persona="+this.getPersona().toString()+
            "activo="+this.isActivo()+
            "}";
    }
}
