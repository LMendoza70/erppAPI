package com.gisnet.erpp.web.api.conceptopago;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.ConceptoPago;
import com.gisnet.erpp.domain.UsuarioOfiAreas;
import com.gisnet.erpp.service.ConceptoPagoService;
import com.gisnet.erpp.web.api.cotizador.ConceptoPagoDTO;

@RestController
@RequestMapping(value = "/api/concepto-pago", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConceptoPagoRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ConceptoPagoService conceptoPagoService;

	
	@GetMapping("/conceptos")
	public ResponseEntity<Page<ConceptoPago>> findAllBy(Pageable pageable, String nombre, Long areaId, Long clasifActoId) {
		log.debug("nombre=" + nombre);
		Page<ConceptoPago> page = conceptoPagoService.findAllBy(pageable, nombre, areaId, clasifActoId);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}
	
	@PostMapping
	public ResponseEntity<?> saveConceptoPago(@RequestBody ConceptoPago conceptoPago) {
		return save(conceptoPago);
	}
	
	@PutMapping
	public ResponseEntity<?> updateConceptoPago(@RequestBody ConceptoPago conceptoPago) {
		return save(conceptoPago);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> deleteAsetnamiento(@PathVariable("id") Long id) {
		try {
			conceptoPagoService.delete(id);
			return new ResponseEntity<>(HttpStatus.OK);
		}
		catch (UnsupportedOperationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	private ResponseEntity<?> save(ConceptoPago ConceptoPago) {
		try {
			conceptoPagoService.save(ConceptoPago);
			return new ResponseEntity<>(ConceptoPago, HttpStatus.OK);
		}
		catch (UnsupportedOperationException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@GetMapping(value = "/area/{areaId}")
	public ResponseEntity<List<ConceptoPago>> findByAreaId(@PathVariable("areaId") Long areaId) {
		return ResponseEntity.ok(conceptoPagoService.findByAreaIdAndClasifActoIsNull(areaId));
	}
		
	@PostMapping("/areaClasifActo")
	public ResponseEntity<List<ConceptoPago>> findByAreaIdAndClasifActoId(@RequestBody ConceptoPagoDTO conceptoPago) {
		return ResponseEntity.ok(conceptoPagoService.findByAreaIdAndClasifActoId(conceptoPago));
	}
	
	@PostMapping("/areaClasifActoTipoActo")
	public ResponseEntity<List<ConceptoPago>> findByAreaIdAndClasifActoIdAndTipoActoId(@RequestBody ConceptoPagoDTO conceptoPago) {
		return ResponseEntity.ok(conceptoPagoService.findByAreaIdAndClasifActoIdAndTipoActoId(conceptoPago));
	}
	@GetMapping ("/mostrar-concepto-pago")
	public ResponseEntity<List<ConceptoPago>> mostrarConceptoPago (){
		return ResponseEntity.ok(conceptoPagoService.findAll());
	}
	
	@PostMapping("/update-conceptoPago")
	public ResponseEntity<List<ConceptoPago>> updateUsuarioOfiA( @RequestBody List <ConceptoPago> conceptoPago ){
		return ResponseEntity.ok(conceptoPagoService.updateConceptoPago(conceptoPago));
	}
	
	@GetMapping ("/conceptos-adicionales")
	public ResponseEntity<List<ConceptoPago>> findConceptosAdicionales (){
		return ResponseEntity.ok(conceptoPagoService.findConceptosAdicionales());
	}
}
