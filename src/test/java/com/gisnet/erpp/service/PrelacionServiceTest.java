package com.gisnet.erpp.service;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.ErppApplication;
import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.vo.PrelacionIngresoVO;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ErppApplication.class)
@Transactional
public class PrelacionServiceTest {


    @Autowired
    private PrelacionService  prelacionService;

    @Test
    public void create() {
        /*
    	PrelacionIngresoVO prelacionIngreso=new PrelacionIngresoVO();

    	Usuario usuario = new Usuario();
    	usuario.setId(1L);
    	
    	
    	Acto[] tramites = new Acto[2];
    	TipoActo tipoActo = new TipoActo();
    	tipoActo.setId(1L);
    	tramites[0]=new Acto();
    	tramites[0].setTipoActo(tipoActo);
    	
    	TipoActo tipoActo2 = new TipoActo();
    	tipoActo2.setId(2L);
    	tramites[1]=new Acto();
    	tramites[1].setTipoActo(tipoActo2);

    	prelacionIngreso.setActos(tramites);
    	prelacionIngreso.setSolicitante(usuario);
    	
    	Long id = prelacionService.create(prelacionIngreso);
    	
    	assertNotNull(id);
    	
        */

    }
    /*
    @Test
    public void findNextPrelacionAsignada(){
    	Prelacion prelacion = prelacionService.findNextPrelacionAsignadaAnalista("6");
    	assertNotNull(prelacion);
    }
    */
}

