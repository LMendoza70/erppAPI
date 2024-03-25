package com.gisnet.erpp.service;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.ErppApplication;
import com.gisnet.erpp.web.api.firma.FirmaDTO;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = ErppApplication.class)
@Transactional
public class RecibosWebServiceTest {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
		
	 @Autowired 
	 ReciboWebService reciboWebService;
	 
	 @Test
	 public void obtieneDatosCobro() {
		 //String result = reciboWebService.obtieneDatosCobro("1");
         
         //log.debug(result);
         
         //assertEquals("HOLA ! !!", original);  
	 }
	 
	 

}
