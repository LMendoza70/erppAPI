package com.gisnet.erpp.web.api.cotizador;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.ConceptoPago;
import com.gisnet.erpp.domain.Parametro;
import com.gisnet.erpp.service.ConceptoPagoService;
import com.gisnet.erpp.service.ParametroService;
import com.gisnet.erpp.util.ServletUtil;
import com.gisnet.erpp.vo.ConceptosPagosVO;

@RestController
@RequestMapping(value = "/api/cotizador", produces = MediaType.APPLICATION_JSON_VALUE)
public class CotizadorRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ConceptoPagoService conceptoPagoService;
	
	@Autowired
	private ParametroService paramtroService;

	@PostMapping("/cotizaServicio")
	public ResponseEntity<ConceptoPagoDTO> cotizarServicio(@RequestBody ConceptoPagoDTO conceptoPagoVO) {
		log.debug("request conceptoPagoId={}, campos={}", conceptoPagoVO.getId(), conceptoPagoVO.getCampos());
		ConceptoPagoDTO result = conceptoPagoService.cotizarServicio(conceptoPagoVO);		
		log.debug("result={}", result);		
		return ResponseEntity.ok(result);
	}
	
	@RequestMapping(value = "/imprimeCotizacion",method = RequestMethod.POST)
	public ResponseEntity<byte[]> imprimeCotizacion(@RequestBody ConceptosPagosVO conceptosPago, HttpServletRequest httpServletRequest) {
		 byte[] pdf = conceptoPagoService.generateMapPdfCotizacion(conceptosPago,ServletUtil.getContextFilePath(httpServletRequest));
		 HttpHeaders headers = new HttpHeaders();
		 headers.setContentType(MediaType.parseMediaType("application/pdf"));
		 String filename = "cotizacion.pdf";
		 headers.setContentDispositionFormData(filename, filename);
		 headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		 return new ResponseEntity<byte[]>(pdf, headers, HttpStatus.OK);
	}
	
	@GetMapping(value = "/servicio-cotizador/{servicioCotizadorId}")
	public ResponseEntity<List<ConceptoPago>> findByServicioCotizadorId(@PathVariable("servicioCotizadorId") Long servicioCotizadorId) {
		return ResponseEntity.ok(conceptoPagoService.findByServicioCotizador(servicioCotizadorId));
	}
	
	@GetMapping(value ="/servicio-clasificacion/{servicioCotizadorId}/{clasificacionConceptoId}")
	public ResponseEntity<List<ConceptoPago>> findByServicioCotizadorClasificacionConcepto(@PathVariable("servicioCotizadorId") Long servicioCotizadorId, @PathVariable("clasificacionConceptoId") Long clasificacionConceptoId) {
		return ResponseEntity.ok(conceptoPagoService.findByServicioCotizadorClasificacionConcepto(servicioCotizadorId, clasificacionConceptoId));
	}
	
	@GetMapping(value = "/mensajes")
	public ResponseEntity<Parametro[]> findByMensajes() {
		return ResponseEntity.ok(paramtroService.loadMensajeCotizador());
	}	
	
}
