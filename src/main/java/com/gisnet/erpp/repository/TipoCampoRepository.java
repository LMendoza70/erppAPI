package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.TipoCampo;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the TipoCampo entity.
 */
@SuppressWarnings("unused")
public interface TipoCampoRepository extends JpaRepository<TipoCampo,Long> {

}
