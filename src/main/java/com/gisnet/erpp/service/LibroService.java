package com.gisnet.erpp.service;

import java.util.List;
import java.util.logging.Level;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.Normalizer;
import java.util.ArrayList;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.Libro;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.PredioAnte;
import com.gisnet.erpp.domain.Seccion;
import com.gisnet.erpp.domain.SeccionPorOficina;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.AntecedenteRepository;
import com.gisnet.erpp.repository.LibroRepository;
import com.gisnet.erpp.repository.OficinaRepository;
import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.repository.SeccionRepository;
import com.gisnet.erpp.repository.SeccionesPorOficinaRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.UtilFecha;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.vo.AntecedenteVO;
import com.gisnet.erpp.vo.LibroVO;
import com.gisnet.erpp.web.api.antecedente.AntecedenteRestController;
import com.querydsl.core.NonUniqueResultException;

@Service
public class LibroService {

	private static final Logger log = LoggerFactory.getLogger(AntecedenteRestController.class);
	
    private  String UPLOADED_FOLDER = "c://tmp//";
    private  String ARCHIVO_ANT = "c://tmp//";

	@Autowired
	private LibroRepository libroRepository;
	
	@Autowired 
	private SeccionesPorOficinaRepository seccionesPorOficinaRepository;
	
     @Autowired
     private OficinaRepository oficinaRepository;
     
     @Autowired
     private SeccionRepository seccionRepository;
    
     @Autowired
     private ParametroRepository parametroRepository; 

	@Autowired 
	AntecedenteRepository antecedenteRepository;

	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	BitacoraLibroAntecedenteService bitacoraLibroAntecedenteService;

	public List<Libro> findAll(){
		return libroRepository.findAll();
	}
	
	public List<Libro> findBy(String numLibro, String libroBis, Long seccionPorOficinaId){
		return libroRepository.findBy(numLibro, libroBis, seccionPorOficinaId);
	}

	public Libro findOneLibroBy(String numLibro, String libroBis, Long seccionId, Long oficinaId, Integer anio, String volumen){
		System.out.println("encontro-- "+libroRepository.findLibroBy(numLibro, libroBis, seccionId,oficinaId,anio,volumen));
		return libroRepository.findLibroBy(numLibro, libroBis, seccionId,oficinaId,anio,volumen);
	}
	
	public Libro findByNombre(String nombre){
		return libroRepository.findByNombre(nombre);
	}
	
	/*public Libro findByNumeroLibro(LibroVO libro){
		return libroRepository.findByNumLibroAndSeccionesPorOficinaOficinaIdAndSeccionesPorOficinaSeccionId(libro.getNumeroLibro(),libro.getOficina().getId(), libro.getSeccion().getId());
	}*/

	public Libro findByNumeroLibro (String numlibro) {
		return libroRepository.findByNumLibro(numlibro);
	}
	
	public Libro findOneByNumeroLibro(Integer numlibro) {
		return libroRepository.findOneByNumLibro(numlibro);
	}
	
	
	public List<Libro> findAllLikeNumLibro (String numlibro) {
		return libroRepository.findAllByNumLibroLike("%"+numlibro+"%");
	}
	
