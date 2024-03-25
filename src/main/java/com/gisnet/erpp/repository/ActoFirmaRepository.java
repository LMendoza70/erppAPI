package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ActoFirma;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ActoFirma entity.
 */
@SuppressWarnings("unused")
public interface ActoFirmaRepository extends JpaRepository<ActoFirma,Long> {
    public List<ActoFirma> findAllByActoIdAndEsActo(Long id, boolean esActo);

    public ActoFirma findByActoIdAndEsActo(Long id, boolean esActo);
    
    public List<ActoFirma> findAllByActoIdAndEsActoOrderByIdDesc(Long id, boolean esActo);
    
    Long deleteByActoId(long actoId);
}
