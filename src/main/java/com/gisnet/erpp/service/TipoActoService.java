package com.gisnet.erpp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.repository.TipoActoRepository;
import com.gisnet.erpp.util.Constantes;

@Service
public class TipoActoService{
	@Autowired
	TipoActoRepository tipoRepository;

	public List<TipoActo> findAll() {
		return tipoRepository.findAllByActivoIsTrueOrderByNombreAsc();
	}

	public List<TipoActo> findAllExcludeServicios() {

		return tipoRepository.findAllByServiciosParaTipoActosIsNullAndActivoIsTrueOrderByNombreAsc();
	}
	
	public TipoActo findOne(Long id) {
		return tipoRepository.findOne(id);
	}

	public List<TipoActo> findByClasifActoId(Long id) {
		return tipoRepository.findByClasifActoIdAndActivoIsTrueOrderByNombreAsc(id);
		//return tipoRepository.findByClasifActoIdAndActivoIsTrue(id);
		//return tipoRepository.findByClasifActoIdOrderByNombreAsc(id);

	}
	
	public List<TipoActo> findAllByTipoFolioId(Long id) {
		return tipoRepository.findAllByTipoFolioId(id);
	}
	
	public List<TipoActo> findAllByAreaId(Long id) {
		return tipoRepository.findAllByAreaId(id);
	}
	
	public TipoActo findOneWithModulosById(Long id){
		return tipoRepository.findOneWithModulosById(id).orElse(null);
	}

	public List<TipoActo> findMasivos(){
		return  tipoRepository.findByActivoAndMasivo(true,true);
	}
	
	@Transactional(readOnly = true)
	public Page<TipoActo> getAllTiposActos(Pageable pageable, String nombre) {
		return tipoRepository.getAllTiposActos(pageable, nombre);
	}
	
	@Transactional(readOnly = true)
	public List<TipoActo> findAllByTipoActoTipoActo(Long tipoActoid) {
		return tipoRepository.findAllByTipoActoTipoActoParaTipoActoPadreTipoActoId(tipoActoid);
	}
	
	@Transactional
	public Long save(TipoActo tipoActo) {
		tipoRepository.save(tipoActo);
		return tipoActo.getId();
	}
	
	@Transactional
	public Long delete(TipoActo tipoActo) {
		tipoRepository.delete(tipoActo);
		return tipoActo.getId();
	}
	
	public List<TipoActo> findByClasifActoCertificadoLG(Long id) {
		List<TipoActo> tipoActoNew = new ArrayList<TipoActo>();
		List<TipoActo> tipoActo = tipoRepository.findByClasifActoIdAndActivoIsTrueOrderByNombreAsc(id);
		for(TipoActo tp:tipoActo) {
			if(tp.getId().equals(Constantes.TIPO_ACTO_CERTIFICADO_LG) || 
					tp.getId().equals(Constantes.TIPO_ACTO_CERTIFICADO_LG_PRIMER_AVISO) ) {
				tipoActoNew.add(tp);
			}
		}
		return tipoActoNew;
	}
}
