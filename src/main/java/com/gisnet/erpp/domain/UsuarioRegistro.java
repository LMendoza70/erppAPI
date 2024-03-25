package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Usuario_Registro.
 */
@Entity
@Table(name = "usuario_registro")
public class UsuarioRegistro implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarioRegistroGenerator")
    @SequenceGenerator(allocationSize = 1, name = "usuarioRegistroGenerator", sequenceName="usuario_registro_seq")
    private Long id;

    @Size(max = 200)
    @Column(name = "email", length = 200)
    private String email;
    
    @Size(max = 80)
    @Column(name = "verificacion_token", length = 80)
    private String verificacionToken;

    @Column(name = "verificacion_fecha_exp")
    private Date verificacionFechaExp;
    
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getVerificacionToken() {
		return verificacionToken;
	}

	public void setVerificacionToken(String verificacionToken) {
		this.verificacionToken = verificacionToken;
	}

	public Date getVerificacionFechaExp() {
		return verificacionFechaExp;
	}

	public void setVerificacionFechaExp(Date verificacionFechaExp) {
		this.verificacionFechaExp = verificacionFechaExp;
	}

	@Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        UsuarioRegistro usuario = (UsuarioRegistro) o;
        if (usuario.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), usuario.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("UsuarioRegistro{");
        sb.append("email=").append(getEmail());
        sb.append("verificacion_token=").append(getVerificacionToken());
        sb.append("VerificacionFechaExp=").append(getVerificacionFechaExp());
        sb.append("}");
        return sb.toString();
    }
}