	public Object findOneLibroFromVentanilla(AntecedenteVO ante) {

		 if (ante.getNumero() != null && ante.getNumero().trim() == "")
		 	ante.setNumero(null);

		if (ante.getDocumento()!= null && ante.getDocumento().trim() == "")
			ante.setDocumento(null);  
			//tipoDocParse = Integer.parseInt(ante.getDocumento());

		if (ante.getDocumentoBis() != null && ante.getDocumentoBis().trim() == "")
			ante.setDocumentoBis( null );

		if (ante.getLibroBis() != null && ante.getLibroBis().trim() == "")
			ante.setLibroBis( null );
		
		
		Long seccionId = Long.parseLong(ante.getSeccion());
		Long oficinaId = Long.parseLong(ante.getOficina());

		Object libroRet = null;

		int tipoFolioId = Integer.parseInt( ante.getTipoBusqueda() );

		Constantes.ETipoFolio tipo = Constantes.ETipoFolio.fromInt(tipoFolioId);

		log.info( "TipoFolio > "+String.valueOf(tipoFolioId ));
		log.info( "ETipoFolio > "+tipo);


		try {
			switch (tipo) {

				case PERSONAS_JURIDICAS:
					libroRet = libroRepository.findPjAnteByAnteLibro(ante.getNumero(), ante.getDocumentoBis(), ante.getDocumento() , ante.getAnio(), 
							cleanString(ante.getLibro()),cleanString(ante.getLibroBis()), seccionId, oficinaId);
					break;
				case PERSONAS_AUXILIARES :
					libroRet = libroRepository.findAuxiliarAnteByAnteLibro(
							ante.getNumero(), ante.getDocumentoBis(), ante.getDocumento() , ante.getAnio(), 
							cleanString(ante.getLibro()),cleanString(ante.getLibroBis()), seccionId, oficinaId);					
					break;
				case MUEBLE:
					libroRet = libroRepository.findMuebleAnteByAnteLibro(
							ante.getNumero(), ante.getDocumentoBis(), ante.getDocumento() , ante.getAnio(), 
							cleanString(ante.getLibro()),cleanString(ante.getLibroBis()), seccionId, oficinaId);
					break;
				case PREDIO :
					try {
						libroRet = libroRepository.findOnePredioAnteByAnteLibro(
							ante.getNumero(), ante.getDocumentoBis(), ante.getDocumento() , ante.getAnio(),  cleanString(ante.getVolumen()),
							cleanString(ante.getLibro()),cleanString(ante.getLibroBis()), seccionId, oficinaId);
					} catch (NonUniqueResultException uniqueException) {
						List<PredioAnte> listPrediosAnte = new ArrayList<PredioAnte>();
						listPrediosAnte = libroRepository.findAllPredioAnteByAnteLibro(
							ante.getNumero(), ante.getDocumentoBis(), ante.getDocumento() , ante.getAnio(),  cleanString(ante.getVolumen()),
							cleanString(ante.getLibro()),cleanString(ante.getLibroBis()), seccionId, oficinaId);
							log.info("Folios Encontrados:"+listPrediosAnte.size());
							libroRet = listPrediosAnte;
							log.info("Pase de Folios");
					}					
					break;
			}



		}
		catch (NonUniqueResultException uniqueException) {
			throw uniqueException;
		}
		
		return libroRet;
		
	}
	
	@Transactional(readOnly = true)
	public Page<Libro> findAllPageable(String numLibro, String libroBis, String oficinaId, String seccionId, String tipoDoc, Pageable pageable){
		return libroRepository.findAllPageable(numLibro, libroBis, oficinaId, seccionId, tipoDoc, pageable);
	}
	
	 public static String cleanString(String texto) {
	        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
	        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	        return texto.toUpperCase();
	    }

	public Libro saveFile(LibroVO libroVO, MultipartFile file) throws IOException {
        	//libroVO.setVolumen(libroVO.getVolumen().toUpperCase());
            Libro libro;
            try{
                libro = libroRepository.findLibroUploadFile(libroVO);
            }
            catch (NonUniqueResultException uniqueException) {
                throw uniqueException;
            }
            
            if (libro == null) {
               // NombreLibro nombreLibroEntity = nombreLibroService.findOne(libroVO.getLibro());
              //  TipoLibro tipoLibroEntity = tipoLibroService.findOne(libroVO.getTipoLibro());

                List<SeccionPorOficina> seccionesPorOficina = seccionesPorOficinaRepository.findByOficinaIdAndSeccionId(libroVO.getOficina(), libroVO.getSeccion());
                SeccionPorOficina seccionPorOficina;
                
                if (seccionesPorOficina.isEmpty()) {
                    SeccionPorOficina seccPorOficina = new SeccionPorOficina();
                    seccPorOficina.setOficina(oficinaRepository.findOne(libroVO.getOficina()));
                    seccPorOficina.setSeccion(seccionRepository.findOne(libroVO.getSeccion()));
                    
                    seccionesPorOficinaRepository.save(seccPorOficina);
                    seccionPorOficina = seccPorOficina;
                } else {
                    seccionPorOficina = seccionesPorOficina.get(seccionesPorOficina.size() - 1);
                }
                
                Libro busquedaLibro;
                try{
                    busquedaLibro = libroRepository.findLibroUploadFileValiation(libroVO);
                }
                catch (NonUniqueResultException uniqueException) {
                    throw uniqueException;
                }
                
                if (busquedaLibro == null) {
                    libro = new Libro();
                     
                    libro.seccionesPorOficina(seccionPorOficina);
                    libro.setAnio(libroVO.getAnio());
                    libro.setNumLibro(libroVO.getTomo());
                    libro.setLibroBis(libroVO.getLibrobis());
                    libro.setVolumen(libroVO.getVolumen());
                    libro.setActivo(true);
                    libro.setRuta("");
                    libroRepository.save(libro);
                } else {
                    libro = busquedaLibro;
                }
               

                Antecedente antecedente = new Antecedente();
                antecedente.setLibro(libro);
                antecedente.setDocumento(libroVO.getNumeroInscripcion());
                antecedente.setNombreArchivo(file.getOriginalFilename());

                antecedenteRepository.save(antecedente);
            }
            
            String fileName = libroVO.getNumeroInscripcion() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
            
            try {                		
                saveUploadedFiles(file, fileName, libroVO);
            } catch (IOException ex) {
                java.util.logging.Logger.getLogger(LibroService.class.getName()).log(Level.SEVERE, null, ex);
                throw ex;
            }
            
            Antecedente antecedente = antecedenteRepository.findAntecedenteByLibroId(libro.getId(), libroVO.getNumeroInscripcion());
            antecedente.setNombreArchivo(fileName);
            antecedenteRepository.save(antecedente);
            System.out.println(".SAVE(antecedente)===>" + antecedenteRepository.save(antecedente));
            libro.setRuta("");
            libroRepository.save(libro);
            System.out.println(".SAVE(libro)===>" + libroRepository.save(libro));

            return libro;
	}
	
