package com.gisnet.erpp.web.api.paseFracCond;

import java.util.List;

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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.FoliosFracCond;
import com.gisnet.erpp.domain.PaseFracCond;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.service.PaseFracCondService;
import com.gisnet.erpp.service.PredioService;

@RestController
@RequestMapping(value = "/api/pase-frac-cond", produces = MediaType.APPLICATION_JSON_VALUE)
public class PaseFracCondController {

	@Autowired
	private PaseFracCondService paseFracCondService;

	@Autowired
	private PredioService predioService;

	@GetMapping(value = "/byUsuario")
	public ResponseEntity<List<PaseFracCond>> findByUsuario() {
		return new ResponseEntity<>(paseFracCondService.findByUsuarioLoginId(), HttpStatus.OK);
	}

	@PostMapping(value = "/")
	public ResponseEntity<PaseFracCond> save(@RequestBody PaseFracCond pase) {
		return new ResponseEntity<>(paseFracCondService.save(pase), HttpStatus.OK);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Boolean> delete(@PathVariable Long id) {
		return new ResponseEntity<>(paseFracCondService.delete(id), HttpStatus.OK);
	}

	@GetMapping(value = "/fracciones")
	public ResponseEntity<Page<Predio>> getAllPrediosFraccionamientoPageable(Pageable pageable, Long idPase,
			String manzana, String lote) {
		return new ResponseEntity<>(
				predioService.findAllPrediosFraccionamientoPageable(idPase, manzana, lote, pageable), HttpStatus.OK);
	}

	@PostMapping(value = "/save-folio-frac-cond")
	public ResponseEntity<FoliosFracCond> saveFindFracCond(@RequestBody FoliosFracCond folio) {
		return new ResponseEntity<>(paseFracCondService.saveFolioFracCond(folio), HttpStatus.OK);
	}
	
	@DeleteMapping(value = "/delete-folio-frac-cond/{idPredio}")
	public ResponseEntity<Boolean> deleteFracCond(@PathVariable Long idPredio){
		return new ResponseEntity<>(paseFracCondService.deleteFracCond(idPredio), HttpStatus.OK);
	}
	
	@GetMapping(value = "/replicar/{predioId}/{paseId}")
	public ResponseEntity<?> replicar(@PathVariable Long predioId,@PathVariable Long paseId) {
		try {
		  return new ResponseEntity<>(predioService.replicarPredio(predioId,paseId), HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.CONFLICT);
		}
	}
}
