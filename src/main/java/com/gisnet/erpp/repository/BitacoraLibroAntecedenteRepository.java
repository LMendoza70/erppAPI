package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.BitacoraLibroAntecedente;

public interface BitacoraLibroAntecedenteRepository  extends JpaRepository<BitacoraLibroAntecedente,Long> {


	List<BitacoraLibroAntecedente> findAllByLibroId(Long libroId);
	
	List<BitacoraLibroAntecedente> findAllByLibroIdAndTipoGestion(Long libroId, String tipo);
	
	List<BitacoraLibroAntecedente> findAllByLibroIdAndTipoGestionAndInscripcion(Long libroId, String tipo, String ins);
	
	List<BitacoraLibroAntecedente> findAllByUsuarioId(Long usuarioId);
	
	List<BitacoraLibroAntecedente> findAllByUsuarioIdAndTipoGestion(Long usuarioId, String tipo);
	
	List<BitacoraLibroAntecedente> findAllByTipoGestion(String tipo);
	
	List<BitacoraLibroAntecedente> findByAnioAndLibroBisAndTomoAndVolumenAndOficinaIdAndSeccionIdAndTipoGestion(Integer anio,
			String bis, String tomo, String volumen, Long oficinaId, Long seccionId, String tipo);
}
