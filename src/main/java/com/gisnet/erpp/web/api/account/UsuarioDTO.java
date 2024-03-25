package com.gisnet.erpp.web.api.account;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

import com.gisnet.erpp.domain.Funcion;
import com.gisnet.erpp.domain.FuncionRol;
import com.gisnet.erpp.domain.FuncionRolUsu;
import com.gisnet.erpp.domain.Rol;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.UsuarioRol;
import com.gisnet.erpp.util.Constantes;

public class UsuarioDTO {
	private Long id;

	@Pattern(regexp = Constantes.LOGIN_REGEX)
	@Size(min = 1, max = 50)
	private String userName;

	private String nombreCompleto;

	private Long oficinaId;

	private Long notarioId;

	private String oficinaNombre;

	private boolean activo = false;

	private Set<String> authorities;
	
	private String rfc;

	private String curp;

	private HashMap<Long, MenuItemDTO> menus;
	
	private long totalNotificaciones=0;

	private String telefono;

	public String getTelefono() {
		return telefono;
	}

	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	public UsuarioDTO() {

	}
	
	public UsuarioDTO(Usuario usuario) {
		this(usuario, 0);
	}

	public UsuarioDTO(Usuario usuario, long totalNotificaciones) {
		this.id = usuario.getId();
		this.userName = usuario.getUserName();
		this.nombreCompleto = usuario.getNombreCompleto();
		this.rfc = usuario.getPersona()!=null?usuario.getPersona().getRfc():"";
		this.curp = usuario.getPersona()!=null?usuario.getPersona().getCurp():"";
		this.telefono = usuario.getTelefono()!=null?usuario.getTelefono():"";
		if (usuario.getOficina() != null) {
			this.oficinaId = usuario.getOficina().getId();
			this.oficinaNombre = usuario.getOficina().getNombre();
		}

		if (usuario.getNotario() != null) {
			this.notarioId = usuario.getNotario().getId();
		}

		this.activo = usuario.isActivo();
		this.authorities = usuario.getUsuarioRolesParaUsuarios().stream().map(usuarioRol -> usuarioRol.getRol().getCve()).collect(Collectors.toSet());
		HashMap<Long, MenuItemDTO> menus = new HashMap<Long, MenuItemDTO>();
		Set<Long> funcionesRolParaUsuario = new HashSet<Long>();

		for (Iterator<FuncionRolUsu> iter = usuario.getFuncionesRolesParaUsuarios().iterator(); iter.hasNext();) {
			FuncionRolUsu actual = iter.next();
			if (actual.isActivo()){
				funcionesRolParaUsuario.add(actual.getFuncionRol().getId());
			}
		}

		Set<UsuarioRol> usuarioRoles = usuario.getUsuarioRolesParaUsuarios();
		for (UsuarioRol usuarioRol : usuarioRoles) {
			Rol rol = usuarioRol.getRol();
			MenuItemDTO menuItemDTO = new MenuItemDTO();
			menuItemDTO.setRol(rol);

			HashMap<Long, FuncionDTO> newFunciones = new HashMap<Long, FuncionDTO>();

			menuItemDTO.setFuncionDTOs(newFunciones);




			for (Iterator<FuncionRol> iter = rol.getFuncionRolesParaRols().iterator(); iter.hasNext();) {
				FuncionRol actual = iter.next();
				FuncionDTO vo = new FuncionDTO();
				if (actual.getFuncion().getFuncionPadre() == null && funcionesRolParaUsuario.contains(actual.getId())) {
					vo.setFuncion(actual.getFuncion());
					List<Funcion> subfunciones = new ArrayList<Funcion>();
					vo.setFunciones(subfunciones);
					newFunciones.put(vo.getFuncion().getId(), vo);
					iter.remove();
				}
			}

			for (Iterator<FuncionRol> iter = rol.getFuncionRolesParaRols().iterator(); iter.hasNext();) {
				FuncionRol actual = iter.next();
				if (actual.getFuncion().getFuncionPadre() != null) {
					Funcion f = actual.getFuncion();
					if (f.getFuncionPadre() != null && funcionesRolParaUsuario.contains(actual.getId()) && newFunciones.containsKey(f.getFuncionPadre().getId())) {
						List<Funcion> subfunciones = newFunciones.get(f.getFuncionPadre().getId()).getFunciones();
						subfunciones.add(f);
					}
					iter.remove();
				}
			}

			menus.put(rol.getId(), menuItemDTO);
		}

		this.menus = menus;
		this.totalNotificaciones=totalNotificaciones;
	}
	
	public String getRfc() {
		return rfc;
	}

	public void setRfc(String rfc) {
		this.rfc = rfc;
	}

	public String getCurp() {	
		return curp;	
	}	

	public void setCurp(String curp) {	
		this.curp = curp;	
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

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getNombreCompleto() {
		return nombreCompleto;
	}

	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	public Long getOficinaId() {
		return oficinaId;
	}

	public void setOficinaId(Long oficinaId) {
		this.oficinaId = oficinaId;
	}

	public Long getNotarioId() {
		return notarioId;
	}

	public void setNotarioId(Long notarioId) {
		this.notarioId = notarioId;
	}

	public String getOficinaNombre() {
		return oficinaNombre;
	}

	public void setOficinaNombre(String oficinaNombre) {
		this.oficinaNombre = oficinaNombre;
	}

	public boolean isActivo() {
		return activo;
	}

	public void setActivo(boolean activo) {
		this.activo = activo;
	}

	public Set<String> getAuthorities() {
		return authorities;
	}

	public void setAuthorities(Set<String> authorities) {
		this.authorities = authorities;
	}

	public HashMap<Long, MenuItemDTO> getMenus() {
		return menus;
	}

	public void setMenus(HashMap<Long, MenuItemDTO> menus) {
		this.menus = menus;
	}

	public long getTotalNotificaciones() {
		return totalNotificaciones;
	}

	public void setTotalNotificaciones(long totalNotificaciones) {
		this.totalNotificaciones = totalNotificaciones;
	}
	

}
