package com.gisnet.erpp.service;

import java.io.StringReader;
import java.io.StringWriter;
import java.math.BigDecimal;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.CampoCotizador;
import com.gisnet.erpp.domain.ConceptoPago;
import com.gisnet.erpp.domain.Recibo;
import com.gisnet.erpp.domain.ReciboConcepto;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.ConceptoPagoRepository;
import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.repository.ReciboConceptoRepository;
import com.gisnet.erpp.repository.ReciboRepository;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.vo.ConceptosPagosVO;
import com.gisnet.erpp.web.api.cotizador.ConceptoPagoDetalleDTO;
import com.gisnet.erpp.web.api.cotizador.ConceptoPagoDTO;
import com.gisnet.erpp.xml.pago.carga.DetalleType;
import com.gisnet.erpp.xml.pago.carga.EmisorType;
import com.gisnet.erpp.xml.pago.carga.Multipago;

import net.sf.jasperreports.data.cache.NumberToSQLTimeTransformer;

@Service
public class PagoService {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Value("${pagoenlinea.servicio-id-asignadoporSEPAF}")
	private int servicioId;
	
	@Value("${pagoenlinea.emisor-id-asignadoporSEPAF}")
	private int emisorId;

	@Autowired
	UsuarioService usuarioService;

	@Autowired
	ConceptoPagoService conceptoPagoService;
	
	@Autowired
	ParametroRepository parametroRepository;
	
	@Autowired
	ReciboRepository reciboRepository;
	
	@Autowired
	ReciboConceptoRepository reciboConceptoRepository;
	
	@Autowired
	ConceptoPagoRepository conceptoPagoRepository;
	
	@Autowired
	ReciboService reciboService;
	
	@Transactional
	private Recibo saveRecibo(ConceptosPagosVO conceptosPagosVO){
		Recibo recibo = new Recibo();
		recibo.setGeneradoEnLinea(true);
		reciboRepository.save(recibo);
		
		ConceptoPagoDTO[] conceptos = conceptosPagosVO.getConceptosPago();
		for (int i = 0; i < conceptos.length; i++) {
			ConceptoPagoDTO conceptoPagoVO = conceptos[i];
			ReciboConcepto reciboConcepto = new ReciboConcepto();
			reciboConcepto.setConceptoPago(conceptoPagoRepository.findOne(conceptoPagoVO.getId()));
			reciboConcepto.setMonto(conceptoPagoVO.getTotal());			
			reciboConcepto.setRecibo(recibo);
			
			reciboConceptoRepository.save(reciboConcepto);
		} 

		return recibo;
		
	}

	@Transactional
	public String pago(ConceptosPagosVO conceptosPagosVO) throws JAXBException {
		StringWriter sw = new StringWriter();
		JAXBContext context = JAXBContext.newInstance(Multipago.class);

		ConceptoPagoDTO[] conceptos = conceptosPagosVO.getConceptosPago();
		
		Recibo recibo =  saveRecibo(conceptosPagosVO);

		Usuario usuario = usuarioService.findOne(SecurityUtils.getCurrentUserId());

		EmisorType emisor = new EmisorType();
		emisor.setUrlresp(parametroRepository.findByCve("QR_BASE_URI").getValor() + "/pago-en-linea/retorno");
		emisor.setServicioid(servicioId);
		emisor.setRfc(conceptosPagosVO.getRfcRecibo());
		emisor.setIdemisor(emisorId);
		emisor.setFolioemisor(String.valueOf(recibo.getId()));
		emisor.setNombrerecibo(conceptosPagosVO.getNombreRecibo());

		for (int i = 0; i < conceptos.length; i++) {
			ConceptoPagoDTO conceptoPagoVO = conceptos[i];

			logger.debug("conceptoPagoVO={}", conceptoPagoVO);
			
			List<ConceptoPagoDetalleDTO> detalle = conceptoPagoVO.getDetalle();
			
			int j=0;
			
			for (Iterator iterator = detalle.iterator(); iterator.hasNext();) {
				ConceptoPagoDetalleDTO conceptoPagoDetalleVO = (ConceptoPagoDetalleDTO) iterator.next();
			
				DetalleType d1 = new DetalleType();

	
				d1.setNdet(++j);
	
				d1.setConceptocobro(Short.valueOf(conceptoPagoDetalleVO.getClave()));
				
				if (conceptoPagoDetalleVO.isTarifaUnitaria()){
					d1.setCantidad(new BigDecimal(conceptoPagoDetalleVO.getCantidad()));
				} else {
					d1.setCantidad(conceptoPagoDetalleVO.getCosto());
				}
								
				emisor.getDetalle().add(d1);
				
			}

		}

		Multipago multiPagoType = new Multipago();
		multiPagoType.setEmisor(emisor);
		Marshaller m = context.createMarshaller();
		// m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);
		m.marshal(multiPagoType, sw);

		return sw.toString();

	}

	public String regresoTest(Long reciboId) throws JAXBException {
		StringWriter sw = new StringWriter();
		JAXBContext context = JAXBContext.newInstance(Multipago.class);

		Recibo recibo = reciboService.findByReciboId(reciboId);

		DetalleType d1 = new DetalleType();

		d1.setCantidad(new BigDecimal(1));

		EmisorType emisor = new EmisorType();
		emisor.setFolioemisor(String.valueOf(reciboId));

		emisor.getDetalle().add(d1);

		Multipago multiPagoType = new Multipago();
		multiPagoType.setEmisor(emisor);
		multiPagoType.setFechapago("");
		multiPagoType.setReferenciasepaf("");
		Marshaller m = context.createMarshaller();
		m.marshal(multiPagoType, sw);

		return sw.toString();

	}

	@Transactional
	public long regreso(String xmlmultipago) throws JAXBException {
		StringWriter sw = new StringWriter();
		JAXBContext context = JAXBContext.newInstance(Multipago.class);
		Unmarshaller m = context.createUnmarshaller();
		StringReader reader = new StringReader(xmlmultipago);
		Multipago multipago = (Multipago) m.unmarshal(reader);

		logger.debug("fechapago={}", multipago.getFechapago());
		logger.debug("referenciasepaf={}", multipago.getReferenciasepaf());

		logger.debug("RFC={}", multipago.getEmisor().getRfc());
		
		EmisorType emisor = multipago.getEmisor();
		
		logger.debug("Recibo Id={}", emisor.getFolioemisor());
		
		Recibo recibo = reciboRepository.findOne(Long.valueOf(emisor.getFolioemisor()));
		recibo.setPagadoEnLinea(true);
		reciboRepository.save(recibo);

		DetalleType detalle = multipago.getEmisor().getDetalle().iterator().next();

		logger.debug("Nombre Detalle={}", detalle.getNombredetalle());
		logger.debug("Folio Detalle={}", detalle.getFoliodetalle());
		logger.debug("Folio sepaf={}", detalle.getFoliosepaf());
		
		return recibo.getId();

	}

}
