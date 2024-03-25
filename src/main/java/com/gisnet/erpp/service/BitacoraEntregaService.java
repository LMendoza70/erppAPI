package com.gisnet.erpp.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.BitacoraEntrega;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.repository.BitacoraEntregaRepository;
import com.gisnet.erpp.util.Constantes;

@Service
public class BitacoraEntregaService {
	@Autowired
	private BitacoraEntregaRepository bitacoraEntregaRepository;

	public List<BitacoraEntrega> findBitacoraEByprelacion(Long prelacionId) {
		return  bitacoraEntregaRepository.findAllBitacoraEntregaByPrelacionIdOrderByIdDesc(prelacionId);
	}	

	public List<BitacoraEntrega> findBitacoraEByusuario(Long usuarioId) {
		return bitacoraEntregaRepository.findAllBitacoraEntregaByUsuarioId(usuarioId);
	}

	public BitacoraEntrega saveBitacoraEntrega(Prelacion prelacion,Usuario usuario, Persona persona) {
		BitacoraEntrega bitacoraE = new BitacoraEntrega();
		BitacoraEntrega newBitacoraE = new BitacoraEntrega(); 

		bitacoraE.setFecha(new Date());
		bitacoraE.setPrelacion(prelacion);
		bitacoraE.setUsuario(usuario);
		bitacoraE.setNombre(persona.getNombre());
		bitacoraE.setPaterno(persona.getPaterno());
		bitacoraE.setMaterno(persona.getMaterno());

		newBitacoraE = bitacoraEntregaRepository.save(bitacoraE);
		
		return newBitacoraE;
	}
}
