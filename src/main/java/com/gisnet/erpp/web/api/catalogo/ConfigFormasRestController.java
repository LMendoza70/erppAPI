package com.gisnet.erpp.web.api.catalogo;
import java.net.URISyntaxException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.domain.ModuloTipoActo;
import com.gisnet.erpp.domain.Modulo;
import com.gisnet.erpp.domain.ModuloCampo;
import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.Campo;
import com.gisnet.erpp.domain.TipoCampo;

import com.gisnet.erpp.service.TipoActoService;
import com.gisnet.erpp.service.ModuloTipoActoService;
import com.gisnet.erpp.service.ModuloService;
import com.gisnet.erpp.service.ModuloCampoService;
import com.gisnet.erpp.service.CampoService;
import com.gisnet.erpp.service.TipoCampoService;


@RestController
@RequestMapping(value = "/api/config-formas", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConfigFormasRestController {
	@Autowired
	private TipoActoService tipoActoService;
	@Autowired
	private ModuloTipoActoService moduloTipoActoService;
	@Autowired
	private ModuloService moduloService;
	@Autowired
	private ModuloCampoService moduloCampoService;
	@Autowired
    private CampoService campoService;
	@Autowired
	private TipoCampoService tipoCampoService;
	
	
	
	
	
	
	///Tipo Campo
	@GetMapping(value = "/tipos-campos")
    public ResponseEntity<List<TipoCampo>> findTiposCampos() {
		return ResponseEntity.ok(tipoCampoService.findAll());
	}
    
	
	
	
	
	
	///Campo
	@GetMapping(value="/campos")
    public ResponseEntity<Page<Campo>> getAllCampos(Pageable pageable, String nombre) {
		Page<Campo> page =  campoService.getAllCampos(pageable, nombre);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
	
	@GetMapping(value = "/campo/{id}")
    public ResponseEntity<Campo> findCampo(@PathVariable("id") Long id) {
		return ResponseEntity.ok(campoService.findOne(id));
	}
	
	@PostMapping("/save/campo")
	public ResponseEntity<Long> saveCampo(@RequestBody Campo campo) throws URISyntaxException {
		Long id = campoService.save(campo);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	@PostMapping("/delete/campo")
	public ResponseEntity<Long> deleteCampo(@RequestBody Campo campo) throws URISyntaxException {
		Long id = campoService.delete(campo);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	
	
	
	
	
	///Modulo - Campo
	@GetMapping(value="/modulo-campos/{id}")
    public ResponseEntity<List<ModuloCampo>> getModuloCampos(@PathVariable("id") Long id) {
        return new ResponseEntity<>(moduloCampoService.findByModuloId(id), HttpStatus.OK);
    }
	
	@GetMapping(value = "/modulo-campo/{id}")
    public ResponseEntity<ModuloCampo> findModuloCampo(@PathVariable("id") Long id) {
		return ResponseEntity.ok(moduloCampoService.findOne(id));
	}
	
	@PostMapping("/save/modulo-campo")
	public ResponseEntity<Long> saveModuloCampo(@RequestBody ModuloCampo moduloCampo) throws URISyntaxException {
		if (moduloCampo.getOrden() == null) {
			moduloCampo.setOrden(moduloCampoService.findCampoMaxOrden(moduloCampo) + 1);
		}
		Long id = moduloCampoService.save(moduloCampo);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	@PostMapping("/delete/modulo-campo")
	public ResponseEntity<Long> deleteModuloCampo(@RequestBody ModuloCampo moduloCampo) throws URISyntaxException {
		Long id = moduloCampoService.delete(moduloCampo);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	@PostMapping("/orden/up/modulo-campo")
	public ResponseEntity<Long> ordenUpModuloCampo(@RequestBody ModuloCampo moduloCampo) throws URISyntaxException {
		Long id = moduloCampoService.ordenUp(moduloCampo);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	@PostMapping("/orden/down/modulo-campo")
	public ResponseEntity<Long> ordenDownModuloCampo(@RequestBody ModuloCampo moduloCampo) throws URISyntaxException {
		Long id = moduloCampoService.ordenDown(moduloCampo);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	
	
	
	
	
	///Modulo
	@GetMapping(value="/modulos")
    public ResponseEntity<Page<Modulo>> getAllModulos(Pageable pageable, String nombre) {
		Page<Modulo> page =  moduloService.getAllModulos(pageable, nombre);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
	
	@GetMapping(value = "/modulo/{id}")
    public ResponseEntity<Modulo> findModulo(@PathVariable("id") Long id) {
		return ResponseEntity.ok(moduloService.findOne(id));
	}
	
	@PostMapping("/save/modulo")
	public ResponseEntity<Long> saveModulo(@RequestBody Modulo modulo) throws URISyntaxException {
		Long id = moduloService.save(modulo);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	@PostMapping("/delete/modulo")
	public ResponseEntity<Long> deleteModulo(@RequestBody Modulo modulo) throws URISyntaxException {
		Long id = moduloService.delete(modulo);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	
	
	
	
	
	///Modulo - Tipo Acto
	@GetMapping(value="/modulos-tipo-acto/{id}")
    public ResponseEntity<List<ModuloTipoActo>> getModulosPorTipoActo(@PathVariable("id") Long id) {
        return new ResponseEntity<>(moduloTipoActoService.findByTipoActoId(id), HttpStatus.OK);
    }
	
	@GetMapping(value = "/modulo-tipo-acto/{id}")
    public ResponseEntity<ModuloTipoActo> findModuloTipoActo(@PathVariable("id") Long id) {
		return ResponseEntity.ok(moduloTipoActoService.findOne(id));
	}
	
	@PostMapping("/save/modulo-tipo-acto")
	public ResponseEntity<Long> saveModuloTipoActo(@RequestBody ModuloTipoActo moduloTipoActo) throws URISyntaxException {
		if (moduloTipoActo.getOrden() == null) {
			moduloTipoActo.setOrden(moduloTipoActoService.findModuloMaxOrden(moduloTipoActo) + 1);
		}
		Long id = moduloTipoActoService.save(moduloTipoActo);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	@PostMapping("/delete/modulo-tipo-acto")
	public ResponseEntity<Long> deleteModuloTipoActo(@RequestBody ModuloTipoActo moduloTipoActo) throws URISyntaxException {
		Long id = moduloTipoActoService.delete(moduloTipoActo);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	@PostMapping("/orden/up/modulo-tipo-acto")
	public ResponseEntity<Long> ordenUpModuloTipoActo(@RequestBody ModuloTipoActo moduloTipoActo) throws URISyntaxException {
		Long id = moduloTipoActoService.ordenUp(moduloTipoActo);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	@PostMapping("/orden/down/modulo-tipo-acto")
	public ResponseEntity<Long> ordenDownModuloTipoActo(@RequestBody ModuloTipoActo moduloTipoActo) throws URISyntaxException {
		Long id = moduloTipoActoService.ordenDown(moduloTipoActo);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	///Tipo Acto
	@GetMapping(value="/tipos-actos")
    public ResponseEntity<Page<TipoActo>> getAllTiposActos(Pageable pageable, String nombre) {
		Page<TipoActo> page =  tipoActoService.getAllTiposActos(pageable, nombre);
        return new ResponseEntity<>(page, HttpStatus.OK);
    }
	
	@GetMapping(value = "/tipo-acto/{id}")
    public ResponseEntity<TipoActo> findTipoActo(@PathVariable("id") Long id) {
		return ResponseEntity.ok(tipoActoService.findOne(id));
	}
	
	@PostMapping("/save/tipo-acto")
	public ResponseEntity<Long> saveTipoActo(@RequestBody TipoActo tipoActo) throws URISyntaxException {
		Long id = tipoActoService.save(tipoActo);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	@PostMapping("/delete/tipo-acto")
	public ResponseEntity<Long> deleteTipoActo(@RequestBody TipoActo tipoActo) throws URISyntaxException {
		Long id = tipoActoService.delete(tipoActo);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	@GetMapping(value = "/all-tipos-actos")
    public ResponseEntity<List<TipoActo>> findTiposActos() {
		return ResponseEntity.ok(tipoActoService.findAll());
	}
	@GetMapping(value = "/tipo-acto/clasif-acto/{id}")
    public ResponseEntity<List<TipoActo>> findTiposActosByClasifActos(@PathVariable("id") Long id) {
		return ResponseEntity.ok(tipoActoService.findByClasifActoId(id));
	}
}
