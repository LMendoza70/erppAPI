package com.gisnet.erpp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Motivo;
import com.gisnet.erpp.domain.TipoMotivo;
import com.gisnet.erpp.domain.TipoRechazo;
import com.gisnet.erpp.repository.TipoMotivoRepository;
import com.gisnet.erpp.repository.TipoRechazoRepository;
import com.gisnet.erpp.repository.MotivoRepository;;

@Service
public class MotivoService {
	
	@Autowired
	MotivoRepository motivoRepository;

	@Autowired
	TipoMotivoRepository tipoMotivoRepository;

	@Autowired
	TipoRechazoRepository tipoRechazoRepository;
	
	public Motivo findOne(Long id) {
		return motivoRepository.findOne(id);
}

	public List<Motivo> findAll() {
			return motivoRepository.findAll();
	}

	public List<Motivo> findAllByActivo(Boolean activo) {
		return motivoRepository.findAllByActivo(activo);
	}

	public List<TipoRechazo> findAllByTipoRechazo() {
		return tipoRechazoRepository.findAll();
	}
	
	public List<TipoRechazo> findAllByTipoRechazo(String tipoMotivo) {
		return tipoRechazoRepository.findAllByTipoMotivoNombre(tipoMotivo);
	}
	
	public List<Motivo> findAllMotivosBy(String tipoMotivo) {
		return motivoRepository.findAllByTipoMotivoNombre(tipoMotivo);
	}
	
	public Motivo create(Motivo motivo)
	{
		motivoRepository.save(motivo);
		return motivo;
	}
	
	public Long delete(Motivo motivo) {
		motivoRepository.delete(motivo);
		return 0l;
	}
}
