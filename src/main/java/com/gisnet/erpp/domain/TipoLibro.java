package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Tipo Libro.
 */
@Entity
@Table(name = "tipo_libro")
public class TipoLibro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "tipoLibroGenerator")
    @SequenceGenerator(allocationSize = 1, name = "tipoLibroGenerator", sequenceName="tipo_libro_seq")
    private Long id;

    @NotNull
    @Column(name = "descripcion", nullable = false)
    private String descripcion;

    @Column(name = "letra_tipo_acto")
    private String letraTipoActo;

    @Column(name = "letra_relacionada")
    private String letraRelacionada;

    @Column(name = "ley_letra_tipo_acto")
    private String leyLetraTipoActo;
    
    @Column(name = "ley_tipo_libro")
    private String leyTipoLibro;

    @OneToMany(mappedBy = "tipoLibro")
    @JsonIgnore
    private Set<Libro> librosParaTipoLibro = new HashSet<>();
    
    @OneToMany(mappedBy = "tipoLibro")
    @JsonIgnore
    private Set<PrelacionAnte> prelacionAntesParaTipoLibro = new HashSet<>();
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }
    
    public String getLetraTipoActo() {
        return this.letraTipoActo;
    }

    public void setLetraTipoActo(String letraTipoActo) {
        this.letraTipoActo = letraTipoActo;
    }
    
    public String getLetraRelacionada() {
        return this.letraRelacionada;
    }

    public void setLetraRelacionada(String letraRelacionada) {
        this.letraRelacionada = letraRelacionada;
    }
    
    public String getLeyLetraTipoActo() {
        return this.leyLetraTipoActo;
    }

    public void setLeyLetraTipoActo(String leyLetraTipoActo) {
        this.leyLetraTipoActo = leyLetraTipoActo;
    }
    
    public String getLeyTipoLibro() {
        return this.leyTipoLibro;
    }

    public void setLeyTipoLibro(String leyTipoLibro) {
        this.leyTipoLibro = leyTipoLibro;
    }
    
    public Set<Libro> getLibrosParaTipoLibro() {
        return this.librosParaTipoLibro;
    }

    public void setLibrosParaTipoLibro(Set<Libro> librosParaTipoLibro) {
        this.librosParaTipoLibro = librosParaTipoLibro;
    }
    
    public void addLibrosParaTipoLibro(Libro libro) {
        this.librosParaTipoLibro.add(libro);
    }
    
    public void removeLibrosParaTipoLibro(Libro libro) {
        this.librosParaTipoLibro.remove(libro);
    }
    
    public Set<PrelacionAnte> getPrelacionAntesParaTipoLibro() {
        return this.prelacionAntesParaTipoLibro;
    }

    public void setPrelacionAntesParaTipoLibro(Set<PrelacionAnte> prelacionAntesParaTipoLibro) {
        this.prelacionAntesParaTipoLibro = prelacionAntesParaTipoLibro;
    }
    
    public void addPrelacionAntesParaTipoLibro(PrelacionAnte prelacionAnte) {
        this.prelacionAntesParaTipoLibro.add(prelacionAnte);
    }
    
    public void removePrelacionAntesParaTipoLibro(PrelacionAnte prelacionAnte) {
        this.prelacionAntesParaTipoLibro.remove(prelacionAnte);
    }

    @Override
    public String toString(){
    	StringBuilder sb = new StringBuilder();
    	sb.append(" Id: ").append(this.getId());
    	sb.append(", Descripcion: ").append(this.getDescripcion());
    	sb.append(", Letra Tipo Acto:" ).append(this.getLetraTipoActo());
    	sb.append(", Letra Relacionada:" ).append(this.getLetraRelacionada());
    	sb.append(", Ley Letra Tipo Acto:" ).append(this.getLeyLetraTipoActo());
    	sb.append(", Ley Tipo Libro:" ).append(this.getLeyTipoLibro());
    	return sb.toString();
    }
}
