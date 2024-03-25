package com.gisnet.erpp.service;

import static org.junit.Assert.assertNotNull;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.ErppApplication;
import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.CampoCotizador;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.vo.PrelacionIngresoVO;
import com.gisnet.erpp.web.api.cotizador.ConceptoPagoDetalleDTO;
import com.gisnet.erpp.web.api.cotizador.ConceptoPagoDTO;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ErppApplication.class)
@Transactional
public class ConceptoPagoServiceTest {


    @Autowired
    private ConceptoPagoService  conceptoPagoService;

    @Test
    public void calculate() {
    	ConceptoPagoDTO conceptoPagoVO = new ConceptoPagoDTO();
    	conceptoPagoVO.setId(81L);
    	CampoCotizador campoCotizador = new CampoCotizador();
    	campoCotizador.setPropiedad("numCopiasSimples");
    	campoCotizador.setValor("10");
    	conceptoPagoVO.setCampos(new CampoCotizador[]{ campoCotizador });
    	
    	conceptoPagoService.cotizarServicio(conceptoPagoVO);    	
    }
    
    @Test
    public void calculate2() {
    	ConceptoPagoDTO conceptoPagoVO = new ConceptoPagoDTO();
    	conceptoPagoVO.setId(5L);
    	CampoCotizador campoCotizador = new CampoCotizador();
    	campoCotizador.setPropiedad("valorInmueble");
    	campoCotizador.setValor("1000000");
    	conceptoPagoVO.setCampos(new CampoCotizador[]{ campoCotizador });
    	
    	conceptoPagoService.cotizarServicio(conceptoPagoVO);    	
    }
    
    @Test
    public void calculate3() {
    	ConceptoPagoDTO conceptoPagoVO = new ConceptoPagoDTO();
    	conceptoPagoVO.setId(82L);
    	CampoCotizador campoCotizador = new CampoCotizador();
    	campoCotizador.setPropiedad("numHojasImprimibles");
    	campoCotizador.setValor("11");
    	conceptoPagoVO.setCampos(new CampoCotizador[]{ campoCotizador });
    	
    	ConceptoPagoDTO result = conceptoPagoService.cotizarServicio(conceptoPagoVO); 
    	
    	for (ConceptoPagoDetalleDTO conceptoPagoDetalleVO : result.getDetalle()){
    		System.out.println("conceptoPagoDetalleVO"+ conceptoPagoDetalleVO);
    		
    	}
    	
    }


 
}

