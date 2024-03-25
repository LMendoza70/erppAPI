package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Suspension;
import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.domain.ConcPagoTipoActo;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.Status;
import com.gisnet.erpp.domain.Oficina;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;
import java.util.List;

/**
 * Spring Data JPA repository for the Bitacora entity.
 */
//JADV-SUSPENSION
@SuppressWarnings("unused")
public interface SuspensionRepository extends JpaRepository<Suspension,Long> {
	@Query (value="select s.* from suspension s "+
	"join prelacion p on s.prelacion_id=p.id "+
	"where s.estatus_suspencion=:estatusSuspension and p.oficina_id=:oficinaId", nativeQuery=true)	
	public List<Suspension> findAllByestatusSuspensionAndOficinaId(@Param("estatusSuspension") Long estatusSuspension,@Param("oficinaId") Long oficinaId);
	public List<Suspension> findAllByestatusSuspension(Long estatusSuspension);
	public List<Suspension> findAllByestatusSuspensionAndPrelacion(Long estatusSuspension, Prelacion prelacion);
	public Suspension findFirstByPrelacionIdAndEstatusSuspensionOrderByIdDesc(Long estatusSuspension, Prelacion prelacion);
	public Suspension findOneByestatusSuspensionAndPrelacion(Long estatusSuspension, Prelacion prelacion);
	public Suspension findOneAllByPrelacionOrderByIdDesc(Prelacion prelacion);
	public Suspension findFirstByPrelacionIdOrderByIdDesc(Long prelacionId);
	public List<Suspension> findOneAllByfechaSuspensionAndPrelacionOficina(Date fechaSuspension,Oficina oficina);
	public Suspension findById(Long idSuspension);
}

