package com.gisnet.erpp.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.sql.Blob;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Usuario.
 */
@Entity
@Table(name = "usuario")
public class Usuario implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Transient
    @JsonProperty
    public String getNombreCompleto(){
    	StringBuilder sb = new StringBuilder();
    	if (persona!=null){
    		if (persona.getNombre()!=null){
    			sb.append(persona.getNombre());
    		} 
    		if (persona.getPaterno()!=null){
    			sb.append(" ").append(persona.getPaterno());
    		}
    		if (persona.getMaterno()!=null){
    			sb.append(" ").append(persona.getMaterno());
    		}
    	}
    	return sb.toString();
	}

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "usuarioGenerator")
    @SequenceGenerator(allocationSize = 1, name = "usuarioGenerator", sequenceName="usuario_seq")
    private Long id;

    @Size(max = 80)
    @Column(name = "user_name", length = 80)
    private String userName;

    @Size(max = 80)
    @Column(name = "contrasenia", length = 80)
    private String contrasenia;

    @Size(max = 100)
    @Column(name = "num_empleado", length = 100)
    private String numEmpleado;

    @Column(name = "enabled")
    private Boolean enabled;

    @Column(name = "account_non_expired")
    private Boolean accountNonExpired;

    @Column(name = "credentials_non_expired")
    private Boolean credentialsNonExpired;

    @Column(name = "account_non_locked")
    private Boolean accountNonLocked;

    @Size(max = 200)
    @Column(name = "motivo_baja", length = 200)
    private String motivoBaja;

    @Size(max = 200)
    @Column(name = "email", length = 200)
    private String email;

    @Size(max = 100)
    @Column(name = "puesto", length = 100)
    private String puesto;
    
    @Column(name = "p12")
    @JsonIgnore
    private Blob p12;
    
    @Size(max = 80)
    @Column(name = "p12_password", length = 80)
    private String p12Password;
    
    @NotNull
    @Column(name = "activo", nullable = false)
    private Boolean activo;
    
    
    @Column(name = "publico_df", nullable = true)
    private Boolean publicoDF;
    
    @Size(max = 20)
    @Column(name="telefono", length = 20)
    private String telefono;

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private Set<Acceso> accesosParaUsuarios = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private Set<ActoFirma> actoFirmasParaUsuarios = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private Set<Bitacora> bitacorasParaUsuarios = new HashSet<>();

    @OneToMany(mappedBy = "usuarioSolicitan")
    @JsonIgnore
    private Set<Prelacion> prelacionesParaUsuarioSolicitans = new HashSet<>();

    @OneToMany(mappedBy = "usuarioValPredios")
    @JsonIgnore
    private Set<Prelacion> prelacionesParaUsuarioValPredios = new HashSet<>();

    @OneToMany(mappedBy = "usuarioNotario")
    @JsonIgnore
    private Set<Prelacion> prelacionesParaUsuarioNotarios = new HashSet<>();

    @OneToMany(mappedBy = "usuarioVentanilla")
    @JsonIgnore
    private Set<Prelacion> prelacionesParaUsuarioVentanillas = new HashSet<>();

    @OneToMany(mappedBy = "usuarioCoordinador")
    @JsonIgnore
    private Set<Prelacion> prelacionesParaUsuarioCoordinadors = new HashSet<>();

    @OneToMany(mappedBy = "usuarioCalificador")
    @JsonIgnore
    private Set<Prelacion> prelacionesParaUsuarioCalificadors = new HashSet<>();

    @OneToMany(mappedBy = "usuarioAnalista")
    @JsonIgnore
    private Set<Prelacion> prelacionesParaUsuarioAnalistas = new HashSet<>();
    
    @OneToMany(mappedBy = "usuarioCapVal")
    @JsonIgnore
    private Set<Prelacion> prelacionesParaUsuarioCapVal = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private Set<UsuNotario> usuNotariosParaUsuarios = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private Set<UsuarioArea> usuarioAreasParaUsuarios = new HashSet<>();
    
    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private Set<UsuarioAreaRol> usuarioAreasRolesParaUsuarios = new HashSet<>();

    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private Set<UsuarioRol> usuarioRolesParaUsuarios = new HashSet<>();
    
    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private Set<Certificado> certificadosParaUsuarios = new HashSet<>();
    
    @OneToMany(mappedBy = "usuario")
    @JsonIgnore
    private Set<FuncionRolUsu> funcionesRolesParaUsuarios = new HashSet<>();

    @ManyToOne(optional = false)
    @NotNull
    private Persona persona;

    @ManyToOne(optional = false)
    @NotNull
    private TipoUsuario tipoUsuario;

    @ManyToOne
    private Notario notario;
    
    @ManyToOne
    private Dependencia dependencia;

    @ManyToOne
    private Oficina oficina;
	
    @ManyToOne
    private Area area;
    
    public String getP12Password() {
		return p12Password;
	}

	public void setP12Password(String p12Password) {
		this.p12Password = p12Password;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public Usuario userName(String userName) {
        this.userName = userName;
        return this;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getContrasenia() {
        return contrasenia;
    }

    public Usuario contrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
        return this;
    }

    public void setContrasenia(String contrasenia) {
        this.contrasenia = contrasenia;
    }

    public String getNumEmpleado() {
        return numEmpleado;
    }

    public Usuario numEmpleado(String numEmpleado) {
        this.numEmpleado = numEmpleado;
        return this;
    }

    public void setNumEmpleado(String numEmpleado) {
        this.numEmpleado = numEmpleado;
    }

    public Boolean isEnabled() {
        return enabled;
    }

    public Usuario enabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    public Usuario accountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
        return this;
    }

    public void setAccountNonExpired(Boolean accountNonExpired) {
        this.accountNonExpired = accountNonExpired;
    }

    public Boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    public Usuario credentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
        return this;
    }

    public void setCredentialsNonExpired(Boolean credentialsNonExpired) {
        this.credentialsNonExpired = credentialsNonExpired;
    }

    public Boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    public Usuario accountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
        return this;
    }

    public void setAccountNonLocked(Boolean accountNonLocked) {
        this.accountNonLocked = accountNonLocked;
    }

    public String getMotivoBaja() {
        return motivoBaja;
    }

    public Usuario motivoBaja(String motivoBaja) {
        this.motivoBaja = motivoBaja;
        return this;
    }

    public void setMotivoBaja(String motivoBaja) {
        this.motivoBaja = motivoBaja;
    }

    public String getEmail() {
        return email;
    }

    public Usuario email(String email) {
        this.email = email;
        return this;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPuesto() {
        return puesto;
    }

    public Usuario puesto(String puesto) {
        this.puesto = puesto;
        return this;
    }

    public void setPuesto(String puesto) {
        this.puesto = puesto;
    }

    public Boolean isActivo() {
        return activo;
    }
    
	public Usuario activo(Boolean activo) {
        this.activo = activo;
        return this;
    }

    public void setActivo(Boolean activo) {
        this.activo = activo;
    }

    public Set<Acceso> getAccesosParaUsuarios() {
        return accesosParaUsuarios;
    }

    public Usuario accesosParaUsuarios(Set<Acceso> accesos) {
        this.accesosParaUsuarios = accesos;
        return this;
    }

    public Usuario addAccesosParaUsuario(Acceso acceso) {
        this.accesosParaUsuarios.add(acceso);
        acceso.setUsuario(this);
        return this;
    }

    public Usuario removeAccesosParaUsuario(Acceso acceso) {
        this.accesosParaUsuarios.remove(acceso);
        acceso.setUsuario(null);
        return this;
    }

    public void setAccesosParaUsuarios(Set<Acceso> accesos) {
        this.accesosParaUsuarios = accesos;
    }

    public Set<ActoFirma> getActoFirmasParaUsuarios() {
        return actoFirmasParaUsuarios;
    }

    public Usuario actoFirmasParaUsuarios(Set<ActoFirma> actoFirmas) {
        this.actoFirmasParaUsuarios = actoFirmas;
        return this;
    }

    public Usuario addActoFirmasParaUsuario(ActoFirma actoFirma) {
        this.actoFirmasParaUsuarios.add(actoFirma);
        actoFirma.setUsuario(this);
        return this;
    }

    public Usuario removeActoFirmasParaUsuario(ActoFirma actoFirma) {
        this.actoFirmasParaUsuarios.remove(actoFirma);
        actoFirma.setUsuario(null);
        return this;
    }

    public void setActoFirmasParaUsuarios(Set<ActoFirma> actoFirmas) {
        this.actoFirmasParaUsuarios = actoFirmas;
    }

    public Set<Bitacora> getBitacorasParaUsuarios() {
        return bitacorasParaUsuarios;
    }

    public Usuario bitacorasParaUsuarios(Set<Bitacora> bitacoras) {
        this.bitacorasParaUsuarios = bitacoras;
        return this;
    }

    public Usuario addBitacorasParaUsuario(Bitacora bitacora) {
        this.bitacorasParaUsuarios.add(bitacora);
        bitacora.setUsuario(this);
        return this;
    }

    public Usuario removeBitacorasParaUsuario(Bitacora bitacora) {
        this.bitacorasParaUsuarios.remove(bitacora);
        bitacora.setUsuario(null);
        return this;
    }

    public void setBitacorasParaUsuarios(Set<Bitacora> bitacoras) {
        this.bitacorasParaUsuarios = bitacoras;
    }

    public Set<Prelacion> getPrelacionesParaUsuarioSolicitans() {
        return prelacionesParaUsuarioSolicitans;
    }

    public Usuario prelacionesParaUsuarioSolicitans(Set<Prelacion> prelacions) {
        this.prelacionesParaUsuarioSolicitans = prelacions;
        return this;
    }

    public Usuario addPrelacionesParaUsuarioSolicitan(Prelacion prelacion) {
        this.prelacionesParaUsuarioSolicitans.add(prelacion);
        prelacion.setUsuarioSolicitan(this);
        return this;
    }

    public Usuario removePrelacionesParaUsuarioSolicitan(Prelacion prelacion) {
        this.prelacionesParaUsuarioSolicitans.remove(prelacion);
        prelacion.setUsuarioSolicitan(null);
        return this;
    }

    public void setPrelacionesParaUsuarioSolicitans(Set<Prelacion> prelacions) {
        this.prelacionesParaUsuarioSolicitans = prelacions;
    }

    public Set<Prelacion> getPrelacionesParaUsuarioValPredios() {
        return prelacionesParaUsuarioValPredios;
    }

    public Usuario prelacionesParaUsuarioValPredios(Set<Prelacion> prelacions) {
        this.prelacionesParaUsuarioValPredios = prelacions;
        return this;
    }

    public Usuario addPrelacionesParaUsuarioValPredios(Prelacion prelacion) {
        this.prelacionesParaUsuarioValPredios.add(prelacion);
        prelacion.setUsuarioValPredios(this);
        return this;
    }

    public Usuario removePrelacionesParaUsuarioValPredios(Prelacion prelacion) {
        this.prelacionesParaUsuarioValPredios.remove(prelacion);
        prelacion.setUsuarioValPredios(null);
        return this;
    }

    public void setPrelacionesParaUsuarioValPredios(Set<Prelacion> prelacions) {
        this.prelacionesParaUsuarioValPredios = prelacions;
    }

    public Set<Prelacion> getPrelacionesParaUsuarioNotarios() {
        return prelacionesParaUsuarioNotarios;
    }

    public Usuario prelacionesParaUsuarioNotarios(Set<Prelacion> prelacions) {
        this.prelacionesParaUsuarioNotarios = prelacions;
        return this;
    }

    public Usuario addPrelacionesParaUsuarioNotario(Prelacion prelacion) {
        this.prelacionesParaUsuarioNotarios.add(prelacion);
        prelacion.setUsuarioNotario(this);
        return this;
    }

    public Usuario removePrelacionesParaUsuarioNotario(Prelacion prelacion) {
        this.prelacionesParaUsuarioNotarios.remove(prelacion);
        prelacion.setUsuarioNotario(null);
        return this;
    }

    public void setPrelacionesParaUsuarioNotarios(Set<Prelacion> prelacions) {
        this.prelacionesParaUsuarioNotarios = prelacions;
    }

    public Set<Prelacion> getPrelacionesParaUsuarioVentanillas() {
        return prelacionesParaUsuarioVentanillas;
    }

    public Usuario prelacionesParaUsuarioVentanillas(Set<Prelacion> prelacions) {
        this.prelacionesParaUsuarioVentanillas = prelacions;
        return this;
    }

    public Usuario addPrelacionesParaUsuarioVentanilla(Prelacion prelacion) {
        this.prelacionesParaUsuarioVentanillas.add(prelacion);
        prelacion.setUsuarioVentanilla(this);
        return this;
    }

    public Usuario removePrelacionesParaUsuarioVentanilla(Prelacion prelacion) {
        this.prelacionesParaUsuarioVentanillas.remove(prelacion);
        prelacion.setUsuarioVentanilla(null);
        return this;
    }

    public void setPrelacionesParaUsuarioVentanillas(Set<Prelacion> prelacions) {
        this.prelacionesParaUsuarioVentanillas = prelacions;
    }

    public Set<Prelacion> getPrelacionesParaUsuarioCoordinadors() {
        return prelacionesParaUsuarioCoordinadors;
    }

    public Usuario prelacionesParaUsuarioCoordinadors(Set<Prelacion> prelacions) {
        this.prelacionesParaUsuarioCoordinadors = prelacions;
        return this;
    }

    public Usuario addPrelacionesParaUsuarioCoordinador(Prelacion prelacion) {
        this.prelacionesParaUsuarioCoordinadors.add(prelacion);
        prelacion.setUsuarioCoordinador(this);
        return this;
    }

    public Usuario removePrelacionesParaUsuarioCoordinador(Prelacion prelacion) {
        this.prelacionesParaUsuarioCoordinadors.remove(prelacion);
        prelacion.setUsuarioCoordinador(null);
        return this;
    }

    public void setPrelacionesParaUsuarioCoordinadors(Set<Prelacion> prelacions) {
        this.prelacionesParaUsuarioCoordinadors = prelacions;
    }

    public Set<Prelacion> getPrelacionesParaUsuarioCalificadors() {
        return prelacionesParaUsuarioCalificadors;
    }

    public Usuario prelacionesParaUsuarioCalificadors(Set<Prelacion> prelacions) {
        this.prelacionesParaUsuarioCalificadors = prelacions;
        return this;
    }

    public Usuario addPrelacionesParaUsuarioCalificador(Prelacion prelacion) {
        this.prelacionesParaUsuarioCalificadors.add(prelacion);
        prelacion.setUsuarioCalificador(this);
        return this;
    }

    public Usuario removePrelacionesParaUsuarioCalificador(Prelacion prelacion) {
        this.prelacionesParaUsuarioCalificadors.remove(prelacion);
        prelacion.setUsuarioCalificador(null);
        return this;
    }

    public void setPrelacionesParaUsuarioCalificadors(Set<Prelacion> prelacions) {
        this.prelacionesParaUsuarioCalificadors = prelacions;
    }

    public Set<Prelacion> getPrelacionesParaUsuarioAnalistas() {
        return prelacionesParaUsuarioAnalistas;
    }

    public Usuario prelacionesParaUsuarioAnalistas(Set<Prelacion> prelacions) {
        this.prelacionesParaUsuarioAnalistas = prelacions;
        return this;
    }

    public Usuario addPrelacionesParaUsuarioAnalista(Prelacion prelacion) {
        this.prelacionesParaUsuarioAnalistas.add(prelacion);
        prelacion.setUsuarioAnalista(this);
        return this;
    }

    public Usuario removePrelacionesParaUsuarioAnalista(Prelacion prelacion) {
        this.prelacionesParaUsuarioAnalistas.remove(prelacion);
        prelacion.setUsuarioAnalista(null);
        return this;
    }

    public void setPrelacionesParaUsuarioAnalistas(Set<Prelacion> prelacions) {
        this.prelacionesParaUsuarioAnalistas = prelacions;
    }

    public Set<UsuNotario> getUsuNotariosParaUsuarios() {
        return usuNotariosParaUsuarios;
    }

    public Usuario usuNotariosParaUsuarios(Set<UsuNotario> usuNotarios) {
        this.usuNotariosParaUsuarios = usuNotarios;
        return this;
    }

    public Usuario addUsuNotariosParaUsuario(UsuNotario usuNotario) {
        this.usuNotariosParaUsuarios.add(usuNotario);
        usuNotario.setUsuario(this);
        return this;
    }

    public Usuario removeUsuNotariosParaUsuario(UsuNotario usuNotario) {
        this.usuNotariosParaUsuarios.remove(usuNotario);
        usuNotario.setUsuario(null);
        return this;
    }

    public void setUsuNotariosParaUsuarios(Set<UsuNotario> usuNotarios) {
        this.usuNotariosParaUsuarios = usuNotarios;
        this.usuNotariosParaUsuarios.forEach(x -> x.setUsuario(this));
    }

    public Set<UsuarioArea> getUsuarioAreasParaUsuarios() {
        return usuarioAreasParaUsuarios;
    }

    public Usuario usuarioAreasParaUsuarios(Set<UsuarioArea> usuarioAreas) {
        this.usuarioAreasParaUsuarios = usuarioAreas;
        return this;
    }

    public Usuario addUsuarioAreasParaUsuario(UsuarioArea usuarioArea) {
        this.usuarioAreasParaUsuarios.add(usuarioArea);
        usuarioArea.setUsuario(this);
        return this;
    }

    public Usuario removeUsuarioAreasParaUsuario(UsuarioArea usuarioArea) {
        this.usuarioAreasParaUsuarios.remove(usuarioArea);
        usuarioArea.setUsuario(null);
        return this;
    }

    public void setUsuarioAreasParaUsuarios(Set<UsuarioArea> usuarioAreas) {
        this.usuarioAreasParaUsuarios = usuarioAreas;
        this.usuarioAreasParaUsuarios.forEach(x -> x.setUsuario(this));
    }
    
    
    public Usuario usuarioAreasRolesParaUsuario(Set<UsuarioAreaRol> usuarioAreasRoles) {
        this.usuarioAreasRolesParaUsuarios = usuarioAreasRoles;
        return this;
    }

    public Usuario addUsuarioAreasRolesParaUsuario(UsuarioAreaRol usuarioAreaRol) {
        this.usuarioAreasRolesParaUsuarios.add(usuarioAreaRol);
        usuarioAreaRol.setUsuario(this);
        return this;
    }

    public Usuario removeUsuarioAreasRolesParaUsuario(UsuarioAreaRol usuarioAreaRol) {
        this.usuarioAreasRolesParaUsuarios.remove(usuarioAreaRol);
        usuarioAreaRol.setUsuario(null);
        return this;
    }

    public void setUsuarioAreasRolesParaUsuarios(Set<UsuarioAreaRol> usuarioAreasRoles) {
        this.usuarioAreasRolesParaUsuarios = usuarioAreasRoles;
        this.usuarioAreasRolesParaUsuarios.forEach(x -> x.setUsuario(this));
    }
    
    public Set<UsuarioAreaRol> getUsuarioAreasRolesParaUsuarios() {
    	return this.usuarioAreasRolesParaUsuarios;
    }

    public Set<UsuarioRol> getUsuarioRolesParaUsuarios() {
        return usuarioRolesParaUsuarios;
    }

    public Usuario usuarioRolesParaUsuarios(Set<UsuarioRol> usuarioRols) {
        this.usuarioRolesParaUsuarios = usuarioRols;
        return this;
    }

    public Usuario addUsuarioRolesParaUsuario(UsuarioRol usuarioRol) {
        this.usuarioRolesParaUsuarios.add(usuarioRol);
        usuarioRol.setUsuario(this);
        return this;
    }

    public Usuario removeUsuarioRolesParaUsuario(UsuarioRol usuarioRol) {
        this.usuarioRolesParaUsuarios.remove(usuarioRol);
        usuarioRol.setUsuario(null);
        return this;
    }

    public void setUsuarioRolesParaUsuarios(Set<UsuarioRol> usuarioRols) {
        this.usuarioRolesParaUsuarios = usuarioRols;
        this.usuarioRolesParaUsuarios.forEach(x -> x.setUsuario(this));
    }
    
    public Set<Certificado> getCertificadosParaUsuarios() {
		return certificadosParaUsuarios;
	}

	public void setCertificadosParaUsuarios(Set<Certificado> certificadosParaUsuarios) {
		this.certificadosParaUsuarios = certificadosParaUsuarios;
	}
	
	public Set<Prelacion> getPrelacionesParaUsuarioCapVal() {
		return prelacionesParaUsuarioCapVal;
	}

	public void setPrelacionesParaUsuarioCapVal(Set<Prelacion> prelacionesParaUsuarioCapVal) {
		this.prelacionesParaUsuarioCapVal = prelacionesParaUsuarioCapVal;
	}

	public Set<FuncionRolUsu> getFuncionesRolesParaUsuarios() {
		return this.funcionesRolesParaUsuarios;
	}

	public Persona getPersona() {
        return persona;
    }

    public Usuario persona(Persona persona) {
        this.persona = persona;
        return this;
    }

    public void setPersona(Persona persona) {
        this.persona = persona;
    }

    public TipoUsuario getTipoUsuario() {
        return tipoUsuario;
    }

    public Usuario tipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
        return this;
    }

    public void setTipoUsuario(TipoUsuario tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

    public Notario getNotario() {
        return notario;
    }

    public Usuario notario(Notario notario) {
        this.notario = notario;
        return this;
    }

    public void setNotario(Notario notario) {
        this.notario = notario;
    }

    public Oficina getOficina() {
        return oficina;
    }

    public Usuario oficina(Oficina oficina) {
        this.oficina = oficina;
        return this;
    }

    public void setOficina(Oficina oficina) {
        this.oficina = oficina;
    }
    
    public Dependencia getDependencia() {
        return dependencia;
    }

    public Usuario dependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
        return this;
    }

    public void setDependencia(Dependencia dependencia) {
        this.dependencia = dependencia;
    }
	
    public Area getArea() {
        return area;
    }

    public Usuario area(Area area) {
        this.area = area;
        return this;
    }

    public void setArea(Area area) {
        this.area = area;
    }
    
    public Blob getP12() {
		return p12;
	}

	public void setP12(Blob p12) {
		this.p12 = p12;
	}
	
	
	public Boolean isPublicoDF() {
        return publicoDF;
    }
    
	public Usuario publicoDF(Boolean publicoDF) {
        this.publicoDF = publicoDF;
        return this;
    }

    public void setPublicoDF(Boolean publicoDF) {
        this.publicoDF = publicoDF;
    }

	public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Usuario usuario = (Usuario) o;
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
        return "Usuario{" +
            "id=" + getId() +
            ", userName='" + getUserName() + "'" +
            ", contrasenia='" + getContrasenia() + "'" +
            ", numEmpleado='" + getNumEmpleado() + "'" +
            ", enabled='" + isEnabled() + "'" +
            ", accountNonExpired='" + isAccountNonExpired() + "'" +
            ", credentialsNonExpired='" + isCredentialsNonExpired() + "'" +
            ", accountNonLocked='" + isAccountNonLocked() + "'" +
            ", motivoBaja='" + getMotivoBaja() + "'" +
            ", email='" + getEmail() + "'" +
            ", puesto='" + getPuesto() + "'" +
            ", activo='" + isActivo() + "'" +
            ", telefono='" + getTelefono() + "'" +
            "}";
    }
}
