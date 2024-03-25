package com.gisnet.erpp.repository;

import java.util.List;
import java.util.Set;

import com.gisnet.erpp.domain.ActoPredio;
import com.gisnet.erpp.domain.ActoPredioCampo;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
* Spring Data JPA repository for the ActoPredioCampo entity.
*/
@SuppressWarnings("unused")
public interface ActoPredioCampoRepository extends JpaRepository<ActoPredioCampo,Long>, ActoPredioCampoRepositoryCustom {

	@Query("select apc from ActoPredioCampo apc inner join apc.moduloCampo mc where apc.actoPredio.id = :actoPredioId and mc.modulo.id = :moduloId")
	public Set<ActoPredioCampo> findActosPredioCamposByCustomQuery(@Param("actoPredioId") Long actoPredioId, @Param("moduloId") Long moduloId);

	public abstract List<ActoPredioCampo> findByActoPredioId(Long id);
	
	@Query("select apc from ActoPredioCampo apc where apc.actoPredio.id = :actoPredioId order by apc.moduloCampo.modulo.id desc , apc.moduloCampo.campo.id asc  ")
	public abstract List<ActoPredioCampo> findByActoPredioIdOrderByModuloCampoCampoId(@Param("actoPredioId") Long id);

	public abstract List<ActoPredioCampo> findAllByActoPredio(ActoPredio actoPredio);
	public abstract List<ActoPredioCampo> findAllByActoPredioOrderById(ActoPredio actoPredio);
	
	public abstract List<ActoPredioCampo> findAllByActoPredioIdAndModuloCampoModuloId(Long actoPredioId, Long moduloId);
	
	public abstract ActoPredioCampo findAllByActoPredioIdAndModuloCampoIdAndOrden(Long actoPredioId, Long moduloId,Integer orden);

	Page<ActoPredioCampo> findAllByModuloCampoIdInOrderByActoPredioId(List<Long>moduloCampoIds, Pageable pageable);
}
