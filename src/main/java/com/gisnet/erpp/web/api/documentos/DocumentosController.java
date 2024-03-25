package com.gisnet.erpp.web.api.documentos;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gisnet.erpp.domain.Documento;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoDocumento;
import com.gisnet.erpp.domain.Archivo;
import com.gisnet.erpp.domain.TipoDocumento;

import com.gisnet.erpp.service.ArchivoService;
import com.gisnet.erpp.service.DocumentoService;
import com.gisnet.erpp.service.ActoDocumentoService;
import com.gisnet.erpp.service.ActoService;

import com.gisnet.erpp.service.TipoDocTipoActoService;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/documentos", produces = MediaType.APPLICATION_JSON_VALUE)
public class DocumentosController {

	private final Logger logger = LoggerFactory.getLogger(DocumentosController.class);

	@Autowired
	ArchivoService archivoService;

	@Autowired
	ActoService actoService;

	@Autowired
	DocumentoService documentoService;

	@Autowired
	ActoDocumentoService actoDocumentoService;

	@Autowired
	private TipoDocTipoActoService tipoDocTipoActoService;

	@GetMapping(value = "/encuentradoc/{id}")
	public ResponseEntity<?> findDocumentoOne(@PathVariable("id") Long id) {
		return ResponseEntity.ok(documentoService.findOne(id));
	}

