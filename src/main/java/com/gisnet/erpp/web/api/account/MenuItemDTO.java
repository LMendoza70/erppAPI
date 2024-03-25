package com.gisnet.erpp.web.api.account;

import java.util.HashMap;
import java.util.List;

import com.gisnet.erpp.domain.Rol;

public class MenuItemDTO {
	private Rol rol;
	private HashMap<Long, FuncionDTO> funcionDTOs;
	public Rol getRol() {
		return rol;
	}
	public void setRol(Rol rol) {
		this.rol = rol;
	}
	public HashMap<Long, FuncionDTO> getFuncionDTOs() {
		return funcionDTOs;
	}
	public void setFuncionDTOs(HashMap<Long, FuncionDTO> funcionDTOs) {
		this.funcionDTOs = funcionDTOs;
	}
	
	
	
	
	
	

}
