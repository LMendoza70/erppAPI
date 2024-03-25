package com.gisnet.erpp.domain;


import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

/**
 * A BitacoraDigitalizador.
 */
@Entity
@Table(name = "bitacora_digitalizador")
public class BitacoraDigitalizador implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "bitacoraDigitalizadorGenerator")
    @SequenceGenerator(allocationSize = 1, name = "bitacoraDigitalizadorGenerator", sequenceName="bitacora_digitalizador_seq")
    private Long id;

    @Column(name = "fecha")
    private Date fecha;

    @ManyToOne(optional = false)
    @NotNull
    private Prelacion prelacion;

    @ManyToOne(optional = false)
    @NotNull
    private Usuario usuario;


    public BitacoraDigitalizador(){}    

    public BitacoraDigitalizador(Date fecha, Prelacion prelacion, Usuario usuario) {
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

    public BitacoraDigitalizador fecha(Date fecha) {
        this.fecha = fecha;
        return this;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Prelacion getPrelacion() {
        return prelacion;
    }

    public BitacoraDigitalizador prelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
        return this;
    }

    public void setPrelacion(Prelacion prelacion) {
        this.prelacion = prelacion;
    }
    
    public Usuario getUsuario() {
        return usuario;
    }

    public BitacoraDigitalizador usuario(Usuario usuario) {
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
        BitacoraDigitalizador bitacoraDig = (BitacoraDigitalizador) o;
        if (bitacoraDig.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), bitacoraDig.getId());
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
            ", usuario='" + getUsuario() + "'" +
            "}";
    }
}
