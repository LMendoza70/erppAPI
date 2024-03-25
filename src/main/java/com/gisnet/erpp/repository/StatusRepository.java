package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Status;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@SuppressWarnings("unused")
@Repository
public interface StatusRepository extends JpaRepository<Status,Long>, StatusRepositoryCustom {

	@Query(value="SELECT s.* from STATUS s where id in (:ids) order by s.nombre", nativeQuery=true)
	List<Status> findAllUsable(@Param("ids") Integer[]ids);
	
	List<Status> findAllByIdIn(List<Long> ids);


}
