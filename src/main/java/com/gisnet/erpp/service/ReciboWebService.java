package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.ConceptoPago;
import com.gisnet.erpp.repository.ConceptoPagoRepository;
import com.gisnet.erpp.web.api.recibooficial.ReciboOficialDTO;
import com.gisnet.erpp.web.api.recibooficial.ReciboOficialDetalleDTO;
import com.gisnet.erpp.ws.recibos.client.RecibosClient;

@Service
public class ReciboWebService{	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Value("${reciboswebservice.datetimeformat}")
	private String RECIBOS_WEB_SERVICE_FORMAT;
	
	@Autowired
	RecibosClient recibosClient;	
	
	@Autowired
	ConceptoPagoRepository conceptoPagoRepository;
	
	public ReciboOficialDTO consulta(String estadoCuenta){
		String detalles = recibosClient.obtieneDatosCobro(estadoCuenta);
		String header = recibosClient.obtieneDatosGral(estadoCuenta);
		ReciboOficialDTO recibo;
		//System.out.println("detalles  : "+ detalles + "header  : " + header);
		if(detalles.equals("NOFOLIO")|| header.equals("NOFOLIO")){
		//	System.out.println("Estoy pasando por aqui!");
			recibo = new ReciboOficialDTO();
		}else {
			
		recibo = new ReciboOficialDTO(header, detalles, RECIBOS_WEB_SERVICE_FORMAT);
	
		List<ReciboOficialDetalleDTO> listaValidada = new ArrayList<ReciboOficialDetalleDTO>();

		List<ConceptoPago> conceptoPagoList;
		List<ReciboOficialDetalleDTO> detallesList = recibo.getDetalles();
		for( ReciboOficialDetalleDTO detalle : detallesList ) {
			log.debug( "===> detalle.getConcepto() = "+detalle.getConcepto() );
			conceptoPagoList = conceptoPagoRepository.findByCveConceptoFinanzas(detalle.getConcepto());
			if( conceptoPagoList != null && conceptoPagoList.size() > 0 ) {
				detalle.setId( conceptoPagoList.get(0).getId() );
				listaValidada.add( detalle );
			}
		}
		recibo.setDetalles(listaValidada);
		}
		return recibo;
	}
		
}
