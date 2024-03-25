package com.gisnet.erpp.repository;


import com.gisnet.erpp.domain.BusquedaResultado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.HashSet;
import java.util.List;

@SuppressWarnings("unused")
public interface BusquedaResultadoRepository extends JpaRepository<BusquedaResultado, Long> {

    HashSet<BusquedaResultado> findAllByPrelacionId (Long prelacionId);
    public BusquedaResultado findOneById(Long id);
    public BusquedaResultado findOneByPrelacionId (Long prelacionId);
    public BusquedaResultado findFirstByPrelacionIdOrderByIdDesc(Long prelacionId);
	
    @Query(value="SELECT br FROM BusquedaResultado br where br.prelacion.id=:prelacionId AND br.prelacionHistorica.id=:prelacionHistoricaId")
    public List<BusquedaResultado> findAllBusquedResultadoByPrelacionIdAndPrelacionHistoricaId(@Param("prelacionId") Long prelacionId,@Param("prelacionHistoricaId") Long prelacionHistoricaId);
    
    @Query(value="SELECT br FROM BusquedaResultado br where br.prelacion.id=:prelacionId AND br.prelacionHistorica.id=:prelacionHistoricaId AND br.actoPublicitario.id=:apId")
    public List<BusquedaResultado> findAllByPrelacionIdAndPrelacionHistoricaIdAndActoPublicitarioId(@Param("prelacionId") Long prelacionId,@Param("prelacionHistoricaId") Long prelacionHistoricaId,@Param("apId") Long apId);

    @Query(value="SELECT br FROM BusquedaResultado br where br.prelacion.id=:prelacionId")
    public List<BusquedaResultado> findAllBusquedResultadoByPrelacionId(@Param("prelacionId") Long prelacionId);
    
    public List<BusquedaResultado> findAllByPrelacionIdAndPredioId(Long prelacionId,Long predioId);

    public List<BusquedaResultado> findAllByPrelacionIdAndPersonaJuridicaId(Long prelacionId,Long pjId);
}
