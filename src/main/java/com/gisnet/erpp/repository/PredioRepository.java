package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.Colindancia;
import com.gisnet.erpp.domain.FoliosFracCond;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.Predio;

/**
 * Spring Data JPA repository for the Predio entity.
 */
@SuppressWarnings("unused")
public interface PredioRepository extends JpaRepository<Predio,Long>, PredioRepositoryCustom {

	@Query("SELECT p FROM Predio p INNER JOIN p.predioAntesParaPredios pa INNER JOIN pa.antecedente ant WHERE ant.id = :idAntecedente")
	Predio findByIdAntecedente(@Param("idAntecedente") Long idAntecedente);
	
	@Query("SELECT p FROM Predio p INNER JOIN p.predioAntesParaPredios pa INNER JOIN pa.antecedente ant WHERE ant.id = :idAntecedente")
	List<Predio> findPrediosByIdAntecedente(@Param("idAntecedente") Long idAntecedente);

	@Query("SELECT c FROM Colindancia c WHERE c.predio.id = :id order by c.id asc")
	List<Colindancia> findOneColindancias(@Param("id") Long id);

	@Query("SELECT f FROM FoliosFracCond f WHERE f.predio.id = :id")
	FoliosFracCond findOneFolioFracCond(@Param("id") Long id);
	
	Predio findByNoFolio(Integer idFolio);
	
	Predio findById(Long folioId);
	
	
	
	public List<Predio>  findDistinctByPredioRelesParaPrediosPredioSigIn(List<Predio> prediosResultado);
	
	Predio findByNoFolioAndOficinaId(Integer folio, Long oficinaId);
	Predio findByNumeroFolioRealAndOficinaIdAndAuxiliarIsNull(Integer folio, Long oficinaId);
	Predio findByNumeroFolioRealAndAuxiliarAndOficinaId(Integer folio, Integer auxiliar,Long oficinaId);
	
    @Query(value="select nextval('PREDIO_FOLIO_SEQ')", nativeQuery = true)
	Long getFolioFromPredioSequence();
    
    @Query(value="select nextval('folio_matriz_seq')", nativeQuery = true)
  	Long getFolioFromPredioMatrizSequentce();
    
    
	List<Predio> findByPrelacionPrediosParaPrediosPrelacionId(Long idPrelacion);

	@Query("SELECT p FROM Predio p WHERE p.noFolio = :noFolio")
	List<Predio> findPredioByFolio(@Param("noFolio") Integer noFolio);
	
	List<Predio> findByClaveCatastral(String clave);
	
	List<Predio> findByCuentaCatastral(String cuenta);
	
	List<Predio> findByCuentaCatastralAndMunicipioId(String cuenta, Long municipioId);

	//@Query(value="SELECT SUM(p.SUPERFICIE_M2) FROM PREDIO p INNER JOIN FOLIOS_FRAC_COND ffc on ffc.PREDIO_ID = p.ID where ffc.ACTO_ID=:actoId AND p.STATUS_ACTO_ID=1 ", nativeQuery=true)
	//Double findTotalSuperficie(@Param("actoId") Long  actoId);
	
	@Query(value="SELECT SUM(CAST(NULLIF (p.SUPERFICIE, '') AS numeric) * (CASE WHEN p.UNIDAD_MEDIDA_ID = 1 THEN 1 "
			+ " WHEN p.UNIDAD_MEDIDA_ID = 2 THEN 10000 END)) FROM PREDIO p INNER JOIN FOLIOS_FRAC_COND ffc on ffc.PREDIO_ID = p.ID "
			+ " where ffc.ACTO_ID=:actoId AND p.STATUS_ACTO_ID=2 ", nativeQuery=true)
	Double findTotalSuperficie(@Param("actoId") Long  actoId);

	//@Query(value="SELECT COUNT(c.nombre) FROM PREDIO p INNER JOIN FOLIOS_FRAC_COND ffc on ffc.PREDIO_ID = p.ID INNER JOIN COLINDANCIA c on c.PREDIO_ID = p.ID where ffc.ACTO_ID=:actoId AND p.STATUS_ACTO_ID=3  and c.ORIENTACION_ID = 16", nativeQuery=true)
	//Double findTotalIndivisos(@Param("actoId") Long  actoId);
	
	@Query(value="SELECT SUM(CAST(NULLIF (c.nombre, '') AS numeric)) FROM PREDIO p INNER JOIN FOLIOS_FRAC_COND ffc on ffc.PREDIO_ID = p.ID INNER JOIN COLINDANCIA c on c.PREDIO_ID = p.ID where ffc.ACTO_ID=:actoId AND p.STATUS_ACTO_ID=2  and c.ORIENTACION_ID = 16", nativeQuery=true)
	Double findTotalIndivisos(@Param("actoId") Long  actoId);

	@Query("SELECT p FROM Predio p WHERE p.procedeDeFolio = :noFolio")
	List<Predio> findPredioByProcedeDeFolio(@Param("noFolio") String noFolio);
	
	@Query("SELECT p FROM Predio p WHERE p.numeroFolioReal = :idFolioReal and p.auxiliar = :auxiliar and p.oficina =:oficina")
	Predio findByNoFolioRealAndAuxiliarAndOficina(@Param("idFolioReal") Integer idFolioReal,@Param("auxiliar") Integer auxiliar,@Param("oficina") Oficina oficina);
	
	@Query("SELECT p FROM Predio p WHERE p.numeroFolioReal = :idFolioReal AND p.oficina =:oficina")
	Predio findByNoFolioRealAndOficina(@Param("idFolioReal") Integer idFolioReal, @Param("oficina") Oficina oficina);

	@Query("SELECT p FROM Predio p WHERE p.noFolio = :idFolio AND p.oficina =:oficina")
	Predio findByNoFolioAndOficina(@Param("idFolio") Integer idFolio,@Param("oficina") Oficina oficina);
}
