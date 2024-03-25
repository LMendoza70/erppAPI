package com.gisnet.erpp.service;

import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.RequisitoVO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.Documento;
import com.gisnet.erpp.domain.DocumentoArchivo;
import com.gisnet.erpp.domain.Notario;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.Archivo;
import com.gisnet.erpp.domain.ActoDocumento;
import com.gisnet.erpp.domain.Acto;

import com.gisnet.erpp.repository.DocumentoRepository;
import com.gisnet.erpp.repository.NotarioRepository;
import com.gisnet.erpp.repository.ActoDocumentoRepository;
import com.gisnet.erpp.repository.ArchivoRepository;
import com.gisnet.erpp.repository.DocumentoArchivoRepository;
import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.repository.PrelacionRepository;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.web.api.documentos.UploadModel;

import org.springframework.web.multipart.MultipartFile;
import org.apache.commons.io.FileUtils;
import java.io.IOException;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.Files;
import java.io.FileOutputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.HashSet;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.springframework.util.ObjectUtils.isEmpty;

@Service
public class DocumentoService {

	private  String UPLOADED_FOLDER = "c://tmp//";
	
	

	@Autowired
		DocumentoRepository documentoRepository; 

	@Autowired
		ActoDocumentoRepository actoDocumentoRepository; 

	@Autowired
		ArchivoRepository archivoRepository; 
	
	@Autowired
	ActoRepository actoRepository; 

	@Autowired
	ParametroRepository parametroRepository;
	
	@Autowired
	PrelacionRepository prelacionRepository;
	
	@Autowired
	NotarioRepository notarioRepository;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired 
	DocumentoArchivoRepository documentoArchivoRepository; 

	@Autowired 
	BitacoraDocumentoEntradaService bitacoraDocumentoEntradaService;

	@Autowired
    PdfService pdfService;
	
	@Transactional(rollbackFor=Exception.class)
	public Long save(Documento documento) {
		documentoRepository.saveAndFlush(documento);
		return documento.getId();
	}
	
	public Documento findOne(Long id) throws NullPointerException {
		Documento response = documentoRepository.findOne(id);
		if (response == null ) {
			throw new NullPointerException("no se encuentra el documento");
		}
		return response;
	}
	
	@Transactional(rollbackFor=Exception.class)
	public Documento updateWithFile(Documento documentoIngreso, 
			MultipartFile file, 
			Long actoId,
			Boolean isReingreso,
			RequisitoVO requisito) throws Exception 
	{
		
		Acto acto = actoRepository.findOne(actoId);
		
		if(acto == null) 
		{
			throw new NoSuchElementException(); 
		}
		
		Archivo archivo = new Archivo();
		archivo = prepareArchivo(file, archivo);
		archivo.setReingreso(isReingreso);
		archivoRepository.saveAndFlush(archivo);
		
		if(!isReingreso)
		{
			/*
			Documento newDoc = createNewDocumentoFromExistingOne(documentoIngreso);
			newDoc.setArchivo(archivo);
			documentoRepository.saveAndFlush(newDoc);
			*/
			if(requisito != null)
			   documentoIngreso.setObservaciones(requisito.getObservaciones());
			
			documentoIngreso.setArchivo(archivo);
			documentoRepository.saveAndFlush(documentoIngreso);
			
			List<ActoDocumento> exampleadoc = new ArrayList<>();
			ActoDocumento nadoc;
			
		}else 
		{
			DocumentoArchivo documentoArchivo = new DocumentoArchivo();
			documentoArchivo.setDocumento(documentoIngreso);
			documentoArchivo.setArchivo(archivo);
			if(requisito != null)
			  documentoArchivo.setObservaciones(requisito.getObservaciones());
			documentoArchivoRepository.save(documentoArchivo);
			
		}
	
		
		/*exampleadoc = actoDocumentoRepository.getAllActoDocumentoByActoId(actoId);
		
		try {
			nadoc = createNewActoDocumentoFromExistingOne(exampleadoc.stream()
				.filter(x -> x.getDocumento().getId().equals(documentoIngreso.getId()))
				.max(Comparator.comparing(x -> ((ActoDocumento)x).getVersion()))
				.orElse(null)
			);
		}
		
		catch(Exception ex) {
			System.out.println(ex);
			throw new NoSuchElementException();	
		}
		
		nadoc.setDocumento(newDoc);
		actoDocumentoRepository.saveAndFlush(nadoc);
		*/
		saveUploadedFiles(Arrays.asList(file),archivo.getNombre());
		
		return documentoIngreso;
	}
	
