package com.gisnet.erpp.web.api.tipoacto;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.*;

import com.gisnet.erpp.domain.ModuloTipoActo;
import com.gisnet.erpp.domain.TipoActo;


public class TipoActoDTO {
    private Long id;

    private String nombre;

    private Set<ModuloDTO> modulos;
    
    public TipoActoDTO() {
        
    }

    public TipoActoDTO(TipoActo tipoActo) {
    	this.id = tipoActo.getId();

    	this.nombre = tipoActo.getNombre();//
    	//Map<String,String> m = new LinkedHashMap<String,String>(tipoActo.getModuloTipoActosParaTipoActos().stream().ma);
    	
    	//this.modulos = tipoActo.getModuloTipoActosParaTipoActos().stream().map(moduloTipoActo -> new ModuloDTO(moduloTipoActo)).collect(Collectors.toCollection(LinkedHashSet::new) );
    	List<ModuloTipoActo> listOrdenada= new ArrayList<ModuloTipoActo>(tipoActo.getModuloTipoActosParaTipoActos());
    	int duplicados=0;
		 for ( int i = 0; i < listOrdenada.size(); i++ )
	     {
	        for ( int j = 0; j < listOrdenada.size(); j++ )
	        {
	           if ( i == j )
	           {
	            
	           }

	           else if (listOrdenada.get(j).getModulo().getId()==listOrdenada.get(i).getModulo().getId()) 
	           {
	        	   listOrdenada.remove( j );
	        	   duplicados++;
	           }
	        }
	    }

		 System.out.println("Modulos Dupilicados ELIMINADOS "+duplicados);
          
		 this.modulos = listOrdenada.stream().map(moduloTipoActo -> new ModuloDTO(moduloTipoActo)).collect(Collectors.toCollection(LinkedHashSet::new) );
    }

	public Long getId(){ 
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNombre() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public Set<ModuloDTO> getModulos() {
		return modulos;
	}

	public void setModulos(Set<ModuloDTO> modulos) {
		this.modulos = modulos;
	}
    
    

   	

}
