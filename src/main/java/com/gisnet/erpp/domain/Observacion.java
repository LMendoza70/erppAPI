package com.gisnet.erpp.domain;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.io.Serializable;

@Entity
@Table(name = "observacion")
public class Observacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "observacionGenerator")
    @SequenceGenerator(allocationSize = 1, name = "observacionGenerator", sequenceName="observacion_seq")
    private Long id;

    @Size(max = 100)
    private String nombre;

    @ManyToOne
    private  Area area;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Area getArea() {
        return area;
    }

    public void setArea(Area area) {
        this.area = area;
    }
}