	@Transactional(rollbackFor=Exception.class)
	public Archivo crearArchivoDigitalizadoWithFile( 
			MultipartFile file, 
			Boolean isReingreso,
			String prelacionId
			) throws Exception 
	{	
	
		Archivo archivo = new Archivo();
		archivo = prepareArchivoDigitalizado(file, archivo,prelacionId);
		archivo.setReingreso(isReingreso);
		archivoRepository.saveAndFlush(archivo);
		
		saveUploadedFiles(Arrays.asList(file),archivo.getNombre());
		
		return archivo;
	}
	
	@Transactional(rollbackFor=Exception.class)
	public Documento updatePrelacionDocumentoWithFile(Documento documentoIngreso,Archivo archivo) throws Exception 
	{		
		if(archivo!=null)
		{		
			documentoIngreso.setArchivo(archivo);
			documentoRepository.saveAndFlush(documentoIngreso);	
			//documentoRepository.save(documentoIngreso);
		}	
		
		
		return documentoIngreso;
	}
	
		
	@Transactional(rollbackFor=Exception.class)
	public ActoDocumento saveWithFile(Documento documentoIngreso, MultipartFile file, Long ActoId) throws Exception {
		Acto acto = actoRepository.findOne(ActoId);
		if(acto == null) {
			throw new NoSuchElementException(); 
		}
		Archivo archivo = new Archivo();
		archivo = prepareArchivo(file, archivo);
		archivoRepository.saveAndFlush(archivo);
		documentoIngreso.setArchivo(archivo);
		documentoRepository.saveAndFlush(documentoIngreso);
		ActoDocumento adoc = new ActoDocumento();
		adoc.setActo(acto);
		adoc.setDocumento(documentoIngreso);
		adoc.setVersion(1);
		actoDocumentoRepository.saveAndFlush(adoc);
		saveUploadedFiles(Arrays.asList(file),documentoIngreso.getArchivo().getId());
		return adoc;
	}
	
	@Transactional(rollbackFor=Exception.class)
	public ActoDocumento saveWithNoFile(Documento documentoIngreso, Long actoId) throws Exception {
		Acto acto = actoRepository.findOne(actoId);
		if(acto == null) {
			throw new NoSuchElementException(); 
		}
		System.out.println(documentoIngreso);
		System.out.println(documentoIngreso.getArchivo());
		Optional<ActoDocumento> ado= actoDocumentoRepository.findFirstByActoIdOrderByVersionDesc(actoId);
		
		documentoRepository.saveAndFlush(documentoIngreso);
		ActoDocumento adoc = new ActoDocumento();
		adoc.setActo(acto);
		if(ado.isPresent()) {
			adoc.setVersion(ado.get().getVersion()+1);
			if(ado.get().getDocumento().getArchivo()!=null && 
			ado.get().getDocumento().getArchivo().getId()>1) 
			{
				documentoIngreso.setArchivo(ado.get().getDocumento().getArchivo());
			}
		}else {
			adoc.setVersion(1);	
		}
		adoc.setDocumento(documentoIngreso);
		actoDocumentoRepository.saveAndFlush(adoc);
		
		return adoc;
	}
	
	
	@Transactional(rollbackFor=Exception.class)
	public Documento updateWithNoFile(Documento documentoIngreso, Long actoId) throws Exception {
		Acto acto = actoRepository.findOne(actoId);
		if(acto == null) {
			throw new NoSuchElementException(); 
		}

		Optional<ActoDocumento> adoc =  actoDocumentoRepository.findFirstByActoIdOrderByVersionDesc(actoId);
		Documento newDoc = createNewDocumentoFromExistingOne(documentoIngreso);
		Archivo archivo = documentoIngreso.getArchivo();
		newDoc.setArchivo(archivo);
		if(adoc.isPresent())
		{
			newDoc.setArchivo(adoc.get().getDocumento().getArchivo());
		}
		documentoRepository.saveAndFlush(newDoc);
		
		List<ActoDocumento> exampleadoc = new ArrayList<>();
		ActoDocumento nadoc;
		
		exampleadoc = actoDocumentoRepository.getAllActoDocumentoByActoId(actoId);
		
		try {
			nadoc = createNewActoDocumentoFromExistingOne(adoc.get());
		}
		
		catch(Exception ex) {
			System.out.println(ex);
			throw new NoSuchElementException();	
		}
		
		nadoc.setDocumento(newDoc);
		actoDocumentoRepository.saveAndFlush(nadoc);
		
		return newDoc;
	}

