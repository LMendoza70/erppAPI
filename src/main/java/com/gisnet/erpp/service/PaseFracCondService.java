package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.FoliosFracCond;
import com.gisnet.erpp.domain.PaseFracCond;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.StatusActo;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.FoliosFracCondRepository;
import com.gisnet.erpp.repository.PaseFracCondRepository;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.util.UtilFecha;

@Service
public class PaseFracCondService {
	@Autowired
	PaseFracCondRepository paseFracCondRepository;
	@Autowired
	FoliosFracCondRepository foliosFracCondRepository;
	@Autowired
	UsuarioService usuarioService;

	@Autowired
	PredioService predioService;
	
	@Autowired
	ActoService actoService;

	@Transactional
	public PaseFracCond save(PaseFracCond pase) {
		try {
			Usuario usuario = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
			pase.setFechaRegistro(new Date());
			pase.setFechaCaduca(UtilFecha.addDays(new Date(), 30));
			pase.setConsecutivo(Integer.parseInt(paseFracCondRepository.getFolioPaseSequence().toString()));
			pase.setUsuario(usuario);
			pase = paseFracCondRepository.save(pase);
			return pase;
		} catch (Exception e) {
			return null;
		}
	}

	@Transactional
	public Boolean delete(Long id) {
		try {
			PaseFracCond pase = paseFracCondRepository.findOne(id);
			List<FoliosFracCond> predios = foliosFracCondRepository.findByPaseFracCondId(pase.getId());
			predioService.deleteFracCond(predios);
			paseFracCondRepository.delete(pase);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

	public List<FoliosFracCond> findFoliosByPaseId(Long paseId) {
		return foliosFracCondRepository.findByPaseFracCondId(paseId);
	}
	
	public PaseFracCond findPase(Long paseId) {
		return paseFracCondRepository.findOne(paseId);
	}

	public List<PaseFracCond> findByUsuarioLoginId() {
		Usuario usuario = usuarioService.findByNombre(SecurityUtils.getCurrentUserLogin());
		return paseFracCondRepository.findByUsuarioNoProcesadosAndVigentes(usuario.getId(),new Date());
	}

	@Transactional
	public FoliosFracCond saveFolioFracCond(FoliosFracCond folio) {
		try {
			Predio predio = predioService.findOne(folio.getPredio().getId());
			folio.setActo(null);
			folio.setProcedente(true);
			Integer noPredio = foliosFracCondRepository.findMaxFolio(folio.getPaseFracCond().getId());
			folio.setNoPredio(noPredio == null ? 1 : noPredio+1);
			folio = foliosFracCondRepository.save(folio);
			StatusActo statusActo = new StatusActo();
			statusActo.setId(Constantes.StatusActo.EN_PROCESO.getIdStatusActo());
			predio.setStatusActo(statusActo);
			predioService.saveAndUpdate(predio);
			return folio;
		} catch (Exception e) {
			return null;
		}
	}

	@Transactional
	public Boolean deleteFracCond(Long idPredio) {
		try {

				predioService.deleteFracCond(foliosFracCondRepository.findByPredioId(idPredio));
			return true;
		}catch(Exception e) {
			return false;
		}
	}
	
	public void desAsociarPase(Long actoId) 
	{
		List<FoliosFracCond> folios =  foliosFracCondRepository.findByIdActo(actoId);
		folios.forEach(x->{
			   x.setActo(null);
			   foliosFracCondRepository.save(x);
		});
	}
	
	@Transactional
	public Boolean asociarPase(Integer numPase,Long actoId) 
	{
	   Optional<PaseFracCond> pase = paseFracCondRepository.findFirstByConsecutivo(numPase);
	   Acto acto = actoService.findById(actoId);
	   
	   if(pase.isPresent() && acto!=null) 
	   {
		   PaseFracCond _pase =  pase.get();
		   this.desAsociarPase(actoId);
		   List<FoliosFracCond> folios =  foliosFracCondRepository.findByPaseFracCondId(_pase.getId());
		   folios.forEach(x->{
			   x.setActo(acto);
			   foliosFracCondRepository.save(x);
		   });
		   _pase.setProcesado(true);
		   paseFracCondRepository.save(_pase);
		   return true;
	   }else
		   return false;
	}
}
