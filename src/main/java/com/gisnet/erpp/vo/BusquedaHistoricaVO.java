package com.gisnet.erpp.vo;

import java.util.List;

public class BusquedaHistoricaVO {

    public FoliosAnteVO foliosAnteVO;
    public List<PersonaVO> personaVO;
    
	public FoliosAnteVO getFoliosAnteVO() {
		return foliosAnteVO;
	}
	
	public void setFoliosAnteVO(FoliosAnteVO foliosAnteVO) {
		this.foliosAnteVO = foliosAnteVO;
	}
	
	public List<PersonaVO> getPersonaVO() {
		return personaVO;
	}
	
	public void setPersonaVO(List<PersonaVO> personaVO) {
		this.personaVO = personaVO;
	}	
}