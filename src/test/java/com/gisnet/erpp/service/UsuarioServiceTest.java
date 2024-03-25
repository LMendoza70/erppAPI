package com.gisnet.erpp.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.ErppApplication;
import com.gisnet.erpp.domain.Funcion;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.web.api.account.FuncionDTO;
import com.gisnet.erpp.web.api.account.MenuItemDTO;
import com.gisnet.erpp.web.api.account.UsuarioDTO;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ErppApplication.class)
@Transactional
public class UsuarioServiceTest {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private UsuarioService  usuarioService;

    @Test
    public void create() {
    	/*
    	Optional<Usuario> usuario = usuarioService.findOneWithRolesAnFuncionesByUserName("6");
    	if (usuario.isPresent()){
    		UsuarioDTO dto = new UsuarioDTO(usuario.get());
	    	HashMap<Long, MenuItemDTO> menus = dto.getMenus();
	    	
	    	for (Map.Entry<Long, MenuItemDTO> m : menus.entrySet()){
	    		MenuItemDTO menu = m.getValue();
	    		log.debug("**" + menu.getRol());
	    		
	    		for (Map.Entry<Long, FuncionDTO> fdto : menu.getFuncionDTOs().entrySet()) {
	    			log.debug("***"+fdto.getValue().getFuncion().getNombre());
	    			
	    			for (Funcion f : fdto.getValue().getFunciones()){
	    				log.debug("****"+f.getNombre());
	    			}
	    		}    		
	    	}
    	} else {
    		log.debug("No se encontro el usuario para probar");
    	}*/
    }
}
