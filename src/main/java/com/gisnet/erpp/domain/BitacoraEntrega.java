package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A BitacoraEntrega.
 */
@Entity
@Table(name = "bitacora_entrega")
public class BitacoraEntrega implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bitacoraEntregaGenerator")
    @SequenceGenerator(allocationSize = 1, name = "bitacoraEntregaGenerator", sequenceName="bitacora_entrega_seq")
    private Long id;

    @Column(name = "fecha")
    private Date fecha;

    @Size(max = 80)
    @Column(name = "paterno", length = 80)
    private String paterno;

    @Size(max = 80)
    @Column(name = "materno", length = 80)
    private String materno;

    @Size(max = 255)
    @Column(name = "nombre", length = 255)
    private String nombre;
    

    @ManyToOne(optional = false)
    @NotNull
    private Prelacion prelacion;
    

    @ManyToOne(optional = false)
    @NotNull
    private Usuario usuario;



    public BitacoraEntrega(){}    

    public BitacoraEntrega(Date fecha, Prelacion prelacion, Usuario usuario) {
		super();
		this.fecha = fecha;
		this.prelacion = prelacion;
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

    public BitacoraEntrega fecha(Date fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }


    public Prelacion getPrelacion() {
        return prelacion;
    }

    public BitacoraEntrega prelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
        return this;
    }

    public void setPrelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public BitacoraEntrega usuario(Usuario usuario) {
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
	
	@JsonIgnore
    public String getNombreCompleto() {
		StringBuilder sb = new StringBuilder();
		if (nombre != null) {
			sb.append(nombre);
		}
		if (paterno != null) {
			sb.append(" " + paterno);
		}
		if (materno != null) {
			sb.append(" " + materno);
		}
		return sb.toString();
	}

}