	@Transactional(rollbackFor=Exception.class)
	public Documento updateWithNoDigital(Documento documentoIngreso, Long actoId) throws Exception {
		Acto acto = actoRepository.findOne(actoId);
		if(acto == null) {
			throw new NoSuchElementException(); 
		}

		documentoRepository.saveAndFlush(documentoIngreso);
	
		return documentoIngreso;
	}
	
	
	@Transactional(rollbackFor=Exception.class)
	public Documento updateWithFile2(Documento documentoIngreso, MultipartFile file) throws Exception {
		
		Archivo archivo = new Archivo();
		archivo = prepareArchivo(file, archivo);
		archivoRepository.saveAndFlush(archivo);
		

		Documento newDoc = createNewDocumentoFromExistingOne(documentoIngreso);
		newDoc.setArchivo(archivo);
		documentoRepository.saveAndFlush(newDoc);
		
		Set<ActoDocumento> exampleadoc = documentoIngreso.getActoDocumentosParaDocumentos();
		
		exampleadoc.forEach( x -> {
			ActoDocumento nadoc;
			try {
				nadoc = createNewActoDocumentoFromExistingOne(x);
			}
			catch(Exception ex) {
				System.out.println(ex);
				throw new NoSuchElementException();	
			}
			
			nadoc.setDocumento(newDoc);
			actoDocumentoRepository.saveAndFlush(nadoc);
		});
		saveUploadedFiles(Arrays.asList(file),newDoc.getArchivo().getId());
		
		return newDoc;
	}
	
	
	
	
	@Transactional(rollbackFor=Exception.class)
	public Documento updateWithNoFile2(Documento documentoIngreso) throws Exception {
		
	

		Documento newDoc = createNewDocumentoFromExistingOne(documentoIngreso);
		Archivo archivo = documentoIngreso.getArchivo();
		newDoc.setArchivo(archivo);
		documentoRepository.saveAndFlush(newDoc);
		
		Set<ActoDocumento> exampleadoc = documentoIngreso.getActoDocumentosParaDocumentos();
		
		exampleadoc.forEach( x -> {
			ActoDocumento nadoc;
			try {
				nadoc = createNewActoDocumentoFromExistingOne(x);
			}
			catch(Exception ex) {
				System.out.println(ex);
				throw new NoSuchElementException();	
			}
			
			nadoc.setDocumento(newDoc);
			actoDocumentoRepository.saveAndFlush(nadoc);
		});
		
		return newDoc;
	}
	
	
	
	private void saveUploadedFiles(List<MultipartFile> files, Long nombre) throws IOException {
		for (MultipartFile file : files) {
	
			if (file.isEmpty()) {
					continue; //next pls
			}
			UPLOADED_FOLDER= parametroRepository.findByCve("RUTA_DOCTO").getValor();
			byte[] bytes = file.getBytes();
			System.out.println("saveUploadedFiles" + UPLOADED_FOLDER);
			Path path = Paths.get(UPLOADED_FOLDER + nombre);
			Files.write(path, bytes);
		}
	}

