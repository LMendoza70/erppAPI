package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.BloqueoFolio;
import com.gisnet.erpp.domain.Predio;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the Area entity.
 */
@SuppressWarnings("unused")
public interface BloqueoFolioRepository extends JpaRepository<BloqueoFolio,Long> {

	public BloqueoFolio findByPredio(Predio predio);
    
	public BloqueoFolio findByPredioAndVersion(Predio predio,Integer version);
  
	List<BloqueoFolio> findAllByPredioIdOrderByVersionAsc(Long predio);
	
	List<BloqueoFolio> findAllByPersonaJuridicaIdOrderByVersionAsc(Long personaId);
	
	List<BloqueoFolio> findAllByMuebleIdOrderByVersionAsc(Long muebleId);
	
	List<BloqueoFolio> findAllByFolioSeccionAuxiliarIdOrderByVersionAsc(Long auxiliarId);

	List<BloqueoFolio> findAllByPredioIdAndVersionIsNotNullOrderByVersionDesc(Long predio);

	List<BloqueoFolio> findAllByMuebleIdAndVersionIsNotNullOrderByVersionDesc(Long mueble);
 
	List<BloqueoFolio> findAllByFolioSeccionAuxiliarIdAndVersionIsNotNullOrderByVersionDesc(Long auxiliar);
	
	List<BloqueoFolio> findAllByPersonaJuridicaIdAndVersionIsNotNullOrderByVersionDesc(Long juridica);
	
	@Query(value=" select max(version) from bloqueo_folio where predio_id=:predio ",
	nativeQuery=true)
	public Integer maxByPredio(@Param("predio") Long predio);
	
	@Query(value=" select max(version) from bloqueo_folio where persona_juridica_id=:pj ",
			nativeQuery=true)
			public Integer maxByPersonaJuridica(@Param("pj") Long pj);
	
	@Query(value=" select max(version) from bloqueo_folio where mueble_id=:mueble ",
			nativeQuery=true)
			public Integer maxByMueble(@Param("mueble") Long mueble);
	
	@Query(value=" select max(version) from bloqueo_folio where folio_seccion_auxiliar=:folio ",
			nativeQuery=true)
			public Integer maxByFolioSeccionAuxiliar(@Param("folio") Long folio);


}
