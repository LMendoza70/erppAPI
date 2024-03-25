package com.gisnet.erpp.repository;

import java.util.List;

import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.domain.TipoFolio;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.FoliosrDigital;

public interface FoliosrDigRepository extends JpaRepository<FoliosrDigital,Long> {

	List<FoliosrDigital> findByTipoFolioIdAndNumFolioRegistral(Long tipoFolioId,Integer folio);

	FoliosrDigital findByTipoFolioIdAndNumFolioRegistralAndOficinaId(Long tipoFolioId,Integer folio,Long oficinaId);

	
	List<FoliosrDigital> findAllByNumFolioRegistralAndTipoFolioAndOficina(Integer numfolio, TipoFolio tipo, Oficina oficina);

	List<FoliosrDigital> findAllByNumFolioRegistralAndTipoFolioAndOficinaAndEscritura(Integer numfolio, TipoFolio tipo, Oficina oficina, String escritura);

	List<FoliosrDigital> findAllByNumFolioRegistralAndTipoFolioAndOficinaAndTipoActo(Integer numfolio, TipoFolio tipo, Oficina oficina, TipoActo tipoActo);

	List<FoliosrDigital> findAllByNumFolioRegistralAndTipoFolioAndOficinaAndEscrituraAndTipoActo(Integer numfolio, TipoFolio tipo, Oficina oficina, String escritura, TipoActo tipoActo);

}
