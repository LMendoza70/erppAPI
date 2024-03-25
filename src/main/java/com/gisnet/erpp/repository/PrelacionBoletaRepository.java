package com.gisnet.erpp.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.PrelacionBoleta;

@SuppressWarnings("unused")
public interface PrelacionBoletaRepository extends JpaRepository<PrelacionBoleta,Long> 
{
	List<PrelacionBoleta> findByPrelacionIdOrderByIdAsc(Long prelacionId);
	List<PrelacionBoleta> findByPrelacionIdAndPagina(Long prelacionId,Integer pagina);
	PrelacionBoleta findFirstByPrelacionIdAndPaginaOrderByIdDesc(Long prelacionId,Integer pagina);
	List<PrelacionBoleta> findByPrelacionIdAndTipoBoletaId(Long prelacionId,Long tipoBoleta);
	PrelacionBoleta findFirstByPrelacionIdOrderByIdDesc(Long prelacionId);
	
	Optional<PrelacionBoleta> findFirstByPrelacionIdOrderByPaginaDesc(Long prelacionId);
}
