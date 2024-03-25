package com.gisnet.erpp.service;





import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.io.File;
import com.gisnet.erpp.util.SHACheckSum;
import com.gisnet.erpp.util.SignerUtilBouncyCastle;

import java.util.Calendar;

import java.util.ArrayList;
import com.gisnet.erpp.domain.Archivo;
import com.gisnet.erpp.domain.DocumentoFirma;
import com.gisnet.erpp.domain.ActoFirma;
import com.gisnet.erpp.domain.ActoDocumento;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.vo.ArchivoFirmaVO;
import com.gisnet.erpp.service.UsuarioService;
import com.gisnet.erpp.repository.ArchivoRepository;
import com.gisnet.erpp.repository.DocumentoFirmaRepository;
import com.gisnet.erpp.repository.ActoRepository;
import com.gisnet.erpp.repository.ActoDocumentoRepository;
import com.gisnet.erpp.repository.DocumentoRepository;
import com.gisnet.erpp.repository.ActoFirmaRepository;
import com.gisnet.erpp.security.SecurityUtils;

@Service
public class ArchivoService {
	@Autowired
	ArchivoRepository archivoRepository; 


	@Autowired
	ActoRepository actoRepository; 

	@Autowired
	DocumentoRepository documentoRepository;
	
	@Autowired
	ActoDocumentoRepository actoDocumentoRepository; 

	@Autowired
	DocumentoFirmaRepository documentoFirmaRepository; 

	@Autowired
	ActoFirmaRepository actoFirmaRepository; 

	@Autowired
	UsuarioService usuarioService;
	
	private static String UPLOADED_FOLDER = "c://tmp//";
	
	@Transactional
	public Long create(Archivo archivoIngreso){
		//Archivo archivo = new Archivo();
	
		archivoRepository.save(archivoIngreso);
				
		
		
		return archivoIngreso.getId();		
		
	}



	public Archivo findOne(Long id) {
		// TODO Auto-generated method stub
		return archivoRepository.findOne(id);
	}


	public List<Archivo> findAllFundatorioByPrelacion(Prelacion prelacion){

			List<Archivo> archivos= null;
			List<Acto> actos=actoRepository.findAllByPrelacion(prelacion);

			if(actos!=null){
				archivos = new ArrayList<Archivo>();
				for(Acto a: actos){

						List<Archivo> listFile=archivoRepository.findAllByDocumentoActo(a.getId());
						for(Archivo file :listFile){
								String ruta=file.getRuta()+file.getNombre();
								File tem= new File(ruta);					
								if(tem!=null){
									try{
										file.setHash( SHACheckSum.getDigestion(tem));
									}catch(Exception e){
										e.printStackTrace();
									}	
									
								}	
								archivos.addAll(listFile);
						}
						
				}

			}
			return archivos;
			
	}



	@Transactional
	public Boolean saveFirmaArchivo(ArchivoFirmaVO archivoVO){
			
			Usuario user= usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
			Archivo archivo= archivoRepository.findOne(archivoVO.getId());
			if(archivo!=null){
				List<ActoDocumento> actoDocs = actoDocumentoRepository.getByAllArchivo(archivo.getId());
						
				if(actoDocs!=null){
					actoDocs.forEach(actoDoc-> {
						DocumentoFirma documentoFirma= new DocumentoFirma();
						documentoFirma.setDocumento(actoDoc.getDocumento());
						documentoFirma.setArchivo(archivo);
						documentoFirma.setPkcs7( archivoVO.getPkcs7());
						System.out.println("getPcs7: "+archivoVO.getPkcs7());
						System.out.println("SignerUtilBouncyCastle.getEncryptedDigest: "+SignerUtilBouncyCastle.getEncryptedDigest(archivoVO.getPkcs7()));
						documentoFirma.setFirma(SignerUtilBouncyCastle.getEncryptedDigest(archivoVO.getPkcs7()));					
						documentoFirma.setSecuencia( archivoVO.getSecuencia()); 
						documentoFirma.setPolitica( archivoVO.getPolitica());
						documentoFirma.setDigestion( archivoVO.getDigestion()) ;
						documentoFirma.setSecuenciaTS( archivoVO.getSecuenciaTS());
						documentoFirma.setEstampilla(archivoVO.getEstampilla()); 
						documentoFirma.setUsuario(user);
						documentoFirmaRepository.save(documentoFirma);
						
					});
					
				}
			}


			return true;
	}

	
}
