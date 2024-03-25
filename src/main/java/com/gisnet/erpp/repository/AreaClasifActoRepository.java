package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.AreaClasifActo;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * Spring Data JPA repository for the AreaClasifActo entity.
 */
@SuppressWarnings("unused")
public interface AreaClasifActoRepository extends JpaRepository<AreaClasifActo,Long> {
        
	List<AreaClasifActo> findAllByClasifActoId(Long id);
    List<AreaClasifActo> findAllByAreaId(Long id);
    
    @Query(value="SELECT p FROM AreaClasifActo p where p.area.id in(:ids) ")
    List<AreaClasifActo> findAllByAreaIds(@Param("ids")List<Long> ids);
    
       
}