	public Libro saveNewFile(LibroVO libroVO, MultipartFile file, Long accion) throws IOException {
		
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		Libro libro;
		libro = this.findLibro( libroVO );

		String fileName = "";

		if(accion == Constantes.CREAR_ARCHIVO) {
			fileName = libroVO.getNumeroInscripcion() + "." + FilenameUtils.getExtension(file.getOriginalFilename());
			try {                		
				saveUploadedFiles(file, fileName, libroVO);

				bitacoraLibroAntecedenteService.createAnte(usuario,libro,libroVO.getNumeroInscripcion(),"CREATE_ARCHIVO",Constantes.TIPO_ANTECEDENTE,this.formato(libroVO)+fileName);

			} catch (IOException ex) {
				java.util.logging.Logger.getLogger(LibroService.class.getName()).log(Level.SEVERE, null, ex);
				throw ex;
			}

		} else if(accion == Constantes.UPDATE_ARCHIVO) {
			fileName = "." + FilenameUtils.getExtension(file.getOriginalFilename());

			try {                		
				saveUploadedFiles(file, libroVO.getNumeroInscripcion(), fileName, libroVO);

				bitacoraLibroAntecedenteService.createAnte(usuario,libro,libroVO.getNumeroInscripcion(),"UPDATE_ARCHIVO",Constantes.TIPO_ANTECEDENTE,ARCHIVO_ANT);
			} catch (IOException ex) {
				java.util.logging.Logger.getLogger(LibroService.class.getName()).log(Level.SEVERE, null, ex);
				throw ex;
			}
		}

		libro.setRuta("");
		libroRepository.save(libro);
		 System.out.println(".SAVE(libro)===>" + libroRepository.save(libro));

		return libro;
	}
	
	public List<Libro> findLibros(LibroVO libroVO){
		Oficina oficina = oficinaRepository.findOne(libroVO.getOficina());
		Seccion seccion = seccionRepository.findOne(libroVO.getSeccion());
		
		return libroRepository.findLibrosBy( libroVO.getTomo(), libroVO.getLibrobis(), seccion.getId(), oficina.getId(), libroVO.getAnio(), libroVO.getVolumen() );
	}
	
	public Libro validaLibro(AntecedenteVO vo) {
		Libro busquedaLibro;
		LibroVO libroVO = new LibroVO();
		libroVO.setOficina( Long.parseLong(vo.getOficina()) );
		libroVO.setAnio( vo.getAnio() );
		libroVO.setSeccion( Long.parseLong(vo.getSeccion()) );
		libroVO.setLibrobis( vo.getLibroBis() );
		libroVO.setTomo( vo.getLibro() );
		libroVO.setVolumen( vo.getVolumen() );
		
		try{
			busquedaLibro = libroRepository.findLibroUploadFileValiation(libroVO);
		}
		catch (NonUniqueResultException uniqueException) {
			throw uniqueException;
		}
		
		return busquedaLibro;
		
	}
	
