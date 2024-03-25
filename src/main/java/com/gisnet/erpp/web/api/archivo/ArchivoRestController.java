package com.gisnet.erpp.web.api.archivo;


import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.CacheControl;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gisnet.erpp.domain.Archivo;
import com.gisnet.erpp.domain.Documento;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.vo.ArchivoFirmaVO;

import com.gisnet.erpp.service.ArchivoService;
import com.gisnet.erpp.service.PrelacionService;
import com.gisnet.erpp.service.DocumentoService;
import com.gisnet.erpp.web.api.documentos.DocumentosController;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import com.gisnet.erpp.domain.Parametro;
import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.repository.PrelacionRepository;
import com.gisnet.erpp.util.Constantes;

@RestController
@RequestMapping(value = "/api/archivo", produces = MediaType.APPLICATION_JSON_VALUE)
public class ArchivoRestController {

	   private final Logger logger = LoggerFactory.getLogger(DocumentosController.class);
	   private static String UPLOADED_FOLDER = "c://tmp//";
	   
	    @Autowired
		ArchivoService archivoService;

		@Autowired
		PrelacionService prelacionService;
		
		@Autowired
		PrelacionRepository prelacionRepository;
		
		@Autowired
		ParametroRepository parametroRepository;
	
	    private MediaType contentFile(String mediaStr)
	    {  
	    	MediaType media;
	    	  switch (mediaStr) {
	            case "image/jpeg":  media = MediaType.IMAGE_JPEG;
	                      break;
	            case "application/pdf":  media =  MediaType.APPLICATION_PDF;
	                     break;
	            case "image/png":  media =  MediaType.IMAGE_PNG;
	                     break;
	            default: media = MediaType.APPLICATION_OCTET_STREAM;
	                     break;
	        }
	    	
	    	return media; 
	    }
	    
	    
	    @GetMapping("/{id}")
	    public ResponseEntity<Archivo> getArchivo(@PathVariable Long id) {
	        Archivo archivo = archivoService.findOne(id);
	        return new ResponseEntity<>(archivo, HttpStatus.OK);
	    }
	    
	    @RequestMapping(value = "api/documentos/image", method = RequestMethod.GET )
	    public ResponseEntity<byte[]> getImageAsResponseEntity() {
	        HttpHeaders headers = new HttpHeaders();

	        Path path = Paths.get(UPLOADED_FOLDER + "2600.jpg");
	        byte[] media = null;
			try {
				media = Files.readAllBytes(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
	        
	        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
	        headers.setContentType(contentFile("image/jpeg"));
	         
	        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
	        return responseEntity;
	    }
	    
	    @RequestMapping(value = "/file/{id}", method = RequestMethod.GET )
	    public ResponseEntity<byte[]> getImageAsResponseEntitybyId(@PathVariable Long id) {
	        HttpHeaders headers = new HttpHeaders();

	        Archivo archivo = archivoService.findOne(id);
	        
	        if (archivo == null )
				return new ResponseEntity<>(null, headers, HttpStatus.NOT_FOUND);

	       // Path path = Paths.get(archivo.getRuta() + archivo.getId());
	        byte[] media = null;
			try {
				   Path path = Paths.get(archivo.getRuta() + archivo.getNombre());
				media = Files.readAllBytes(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.NOT_FOUND);
				  return responseEntity;
			}
	        
	        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
	        headers.setContentType(contentFile(archivo.getTipo()));

	        if (archivo.getTipo().contains("word") )
	        	headers.setContentDispositionFormData("attachment", archivo.getNombre());

	        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
	        return responseEntity;
	    }


		@GetMapping("/fundatorios/{id}")
	    public ResponseEntity<List<Archivo>> getDocumentoFundatorioByPrelacion(@PathVariable Long id) {

			Prelacion prelacionActual= prelacionService.findOne(id); 
				
			List<Archivo> archivos= archivoService.findAllFundatorioByPrelacion(prelacionActual);

	        return new ResponseEntity<>(archivos, HttpStatus.OK);
	    }
		


	@PostMapping("/fundatorios")
	public ResponseEntity<?> saveFirmaFundatorio(@RequestBody ArchivoFirmaVO archivo) 
	{

		Boolean exito=false;
	
			if(archivo==null){

					
					return new ResponseEntity<>("Los datos de ingreso para guardar el archivo son incorrectos",HttpStatus.BAD_REQUEST);
			}
			try{
					
				
				    exito=archivoService.saveFirmaArchivo(archivo);
					
			}			
			catch (Exception e) {
				e.printStackTrace();
				return new ResponseEntity<>("Los datos de ingreso para guardar el archivo son incorrectos",HttpStatus.BAD_REQUEST);
			}
			
			
		return new ResponseEntity<>( exito, HttpStatus.OK);
	}
	

	 @RequestMapping(value = "/fileEntradaMigrada/byPrelacionId/{prelacionId}", method = RequestMethod.GET )
	    public ResponseEntity<byte[]> getImageAsResponseEntitybyPrelacionIdOfEntradaMigrada(@PathVariable Long prelacionId) {
	    	HttpHeaders headers = new HttpHeaders();
	        String rutaArchivoEntrada=null;
	        
	        byte[] media = null;
	        
	    	Prelacion p = prelacionRepository.findById(prelacionId);
						
				Parametro parametro = parametroRepository.findByCve(Constantes.RUTA_PRELACION); 
				rutaArchivoEntrada = parametro.getValor() + "MIG_"+p.getId_entrada()+ "_ENTRADA.pdf";
				
			try {
				   Path path = Paths.get(rutaArchivoEntrada);
				media = Files.readAllBytes(path);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.NOT_FOUND);
				  return responseEntity;
			}
	        
	        headers.setCacheControl(CacheControl.noCache().getHeaderValue());
	        headers.setContentType(contentFile("application/pdf"));

	        ResponseEntity<byte[]> responseEntity = new ResponseEntity<>(media, headers, HttpStatus.OK);
	        return responseEntity;
	    }
}
