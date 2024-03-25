package com.gisnet.erpp.service;

import java.time.Year;
import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Optional;
import java.util.Date;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.Normalizer;

import javax.persistence.EntityManagerFactory;


import org.apache.tomcat.jni.Time;
//import org.apache.xpath.operations.String;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestParam;

import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.SessionFactory;
import org.hibernate.Session;
import org.hibernate.engine.jdbc.connections.spi.ConnectionProvider;

import com.gisnet.erpp.domain.AnteUsuarioCapHist;
import com.gisnet.erpp.domain.PrelacionAnteCapHist;
import com.gisnet.erpp.domain.PrelacionPredio;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.MessageObject;

import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionAnte;
import com.gisnet.erpp.domain.TipoDocumento;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.SeccionPorOficina;
import com.gisnet.erpp.domain.Libro;
import com.gisnet.erpp.domain.PrelacionAnteCapHist;
import com.gisnet.erpp.domain.Prioridad;

import javax.persistence.RollbackException;

import com.gisnet.erpp.repository.AntecedenteRepository;
import com.gisnet.erpp.repository.LibroRepository;
import com.gisnet.erpp.repository.UsuarioRepository;
import com.gisnet.erpp.repository.OficinaRepository;
import com.gisnet.erpp.repository.PrelacionAnteRepository;
import com.gisnet.erpp.repository.PrelacionRepository;
import com.gisnet.erpp.repository.PrioridadRepository;
import com.gisnet.erpp.repository.SeccionRepository;
import com.gisnet.erpp.repository.TipoFolioRepository;
import com.gisnet.erpp.repository.StatusRepository;
import com.gisnet.erpp.repository.StatusAntecedenteRepository;
import com.gisnet.erpp.repository.PrelacionAnteCapHistRepository;

import com.gisnet.erpp.vo.AntecedenteVO;
import com.gisnet.erpp.security.SecurityUtils;

@Service
public class AntecedenteService {

	@Autowired
	private PrelacionRepository prelacionRepository;

	@Autowired
	private AntecedenteRepository antecedenteRepository;
	
	@Autowired
	private SeccionRepository seccionRepository;

	@Autowired
	private OficinaRepository oficinaRepository;

	@Autowired
	private PrelacionAnteRepository prelacionAnteRepository;

	@Autowired
	private TipoFolioRepository tipoFolioRepository;

	public PrelacionAnte createPrelacionAntecedente(AntecedenteVO anteVO) {
		PrelacionAnte prelacionAnte= null;
		prelacionAnte = copyFromVO (anteVO);
		prelacionAnteRepository.saveAndFlush(prelacionAnte);
		return prelacionAnte;
	}

	private PrelacionAnte copyFromVO (AntecedenteVO ante) {
		PrelacionAnte antecedente = new PrelacionAnte();
		antecedente.setLibro(cleanString(ante.getLibro()));
		antecedente.setLibroBis(cleanString(ante.getLibroBis()));
		antecedente.setSeccion(seccionRepository.findOne(Long.valueOf(ante.getSeccion())) );
		antecedente.setOficina(oficinaRepository.findOne(Long.valueOf(ante.getOficina())));
		antecedente.setDocumento(ante.getDocumento());
		antecedente.setDocumentoBis(ante.getDocumentoBis());
		antecedente.setAnio(ante.getAnio());
		antecedente.setVolumen(cleanString(ante.getVolumen()));
		antecedente.setTipoFolio(tipoFolioRepository.findOne(Long.valueOf(ante.getTipoBusqueda())));
		antecedente.setPrelacion(prelacionRepository.findOne(ante.getPrelacionId())); 
		return antecedente;
	}