	public Libro createLibro(AntecedenteVO vo) {
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		Libro newLibro = new Libro();
		Libro nuevo = new Libro();
		List<SeccionPorOficina> seccionesPorOficina = seccionesPorOficinaRepository.findByOficinaIdAndSeccionId(Long.parseLong(vo.getOficina()), Long.parseLong(vo.getSeccion()) );
		SeccionPorOficina seccionPorOficina;

		if (seccionesPorOficina.isEmpty()) {
			SeccionPorOficina seccPorOficina = new SeccionPorOficina();
			seccPorOficina.setOficina(oficinaRepository.findOne(Long.parseLong(vo.getOficina())));
			seccPorOficina.setSeccion(seccionRepository.findOne(Long.parseLong(vo.getSeccion())));

			seccionesPorOficinaRepository.save(seccPorOficina);
			seccionPorOficina = seccPorOficina;
		} else {
			seccionPorOficina = seccionesPorOficina.get(seccionesPorOficina.size() - 1);
		}
		
		newLibro.seccionesPorOficina(seccionPorOficina);
		newLibro.setAnio(vo.getAnio());
		newLibro.setNumLibro(vo.getLibro());
		newLibro.setLibroBis(vo.getLibroBis());
		newLibro.setVolumen(vo.getVolumen());
		newLibro.setActivo(true);
		newLibro.setRuta("");
		nuevo =libroRepository.save(newLibro);
		
		bitacoraLibroAntecedenteService.createLibro(usuario,nuevo,"CREATE_LIBRO",Constantes.TIPO_LIBRO);
		
		return nuevo;
		
	}
	
	public Libro updateLibro(AntecedenteVO vo) {
		Usuario usuario= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		Libro updateLibro = new Libro();
		Libro actualizado = new Libro();
		
		updateLibro = libroRepository.findById(vo.getId());
		
		List<SeccionPorOficina> seccionesPorOficina = seccionesPorOficinaRepository.findByOficinaIdAndSeccionId(Long.parseLong(vo.getOficina()), Long.parseLong(vo.getSeccion()) );
		SeccionPorOficina seccionPorOficina;

		if (seccionesPorOficina.isEmpty()) {
			SeccionPorOficina seccPorOficina = new SeccionPorOficina();
			seccPorOficina.setOficina(oficinaRepository.findOne(Long.parseLong(vo.getOficina())));
			seccPorOficina.setSeccion(seccionRepository.findOne(Long.parseLong(vo.getSeccion())));

			seccionesPorOficinaRepository.save(seccPorOficina);
			seccionPorOficina = seccPorOficina;
		} else {
			seccionPorOficina = seccionesPorOficina.get(seccionesPorOficina.size() - 1);
		}
		
		bitacoraLibroAntecedenteService.createLibro(usuario,updateLibro,"UPDATE_LIBRO",Constantes.TIPO_LIBRO);
		
		updateLibro.seccionesPorOficina(seccionPorOficina);
		updateLibro.setAnio(vo.getAnio());
		updateLibro.setNumLibro(vo.getLibro());
		updateLibro.setLibroBis(vo.getLibroBis());
		updateLibro.setVolumen(vo.getVolumen());
		
		actualizado = libroRepository.saveAndFlush(updateLibro);
		
		return actualizado;
		
	}


	private Libro findLibro( LibroVO libroVO ) {
		Libro libro;
		Libro busquedaLibro;

		List<SeccionPorOficina> seccionesPorOficina = seccionesPorOficinaRepository.findByOficinaIdAndSeccionId(libroVO.getOficina(), libroVO.getSeccion());
		SeccionPorOficina seccionPorOficina;

		if (seccionesPorOficina.isEmpty()) {
			SeccionPorOficina seccPorOficina = new SeccionPorOficina();
			seccPorOficina.setOficina(oficinaRepository.findOne(libroVO.getOficina()));
			seccPorOficina.setSeccion(seccionRepository.findOne(libroVO.getSeccion()));

			seccionesPorOficinaRepository.save(seccPorOficina);
			seccionPorOficina = seccPorOficina;
		} else {
			seccionPorOficina = seccionesPorOficina.get(seccionesPorOficina.size() - 1);
		}

		try{
			busquedaLibro = libroRepository.findLibroUploadFileValiation(libroVO);
		}
		catch (NonUniqueResultException uniqueException) {
			throw uniqueException;
		}

		if (busquedaLibro == null) {
			libro = new Libro();

			libro.seccionesPorOficina(seccionPorOficina);
			libro.setAnio(libroVO.getAnio());
			libro.setNumLibro(libroVO.getTomo());
			libro.setLibroBis(libroVO.getLibrobis());
			libro.setVolumen(libroVO.getVolumen());
			libro.setActivo(true);
			libro.setRuta("");
			libroRepository.save(libro);
		} else {
			libro = busquedaLibro;
		}

		return libro;
	}

