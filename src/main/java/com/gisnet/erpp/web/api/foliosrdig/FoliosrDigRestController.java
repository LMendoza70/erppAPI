package com.gisnet.erpp.web.api.foliosrdig;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import com.gisnet.erpp.domain.FoliosrDigital;
import org.springframework.web.bind.annotation.PathVariable;
import com.gisnet.erpp.service.FoliosrDigService;

@RestController
@RequestMapping(value = "/api/foliosr-dig", produces = MediaType.APPLICATION_JSON_VALUE)
public class FoliosrDigRestController {

	
	@Autowired
	private FoliosrDigService foliosrDigService;
	
	@PostMapping("/getImagePathsFromFolio")
	public ResponseEntity<List<String>> getImagePathsFromFolio(@RequestBody FoliosrDigital foliosrDigital){
		return new ResponseEntity<>(foliosrDigService.getPathOfImages(foliosrDigital),HttpStatus.OK);
	}
	
	@PostMapping("/getImagePathsFromFolioSinBitacora")
	public ResponseEntity<List<String>> getImagePathsFromFolioSinBitacora(@RequestBody FoliosrDigital foliosrDigital){
		System.out.println("CONTROLLER--"+new ResponseEntity<>(foliosrDigService.getPathOfImagesFoliosSinBitacora(foliosrDigital),HttpStatus.OK));
		return new ResponseEntity<>(foliosrDigService.getPathOfImagesFoliosSinBitacora(foliosrDigital),HttpStatus.OK);
	}
	
	@GetMapping("/getImagePathsFromPrelacionSinBitacora/{prelacionId}")
	public ResponseEntity<List<String>> getImagePathsFromPrelacionSinBitacora(@PathVariable("prelacionId") Long prelacionId){
		return new ResponseEntity<>(foliosrDigService.getImagePathsFromPrelacionSinBitacora(prelacionId),HttpStatus.OK);
	}

	
	@PostMapping("/getImagePathsFromLibroSinBitacora")
	public ResponseEntity<List<String>> getImagePathsFromLibroSinBitacora(@RequestBody LibroDTO libroDTO){
		return new ResponseEntity<>(foliosrDigService.getPathOfImagesLibroSinBitacora(libroDTO),HttpStatus.OK);
	}

	
	@PostMapping(value = "/getFolios")
	public ResponseEntity<List<FoliosrDigital>> getImagePathsFromAntecedente(@RequestBody FoliosrDigital foliosrDigital){
		return new ResponseEntity<>(foliosrDigService.findByTipoFolioIdAndNumFolioRegistral(foliosrDigital.getTipoFolio().getId(),foliosrDigital.getNumFolioRegistral()),HttpStatus.OK);
	}


	@GetMapping(value = "/{folioReal}/tipo-folio/{tipoFolioId}/oficina/{oficinaId}")
	public ResponseEntity <FoliosrDigital> findTipoActoByActoId(@PathVariable("folioReal") Integer folioReal, @PathVariable("tipoFolioId") Long  tipoFolioId, @PathVariable("oficinaId") Long  oficinaId ) {
		
		if (folioReal!=null) {
			return new ResponseEntity<>(foliosrDigService.findByTipoFolioIdAndNumFolioRegistraAndOficinal(tipoFolioId,folioReal,oficinaId),HttpStatus.OK);
		}
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);

	}

	@PostMapping(value = "/getObtenTotalHojas/")
     public ResponseEntity<Integer> getObtenTotalHojas(HttpServletResponse response, @RequestBody LibroDTO libro)throws IOException {
	 return new ResponseEntity<>(foliosrDigService.getObtenTotalHojas(libro), HttpStatus.OK);
	 	  
	}

}
