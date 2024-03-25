package com.gisnet.erpp.web.api.tipoacto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URISyntaxException;
import java.util.*;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Notario;
import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.service.TipoActoService;
import com.gisnet.erpp.vo.PrelacionIngresoVO;
import com.gisnet.erpp.domain.ActoPredioCampo;
import com.gisnet.erpp.service.ActoPredioCampoService;

@RestController
@RequestMapping(value = "/api/tipo-acto", produces = MediaType.APPLICATION_JSON_VALUE)
public class TipoActoRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	TipoActoService tipoActoService;
	
	@Autowired
	ActoPredioCampoService actoPredioCampoService;

	@GetMapping(value="/{id}")
    public ResponseEntity<TipoActoDTO> findOneWithModulosById(@PathVariable("id") Long id) {
		
		TipoActoDTO entidad =  new TipoActoDTO(tipoActoService.findOneWithModulosById(id));
		//List<ModuloDTO> entidadModulos =  entidad.parallelStream().collect(Collectors.toList());;
		List<ModuloDTO> entidadModulos = Collections.list(Collections.enumeration(entidad.getModulos()));
		//System.out.println("Orden ===> "+entidadModulos.get(1).getOrden());
		for(int i = 0; i <= entidadModulos.size()-1; i++)
		{
			System.out.println("Orden ===> "+entidadModulos.get(i).getOrden());
		}
		//return ResponseEntity.ok(new TipoActoDTO(tipoActoService.findOneWithModulosById(id)));
		return ResponseEntity.ok(entidad);
    }
	
	@GetMapping(value="/tipo-acto-tipo-acto/{tipoActoId}")
    public ResponseEntity<List<TipoActo>> findAllByTipoActoTipoActo(@PathVariable("tipoActoId") Long tipoActoId) {
		return ResponseEntity.ok(tipoActoService.findAllByTipoActoTipoActo(tipoActoId));
	}	
	
	@PostMapping("/save/{cambiaVersion}/{tipoEntradaId}")
	public ResponseEntity<Long> saveDatos(@RequestBody List<ActoPredioCampo> valores, @PathVariable("cambiaVersion") Long cambiaVersion, @PathVariable("tipoEntradaId") Long tipoEntradaId) throws URISyntaxException {
		log.debug("REST request salvando acto: cambioVersion = {}, tipoEntradaId = {} ", cambiaVersion, tipoEntradaId);
		Long id = actoPredioCampoService.save(valores, cambiaVersion, tipoEntradaId,false);
		if (id > 0) {
			return new ResponseEntity<>(id, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(id, HttpStatus.OK);
			
		}
	}
	
	@PostMapping("/save-mantenimiento/{cambiaVersion}/{tipoEntradaId}")
	public ResponseEntity<Long> saveDatosMantenimiento(@RequestBody List<ActoPredioCampo> valores, @PathVariable("cambiaVersion") Long cambiaVersion, @PathVariable("tipoEntradaId") Long tipoEntradaId) throws URISyntaxException {
		log.debug("REST request salvando acto: cambioVersion = {}, tipoEntradaId = {} ", cambiaVersion, tipoEntradaId);
		Long id = actoPredioCampoService.save(valores, cambiaVersion, tipoEntradaId,true);
		if (id > 0) {
			return new ResponseEntity<>(id, HttpStatus.OK);
		}
		else {
			return new ResponseEntity<>(id, HttpStatus.OK);
			
		}
	}
	
	@GetMapping(value="/masivos-dia-dia")
	public ResponseEntity<?> findMasivos()
	{
		return new ResponseEntity<>(tipoActoService.findMasivos(), HttpStatus.OK);
	}
	

}
