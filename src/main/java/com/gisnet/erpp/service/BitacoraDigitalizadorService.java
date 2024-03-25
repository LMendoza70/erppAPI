package com.gisnet.erpp.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.BitacoraDigitalizador;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.repository.BitacoraDigitalizadorRepository;
import com.gisnet.erpp.util.Constantes;

@Service
public class BitacoraDigitalizadorService {
	@Autowired
	private BitacoraDigitalizadorRepository bitacoraDigitalizadorRepository;
	
	public List<BitacoraDigitalizador> findBitacoraDByprelacion(Long prelacionId) {
		return  bitacoraDigitalizadorRepository.findAllBitacoraDigitalizadorByPrelacionIdOrderByIdDesc(prelacionId);
	}	
	
	public List<BitacoraDigitalizador> findBitacoraDByusuario(Long usuarioId) {
	  	return bitacoraDigitalizadorRepository.findAllBitacoraDigitalizadorByUsuarioId(usuarioId);
	}
	
	public BitacoraDigitalizador saveBitacoraDig(Prelacion prelacion,Usuario usuario) {
		BitacoraDigitalizador bDigitalizador = new BitacoraDigitalizador();
		BitacoraDigitalizador newBDigitalizador = new BitacoraDigitalizador();
		bDigitalizador.setPrelacion(prelacion);
		bDigitalizador.setUsuario(usuario);
		bDigitalizador.setFecha(new Date());
		
		newBDigitalizador = bitacoraDigitalizadorRepository.save(bDigitalizador);
		return newBDigitalizador;
	}
}
