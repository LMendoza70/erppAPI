package com.gisnet.erpp.web.api.recibo;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.Recibo;
import com.gisnet.erpp.service.ReciboService;

@RestController
@RequestMapping(value = "/api/recibo", produces = MediaType.APPLICATION_JSON_VALUE)
public class ReciboRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private ReciboService reciboService;

	@GetMapping(value = "/asignados-acto/{actoId}/{version}")
	public List<Recibo> findAsignadosActoIdAndVersion(@PathVariable("actoId") Long actoId, @PathVariable("version") Integer version) {
		return reciboService.findAsignadosActoIdAndVersion(actoId, version);
	}

	@GetMapping(value = "/disponibles-para-acto/{actoId}/{version}")
	public List<Recibo> findDisponiblesByActo(@PathVariable("actoId") Long actoId, @PathVariable("version") Integer version) {
		return reciboService.findDisponiblesByActo(actoId, version);
	}

}