	private void saveUploadedFiles(List<MultipartFile> files, String nombre) throws IOException {
		for (MultipartFile file : files) {
	
			if (file.isEmpty()) {
					continue; //next pls
			}
			UPLOADED_FOLDER= parametroRepository.findByCve("RUTA_DOCTO").getValor();
			byte[] bytes = file.getBytes();
			System.out.println("saveUploadedFiles" + UPLOADED_FOLDER);
			Path path = Paths.get(UPLOADED_FOLDER + nombre);
			Files.write(path, bytes);
		}
	}
	
	
	private Archivo prepareArchivo(MultipartFile file, Archivo archivo ) {
		if(archivo == null) {
			archivo = new Archivo();
		}

		UPLOADED_FOLDER= parametroRepository.findByCve("RUTA_DOCTO").getValor();
		System.out.println("prepareArchivo" + UPLOADED_FOLDER);
	
		String _sufijo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String extencion = file.getOriginalFilename().split("\\.")[1];
		String nombre    = file.getOriginalFilename().split("\\.")[0];
		String fileName = nombre+"_"+_sufijo+"."+ extencion;
		
		archivo.setRuta(UPLOADED_FOLDER);
		archivo.setTipo(file.getContentType());
		archivo.setNombre(fileName);
		
	
		return archivo;
	}

	private Archivo prepareArchivoDigitalizado(MultipartFile file, Archivo archivo, String prelacionId ) {
		if(archivo == null) {
			archivo = new Archivo();
		}

		UPLOADED_FOLDER= parametroRepository.findByCve("RUTA_DOCTO").getValor();
		System.out.println("prepareArchivo" + UPLOADED_FOLDER);
	
		String _sufijo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String extencion = file.getOriginalFilename().split("\\.")[1];
		String nombre    = prelacionId;
		String fileName = nombre+"_"+_sufijo+"."+ extencion;
		
		archivo.setRuta(UPLOADED_FOLDER);
		archivo.setTipo(file.getContentType());
		archivo.setNombre(fileName);
		
	
		return archivo;
	}
	
	public boolean multiup(UploadModel model) {
	
		Long nombre=(long) 0;
		Archivo archivo = new Archivo();
		try {
			saveUploadedFiles(Arrays.asList(model.getFiles()),nombre);
		}
		catch (Exception e) {
			return false;
		}
		return true;
	}

	private Documento createNewDocumentoFromExistingOne(Documento  doc) {
		Documento nDoc =  new Documento();
		nDoc.setNumero(doc.getNumero());
		nDoc.setNumero2(doc.getNumero2());
		nDoc.setFecha(doc.getFecha());
		nDoc.setRan(doc.isRan());
		nDoc.setRatificado(doc.isRatificado());
		nDoc.setBis(doc.isBis());
		nDoc.setCausaUtilidad(doc.getCausaUtilidad());
		nDoc.setTipoDocumento(doc.getTipoDocumento());
		nDoc.setMunicipio(doc.getMunicipio());
		nDoc.setTipoCert(doc.getTipoCert());
		nDoc.setTipoAutoridad(doc.getTipoAutoridad());
		//nDoc.setPersona(doc.getPersona());
		nDoc.setNotario(doc.getNotario());

		return nDoc;
		
	}

	private ActoDocumento createNewActoDocumentoFromExistingOne(ActoDocumento  aDoc) {
		ActoDocumento nadoc = new ActoDocumento();
		System.out.println(aDoc);
		nadoc.setActo(aDoc.getActo());
		nadoc.setVersion(aDoc.getVersion() +1);

		return nadoc;
		
	}
	
	@Transactional(rollbackFor=Exception.class)
	public Long saveDocumentNotario(Long prelacion,Long IdNotario) {
		
		
		List <Acto> actos = actoRepository.findAllByPrelacion(prelacionRepository.findOne(prelacion));
		Notario notario =  notarioRepository.findOne(IdNotario);
		
		System.out.println(" Actos " +  actos.size() + "   Notario" + notario.getPersona().getNombre() );
		List <ActoDocumento> actoDoc = actoDocumentoRepository.getAllByActo(actos);
		List <Long> idDocs = new ArrayList<Long>();
		
		List <Documento> doc = new ArrayList<Documento>();
		
		for(ActoDocumento Acto : actoDoc) {
			Acto.getDocumento().setNotario(notario);
			doc.add(Acto.getDocumento());
		}
		
		documentoRepository.save(doc);
	
		return 1L;
	}

	public String getEscrituraFromActoDocumento (ActoDocumento adoc) {
		if (!isEmpty(adoc.getDocumento()) && (adoc.getDocumento().getTipoDocumento().getId().longValue() == Constantes.ID_TIPO_DOCUMENTO_ESCRITURA) )
			return adoc.getDocumento().getNumero()!=null ?  adoc.getDocumento().getNumero().toString() : "";
		else return "";
	}

