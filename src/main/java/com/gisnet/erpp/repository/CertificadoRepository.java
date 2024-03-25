package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Certificado;


import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import java.util.List;
import java.util.Optional;

/**
 * Spring Data JPA repository for the Certificado entity.
 */
@SuppressWarnings("unused")
public interface CertificadoRepository extends JpaRepository<Certificado,Long> {
	Optional<Certificado> findOneBySecuencia(Integer secuencia);


	@Query(value=" SELECT c.* FROM certificado c join prelacion_servicio ps on c.prelacion_servicio_id=ps.id  join prelacion p  on ps.prelacion_id=p.id    where p.id= :prelacionId  ", nativeQuery=true)
	List<Certificado> certificadoByPrelacionId(@Param("prelacionId") Long prelacionId );
	
	List<Certificado> findByPrelacionServicioId( Long prelacionServId );

}
