package com.gisnet.erpp.web.api.area;

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

import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.AreaClasifActo;
import com.gisnet.erpp.domain.ClasifActo;
import com.gisnet.erpp.domain.Servicio;
import com.gisnet.erpp.domain.SubServicio;
import com.gisnet.erpp.domain.SubSubServicio;
import com.gisnet.erpp.domain.ConceptoPago;

import com.gisnet.erpp.service.AreaService;
import com.gisnet.erpp.service.AreaClasifActoService;
import com.gisnet.erpp.service.ClasifActoService;
import com.gisnet.erpp.service.ServicioService;
import com.gisnet.erpp.service.ConceptoPagoService;

@RestController
@RequestMapping(value = "/api/config-formas", produces = MediaType.APPLICATION_JSON_VALUE)
public class ConfigAreasRestController {
	@Autowired
	private AreaService areaService;
	@Autowired
	private AreaClasifActoService areaClasifActoService;
	@Autowired
	private ClasifActoService clasifActoService;
	@Autowired
	private ServicioService servicioService;
	@Autowired
	private ConceptoPagoService conceptoPagoService;
	
		
	
	///Area
	@GetMapping(value="/areas")
	public ResponseEntity<Page<Area>> getAllAreas(Pageable pageable, String nombre) {
		Page<Area> page =  areaService.getAllAreas(pageable, nombre);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}
	
	@GetMapping(value = "/area/{id}")
	public ResponseEntity<Area> findArea(@PathVariable("id") Long id) {
		return ResponseEntity.ok(areaService.findOne(id));
	}
	
