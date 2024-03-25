package com.gisnet.erpp.service;

import java.io.File;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.lowagie.text.DocumentException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gisnet.erpp.domain.BitacoraGestor;
import com.gisnet.erpp.domain.FoliosrDigital;
import com.gisnet.erpp.domain.Libro;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.Parametro;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Seccion;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.FoliosrDigRepository;
import com.gisnet.erpp.repository.LibroRepository;
import com.gisnet.erpp.repository.OficinaRepository;
import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.repository.SeccionRepository;
import com.gisnet.erpp.repository.TipoFolioRepository;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.util.CommonUtil;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.ArchivoVO;
import com.gisnet.erpp.vo.LibroVO;
import com.gisnet.erpp.web.api.foliosrdig.LibroDTO;

@Service
public class FoliosrDigService {

	private static final Logger log = LoggerFactory.getLogger(FoliosrDigService.class);

	@Autowired
	private FoliosrDigRepository foliosrDigRepository;

	@Autowired
	private ParametroRepository parametroRepository;

	@Autowired
	private LibroRepository libroRepository;
	
	@Autowired
	private TipoFolioRepository tipoFolioRepository;
	
	@Autowired
	private BitacoraGestorService bitacoraGestorService;

	@Autowired
	private OficinaRepository oficinaRepository;
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private PrelacionService prelacionService;
	
	@Autowired
	private SeccionRepository seccionRepository;
	
	public List<FoliosrDigital> findByTipoFolioIdAndNumFolioRegistral(Long tipoFolioId, Integer folio) {
		FoliosrDigital foliosrDigital = new FoliosrDigital();
		foliosrDigital.setNumFolioRegistral(folio);
		foliosrDigital.setTipoFolio(tipoFolioRepository.findOne(tipoFolioId));
		
		BitacoraGestor bitacoraGestor = new BitacoraGestor();
		bitacoraGestor.setNoFolio(folio);
		bitacoraGestor.setTipoFolio(tipoFolioRepository.findOne(tipoFolioId));
		bitacoraGestorService.saveBitacora(bitacoraGestor);
		return foliosrDigRepository.findByTipoFolioIdAndNumFolioRegistral(tipoFolioId, folio);
	}

	public FoliosrDigital findByTipoFolioIdAndNumFolioRegistraAndOficinal(Long tipoFolioId, Integer folio,Long oficinaId) {

		
		BitacoraGestor bitacoraGestor = new BitacoraGestor();
		bitacoraGestor.setNoFolio(folio);
		bitacoraGestor.setTipoFolio(tipoFolioRepository.findOne(tipoFolioId));
		bitacoraGestorService.saveBitacora(bitacoraGestor);
		return foliosrDigRepository.findByTipoFolioIdAndNumFolioRegistralAndOficinaId(tipoFolioId, folio,oficinaId);
	}


	public List<String> getPathOfImages(FoliosrDigital foliosrDigital) {
		BitacoraGestor bitacoraGestor = new BitacoraGestor();
		bitacoraGestor.setNoFolio(foliosrDigital.getNumFolioRegistral());
		bitacoraGestor.setTipoFolio(foliosrDigital.getTipoFolio());
		
		return getPathOfImagesConsulta(foliosrDigital);
	}

	public List<String> getPathOfImagesFoliosSinBitacora(FoliosrDigital foliosrDigital) {
		return getPathOfImagesConsulta(foliosrDigital);
	}
	
	public List<String> getImagePathsFromPrelacionSinBitacora(Long prelacionId) {
		Prelacion prelacion = prelacionService.findOne(prelacionId);
		Long id = null;
		List<String> result = new ArrayList<>();
		Parametro param = parametroRepository.findByCve(Constantes.RUTA_PRELACION);
		if (prelacion.getId_entrada()!=null) {
			id = prelacion.getId_entrada();
		} else {
			id = prelacionId;
		}
		String path = param.getValor() +  "MIG_" + id + "_ENTRADA.pdf";
		result.add(path);
		return result;
	}

