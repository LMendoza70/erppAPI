package com.gisnet.erpp.web.api.pdf;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.web.api.documentos.DocumentosController;

@RestController
@RequestMapping(value = "/api/pdf", produces = MediaType.APPLICATION_JSON_VALUE)
public class PdfRestController {

	   private final Logger logger = LoggerFactory.getLogger(DocumentosController.class);
	   
	    @Autowired
	    ParametroRepository parametroRepository;

	    @RequestMapping(value = "/asiento/{id}", method = RequestMethod.GET )
	    public ResponseEntity<byte[]> getPdfAsientobyId(@PathVariable Long id) throws IOException {
	        HttpHeaders headers = new HttpHeaders();
	        
	        String folder = parametroRepository.findByCve(Constantes.RUTA_ASIENTOS_CVE).getValor();

			Path path = Paths.get(folder + "/MIG_" +id + "_ASIENTO.pdf");
			byte[]  media = Files.readAllBytes(path);
	        
	        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
	        headers.setContentType(MediaType.APPLICATION_PDF);

	        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
	        return responseEntity;
	    }


}
