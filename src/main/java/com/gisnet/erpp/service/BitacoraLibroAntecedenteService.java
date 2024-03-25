package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.BitacoraLibroAntecedente;
import com.gisnet.erpp.domain.Libro;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.Seccion;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.BitacoraLibroAntecedenteRepository;
import com.gisnet.erpp.repository.LibroRepository;
import com.gisnet.erpp.repository.OficinaRepository;
import com.gisnet.erpp.repository.SeccionRepository;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.LibroVO;
import com.querydsl.core.NonUniqueResultException;

@Service
public class BitacoraLibroAntecedenteService {

	@Autowired
	BitacoraLibroAntecedenteRepository bitacoraLibroAntecedenteRepository ; 
	
	@Autowired
	LibroRepository libroRepository; 
	
	@Autowired
	OficinaRepository oficinaRepository;
	
	@Autowired
	SeccionRepository seccionRepository;
	
	public BitacoraLibroAntecedente createAnte(Usuario usu,Libro libro, String ins, String accion, String tipo, String rutaArchivo) {
		BitacoraLibroAntecedente bitacoraLibroAnt = new BitacoraLibroAntecedente();
		
		bitacoraLibroAnt = this.getLibro(libro,accion);
		
		bitacoraLibroAnt.fecha(new Date());
		bitacoraLibroAnt.setUsuario(usu);
		bitacoraLibroAnt.setAccion(accion);
		bitacoraLibroAnt.setInscripcion(ins);
		bitacoraLibroAnt.setTipoGestion(tipo);
		bitacoraLibroAnt.setRutaArchivo(rutaArchivo);
		
		
		return bitacoraLibroAntecedenteRepository.saveAndFlush(bitacoraLibroAnt);
	}
	
	public BitacoraLibroAntecedente createLibro(Usuario usu,Libro libro, String accion, String tipo) {
		BitacoraLibroAntecedente bitacoraLibroAnt = new BitacoraLibroAntecedente();
		
		bitacoraLibroAnt = this.getLibro(libro,accion);
		
		bitacoraLibroAnt.fecha(new Date());
		bitacoraLibroAnt.setUsuario(usu);
		bitacoraLibroAnt.setAccion(accion);
		bitacoraLibroAnt.setTipoGestion(tipo);
		
		
		return bitacoraLibroAntecedenteRepository.saveAndFlush(bitacoraLibroAnt);
	}
	
	
	private BitacoraLibroAntecedente getLibro(Libro libro, String accion) {
		BitacoraLibroAntecedente bitacoraLibroAnt = new BitacoraLibroAntecedente();
		
		bitacoraLibroAnt.setLibro(libro);
		bitacoraLibroAnt.setOficina(libro.getSeccionesPorOficina().getOficina());
		bitacoraLibroAnt.setSeccion(libro.getSeccionesPorOficina().getSeccion());
		
		if(accion.equals("CREATE_ARCHIVO") || accion.equals("CREATE_LIBRO") || accion.equals("UPDATE_LIBRO")) {
			bitacoraLibroAnt.setAnio(libro.getAnio());
			bitacoraLibroAnt.setLibroBis(libro.getLibroBis());
			bitacoraLibroAnt.setTomo(libro.getNumLibro());
			bitacoraLibroAnt.setVolumen(libro.getVolumen());
		}
		
		return bitacoraLibroAnt;
	}
	
	public List<BitacoraLibroAntecedente> bitacoraListAnte (LibroVO libroVO, String tipo){
		List<BitacoraLibroAntecedente> list = new ArrayList<BitacoraLibroAntecedente>(); 
		Libro busquedaLibro = null;
		Oficina oficina = oficinaRepository.findOne(libroVO.getOficina());
		Seccion seccion = seccionRepository.findOne(libroVO.getSeccion());
		
		try{
			busquedaLibro = libroRepository.findLibroUploadFileValiation(libroVO);
		} catch (NonUniqueResultException uniqueException) {
			throw uniqueException;
		}
		
		if (busquedaLibro == null) {
			list = bitacoraLibroAntecedenteRepository.findByAnioAndLibroBisAndTomoAndVolumenAndOficinaIdAndSeccionIdAndTipoGestion(
					libroVO.getAnio(),libroVO.getLibrobis(),libroVO.getTomo(),libroVO.getVolumen(),oficina.getId(),seccion.getId(),Constantes.TIPO_ANTECEDENTE);
		} else {
			list = bitacoraLibroAntecedenteRepository.findAllByLibroIdAndTipoGestionAndInscripcion(busquedaLibro.getId(),tipo,libroVO.getNumeroInscripcion());
		}
		
		return list;
	}
	
	public List<BitacoraLibroAntecedente> bitacoraListLibro (Long id, String tipo){
		List<BitacoraLibroAntecedente> list = new ArrayList<BitacoraLibroAntecedente>(); 
		Libro busquedaLibro = null;

		busquedaLibro = libroRepository.findById(id);

		list = bitacoraLibroAntecedenteRepository.findAllByLibroIdAndTipoGestion(busquedaLibro.getId(),tipo);


		return list;
	}
}
