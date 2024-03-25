package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Status;
import com.gisnet.erpp.domain.StatusVis;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface StatusVisRepository extends JpaRepository<StatusVis,Long> {

	List<StatusVis> findStatusIdByStatusExternoId(Long id);
	
}