	public List<ActoDocumento> findDocumentos (){
		Usuario us = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
	System.out.println("Usuario : " + us.getNombreCompleto());
	//hgo
		//List<Prelacion> listPrel = prelacionRepository.findAllByUsuarioSolicitanAndConsecutivoIsNull(us);
	List<Prelacion> listPrel = prelacionRepository.findAllByStatusIdAndUsuarioSolicitanAndConsecutivoIsNull(Constantes.Status.INGRESO_POR_VENTANILLA.getIdStatus(), us);//master
	System.out.println(".findAllByStatusIdAndUsuarioSolicitanAndConsecutivoIsNull "+listPrel.size());
		List <Prelacion> listPrel2 = new ArrayList<Prelacion>();
		List<Acto>listActo = new ArrayList<Acto>();
		List<Acto>listActo2 = new ArrayList<Acto>();
		List<ActoDocumento> listActDocument  = new ArrayList<ActoDocumento>();
		for(Prelacion pel:listPrel) {
			listPrel2.add(pel);
			System.out.println("prelacion ID ->" + pel.getId());
			
			if(listPrel2.size() == 1000){
				  listActo.addAll(actoRepository.findAllByListPrelacion(listPrel2));
				 listPrel2.clear();
				System.out.println("Tamaño de ListActo ->" + listActo.size());
			}
			
		}
		
		
		if(listPrel2.size() < 1000){
			listActo.addAll(actoRepository.findAllByListPrelacion(listPrel2));
		}
		System.out.println("Tamaño de ListActo2 ->" + listActo.size());
		
		for (Acto lActo: listActo) {
			listActo2.add(lActo);
			
			if(listActo2.size()==1) {
				listActDocument.addAll(actoDocumentoRepository.findAllByListActo(listActo2));
				listActo2.clear();
			System.out.println("Tamaño de ListActoDocumento ->" + listActDocument.size());
			}
		}
		
		System.out.println("ListActoDocumento longitud: "  + listActDocument.size());
		
		return listActDocument;
	}
	public List<ActoDocumento> findDocumentosByIdPrelacion(Long idPrelacion) {
	    //Long version =actoRepository.findActoVersionByPrelacionId(idPrelacion);
		//System.out.println("prelacoion : " + idPrelacion +  "  version : " + version);
		//Acto acto =actoRepository.findActoByPrelacion(version,idPrelacion);
	    Acto acto = actoRepository.findActoByPrelacion(idPrelacion);
	    Long actoId = acto.getId();
	    List <ActoDocumento> actDocument = actoDocumentoRepository.getAllActoDocumentoByActoId(actoId);
		//System.out.println("Acto : " + acto.getId());
		//System.out.println("ACTO DOCUMENTO : " + actDocument.size());
		return actDocument;
		
	}
	
	@Transactional
	public Boolean eliminarFundatorio(Long documentoArchivoId)
	{
		DocumentoArchivo documentoArchivo = documentoArchivoRepository.findOne(documentoArchivoId);
		documentoArchivoRepository.delete(documentoArchivo);
		return true;
	}
	
	public Set<Documento> getAllActoAssignableDocuments(Long id) {
		return documentoRepository.getAllActoAssignableDocuments(id);
	}
	
	@Transactional(rollbackFor=Exception.class)
	public Archivo crearArchivoDigitalizadoWithFileAppend( 
			byte[] file, 
			Boolean isReingreso,
			String prelacionId
			) throws Exception 
	{	
	
		Archivo archivo = new Archivo();
		archivo = prepareArchivoDigitalizadoAppend(file, archivo,prelacionId);
		archivo.setReingreso(isReingreso);
		archivoRepository.saveAndFlush(archivo);
		
		saveUploadedFilesAppend(file,archivo.getNombre());
		
		return archivo;
	}
	
