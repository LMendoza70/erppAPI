package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;
import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A AreaClasifActo.
 */
@Entity
@Table(name = "area_clasif_acto")
public class AreaClasifActo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "areaClasifActoGenerator")
    @SequenceGenerator(allocationSize = 1, name = "areaClasifActoGenerator", sequenceName="area_clasif_acto_seq")
    private Long id;

    @ManyToOne(optional = false)
    @NotNull
    private Area area;

    @ManyToOne(optional = false)
    @NotNull
    @JsonIgnore
    private ClasifActo clasifActo;

    @ManyToOne
    private SeccionFolio seccionFolio;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Area getArea() {
        return area;
    }

    public AreaClasifActo area(Area area) {
        this.area = area;
        return this;
    }

    public void setArea(Area area) {
        this.area = area;
    }

    public ClasifActo getClasifActo() {
        return clasifActo;
    }

    public AreaClasifActo clasifActo(ClasifActo clasifActo) {
        this.clasifActo = clasifActo;
        return this;
    }

    public void setClasifActo(ClasifActo clasifActo) {
        this.clasifActo = clasifActo;
    }

    public SeccionFolio getSeccionFolio() {
        return seccionFolio;
    }

    public AreaClasifActo seccionFolio(SeccionFolio seccionFolio) {
        this.seccionFolio = seccionFolio;
        return this;
    }

    public void setSeccionFolio(SeccionFolio seccionFolio) {
        this.seccionFolio = seccionFolio;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AreaClasifActo areaClasifActo = (AreaClasifActo) o;
        if (areaClasifActo.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), areaClasifActo.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "AreaClasifActo{" +
            "id=" + getId() +
            "}";
    }
}
