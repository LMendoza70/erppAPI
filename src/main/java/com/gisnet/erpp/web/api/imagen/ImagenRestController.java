    
package com.gisnet.erpp.web.api.imagen;

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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.BitacoraGestor;
import com.gisnet.erpp.domain.Libro;
import com.gisnet.erpp.service.ImagenService;


@RestController
@RequestMapping(value = "/api/imagen/", produces = MediaType.APPLICATION_JSON_VALUE)
public class ImagenRestController {

	private static final Logger log = LoggerFactory.getLogger(ImagenRestController.class);
	
	@Autowired
	private ImagenService imagenService;

	@RequestMapping(value = "consultaImagen", method = RequestMethod.GET)
	public ResponseEntity<byte[]> getImage(@RequestParam(value="imagePath") String imagePath) throws IOException{
		  HttpHeaders headers = new HttpHeaders();

	        Path path = Paths.get(imagePath);
			byte[] media = null;
			ResponseEntity<byte[]> responseEntity = null;
			try {
				media = Files.readAllBytes(path);
			} catch (IOException e) {
				System.out.println("Error: "+e.toString());
			}
	        
	        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
	        if(media!=null){ 
				responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
			} else {
				responseEntity = new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);
			}
			
	        return responseEntity;
	}
	
	
	
	@PostMapping("findLibroBy")
	public ResponseEntity<Libro> getImagePathsFromLibro(@RequestBody BitacoraGestor bitacoraGestor){
		return new ResponseEntity<>(imagenService.findLibroBy(bitacoraGestor),HttpStatus.OK);
	}
}