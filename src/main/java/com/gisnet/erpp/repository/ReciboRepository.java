package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ActoFolioRecibo;
import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.Recibo;
import com.gisnet.erpp.domain.ReciboConcepto;
import com.gisnet.erpp.vo.ReciboVO;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

/**
 * Spring Data JPA repository for the Recibo entity.
 */
@SuppressWarnings("unused")
public interface ReciboRepository extends JpaRepository<Recibo,Long> {

    List<Recibo> findByPrelacionId(Long id);
    
	List<Recibo> findByCuenta( String cuenta );
	List<Recibo> findByReferenciaAndPrelacionConsecutivoIsNotNull( String referencia );
    
	public abstract List<Recibo> findByActoFolioRecibosParaRecibosActoPredioActoIdAndActoFolioRecibosParaRecibosActoPredioVersion(Long actoId, Integer version);
	
	@Query(value="select r from Recibo r join r.actoFolioRecibosParaRecibos raf join raf.actoPredio ap join ap.acto a where a.prelacion.id= :prelacionId and ap.version= :version and r.id not in "+
				"(select r.id from Recibo r join r.actoFolioRecibosParaRecibos raf join raf.actoPredio ap where ap.acto.id= :actoId and ap.version= :currentVersion)")
	public abstract List<Recibo> findDisponiblesByActo(@Param("prelacionId") Long prelacionId, @Param("version") Integer version, @Param("actoId") Long actoId, @Param("currentVersion") Integer currentVersion);

	@Query(value="SELECT r FROM Recibo r where r.prelacion.id =:prelacionId") 
    public  List<Recibo> findByPrelacionIds(@Param("prelacionId") Long prelacionId);
	
    @Query(value="SELECT r FROM Recibo r where r in(:listActFolioRecibo)") 
    public  List <Recibo> findRecibosByListActFolRecibo(@Param("listActFolioRecibo")List<ActoFolioRecibo> listActFolioRecibo);
    
    @Query(value="SELECT AFR.recibo FROM ActoFolioRecibo AFR where AFR.actoPredio in(:listActPre)")
	public List<Recibo> findAllByListActoPredio(@Param("listActPre")List<ActoPredio> listActPre);
}
