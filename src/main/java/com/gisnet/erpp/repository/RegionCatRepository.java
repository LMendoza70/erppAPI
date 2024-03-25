package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.RegionCat;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the RegionCat entity.
 */
@SuppressWarnings("unused")
public interface RegionCatRepository extends JpaRepository<RegionCat,Long> {

}
