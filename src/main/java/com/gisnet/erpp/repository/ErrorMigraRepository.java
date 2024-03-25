package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.ErrorMigra;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the ErrorMigra entity.
 */
@SuppressWarnings("unused")
public interface ErrorMigraRepository extends JpaRepository<ErrorMigra,Long> {

}