	private void saveUploadedFiles(MultipartFile file, String nombre, LibroVO libroVO) throws IOException {
		Usuario user=this.usuarioService.getUsuario();
		user.getOficina().getId();
		String parametro =Constantes.RUTA_LIBRO_CVE+user.getOficina().getNumOficina();
		System.out.println("ESTA ES LA RUTA DE LIBRO "+parametro);
		UPLOADED_FOLDER= parametroRepository.findByCve(parametro).getValor();
		System.out.println("UPLOAD_FOLDER===>" + UPLOADED_FOLDER);
		byte[] bytes = file.getBytes();

		//formato
		String pathCustom = this.formato(libroVO);

		System.out.println("saveUploadedFiles" + pathCustom + nombre);
		File directory = new File(pathCustom);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		File f = new File(pathCustom + nombre);
		if(f.exists() && !f.isDirectory()) {
			throw new IOException("Archivo para ese antecedente ya existe");
		}

		Path path = Paths.get(pathCustom + nombre);
		System.out.println("PATH===>" + Paths.get(pathCustom + nombre));
		Files.write(path, bytes);

	}

	private void saveUploadedFiles(MultipartFile file, String nombre, String extension, LibroVO libroVO) throws IOException {
		Usuario user=this.usuarioService.getUsuario();
		user.getOficina().getId();
		String parametro =Constantes.RUTA_LIBRO_CVE+user.getOficina().getNumOficina();
		System.out.println("ESTA ES LA RUTA DE LIBRO "+parametro);
		UPLOADED_FOLDER= parametroRepository.findByCve(parametro).getValor();
		System.out.println("UPLOAD_FOLDER===>" + UPLOADED_FOLDER);
		byte[] bytes = file.getBytes();

		//formato
		String pathCustom = this.formato(libroVO);

		System.out.println("saveUploadedFiles" + pathCustom + nombre + extension);
		File directory = new File(pathCustom);
		if (!directory.exists()) {
			directory.mkdirs();
		}

		File f = new File(pathCustom + nombre + extension);
		if(f.exists() && !f.isDirectory()) {
			String newValor = pathCustom + nombre+ UtilFecha.getTimeStamp() + extension;
			File f2 = new File(newValor);
			f.renameTo(f2);
			ARCHIVO_ANT = newValor;
		}

		Path path = Paths.get(pathCustom + nombre + extension);
		System.out.println("PATH===>" + Paths.get(pathCustom + nombre + extension));
		Files.write(path, bytes);

	}

	private String formato(LibroVO libroVO) {

		Oficina oficina = oficinaRepository.findOne(libroVO.getOficina());
		Seccion seccion = seccionRepository.findOne(libroVO.getSeccion());

		String pathCustom = UPLOADED_FOLDER + "/" + StringUtils.leftPad(
				oficina.getNumOficina(), 2, "0") + "_"//OFICINA
				+StringUtils.leftPad(""+libroVO.getAnio(), 4, "0") +"_"//ANIO 
				+StringUtils.leftPad(seccion.getClave(), 2, "0") +"_"//SECCION
				+StringUtils.leftPad(libroVO.getLibrobis(), 2, "0")+"_"//LIBRO 
				+StringUtils.leftPad(libroVO.getTomo(), 2, "0")+"_"//TOMO
				+StringUtils.leftPad(libroVO.getVolumen(), 2, "0") +"/";//VOLUMEN
		//en carpeta es-->>oficina_año_seccion_libro(librobis)_tomo(numLibro)_volumen

		return pathCustom;
	}
	
}
