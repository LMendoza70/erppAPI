package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A BitacoraLibroAntecedente.
 */
@Entity
@Table(name = "bitacora_libro_antecedente")
public class BitacoraLibroAntecedente implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bitacoraLibroAntecedenteGenerator")
    @SequenceGenerator(allocationSize = 1, name = "bitacoraLibroAntecedenteGenerator", sequenceName="bitacora_libro_antecedente_seq")
    private Long id;

    @Column(name = "fecha")
    private Date fecha;

    @Column(name = "anio")
    private Integer anio;
    
    @Column(name = "volumen")
    private String volumen;
    
    @Column(name = "tomo")
    private String tomo;
    
    @Column(name = "libro_bis")
    private String libroBis;  
    
    @Column(name = "inscripcion")
    private String inscripcion;  
    
    @Size(max = 100)
    @Column(name = "accion", length = 100)
    private String accion;
    
    @Column(name = "tipo_gestion")
    private String tipoGestion;
    
    @Column(name = "ruta_archivo")
    private String rutaArchivo;
    
    @ManyToOne(optional = false)
    @NotNull
    private Oficina oficina;
    
    @ManyToOne(optional = false)
    @NotNull
    private Seccion seccion;

    @ManyToOne(optional = false)
    @NotNull
    private Usuario usuario;
    
    @ManyToOne(optional = false)
    @NotNull
    private Libro libro;



    public BitacoraLibroAntecedente(){}    

    public BitacoraLibroAntecedente(Date fecha, Usuario usuario) {
		super();
		this.fecha = fecha;
		this.usuario = usuario;
	}

	
	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public BitacoraLibroAntecedente fecha(Date fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }


    public Usuario getUsuario() {
        return usuario;
    }

    public BitacoraLibroAntecedente usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

	public Integer getAnio() {
		return anio;
	}
	
	public BitacoraLibroAntecedente anio(Integer anio) {
		this.anio = anio;
        return this;
    }

	public void setAnio(Integer anio) {
		this.anio = anio;
	}

	public String getVolumen() {
		return volumen;
	}
	
	public BitacoraLibroAntecedente volumen(String volumen) {
		this.volumen = volumen;
        return this;
    }

	public void setVolumen(String volumen) {
		this.volumen = volumen;
	}

	public String getTomo() {
		return tomo;
	}
	
	public BitacoraLibroAntecedente tomo(String tomo) {
		this.tomo = tomo;
        return this;
    }

	public void setTomo(String tomo) {
		this.tomo = tomo;
	}

	public String getLibroBis() {
		return libroBis;
	}
	
	public BitacoraLibroAntecedente libroBis(String libroBis) {
		this.libroBis = libroBis;
        return this;
    }

	public void setLibroBis(String libroBis) {
		this.libroBis = libroBis;
	}

	public String getInscripcion() {
		return inscripcion;
	}
	
	public BitacoraLibroAntecedente inscripcion(String inscripcion) {
		this.inscripcion = inscripcion;
        return this;
    }

	public void setInscripcion(String inscripcion) {
		this.inscripcion = inscripcion;
	}

	public String getAccion() {
		return accion;
	}
	
	public BitacoraLibroAntecedente accion(String accion) {
		this.accion = accion;
        return this;
    }

	public void setAccion(String accion) {
		this.accion = accion;
	}

	public String getTipoGestion() {
		return tipoGestion;
	}
	
	public BitacoraLibroAntecedente tipoGestion(String tipoGestion) {
		this.tipoGestion = tipoGestion;
        return this;
    }

	public void setTipoGestion(String tipoGestion) {
		this.tipoGestion = tipoGestion;
	}

	public String getRutaArchivo() {
		return rutaArchivo;
	}
	
	public BitacoraLibroAntecedente rutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
        return this;
    }

	public void setRutaArchivo(String rutaArchivo) {
		this.rutaArchivo = rutaArchivo;
	}

	public Oficina getOficina() {
		return oficina;
	}
	
	public BitacoraLibroAntecedente oficina(Oficina oficina) {
		this.oficina = oficina;
        return this;
    }

	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

	public Seccion getSeccion() {
		return seccion;
	}
	
	public BitacoraLibroAntecedente seccion(Seccion seccion) {
		this.seccion = seccion;
        return this;
    }

	public void setSeccion(Seccion seccion) {
		this.seccion = seccion;
	}

	public Libro getLibro() {
		return libro;
	}
	
	public BitacoraLibroAntecedente libro(Libro libro) {
		this.libro = libro;
        return this;
    }

	public void setLibro(Libro libro) {
		this.libro = libro;
	}
    
	 @Override
	    public boolean equals(Object o) {
	        if (this == o) {
	            return true;
	        }
	        if (o == null || getClass() != o.getClass()) {
	            return false;
	        }
	        BitacoraLibroAntecedente bitacora = (BitacoraLibroAntecedente) o;
	        if (bitacora.getId() == null || getId() == null) {
	            return false;
	        }
	        return Objects.equals(getId(), bitacora.getId());
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hashCode(getId());
	    }

	    @Override
	    public String toString() {
	        return "Bitacora{" +
	            "id=" + getId() +
	            ", fecha='" + getFecha() + "'" +
	            "}";
	    }
    
}
