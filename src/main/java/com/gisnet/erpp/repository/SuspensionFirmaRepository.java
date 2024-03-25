package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.Status;
import com.gisnet.erpp.domain.Suspension;
import com.gisnet.erpp.domain.SuspensionFirma;

import org.springframework.data.jpa.repository.*;

import java.util.List;
import java.util.Date;

/**
* Spring Data JPA repository for the SuspensionFirma entity.
*/
@SuppressWarnings("unused")
public interface SuspensionFirmaRepository extends JpaRepository<SuspensionFirma,Long> {
    public SuspensionFirma findOneBySuspension(Suspension suspension);
    public List<SuspensionFirma> findAllBySuspensionPrelacionOficinaAndSuspensionPrelacionStatus(Oficina oficina,Status status);
    public SuspensionFirma findFirstBySuspensionIdOrderByIdDesc(Long id);
}
