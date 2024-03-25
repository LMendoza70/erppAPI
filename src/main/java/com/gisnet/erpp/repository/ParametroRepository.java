package com.gisnet.erpp.repository;

import java.util.List;

import javax.persistence.QueryHint;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.Parametro;

/**
 * Spring Data JPA repository for the Parametro entity.
 */
@SuppressWarnings("unused")
public interface ParametroRepository extends JpaRepository<Parametro,Long> {
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true")})
	List<Parametro> findAll();
	
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true")})
	Parametro findByCve(String cve);
	
	@Query(value= "SELECT P FROM Parametro P WHERE REPLACE(P.cve,' ','' )=:cve")
	@QueryHints({ @QueryHint(name = "org.hibernate.cacheable", value ="true")})
    List<Parametro> findParametroBycve( @Param("cve") String cve);
}