	private Archivo prepareArchivoDigitalizadoAppend(byte[] file, Archivo archivo, String prelacionId ) {
		if(archivo == null) {
			archivo = new Archivo();
		}

		UPLOADED_FOLDER= parametroRepository.findByCve("RUTA_DOCTO").getValor();
		System.out.println("prepareArchivoAppend" + UPLOADED_FOLDER);
	
		String _sufijo = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
		String extencion = "pdf";
		String nombre    = prelacionId;
		String fileName = nombre+"_"+_sufijo+"."+ extencion;
		
		archivo.setRuta(UPLOADED_FOLDER);
		archivo.setTipo("application/pdf");
		archivo.setNombre(fileName);
		
	
		return archivo;
	}
	
	private void saveUploadedFilesAppend(byte[] file, String nombre) throws IOException {
			UPLOADED_FOLDER= parametroRepository.findByCve("RUTA_DOCTO").getValor();
			System.out.println("saveUploadedFilesAppend" + UPLOADED_FOLDER);
			String path = UPLOADED_FOLDER + nombre;
			//Files.write(path, file);
			try (FileOutputStream fos = new FileOutputStream(path)) {
				   fos.write(file);
			 }
		
	}

	public void reemplazarDigitalizado(MultipartFile archivoAdjunto, Long archivoId,String obs,Usuario usuario) {

		Documento documento = null;
		Documento documentoAnexo = null;
		Archivo archivo = null;
		Archivo archivoAnexo = null;

		DocumentoArchivo documentoArchivo = null;
		
		Boolean isReingreso = false;
		Prelacion prelacion = null;

		
		System.out.println("IHM parametro archivoId:"+archivoId);
		System.out.println("IHM parametro obs:"+obs);

		// * Obtener documento
		documento=documentoRepository.findFirstByArchivoIdAndActoDocumentosParaDocumentosActoVfOrderByIdDesc(archivoId,false);
		documentoArchivo=documentoArchivoRepository.findFirstByArchivoIdOrderByIdDesc(archivoId);


		if(documento!=null){
			if(documento.getArchivo()!=null) {
				archivo=documento.getArchivo();
				prelacion = documento.getActoDocumentosParaDocumentos().stream().findFirst().get().getActo().getPrelacion();		
				System.out.println("ArchivoActualId:"+archivo.getId()+" DocumentoId:"+documento.getId()+" Consecutivo:"+prelacion.getConsecutivo());
			}
		} else if (documentoArchivo!=null) {
			if(documentoArchivo.getArchivo()!=null){
				documentoAnexo = documentoArchivo.getDocumento();
				archivoAnexo = documentoArchivo.getArchivo();
				isReingreso = true;
				prelacion = documentoArchivo.getDocumento().getActoDocumentosParaDocumentos().stream().findFirst().get().getActo().getPrelacion();
				System.out.println("ArchivoAnexoActualId:"+archivoAnexo.getId()+" DocumentoId:"+documentoArchivo.getDocumento().getId()+" Consecutivo:"+prelacion.getConsecutivo());
			}
		}

		
		// * Crear Archivo
		Archivo archivoNvo = null;	
		
		try {			
			archivoNvo= crearArchivoDigitalizadoWithFile(archivoAdjunto,isReingreso,prelacion.getId().toString());			
			if(archivoNvo!=null){
				// * Crear Bitacora		
				if (isReingreso){
					bitacoraDocumentoEntradaService.crearBitacora(
						prelacion,usuario,"REEMPLAZAR_DOCUMENTO",documentoAnexo,archivoAnexo,obs,true
					);
				} else {
					bitacoraDocumentoEntradaService.crearBitacora(
						prelacion,usuario,"REEMPLAZAR_DOCUMENTO",documento,archivo,obs,false
					);
				}		

				// * Reemplazar Archivo en Documento		
				if(isReingreso){
					if(documentoArchivo.getArchivo()!=null){
						documentoArchivo.setArchivo(archivoNvo);
						documentoArchivoRepository.save(documentoArchivo);
					}
				}else {
					documento.setArchivo(archivoNvo);
					documentoRepository.save(documento);
				}
				System.out.println("IHM ArchivoNvo:"+archivoNvo.getId()+" ArchivoNvoNombre:"+archivoNvo.getNombre());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void anexarDigitalizado(MultipartFile archivoAdjunto, Long archivoId,String obs,Usuario usuario) {

		Documento documento = null;
		Documento documentoAnexo = null;
		Archivo archivo = null;
		Archivo archivoAnexo = null;

		DocumentoArchivo documentoArchivo = null;
		
		Boolean isReingreso = false;
		Prelacion prelacion = null;

		
		// * Obtener documento
		documento=documentoRepository.findFirstByArchivoIdAndActoDocumentosParaDocumentosActoVfOrderByIdDesc(archivoId,false);
		documentoArchivo=documentoArchivoRepository.findFirstByArchivoIdOrderByIdDesc(archivoId);


		if(documento!=null){
			if(documento.getArchivo()!=null) {
				archivo=documento.getArchivo();
				prelacion = documento.getActoDocumentosParaDocumentos().stream().findFirst().get().getActo().getPrelacion();
				System.out.println("ArchivoActualId:"+archivo.getId()+" DocumentoId:"+documento.getId()+" Consecutivo:"+prelacion.getConsecutivo());
			}
		} else if (documentoArchivo!=null) {
			if(documentoArchivo.getArchivo()!=null){
				documentoAnexo = documentoArchivo.getDocumento();
				archivoAnexo = documentoArchivo.getArchivo();
				isReingreso = true;
				prelacion = documentoArchivo.getDocumento().getActoDocumentosParaDocumentos().stream().findFirst().get().getActo().getPrelacion();
				System.out.println("ArchivoAnexoActualId:"+archivoAnexo.getId()+" DocumentoId:"+documentoArchivo.getDocumento().getId()+" Consecutivo:"+prelacion.getConsecutivo());
			}
		}


		// * Crear Bitacora		
		if (isReingreso){
			bitacoraDocumentoEntradaService.crearBitacora(
				prelacion,usuario,"ANEXAR_HOJAS",documentoAnexo,archivoAnexo,obs,true
			);
		} else {
			bitacoraDocumentoEntradaService.crearBitacora(
				prelacion,usuario,"ANEXAR_HOJAS",documento,archivo,obs,false
			);
		}

		// * Anexar Hojas

		// - Obtener archivo actual
		byte[] archivoOriginal = null;
		if (isReingreso) {
			archivoOriginal = obtenerArchivo(archivoAnexo);
		} else {
			archivoOriginal = obtenerArchivo(archivo);
		}

		byte[] nuevo = null;	
	 	try {
	 		byte[] anexo;
			
	 		// - Obtener anexo
	 		anexo = convertMultiPartToFile(archivoAdjunto);
	 		// - Anexar Hojas
			nuevo=pdfService.appendPDF(archivoOriginal,anexo);
		} catch (Exception e1) {			
			e1.printStackTrace();
		}

		
		// * Crear Archivo nuevo
		Archivo archivoNvo = null;
		try {			
			archivoNvo= crearArchivoDigitalizadoWithFileAppend(nuevo,isReingreso,prelacion.getId().toString());			
			if(archivoNvo!=null){
				// * Reemplazar Archivo en Documento		
				if(isReingreso){
					if(documentoArchivo.getArchivo()!=null){
						documentoArchivo.setArchivo(archivoNvo);
						documentoArchivoRepository.save(documentoArchivo);
					}
				}else {
					documento.setArchivo(archivoNvo);
					documentoRepository.save(documento);
				}
				System.out.println("IHM ArchivoNvo:"+archivoNvo.getId()+" ArchivoNvoNombre:"+archivoNvo.getNombre());
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private byte[] obtenerArchivo(Archivo archivo){
		String ofname=archivo.getRuta()+archivo.getNombre();
		byte[] lstBytes = null; 
		
		try {
			File file= new File(ofname);		
			lstBytes = Files.readAllBytes(file.toPath());
		} catch (FileNotFoundException e) {
        	System.out.println(e.getMessage());
            e.printStackTrace();
        }  catch (IOException e) {
        	System.out.println(e.getMessage());
            e.printStackTrace();
        }
	 	
	 	return lstBytes;
	}

	private byte[] convertMultiPartToFile(MultipartFile file ) throws IOException
    {
        File convFile = new File( file.getOriginalFilename() );
        FileOutputStream fos = new FileOutputStream( convFile );
        fos.write( file.getBytes() );
        fos.close();
        
        byte[] data = FileUtils.readFileToByteArray(convFile);
        convFile.delete();
        return data;
    }
	
}