	public List<AntecedenteVO> findAntecedentesDePrelacion(Long idPrelacion){
	//	List<Antecedente> lstAntecedentes = antecedenteRepository.findByPrelacionAntesParaAntecedentesPrelacionId(idPrelacion);
		List<Antecedente> lstAntecedentes = null;
		List<AntecedenteVO> result = null;
		AntecedenteVO antecedenteVO = null;
		
		
		if(lstAntecedentes != null && !lstAntecedentes.isEmpty()){
			result = new ArrayList<>();
			for(Antecedente antecedente:lstAntecedentes){
				antecedenteVO = new AntecedenteVO();
				//copyProperties(antecedente, antecedenteVO);
				result.add(antecedenteVO);
			}
		}

		return result;
	}


	public List<PrelacionAnte> findByPrelacionId(Long id) {
		return prelacionAnteRepository.findByPrelacionId(id);
	}


	public Antecedente findByDatosAntecedente(String doc, String docbis,Integer libro, String librobis,String seccion, String oficina){
		return	antecedenteRepository.findByDatosAntecedente(doc, docbis, libro, librobis, seccion, oficina);
	}

	/*
	public Antecedente findByComponentes(Libro libro, String libroBis,Seccion seccion , Oficina oficina, String documento, String documentoBis){

		if (documento!= null)
			documento = documento.trim()== "" ? null: documento.trim();
		if (documentoBis != null)
			documentoBis = documentoBis.trim()== "" ? null: documentoBis.trim();

		if (libroBis == null && documento == null && documentoBis == null)
			return antecedenteRepository.findBylibroAndSeccionAndOficina(libro, seccion, oficina );

		if (libroBis != null && documento == null && documentoBis == null)
			return antecedenteRepository.findBylibroAndSeccionAndOficinaAndLibroBis(libro, seccion, oficina, libroBis );

		if (libroBis == null && documento != null && documentoBis == null)
			return antecedenteRepository.findBylibroAndSeccionAndOficinaAndDocumento(libro, seccion, oficina, documento);

		if (libroBis == null && documento == null && documentoBis != null)
			return antecedenteRepository.findBylibroAndSeccionAndOficinaAndDocumentoBis(libro, seccion, oficina, documentoBis);

		if (libroBis != null && documento != null && documentoBis == null)
			return antecedenteRepository.findBylibroAndSeccionAndOficinaAndLibroBisAndDocumento(libro, seccion, oficina, libroBis, documento);

		if (libroBis != null && documento == null && documentoBis != null)
			return antecedenteRepository.findBylibroAndSeccionAndOficinaAndLibroBisAndDocumentoBis(libro, seccion, oficina, libroBis, documentoBis);

		if (libroBis == null && documento != null && documentoBis != null)
			return antecedenteRepository.findBylibroAndSeccionAndOficinaAndDocumentoAndDocumentoBis(libro, seccion, oficina, documento, documentoBis);

		if (libroBis != null && documento != null && documentoBis != null)
			return	antecedenteRepository.findByLibroAndLibroBisAndSeccionAndOficinaAndDocumentoAndDocumentoBis(libro, libroBis, seccion, oficina, documento, documentoBis);

		return null;
	} */


	public void saveAndUpdate( Antecedente antecedente ){
		if(antecedente!=null)
		antecedenteRepository.saveAndFlush(antecedente);
	}

	public void delete( Long antecedente ){
		if(antecedente!=null)
		antecedenteRepository.delete(antecedente);
	}


	@Transactional
	public PrelacionAnte saveAndUpdateAntecedente(PrelacionAnte antecedente, Long idPrelacion )
	{
		Prelacion prelacionActual = prelacionRepository.findOne(idPrelacion);
		//Antecedente nuevoAntecedente=null;
		/*if(antecedente.getId()!=null){
				nuevoAntecedente = antecedenteRepository.findOne(antecedente.getId());
		}else{

			 nuevoAntecedente = antecedenteRepository.saveAndFlush(antecedente);
		}*/

		PrelacionAnte preAnte= new PrelacionAnte();

		preAnte.setPrelacion(prelacionActual);
		//preAnte.setAntecedente(antecedente);
		preAnte.setLibro(antecedente.getLibro());
		if (antecedente.getLibroBis() != null)
			preAnte.setLibroBis( antecedente.getLibroBis());
		preAnte.setSeccion(antecedente.getSeccion());
		preAnte.setOficina(antecedente.getOficina());
		preAnte.setDocumento(antecedente.getDocumento());
		preAnte.setDocumentoBis(antecedente.getDocumentoBis());
		preAnte.setTipoFolio(antecedente.getTipoFolio());

		prelacionAnteRepository.saveAndFlush(preAnte);

		return preAnte;
	}

