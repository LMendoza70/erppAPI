package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import com.gisnet.erpp.domain.PrelacionAnteCapHist;
import com.gisnet.erpp.domain.SeccionPorOficina;
import com.gisnet.erpp.domain.Usuario;

public interface PrelacionAnteCapHistRepositoryCustom {
	public abstract Page<PrelacionAnteCapHist> findAllBy(String numLibro, String libroBis, Long seccionPorOficinaId, Integer anio, String volumen, Usuario usuarioCaptura, List<Long> statusAnteIds, String carga, Pageable pageable);
	public abstract PrelacionAnteCapHist findFirstCarga(String numLibro, Long seccionPorOficinaId, Integer anio, String volumen, String carga);	
}