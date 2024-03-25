package com.gisnet.erpp.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.BitacoraGestor;
import com.gisnet.erpp.domain.Libro;
import com.gisnet.erpp.repository.AntecedenteRepository;
import com.gisnet.erpp.repository.LibroRepository;
import com.gisnet.erpp.repository.ParametroCotizadorRepository;
import com.gisnet.erpp.repository.PrelacionAnteRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.AntecedenteVO;

@Service
public class ImagenService{
	
	private static final Logger log = LoggerFactory.getLogger(ImagenService.class);
	
	
	@Autowired
	private LibroRepository libroRepository;
		
	@Autowired
	private BitacoraGestorService bitacoraGestorService;

	@Autowired
	private AntecedenteRepository antecedenteRepository;

	public String getImage(String imagePath){
		String encodeImage = null;
		InputStream in = null;
		File file = new File(imagePath);
		
		if(file.exists()){
			try {
				in = new FileInputStream(imagePath);
				encodeImage = Base64.getEncoder().withoutPadding().encodeToString(IOUtils.toByteArray(in));
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				try {
					if(in != null){
						in.close();
					}
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return encodeImage;
	}
	

	
	public Libro findLibroBy(BitacoraGestor bitacoraGestor){
		log.debug("Buscando libro por criterios {}", bitacoraGestor);
		
		bitacoraGestorService.saveBitacora(bitacoraGestor);
		Libro libroConsulta =null;
		String codigoLibro = null;
		if(bitacoraGestor != null){
			
			if(bitacoraGestor.getCodigoLibro() != null && !bitacoraGestor.getCodigoLibro().isEmpty()){
				log.debug("Buscando por codigo de libro {}",bitacoraGestor.getCodigoLibro());
				//if(libro.getTipo() != null &&  !libro.getTipo().isEmpty()){
				if(bitacoraGestor.getLibroBis() != null &&  !bitacoraGestor.getLibroBis().isEmpty()){
					codigoLibro = bitacoraGestor.getCodigoLibro()+bitacoraGestor.getLibroBis().charAt(0);
					log.debug("Codigo de libro construido : {}",codigoLibro);
					libroConsulta = libroRepository.findByNombre(codigoLibro);
					if(libroConsulta == null && bitacoraGestor.getLibroBis().length() > 1){
						codigoLibro = bitacoraGestor.getCodigoLibro()+bitacoraGestor.getLibroBis().charAt(1);
						log.debug("Codigo de libro construido : {}",codigoLibro);
						libroConsulta = libroRepository.findByNombre(codigoLibro);
					}
				}else{
					codigoLibro = bitacoraGestor.getCodigoLibro()+"0";
					log.debug("Codigo de libro construido : {}",codigoLibro);
					libroConsulta = libroRepository.findByNombre(codigoLibro);
				}
			}else{
				log.debug("Buscando por numero de libro ..");
				
				libroConsulta = libroRepository.findLibroBy(bitacoraGestor.getLibro(), bitacoraGestor.getLibroBis(), bitacoraGestor.getSeccion().getId(), bitacoraGestor.getOficina().getId(), bitacoraGestor.getAnio(), bitacoraGestor.getVolumen());
				
			}
		}
			
		return libroConsulta;
	}
	public List<Antecedente> findLibroByAmplia(BitacoraGestor bitacoraGestor){
		System.out.println("BitacoraGestor : " +  bitacoraGestor.getOficina().getId());
		List<Antecedente> antecedete = new ArrayList<Antecedente>();
		if(bitacoraGestor != null){ 
				antecedete = antecedenteRepository.findAntecedenteAmpliaByOficinaAndSeccionAndAnioAndLibro(bitacoraGestor.getOficina().getId(), bitacoraGestor.getSeccion().getId(), bitacoraGestor.getAnio(),bitacoraGestor.getDocumento());
			}
		return antecedete;
	}
}
