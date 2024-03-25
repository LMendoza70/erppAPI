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
 * A Libro.
 */
@Entity
@Table(name = "libro")
public class Libro implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "libroGenerator")
    @SequenceGenerator(allocationSize = 1, name = "libroGenerator", sequenceName="libro_seq")
    private Long id;

    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "libro_bis")
    private String libroBis;

    @Column(name = "ruta", length = 250)
    private String ruta;

    @Column(name = "num_libro", length = 7)
    private String numLibro;
    
    @Column(name = "anio")
    private Integer anio;
    
    @Column(name = "volumen")
    private String volumen;    
    
    @Column(name = "tipo_doc")
    private Boolean tipoDoc;


    @Column(name = "inscriptiones")
    private String inscripciones;
    
    @Column(name = "fecha_ingreso")
    private Date fechaIngreso;
    
    @ManyToOne
    private NombreLibro nombreLibro;
    
    @ManyToOne
    private TipoLibro tipoLibro;
    
    @ManyToOne
    private SeccionPorOficina seccionesPorOficina; 

    @Column(name = "libro_id_hist")
    private Integer libro_id_hist;
    
    @OneToMany(mappedBy = "libro")
    @JsonIgnore
    private Set<Antecedente> antecedentesParaLibros = new HashSet<>();
    
    @OneToMany(mappedBy = "libro")
    @JsonIgnore
    private Set<Predio> prediosParaLibros = new HashSet<>();

    @OneToMany(mappedBy = "libro")
    @JsonIgnore
    private Set<PredioBitacora> prediosBitacorasParaLibros = new HashSet<>();

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

    public String getLibroBis() {
        return libroBis;
    }

    public void setLibroBis(String libroBis) {
        this.libroBis = libroBis;
    }

    public String getNumLibro() {
        return numLibro;
    }

    public Libro numLibro(String numLibro) {
        this.numLibro = numLibro;
        return this;
    }

    public void setNumLibro(String numLibro) {
        this.numLibro = numLibro;
    }
    
    public TipoLibro getTipoLibro() {
        return tipoLibro;
    }

    public Libro tipoLibro(TipoLibro tipoLibro) {
        this.tipoLibro = tipoLibro;
        return this;
    }

    public void setTipoLibro(TipoLibro tipoLibro) {
        this.tipoLibro = tipoLibro;
    }
    
    public NombreLibro getNombreLibro() {
        return nombreLibro;
    }

    public Libro nombreLibro(NombreLibro nombreLibro) {
        this.nombreLibro = nombreLibro;
        return this;
    }

    public void setNombreLibro(NombreLibro nombreLibro) {
        this.nombreLibro = nombreLibro;
    }
    

    public Boolean isActivo() {
        return activo;
    }

    public Libro activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }
    
    public Boolean getTipoDoc() {
        return tipoDoc;
    }

    public Libro tipoDoc(Boolean tipoDoc) {
        this.tipoDoc = tipoDoc;
        return this;
    }

    public void setTipoDoc(Boolean tipoDoc) {
        this.tipoDoc = tipoDoc;
    }
    
    public String getRuta() {
        return ruta;
    }
    
    public Libro ruta(String ruta) {
        this.ruta = ruta;
        return this;
    }

    public void setRuta(String ruta) {
        this.ruta = ruta;
    }
    
    public SeccionPorOficina getSeccionesPorOficina() {
        return seccionesPorOficina;
    }

    public Libro seccionesPorOficina(SeccionPorOficina seccionesPorOficina) {
        this.seccionesPorOficina = seccionesPorOficina;
        return this;
    }

    public void setSeccionesPorOficina(SeccionPorOficina seccionesPorOficina) {
        this.seccionesPorOficina = seccionesPorOficina;
    }

    public Set<Antecedente> getAntecedentesParaLibros() {
        return antecedentesParaLibros;
    }

    public Libro antecedentesParaLibros(Set<Antecedente> antecedentes) {
        this.antecedentesParaLibros = antecedentes;
        return this;
    }

    public Libro addAntecedentesParaLibro(Antecedente antecedente) {
        this.antecedentesParaLibros.add(antecedente);
        antecedente.setLibro(this);
        return this;
    }

    public Libro removeAntecedentesParaLibro(Antecedente antecedente) {
        this.antecedentesParaLibros.remove(antecedente);
        antecedente.setLibro(null);
        return this;
    }

    public void setAntecedentesParaLibros(Set<Antecedente> antecedentes) {
        this.antecedentesParaLibros = antecedentes;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Libro libro = (Libro) o;
        if (libro.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), libro.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "Libro{" +
            "id=" + getId() + descripcion() +
            "}";
    }
    
    public String descripcion(){
    	StringBuilder sb = new StringBuilder();
    	sb.append("Tomo: ").append(numLibro);
    	sb.append(", Libro: ").append(libroBis);
    	sb.append(", Sección:" ).append(seccionesPorOficina.getSeccion().getNombre());
    	sb.append(", Oficina:" ).append(seccionesPorOficina.getOficina().getNombre());
    	sb.append(", Año:" ).append(anio);
    	sb.append(", Volumen:" ).append(volumen);
    	return sb.toString();
    }

	public Integer getAnio() {
		return anio;
	}

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public String getVolumen() {
		return volumen;
	}

	public void setVolumen(String volumen) {
		this.volumen = volumen;
	}
	
	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}
    
    public Set<Predio> getPrediosParaLibros(){
        return this.prediosParaLibros;
    }
    public void setPrediosParaLibros(Set<Predio> prediosParaLibros){
        this.prediosParaLibros= prediosParaLibros;
    }

    public Set<PredioBitacora> getPrediosBitacorasParaLibros(){
        return this.prediosBitacorasParaLibros;
    }
    public void setPrediosBitacorasParaLibros(Set<PredioBitacora> prediosBitacorasParaLibros){
        this.prediosBitacorasParaLibros= prediosBitacorasParaLibros;
    }
    
    public Integer getLibro_id_hist() {
		return libro_id_hist;
	}

	public void setLibro_id_hist(Integer libro_id_hist) {
		this.libro_id_hist = libro_id_hist;
	}

    
}
