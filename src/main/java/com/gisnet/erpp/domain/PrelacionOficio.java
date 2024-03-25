package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A PrelacionOficio.
 */
@Entity
@Table(name = "prelacion_oficio")
public class PrelacionOficio implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "prelacionOficioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "prelacionOficioGenerator", sequenceName="prelacion_oficio_seq")
    private Long id;
    
    @ManyToOne(optional = false)
    @NotNull
    private TipoOficio tipoOficio;

    @ManyToOne
    private Prelacion prelacion;
        
    @Column(name = "version")
    private Integer version;
    
    @Column(name = "plantilla", length = 4000)
    private String plantilla;

    @Column(name = "num_oficio")
    private Integer numOficio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public TipoOficio getTipoOficio(){
        return tipoOficio;
    }
    
    public void setTipoOficio(TipoOficio tipoOficio){
        this.tipoOficio = tipoOficio;
    }
    
    public Prelacion getPrelacion(){
        return prelacion;
    }
    
    public void setPrelacion(Prelacion prelacion){
        this.prelacion = prelacion;
    }
    
    public Integer getVersion(){
        return version;
    }
    
    public void setVersion(Integer version){
        this.version = version;
    }
    
    public String getPlantilla(){
        return plantilla;
    }
    
    public void setPlantilla(String plantilla){
        this.plantilla = plantilla;
    }

    public Integer getNumOficio(){
        return numOficio;
    }
    
    public void setNumOficio(Integer numOficio){
        this.numOficio = numOficio;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

}
