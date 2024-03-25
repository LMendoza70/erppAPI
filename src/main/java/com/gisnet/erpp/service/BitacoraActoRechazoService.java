package com.gisnet.erpp.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.BitacoraActoRechazo;
import com.gisnet.erpp.repository.BitacoraActoRechazoRepository;

@Service
public class BitacoraActoRechazoService {
	@Autowired
	private BitacoraActoRechazoRepository bitacoraActoRechazoRepository;

	public List<BitacoraActoRechazo> findBitacoraByActo(Long actoid) {
		return  bitacoraActoRechazoRepository.findByActoIdOrderByIdDesc(actoid);
	}	

	public List<BitacoraActoRechazo> findBitacoraByusuario(Long usuarioId) {
		return bitacoraActoRechazoRepository.findAllBitacoraActoRechazoByUsuarioId(usuarioId);
	}
	
	public Boolean deleteByActo(Long actoId) {
		bitacoraActoRechazoRepository.deleteByActoId(actoId);
		return true;
	}

	public void saveBitacora(Acto acto, Usuario usuario, String motivo, String fundamento, String observaciones) {
		BitacoraActoRechazo bitacora = new BitacoraActoRechazo();

		bitacora.setFecha(new Date());
		bitacora.setActo(acto);
		bitacora.setUsuario(usuario);
		bitacora.setMotivo(motivo);
		bitacora.setFundamento(fundamento);
		bitacora.setObservaciones(observaciones);

		bitacoraActoRechazoRepository.save(bitacora);
		
	}
}
