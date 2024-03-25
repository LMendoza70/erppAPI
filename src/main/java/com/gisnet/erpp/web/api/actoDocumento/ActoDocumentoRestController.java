package com.gisnet.erpp.web.api.actoDocumento;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.ArrayList;
import java.util.Comparator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoDocumento;
import com.gisnet.erpp.service.ActoDocumentoService;

@RestController
@RequestMapping(value = "/api/actodocumento", produces = MediaType.APPLICATION_JSON_VALUE)
public class ActoDocumentoRestController {
	
	@Autowired
	ActoDocumentoService actoDocumentoService;
	
	@GetMapping(value = "/byactoid/{id}")
	public ResponseEntity<List<ActoDocumento>> findByPrelacionId(@PathVariable("id") Long id) {
		List adoc = new ArrayList();
		adoc.add(actoDocumentoService.getAllActoDocumentoByActoId(id).stream().max(Comparator.comparing(x -> ((ActoDocumento)x).getVersion())).orElse(null));
		return new ResponseEntity<>(adoc, HttpStatus.OK);
	}
	@GetMapping(value = "/acto/{id}/documentos-asignables")
	public ResponseEntity<Set<ActoDocumento>> getAllActoAssignableDocuments(@PathVariable("id") Long id) {
		return new ResponseEntity<>(actoDocumentoService.getAllActoAssignableDocuments(id), HttpStatus.OK);
	}

	
	@GetMapping(value = "/delete/{id}")
	public ResponseEntity<Long> delete(@PathVariable("id") Long id) {
		System.out.println("/////////////////////////////////////////////////////");
		System.out.println(id);
		this.actoDocumentoService.delete(id);
		System.out.println(this.actoDocumentoService.findOne(id));
		
		return new ResponseEntity<>(null, HttpStatus.OK);
	}

	
	//@GetMapping(value = "/asociar")
	 @PostMapping("/asociar")
	public ResponseEntity<Long> asociar(@RequestParam("actoId") Long actoId,@RequestParam("documentoId")Long documentoId) {
		
		return new ResponseEntity<>(actoDocumentoService.asociar(actoId, documentoId), HttpStatus.OK);
	}
	
}