	private List<String> getPathOfImagesConsulta(FoliosrDigital foliosrDigital) {
		log.debug("Consultando paths de imagenes de 1folio = {} ", foliosrDigital);
		log.debug("Consultando paths de imagenes de 2folio = {} ", foliosrDigital.getNumFolioRegistral());
		log.debug("Consultando paths de imagenes de 3folio = {} ", foliosrDigital.getNumContPrelacion());
		List<String> lstPaths = new ArrayList<>();
		StringBuffer path = null;
		File folder = null;
		Parametro rutaFolios = parametroRepository.findByCve(Constantes.RUTA_FOLIO_CVE);
		String nFR = foliosrDigital.getNumFolioRegistral().toString();
		String nCP = foliosrDigital.getNumContPrelacion().toString();
		if (rutaFolios != null) {
			path = new StringBuffer(rutaFolios.getValor());
			//path.append(File.separator).append(nFR+"_"+nCP+".PDF");
			
			log.debug("<<<<<<<<<<<<RUTA X FOLIO Y CONSECUTIVO>>>>>>>>>>>>>>\n");			
			log.debug(path.toString());
			folder = new File(path.toString());
			log.debug("PATH IMAGENES: {}", path.toString());
			File[] listOfFiles = folder.listFiles();
			log.debug("list IMAGENES: {}", listOfFiles);
			if (listOfFiles != null) {
				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile()) {
						if(listOfFiles[i].getName().equals(nFR+"_"+nCP+".PDF")||listOfFiles[i].getName().equals(nFR+"_"+nCP+".pdf")){
							lstPaths.add(folder.getPath() + File.separator + nFR+"_"+nCP+".pdf");
						}						
					}
				}
			} 
		}
		log.debug("list IMAGENES: {}", lstPaths);
		return lstPaths;
	}

	public List<String> getPathOfImagesLibroSinBitacora(LibroDTO libroDTO) {
		log.debug("Consultando paths de imagenes de libroId={}", libroDTO.getLibroId());
		String rutaLibro=null;
	
        
		if(libroDTO.getOficina()!=null) {
			Oficina oficina = oficinaRepository.findByNombre(libroDTO.getOficina());
			rutaLibro = Constantes.RUTA_LIBRO_CVE+oficina.getNumOficina();
			}else {
				Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
				if(usuario!=null)
				rutaLibro = Constantes.RUTA_LIBRO_CVE+usuario.getOficina().getNumOficina();
			}
		
		Parametro parametro = parametroRepository.findByCve(rutaLibro);
		List<String> lstPaths = new ArrayList<>();
		File folder = null;

		Libro libro = libroRepository.findOne(libroDTO.getLibroId());
		if (libro != null && libro.getRuta() != null) {
			StringBuffer path = new StringBuffer(parametro.getValor());
			//ARMA LA RUTA CON LOS ANTECEDENTES (....)
			path.append(File.separator).append(libro.getSeccionesPorOficina().getOficina().getNumOficina()).append("_").append(libro.getAnio()).append("_").append(libro.getSeccionesPorOficina().getSeccion().getClave()).append("_").append(CommonUtil.padCeros(libro.getLibroBis(), 2)).append("_").append(CommonUtil.padCeros(libro.getNumLibro(), 2)).append("_").append(CommonUtil.padCeros(libro.getVolumen(), 2));

			//path.append(File.separator).append(CommonUtil.generarBisDocumento(libroDTO.getDocumento(), libroDTO.getDocumentoBis()));

			folder = new File(path.toString());
			log.debug("PATH IMAGENES: {}", path.toString());
			
			log.debug("filename={}", libroDTO.getDocumento());
			//TOMA LA RUTA DEL DOCUMENTO
			lstPaths.add(folder.getPath() + File.separator + libroDTO.getDocumento() + ".pdf");
			log.debug("===> PATH: "+ folder.getPath() + File.separator + libroDTO.getDocumento());
			
			//File[] listOfFiles = folder.listFiles();

			//if (listOfFiles != null) {
			//	for (int i = 0; i < listOfFiles.length; i++) {
			//		if (listOfFiles[i].isFile() && CommonUtil.isImageExtension(listOfFiles[i].getName())) {
						//if (libroDTO.getPaginaIni()==0 || libroDTO.getPaginaFin()==0){
							//lstPaths.add(folder.getPath() + File.separator + listOfFiles[i].getName());			
						//} else {
						//	if (CommonUtil.isInRange(listOfFiles[i].getName(), libroDTO.getPaginaIni(), libroDTO.getPaginaFin())) {
						//		lstPaths.add(folder.getPath() + File.separator + listOfFiles[i].getName());
						//	}
						//}
			//		}
			//	}
			//}

		}
		return lstPaths;

	}

	//IHM METODO AGREGADO
	public  byte[] getImagesConsultaBySeleccionadas(Libro libro, String documento,String seleccionadas) {
		//System.out.println("Consultando paths de imagenes de folio =  "+ foliosrDigital.getId());
		byte[] lstBytes = null;
		InputStream in;
		
		File folder = null;
		String ofname = null;
		String rutaLibro=null;
		
		if(libro.getSeccionesPorOficina().getOficina()!=null) {		
			rutaLibro = Constantes.RUTA_LIBRO_CVE+libro.getSeccionesPorOficina().getOficina().getNumOficina();
		}else {
				Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
				if(usuario!=null)
					rutaLibro = Constantes.RUTA_LIBRO_CVE+usuario.getOficina().getNumOficina();
		}
		Parametro parametro = parametroRepository.findByCve(rutaLibro);		

		if (libro != null && libro.getRuta() != null) {
			StringBuffer path = new StringBuffer(parametro.getValor());

			path.append(File.separator).append(libro.getSeccionesPorOficina().getOficina().getNumOficina()).append("_").append(libro.getAnio()).append("_").append(libro.getSeccionesPorOficina().getSeccion().getClave()).append("_").append(CommonUtil.padCeros(libro.getLibroBis(), 2)).append("_").append(CommonUtil.padCeros(libro.getNumLibro(), 2)).append("_").append(CommonUtil.padCeros(libro.getVolumen(), 2));
			//path.append(File.separator).append(CommonUtil.padCeros(libro.getSeccionesPorOficina().getOficina().getNumOficina(), 2)).append("_").append(libro.getSeccionesPorOficina().getSeccion().getClave()).append("_").append(CommonUtil.padCeros(libro.getNombreLibro().getId().intValue(), 2)).append("_").append(CommonUtil.padCeros(libro.getTipoLibro().getId().intValue(), 2)).append("_").append(CommonUtil.padCeros(libro.getVolumen(), 4));

			folder = new File(path.toString());			
			ofname=folder.getPath() + File.separator + documento + ".pdf";
			log.info("IHM Libro-Documento:"+ofname);
			log.info("IHM Libro-TipoDoc:"+libro.getTipoDoc());
		}
		
		try {
			/*in = new FileInputStream(new File(ofname));

			lstBytes=CommonUtil.doExtraeToByteArray(in,seleccionadas,libro.getTipoDoc());*/
			File file= new File(ofname);
			lstBytes = Files.readAllBytes(file.toPath());

		} catch (FileNotFoundException e) {
        	log.error(e.getMessage());
            e.printStackTrace();
        }  catch (IOException e) {
        	log.error(e.getMessage());
            e.printStackTrace();
        }
		return lstBytes;
	}	

	public int getObtenTotalHojas(LibroDTO libroDTO){
		System.out.println("Entra a funcion");
		String fname=null;
		File folder = null;
		int totalhojas = 0;
		
		InputStream in;

        String rutaLibro=null;
	
        
		if(libroDTO.getOficina()!=null) {
			Oficina oficina = oficinaRepository.findByNombre(libroDTO.getOficina());
			rutaLibro = Constantes.RUTA_LIBRO_CVE+oficina.getNumOficina();
			}else {
				Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
				if(usuario!=null)
				rutaLibro = Constantes.RUTA_LIBRO_CVE+usuario.getOficina().getNumOficina();
			}
		
		Parametro parametro = parametroRepository.findByCve(rutaLibro);

		Libro libro = libroRepository.findOne(libroDTO.getLibroId());
		if (libro != null && libro.getRuta() != null) {
			StringBuffer path = new StringBuffer(parametro.getValor());

			path.append(File.separator).append(libro.getSeccionesPorOficina().getOficina().getNumOficina()).append("_").append(libro.getAnio()).append("_").append(libro.getSeccionesPorOficina().getSeccion().getClave()).append("_").append(CommonUtil.padCeros(libro.getLibroBis(), 2)).append("_").append(CommonUtil.padCeros(libro.getNumLibro(), 2)).append("_").append(CommonUtil.padCeros(libro.getVolumen(), 2));
			//Buscando libro por criterios		//path.append(File.separator).append(CommonUtil.padCeros(libro.getSeccionesPorOficina().getOficina().getNumOficina(), 2)).append("_").append(libro.getSeccionesPorOficina().getSeccion().getClave()).append("_").append(CommonUtil.padCeros(libro.getNombreLibro().getId().intValue(), 2)).append("_").append(CommonUtil.padCeros(libro.getTipoLibro().getId().intValue(), 2)).append("_").append(CommonUtil.padCeros(libro.getVolumen(), 4));

			folder = new File(path.toString());
			log.debug("PATH IMAGENES: {}", path.toString());
			
			log.debug("filename={}", libroDTO.getDocumento());
			
			fname =folder.getPath() + File.separator + libroDTO.getDocumento() + ".pdf";
		
					try{

						in = new FileInputStream(new File(fname));
						totalhojas = CommonUtil.getNumeroDeHojas(in);
						System.out.println("El total de hojas es"+totalhojas);
						in.close();
				
					}catch(FileNotFoundException e){
						log.error(e.getMessage());
					}catch(IOException e){
						log.error(e.getMessage());						
					}
		}
		System.out.println(totalhojas);
		return totalhojas;	
	}
	
	
	public ArchivoVO getPathOfImagesArchivo(LibroVO libroVO) {
		ArchivoVO avo = new ArchivoVO();
		log.debug("Consultando paths de imagenes de libroId={}", libroVO);
		Oficina oficina = oficinaRepository.findOne(libroVO.getOficina());
		Seccion seccion = seccionRepository.findOne(libroVO.getSeccion());
		String rutaLibro=null;
	
        
		if(libroVO.getOficina()!=null) {
			rutaLibro = Constantes.RUTA_LIBRO_CVE+oficina.getNumOficina();
			}else {
				Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
				if(usuario!=null)
				rutaLibro = Constantes.RUTA_LIBRO_CVE+usuario.getOficina().getNumOficina();
			}
		
		Parametro parametro = parametroRepository.findByCve(rutaLibro);
		List<String> lstPaths = new ArrayList<>();
		File folder = null;

//		Libro libro = libroRepository.findOne(libroDTO.getLibroId());
//		if (libro != null && libro.getRuta() != null) {
		
			StringBuffer path = new StringBuffer(parametro.getValor());
			//ARMA LA RUTA CON LOS ANTECEDENTES (....)
			path.append(File.separator).append(oficina.getNumOficina()).append("_")
				.append(libroVO.getAnio()).append("_")
				.append(seccion.getClave()).append("_")
				.append(CommonUtil.padCeros(libroVO.getLibrobis(), 2)).append("_")
				.append(CommonUtil.padCeros(libroVO.getTomo(), 2)).append("_")
				.append(CommonUtil.padCeros(libroVO.getVolumen(), 2));

			folder = new File(path.toString());
			log.debug("PATH IMAGENES: {}", path.toString());
			
			log.debug("filename={}", libroVO.getNumeroInscripcion());
			//TOMA LA RUTA DEL DOCUMENTO
			lstPaths.add(folder.getPath() + File.separator + libroVO.getNumeroInscripcion() + ".pdf");
			Path pathss = Paths.get(folder.getPath() + File.separator + libroVO.getNumeroInscripcion() + ".pdf");

			if(Files.exists(pathss)) {
				avo.setExists(true);
			}else {
				avo.setExists(false);	
			}
			log.debug("===> PATH: "+ folder.getPath() + File.separator + libroVO.getNumeroInscripcion());
			
			avo.setRuta(lstPaths);
//		}
		
		return avo;

	}


}
