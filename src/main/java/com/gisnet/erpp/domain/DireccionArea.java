package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A DireccionArea.
 */
@Entity
@Table(name = "direccion_area")
public class DireccionArea implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "direccionAreaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "direccionAreaGenerator", sequenceName="direccion_area_seq")
    private Long id;
    
    @ManyToOne(optional = false)
    @NotNull
    private Oficina oficina;        
     
    @Column(name = "direccion")
    private String direccion;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "correo")
    private String correo;

    @Column(name = "puesto")
    private String puesto;

    @Column(name = "clave")
    private String clave;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }   
    
    
    public String getDireccion(){
        return direccion;
    }
    
    public void setDireccion(String direccion){
        this.direccion = direccion;
    }

    public String getNombre(){
        return nombre;
    }
    
    public void setNombre(String nombre){
        this.nombre = nombre;
    }

    public String getCorreo(){
        return correo;
    }
    
    public void setCorreo(String correo){
        this.correo = correo;
    }


    public String getPuesto(){
        return puesto;
    }
    
    public void setPuesto(String puesto){
        this.puesto = puesto;
    }

    public String getClave(){
        return clave;
    }
    
    public void setClave(String clave){
        this.clave = clave;
    }

    public Oficina getOficina(){
        return oficina;
    }
    
    public void setOficina(Oficina oficina){
        this.oficina = oficina;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