	@PostMapping("/upload-with-file")
	public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile uploadedFile,
			@RequestParam("entidad") String documento, @RequestParam("ActoId") String actoId) {
		if (uploadedFile.isEmpty()) {
			return new ResponseEntity<>("please select a file!", HttpStatus.CONFLICT);
		}

		Documento jsonDoc;
		ActoDocumento adoc;

		try {
			jsonDoc = new ObjectMapper().readValue(documento, Documento.class);
			adoc = documentoService.saveWithFile(jsonDoc, uploadedFile, Long.parseLong(actoId));

		} catch (IOException e) {
			e.printStackTrace();
			return new ResponseEntity<>("El documento no es valido", HttpStatus.BAD_REQUEST);
		} catch (NoSuchElementException ex) {
			ex.printStackTrace();
			return new ResponseEntity<>("El Acto no Existe", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<>("Error de servidor", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(adoc, new HttpHeaders(), HttpStatus.OK);
	}

	@PutMapping("/{idActo}")
	public ResponseEntity<?> updateDocumentoArchivo(@RequestBody Documento documento, @PathVariable Long idActo) {
		Documento docReturn;
		System.out.println("OBJECTO CNTROLLER--" + documento.toString());
		try {
			docReturn = documentoService.updateWithNoDigital(documento,idActo);
		} catch (Exception e) {
			e.printStackTrace();
			return new ResponseEntity<>("ERROR DE SERVIDOR", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(docReturn, HttpStatus.OK);
	}

	@PostMapping("/upload-nofile")
	public ResponseEntity<?> uploadWithNoFile(@RequestParam("entidad") String documento,
			@RequestParam("ActoId") String actoId) {
		Documento jsonDoc;
		ActoDocumento adoc;
		try {
			jsonDoc = new ObjectMapper().readValue(documento, Documento.class);
			adoc = documentoService.saveWithNoFile(jsonDoc, Long.parseLong(actoId));
		} catch (IOException e) {
			logger.debug(e.getMessage().toString());
			return new ResponseEntity<>("El documento no es valido", HttpStatus.BAD_REQUEST);
		} catch (NoSuchElementException ex) {
			ex.printStackTrace();
			return new ResponseEntity<>("El Acto no Existe", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<>("Error de servidor", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(adoc, new HttpHeaders(), HttpStatus.OK);
	}

	// 3.1.1 Single file upload
	@PostMapping("/nofile")
	public ResponseEntity<?> updateWithNoFile(@RequestParam("entidad") String documento,
			@RequestParam("ActoId") String actoId) {
		Documento jsonDoc;
		try {
			jsonDoc = new ObjectMapper().readValue(documento, Documento.class);
			jsonDoc = documentoService.updateWithNoFile(jsonDoc, Long.parseLong(actoId));
		} catch (IOException e) {
			logger.debug(e.getMessage().toString());
			return new ResponseEntity<>("El documento no es valido", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<>("Error de servidor", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(jsonDoc, new HttpHeaders(), HttpStatus.OK);
	}

	// 3.1.1 Single file upload
	@PostMapping("/update")
	public ResponseEntity<?> uploadFileUpdate(@RequestParam("file") MultipartFile uploadedFile,
			@RequestParam("entidad") String documento, @RequestParam("ActoId") String actoId) {

		if (uploadedFile.isEmpty()) {
			return new ResponseEntity<>("please select a file!", HttpStatus.BAD_REQUEST);
		}
		Documento jsonDoc;
		try {
			jsonDoc = new ObjectMapper().readValue(documento, Documento.class);
			jsonDoc = documentoService.updateWithFile(jsonDoc, uploadedFile, Long.parseLong(actoId), false, null);
			jsonDoc = documentoService.updateWithNoFile(jsonDoc, Long.parseLong(actoId));
		}

		catch (IOException e) {
			logger.debug(e.getMessage().toString());
			return new ResponseEntity<>("El documento no existe", HttpStatus.BAD_REQUEST);
		} catch (Exception ex) {
			ex.printStackTrace();
			return new ResponseEntity<>("Error de servidor", HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity<>(jsonDoc, new HttpHeaders(), HttpStatus.OK);
	}

	@PostMapping("/multi")
	public ResponseEntity<?> uploadFileMulti(@RequestParam("extraField") String extraField,
			@RequestParam("files") MultipartFile[] uploadfiles) {
		// Get file name
		String uploadedFileName = Arrays.stream(uploadfiles).filter(x -> !StringUtils.isEmpty(x))
				.map(x -> x.getOriginalFilename()).collect(Collectors.joining(" , "));

		if (StringUtils.isEmpty(uploadedFileName)) {
			return new ResponseEntity<>("please select a file!", HttpStatus.OK);
		}
		return new ResponseEntity<>("Successfully uploaded - " + uploadedFileName, HttpStatus.OK);
		// try {
		// saveUploadedFiles(Arrays.asList(uploadfiles,1l));
		// } catch (IOException e) {
		// return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		// }
	}

	@PostMapping("/multi/model")
	public ResponseEntity<?> multiUploadFileModel(@ModelAttribute UploadModel model) {
		if (!documentoService.multiup(model)) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}
		return new ResponseEntity<>("Successfully uploaded!", HttpStatus.OK);

	}

	@GetMapping(value = "/acto/tipo-acto/{id}")
	public ResponseEntity<List<TipoDocumento>> findBytipoDocumento(@PathVariable("id") Long id) {
		return ResponseEntity.ok(tipoDocTipoActoService.findBytipoDocumento(id));
	}

	@GetMapping(value = "/acto/tipo-documento/{id}")
	public ResponseEntity<TipoDocumento> findBytipoDocumentoId(@PathVariable("id") Long id) {
		return ResponseEntity.ok(tipoDocTipoActoService.findBytipoDocumentoId(id));
	}

	@PostMapping("/archivo")
	// If not @RestController, uncomment this
	// @ResponseBody
	public ResponseEntity<?> create(@RequestBody Archivo archivoNuevo) throws URISyntaxException {
		Long archivoId = archivoService.create(archivoNuevo);
		return new ResponseEntity<>(archivoId, HttpStatus.OK);
	}

	@GetMapping(value = "/update-documento-notario/{idPrelacion}/{idNotario}")
	public ResponseEntity<Long> saveDocumentNotario(@PathVariable("idPrelacion") Long idPrelacion,
			@PathVariable("idNotario") Long idNotario) {
		return ResponseEntity.ok(documentoService.saveDocumentNotario(idPrelacion, idNotario));
	}

	@GetMapping(value = "/findDocumentos")
	public ResponseEntity<List<ActoDocumento>> findDocumentos() {
		return ResponseEntity.ok(documentoService.findDocumentos());
	}

	@GetMapping(value = "/findDocumento/{idPrelacion}")
	public ResponseEntity<List<ActoDocumento>> findDocumento(@PathVariable("idPrelacion") Long idPrelacion) {
		return ResponseEntity.ok(documentoService.findDocumentosByIdPrelacion(idPrelacion));
	}

	@GetMapping(value = "/acto/{id}/documentos-asignables")
	public ResponseEntity<Set<Documento>> getAllActoAssignableDocuments(@PathVariable("id") Long id) {
		return new ResponseEntity<>(documentoService.getAllActoAssignableDocuments(id), HttpStatus.OK);
	}

	

}