	public Antecedente findAntecedenteByPersonaJuridicaId (Long personaJuridicaId) {
		return antecedenteRepository.findAntecedenteByPersonaJuridicaId(personaJuridicaId);
	}
	public Antecedente findAntecedenteByFolioSeccionAuxiliarId (Long folioSeccionAuxiliarId) {
		return antecedenteRepository.findAntecedenteByFolioSeccionAuxiliarId(folioSeccionAuxiliarId);
	}
	public Antecedente findAntecedenteByMuebleId (Long muebleId) {
		return antecedenteRepository.findAntecedenteByMuebleId(muebleId);
	}
	public Antecedente findAntecedenteByPredioId (Long predioId) {
		return antecedenteRepository.findAntecedenteByPredioId(predioId);
	}
	
	public List<Antecedente> findAntecedenteByLibroIdAndDocumento (Long idLibro,String documento) {
		System.out.println("Ante serv-- "+idLibro+"---"+documento);
		return antecedenteRepository.findAntecedenteByLibroIdAndDocumento(idLibro,documento);
	}

	/*public void copyProperties(Antecedente from,AntecedenteVO to){
		to.setId(from.getId());
		if(from.getPrelacionAntesParaAntecedentes() != null && !from.getPrelacionAntesParaAntecedentes().isEmpty()){
			to.setTipoFolio(from.getPrelacionAntesParaAntecedentes().iterator().next().getTipoFolio());
		}
		to.setSeccion(from.getSeccion().getNombre());
		to.setOficina(from.getOficina().getNombre());
		to.setNombreLibro(from.getLibro().getNombre());
	}*/

	public Antecedente getAntecedenteByTipo(PrelacionPredio prel, Constantes.ETipoFolio tipo) {
		Antecedente ante = null;

		switch (tipo) {
			case PREDIO:
				ante =	this.findAntecedenteByPredioId(prel.getPredio().getId());
				break;
			case MUEBLE:
				ante =	this.findAntecedenteByMuebleId(prel.getMueble().getId());
				break;
			case PERSONAS_AUXILIARES:
				ante =	this.findAntecedenteByFolioSeccionAuxiliarId(prel.getFolioSeccionAuxiliar().getId());
				break;
			case PERSONAS_JURIDICAS:
				ante = this.findAntecedenteByPersonaJuridicaId(prel.getPersonaJuridica().getId());
				break;
			default:
				break;

		}

		return ante;
	}

	 public static String cleanString(String texto) {
	        texto = Normalizer.normalize(texto, Normalizer.Form.NFD);
	        texto = texto.replaceAll("[\\p{InCombiningDiacriticalMarks}]", "");
	        return texto.toUpperCase();
	    }
	 
	 public String createComentarioAntecedente(Antecedente ant) {
		 StringBuilder comment = new StringBuilder();
		 
		 comment.append(" TOMO: "+ant.getLibro().getNumLibro().toString());
		 comment.append(" LIBRO: "+ ant.getLibro().getLibroBis());
		 comment.append(" SECCION: "+ant.getLibro().getSeccionesPorOficina().getSeccion().getNombre());
		 comment.append(" OFICINA: "+ant.getLibro().getSeccionesPorOficina().getOficina().getNombre());
			
		 comment.append(" VOLUMEN: "+ant.getLibro().getVolumen());
		 comment.append(" AÑO: "+ant.getLibro().getAnio());
		 comment.append(" INSCRIPCIÓN: "+ant.getDocumento());
		 //comment.append(" INSCRIPCIÓN: "+ant.getDocumentoBis());
		 
		 	return comment.toString();
	 }
	 
}
