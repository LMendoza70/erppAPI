package com.gisnet.erpp.web.api.libro;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.stream.Collectors;

import javax.persistence.NonUniqueResultException;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gisnet.erpp.domain.BitacoraLibroAntecedente;
import com.gisnet.erpp.domain.Libro;
import com.gisnet.erpp.domain.Notario;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.service.BitacoraLibroAntecedenteService;
import com.gisnet.erpp.service.FoliosrDigService;
import com.gisnet.erpp.service.LibroService;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.AntecedenteVO;
import com.gisnet.erpp.vo.ArchivoVO;
import com.gisnet.erpp.vo.LibroVO;


@RestController
@RequestMapping(value = "/api/libro", produces = MediaType.APPLICATION_JSON_VALUE)
public class LibroRestController {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	LibroService libroService;
	
	@Autowired
	FoliosrDigService foliosrDigService;
	
	@Autowired
	BitacoraLibroAntecedenteService bitacoraLibroAntecedenteService;
	
	@GetMapping("/libros")
	public ResponseEntity<List<Libro>> getAllLibros() {
		return new ResponseEntity<>(libroService.findAll(), HttpStatus.OK);
	}
	
	@GetMapping("/findBy")
	public ResponseEntity<List<Libro>> findLibrosBy(@RequestParam("numLibro") String numLibro, @RequestParam("libroBis") String libroBis ,@RequestParam("seccionPorOficina") Long seccionPorOficinaId) {
		System.out.println("ENTRO CONTROLLER");	
		return new ResponseEntity<>(libroService.findBy(numLibro, libroBis, seccionPorOficinaId), HttpStatus.OK);
	}
	
	@GetMapping("/findPageableBy")
	public ResponseEntity<Page<Libro>> findAllLibros(Pageable pageable, String numLibro, String libroBis, String oficinaId, String seccionId, String tipoDoc) {
		Page<Libro> page = libroService.findAllPageable(numLibro, libroBis, oficinaId, seccionId, tipoDoc, pageable);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}
					
	@GetMapping("/findLibro")
	public ResponseEntity<Libro> findOneLibro(
    	@RequestParam("numLibro") String numLibro, 
		@RequestParam("libroBis") String libroBis,
		@RequestParam("seccionId") Long seccionId,
		@RequestParam("oficinaId") Long oficinaId,
		@RequestParam("anio") Integer anio,
		@RequestParam("volumen") String volumen 
		) {		
		/*
		Integer numLibro, 
		String libroBis,
		Long seccionId, 
		Long oficinaId, 
		Integer anio,
		Integer volumen*/	
		System.out.println("ENTRO CONTROLLER--"+numLibro+"--"+ libroBis+"--"+ seccionId+"--"+ oficinaId+"--"+ anio+"--"+ volumen);	
		//return new ResponseEntity<>(libroService.findOneLibroBy(numLibro, libroBis, seccionId,oficinaId,anio,volumen), HttpStatus.OK);
		return new ResponseEntity<>(libroService.findOneLibroBy(numLibro, libroBis, seccionId,oficinaId,anio,volumen), HttpStatus.OK);
	}

