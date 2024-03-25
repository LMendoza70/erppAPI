package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A Bitacora.
 */
@Entity
@Table(name = "bitacora_prelacion")
public class BitacoraPrelacion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bitacoraPrelacionGenerator")
    @SequenceGenerator(allocationSize = 1, name = "bitacoraPrelacionGenerator", sequenceName="bitacora_prelacion_seq")
    private Long id;

    @Column(name = "fecha")
    private Date fecha;

    @Size(max = 400)
    @Column(name = "paterno", length = 400)
    private String paterno;

    @Size(max = 80)
    @Column(name = "materno", length = 80)
    private String materno;

    @Size(max = 255)
    @Column(name = "nombre", length = 255)
    private String nombre;
    
    @Size(max = 400)
    @Column(name = "solicitante", length = 400)
    private String solicitante;
    
    @Column(name = "fecha_ingreso")
    private Date fechaIngreso;

    @ManyToOne(optional = false)
    @NotNull
    private Prelacion prelacion;
    
    @ManyToOne
    private PrelacionUsuarioDef prelacionUsuarioDef;

    @ManyToOne(optional = false)
    @NotNull
    private Prioridad prioridad;
    
    @ManyToOne(optional = false)
    @NotNull
    private Status status;
    

    @ManyToOne
    private Area area;
    
    @Column(name = "es_digitalizado")
    private Boolean es_digitalizado; 
    
    @Column(name = "subindice")
    private Long subindice;
    
    @ManyToOne
    private Usuario usuarioCapVal;
    
    @ManyToOne(optional = false)
    @NotNull
    private Usuario usuarioSolicitan;

    @ManyToOne(optional = false)
    @NotNull
    private Usuario usuario;



    public BitacoraPrelacion(){}    

    public BitacoraPrelacion(Date fecha, String observaciones, Prelacion prelacion, Status status, Usuario usuario) {
		super();
		this.fecha = fecha;
		this.prelacion = prelacion;
		this.status = status;
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

    public BitacoraPrelacion fecha(Date fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }


    public Prelacion getPrelacion() {
        return prelacion;
    }

    public BitacoraPrelacion prelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
        return this;
    }

    public void setPrelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
    }

    public Status getStatus() {
        return status;
    }

    public BitacoraPrelacion status(Status status) {
        this.status = status;
        return this;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public BitacoraPrelacion usuario(Usuario usuario) {
        this.usuario = usuario;
        return this;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Bitacora bitacora = (Bitacora) o;
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

	public String getPaterno() {
		return paterno;
	}

	public void setPaterno(String paterno) {
		this.paterno = paterno;
	}

	public String getMaterno() {
		return materno;
	}

	public void setMaterno(String materno) {
		this.materno = materno;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getSolicitante() {
		return solicitante;
	}

	public void setSolicitante(String solicitante) {
		this.solicitante = solicitante;
	}

	public Date getFechaIngreso() {
		return fechaIngreso;
	}

	public void setFechaIngreso(Date fechaIngreso) {
		this.fechaIngreso = fechaIngreso;
	}

	public PrelacionUsuarioDef getPrelacionUsuarioDef() {
		return prelacionUsuarioDef;
	}

	public void setPrelacionUsuarioDef(PrelacionUsuarioDef prelacionUsuarioDef) {
		this.prelacionUsuarioDef = prelacionUsuarioDef;
	}

	public Prioridad getPrioridad() {
		return prioridad;
	}

	public void setPrioridad(Prioridad prioridad) {
		this.prioridad = prioridad;
	}

	public Area getArea() {
		return area;
	}

	public void setArea(Area area) {
		this.area = area;
	}

	public Boolean getEs_digitalizado() {
		return es_digitalizado;
	}

	public void setEs_digitalizado(Boolean es_digitalizado) {
		this.es_digitalizado = es_digitalizado;
	}

	public Long getSubindice() {
		return subindice;
	}

	public void setSubindice(Long subindice) {
		this.subindice = subindice;
	}

	public Usuario getUsuarioCapVal() {
		return usuarioCapVal;
	}

	public void setUsuarioCapVal(Usuario usuarioCapVal) {
		this.usuarioCapVal = usuarioCapVal;
	}

	public Usuario getUsuarioSolicitan() {
		return usuarioSolicitan;
	}

	public void setUsuarioSolicitan(Usuario usuarioSolicitan) {
		this.usuarioSolicitan = usuarioSolicitan;
	}
}
