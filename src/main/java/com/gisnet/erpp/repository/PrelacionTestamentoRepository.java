package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.PrelacionTestamento;

import org.springframework.data.jpa.repository.*;

import java.util.List;

import java.util.Date;
import org.springframework.data.repository.query.Param;
/**
 * Spring Data JPA repository for the Recibo entity.
 */
@SuppressWarnings("unused")
public interface PrelacionTestamentoRepository extends JpaRepository<PrelacionTestamento,Long> {



}