	@PostMapping("/saveFile")
	public ResponseEntity<?> agregarArchivoLibro(@RequestParam("libroVO") String strLibroVO, @RequestParam("file") MultipartFile archivoAdjunto)
	{   
		System.out.println("strLibroVO= "+strLibroVO);
		LibroVO libroVO ;

		try {
			libroVO = new ObjectMapper().readValue(strLibroVO, LibroVO.class);
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(LibroRestController.class.getName()).log(Level.SEVERE, null, ex);
			return new ResponseEntity<>("Error de servidor",HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Libro libro = null;
		try {
			//libro = libroService.saveFile(libroVO, archivoAdjunto);
			libro = libroService.saveNewFile(libroVO, archivoAdjunto,Constantes.CREAR_ARCHIVO);
		}
		catch (NonUniqueResultException nuException) {
			return new ResponseEntity<>(nuException.getMessage(), HttpStatus.MULTIPLE_CHOICES);
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(LibroRestController.class.getName()).log(Level.SEVERE, null, ex);
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
		}

		return new ResponseEntity<>(libro, HttpStatus.OK);
	}

	@PostMapping("/updateFile")
	public ResponseEntity<?> actualizarArchivoLibro(@RequestParam("libroVO") String strLibroVO, 
			@RequestParam("file") MultipartFile archivoAdjunto )
	{
		System.out.println("strLibroVO= "+strLibroVO);
		LibroVO libroVO ;

		try {
			libroVO = new ObjectMapper().readValue(strLibroVO, LibroVO.class);
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(LibroRestController.class.getName()).log(Level.SEVERE, null, ex);
			return new ResponseEntity<>("Error de servidor",HttpStatus.INTERNAL_SERVER_ERROR);
		}

		Libro libro = null;
		try {
			libro = libroService.saveNewFile(libroVO, archivoAdjunto,Constantes.UPDATE_ARCHIVO);
		}
		catch (NonUniqueResultException nuException) {
			return new ResponseEntity<>(nuException.getMessage(), HttpStatus.MULTIPLE_CHOICES);
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(LibroRestController.class.getName()).log(Level.SEVERE, null, ex);
			return new ResponseEntity<>(ex.getMessage(), HttpStatus.CONFLICT);
		}

		return new ResponseEntity<>(libro, HttpStatus.OK);
	}
	
	@PostMapping("/findLibro")
	public ResponseEntity<?> findLibros(@RequestParam("libroVO") String strLibroVO)
	{
		System.out.println("strLibroVO= "+strLibroVO);
		LibroVO libroVO ;

		try {
			libroVO = new ObjectMapper().readValue(strLibroVO, LibroVO.class);
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(LibroRestController.class.getName()).log(Level.SEVERE, null, ex);
			return new ResponseEntity<>("Error de servidor",HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<Libro> libros = null;
		try {
			libros = libroService.findLibros(libroVO);
		}
		catch (NonUniqueResultException nuException) {
			return new ResponseEntity<>(nuException.getMessage(), HttpStatus.MULTIPLE_CHOICES);
		}
		if(libros != null && libros.size()>0) {
		return new ResponseEntity<>(libros, HttpStatus.OK);
		}else {
			return new ResponseEntity<>("Libro No Encontrado", HttpStatus.BAD_REQUEST);
		}

		
	}

	@PostMapping("/searchFile")
	public ResponseEntity<?> buscarArchivoLibro(@RequestParam("libroVO") String strLibroVO)
	{
		LibroVO libroVO ;

		try {
			libroVO = new ObjectMapper().readValue(strLibroVO, LibroVO.class);
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(LibroRestController.class.getName()).log(Level.SEVERE, null, ex);
			return new ResponseEntity<>("Error de servidor",HttpStatus.INTERNAL_SERVER_ERROR);
		}

		ArchivoVO lstPaths = new ArchivoVO();
		try {
			lstPaths = foliosrDigService.getPathOfImagesArchivo(libroVO);
		}
		catch (NonUniqueResultException nuException) {
			return new ResponseEntity<>(nuException.getMessage(), HttpStatus.MULTIPLE_CHOICES);
		} 

		return new ResponseEntity<>(lstPaths, HttpStatus.OK);
	}
	
	
	@PostMapping(value = "/createUpdateLibro")
	public ResponseEntity<? >  getAntencedente(@RequestBody AntecedenteVO antecedente) {

		StringBuilder sb = new StringBuilder();
		Libro libro = null;
		Libro libroReturn = null;
		try {
			libro = libroService.validaLibro(antecedente);
			if(libro == null) {
				if(antecedente.getId()>0) {
					libroReturn = libroService.updateLibro(antecedente);
				}else{
					libroReturn = libroService.createLibro(antecedente);
				}
			}else {
				sb.append("Existe un libro con esas características");
			}
		}
		catch (NonUniqueResultException nuException) {
			return new ResponseEntity<>(nuException.getMessage(), HttpStatus.MULTIPLE_CHOICES);
		}
		if(sb.toString().length()>0) {
			return new ResponseEntity<>(sb.toString(), HttpStatus.BAD_REQUEST);
		}else {
			return new ResponseEntity<>(libroReturn, HttpStatus.OK);
		}

	}
	
	

	@PostMapping("/searchBitacoraFile")
	public ResponseEntity<?> searchBitacoraArchivo(@RequestParam("libroVO") String strLibroVO)
	{   
		System.out.println("strLibroVO= "+strLibroVO);
		LibroVO libroVO ;

		try {
			libroVO = new ObjectMapper().readValue(strLibroVO, LibroVO.class);
		} catch (IOException ex) {
			java.util.logging.Logger.getLogger(LibroRestController.class.getName()).log(Level.SEVERE, null, ex);
			return new ResponseEntity<>("Error de servidor",HttpStatus.INTERNAL_SERVER_ERROR);
		}

		List<BitacoraLibroAntecedente> bitacora = null;
		try {
			bitacora = bitacoraLibroAntecedenteService.bitacoraListAnte(libroVO, Constantes.TIPO_ANTECEDENTE);
		}
		catch (NonUniqueResultException nuException) {
			return new ResponseEntity<>(nuException.getMessage(), HttpStatus.MULTIPLE_CHOICES);
		}

		return new ResponseEntity<>(bitacora, HttpStatus.OK);
	}
	
	@GetMapping("/searchBitacoraFile/{libroId}")
	public ResponseEntity<?> validaPagos(@PathVariable("libroId") Long libroId)
	{   

		List<BitacoraLibroAntecedente> bitacora = null;
		try {
			bitacora = bitacoraLibroAntecedenteService.bitacoraListLibro(libroId, Constantes.TIPO_LIBRO);
		}
		catch (NonUniqueResultException nuException) {
			return new ResponseEntity<>(nuException.getMessage(), HttpStatus.MULTIPLE_CHOICES);
		}

		return new ResponseEntity<>(bitacora, HttpStatus.OK);
	}
		
}