	@PostMapping("/save/area")
	public ResponseEntity<Long> saveArea(@RequestBody Area area) throws URISyntaxException {
		Long id = areaService.save(area);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	@PostMapping("/delete/area")
	public ResponseEntity<Long> deleteArea(@RequestBody Area area) throws URISyntaxException {
		Long id = areaService.delete(area);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}

	
	/// AREA - CLASIFICACION ACTO
	@PostMapping("/save/area-clasif-acto")
	public ResponseEntity<Long> saveAreaClasifActo(@RequestBody AreaClasifActo areaClasifActo) throws URISyntaxException {
		System.out.println(areaClasifActo);
		Long id = areaClasifActoService.save(areaClasifActo);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	@PostMapping("/delete/area-clasif-acto")
	public ResponseEntity<Long> deleteAreaClasifActo(@RequestBody AreaClasifActo areaClasifActo) throws URISyntaxException {
		Long id = areaClasifActoService.delete(areaClasifActo);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	@GetMapping(value = "/area/clasif-acto/{id}")
    public ResponseEntity<List<AreaClasifActo>> findAreasPorClasifActos(@PathVariable("id") Long id) {
		return ResponseEntity.ok(areaClasifActoService.findByClasifActoId(id));
	}
	
	@GetMapping(value = "/clasif-acto/area/{id}")
    public ResponseEntity<List<AreaClasifActo>> findClasifActosPorArea(@PathVariable("id") Long id) {
		return ResponseEntity.ok(areaClasifActoService.findByAreaId(id));
	}
	
	
	///Clasificacion Acto
	@GetMapping(value="/clasif-actos")
	public ResponseEntity<Page<ClasifActo>> getAllClasifActos(Pageable pageable, String nombre) {
		Page<ClasifActo> page =  clasifActoService.getAllClasifActos(pageable, nombre);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}
	
	@GetMapping(value = "/clasif-acto/{id}")
	public ResponseEntity<ClasifActo> findClasifActo(@PathVariable("id") Long id) {
		return ResponseEntity.ok(clasifActoService.findOne(id));
	}
	
	@PostMapping("/save/clasif-acto")
	public ResponseEntity<Long> saveClasifActo(@RequestBody ClasifActo clasifActo) throws URISyntaxException {
		Long id = clasifActoService.save(clasifActo);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	@PostMapping("/delete/clasif-acto")
	public ResponseEntity<Long> deleteClasifActo(@RequestBody ClasifActo clasifActo) throws URISyntaxException {
		Long id = clasifActoService.delete(clasifActo);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	@GetMapping(value = "/all-clasif-actos")
    public ResponseEntity<List<ClasifActo>> findClasifActos() {
		return ResponseEntity.ok(clasifActoService.findAll());
	}
	
	
	///Servicio
	@GetMapping(value="/servicios")
	public ResponseEntity<Page<Servicio>> getAllServicios(Pageable pageable, String nombre) {
		Page<Servicio> page =  servicioService.getAllServicios(pageable, nombre);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}
	
	@GetMapping(value="/servicios/area/{id}")
	public ResponseEntity<List<Servicio>> getServiciosArea(@PathVariable("id") Long id) {
		return new ResponseEntity<>(servicioService.findByAreaId(id), HttpStatus.OK);
	}
	
	@GetMapping(value="/servicios/clasifActo/{id}")
	public ResponseEntity<List<Servicio>> getServiciosClasifActo(@PathVariable("id") Long id) {
		return new ResponseEntity<>(servicioService.findByClasifActoId(id), HttpStatus.OK);
	}
	
	@GetMapping(value = "/servicio/{id}")
	public ResponseEntity<Servicio> findServicio(@PathVariable("id") Long id) {
		return ResponseEntity.ok(servicioService.findOne(id));
	}
	
	@PostMapping("/save/servicio")
	public ResponseEntity<Long> saveServicio(@RequestBody Servicio servicio) throws URISyntaxException {
		Long id = servicioService.saveServicio(servicio);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	@PostMapping("/delete/servicio")
	public ResponseEntity<Long> deleteServicio(@RequestBody Servicio servicio) throws URISyntaxException {
		Long id = servicioService.deleteServicio(servicio);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	
	///TODO SUB SERVICIOS
	@GetMapping(value="/sub-servicios/{id}")
	public ResponseEntity<List<SubServicio>> getSubServicios(@PathVariable("id") Long id) {
		return new ResponseEntity<>(servicioService.findSubServicios(id), HttpStatus.OK);
	}
	
	@GetMapping(value = "/sub-servicio/{id}")
	public ResponseEntity<SubServicio> findSubServicio(@PathVariable("id") Long id) {
		return ResponseEntity.ok(servicioService.findOneSub(id));
	}
	
	@PostMapping("/save/sub-servicio")
	public ResponseEntity<Long> saveSubServicio(@RequestBody SubServicio subServicio) throws URISyntaxException {
		Long id = servicioService.saveSubServicio(subServicio);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	@PostMapping("/delete/sub-servicio")
	public ResponseEntity<Long> deleteSubServicio(@RequestBody SubServicio subServicio) throws URISyntaxException {
		Long id = servicioService.deleteSubServicio(subServicio);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	
	///TODO SUB SUB SERVICIOS
	@GetMapping(value="/sub-sub-servicios/{id}")
	public ResponseEntity<List<SubSubServicio>> getAllSubSubServicios(@PathVariable("id") Long id) {
		return new ResponseEntity<>(servicioService.findSubSubServicios(id), HttpStatus.OK);
	}
	
	@GetMapping(value = "/sub-sub-servicio/{id}")
	public ResponseEntity<SubSubServicio> findSubSubServicio(@PathVariable("id") Long id) {
		return ResponseEntity.ok(servicioService.findOneSubSub(id));
	}
	
	@PostMapping("/save/sub-sub-servicio")
	public ResponseEntity<Long> saveSubSubServicio(@RequestBody SubSubServicio subSubServicio) throws URISyntaxException {
		Long id = servicioService.saveSubSubServicio(subSubServicio);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	@PostMapping("/delete/sub-sub-servicio")
	public ResponseEntity<Long> deleteSubSubServicio(@RequestBody SubSubServicio subSubServicio) throws URISyntaxException {
		Long id = servicioService.deleteSubSubServicio(subSubServicio);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
	
	
	///Concepto Pago
	@GetMapping(value = "/all-conceptos-pagos")
    public ResponseEntity<List<ConceptoPago>> findConceptoPago() {
		return ResponseEntity.ok(conceptoPagoService.findAll());
	}
}
