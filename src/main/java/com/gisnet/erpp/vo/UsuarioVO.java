package com.gisnet.erpp.vo;

import java.util.Set;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.HashSet;

import com.gisnet.erpp.domain.Notario;
import com.gisnet.erpp.domain.Rol;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.TipoUsuario;
import com.gisnet.erpp.domain.UsuNotario;
import com.gisnet.erpp.domain.Usuario;

public class UsuarioVO {

	private Long id = null;
	private String userName = "";
	private TipoUsuario tipoUsuario;
	private Boolean activo = false;
	private String contrasenia;
	private String email = "";
	private Notario notario = null;
	private Persona persona;
	private Oficina oficina;
	private String numEmpleado = null;
	private String puesto = null;
	
	private Set<Long> areasRoles = new HashSet<>();
	private Set<Long> funcionesRoles = new HashSet<>();
	private Set<Rol> roles= new HashSet<>();
	private Set<UsuNotario> usuNotariosParaUsuarios = new HashSet<>();
	
	private String hashFila = "";
	//private Set<Direccion> direccionesParaPersonas = new HashSet();
	
	public Long getId() {
		return this.id;
	}

	public void setId(Long id) {
			this.id = id;
	}

	public void setUserName(String userName) {
			this.userName = userName;
	}
	public String getUserName() {
		return this.userName;
	}
	
	public void settipoUsuario(TipoUsuario tipoUsuario) {
		this.tipoUsuario = tipoUsuario;
	}
	public TipoUsuario getTipoUsuario() {
		return this.tipoUsuario;
	}

	public void setActivo(Boolean activo) {
			this.activo = activo;
	}
	
	public Boolean isActivo() {
			return this.activo;
	}

	public void setContrasenia(String contrasenia) {
		PasswordEncoder pass = new BCryptPasswordEncoder();
		this.contrasenia = pass.encode(contrasenia);			
	}

	public String getContrasenia() {
			return this.contrasenia;
	}


	public String getEmail() {
			return this.email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}

	public void setNotario(Notario notario) {
			this.notario = notario;
	}
	
	public Notario getNotario() {
		return this.notario;
	}
	
	public void setOficina(Oficina oficina) {
		this.oficina = oficina;
	}

	public Oficina getOficina() {
		return this.oficina;
	}
	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	public Persona getPersona() {
		return this.persona;
	}
	
	public void setNumEmpleado(String numEmpleado) {
		this.numEmpleado = numEmpleado;
	}

	public String getNumEmpleado() {
		return this.numEmpleado;
	}
	
	public void setPuesto(String puesto) {
		this.puesto = puesto;
	}

	public String getPuesto() {
		return this.puesto;
	}
	
	public void setAreasRoles(ObjectLong[] areasRoles) {
		//System.out.println(areasRoles.length);
		
		for (ObjectLong arol : areasRoles) {
			try {
				this.areasRoles.add(arol.id);		
			}
			catch (Exception e) {
			}
		}
		
	}
	
	public Set<Long> getAreasRoles() {
		return this.areasRoles;
	}

	public void setFuncionesRolesParaUsuarios(ObjectLong[] funcionesRoles) {
		System.out.println(funcionesRoles.length);
		
		for (ObjectLong frol : funcionesRoles) {
			try {
				this.funcionesRoles.add(frol.id);		
			}
			catch (Exception e) {
			}
		}
		
	}
	
	public Set<Long> getFuncionesRolesParaUsuarios() {
		return this.funcionesRoles;
	}
	
	public void setRoles(ObjectRoles[] roles) {
		for (ObjectRoles rol : roles) {
			try {
				this.roles.add(rol.rol);		
			}
			catch (Exception e) {}
		}
	}
	
	public Set<Rol> getRoles() {
		return this.roles;
	}
	
	
	public void setUsuNotariosParaUsuarios(ObjectUsuaNotarios[] notarios) {
		UsuNotario usuNotario;
		for(ObjectUsuaNotarios objectUsuaNotario : notarios) {
			usuNotario = new UsuNotario();
			usuNotario.setNotario(objectUsuaNotario.notario);;
			this.usuNotariosParaUsuarios.add(usuNotario);
		}
	}
	
	public Set<UsuNotario> getUsuNotariosParaUsuarios() {
		return this.usuNotariosParaUsuarios;
	}

    public Usuario transformIntoUsuario() {


    	Usuario usuario = new Usuario();
    	usuario.setId(this.getId());
    	usuario.setUserName(this.getUserName());
    	usuario.setTipoUsuario(this.getTipoUsuario());
    	usuario.setActivo(this.isActivo());
    	usuario.setContrasenia(this.getContrasenia());

    	if(this.getEmail() != null ) {
				usuario.setEmail(this.getEmail());
		}

    	
    	if(this.getNotario() != null ) {
			usuario.setNotario(this.getNotario());
    	}
    	
    	usuario.setOficina(this.getOficina());
    	usuario.setPersona(this.getPersona());
    	
    	if(this.getNumEmpleado() != null ) {
			usuario.setNumEmpleado(this.getNumEmpleado());
    	}
    	
    	if(this.getPuesto() != null ) {
			usuario.setPuesto(this.getPuesto());
    	}
    	
    	usuario.setUsuNotariosParaUsuarios(this.getUsuNotariosParaUsuarios());
    	return usuario;
    }
    
}

class ObjectLong {
	public Long id;
}

class ObjectRoles {
	public Rol rol;
}

class ObjectUsuaNotarios {
	public Notario notario;
}

