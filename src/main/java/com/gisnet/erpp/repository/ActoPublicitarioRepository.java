package com.gisnet.erpp.repository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.ActoPublicitario;

/**
 * Spring Data JPA repository for the Acto entity.
 */
@SuppressWarnings("unused")
public interface ActoPublicitarioRepository extends JpaRepository<ActoPublicitario,Long> {

	@Query(value="select nextval(:nameSequence)",nativeQuery=true)
	Long getNumeroActoPublicitario(@Param("nameSequence") String nameSequence);

	//select * from acto_publicitario where acto_id=92359 and numero =(select max(numero) from acto_publicitario where acto_id=92359);
	@Query(value="select ap from ActoPublicitario ap where ap.acto.id=:actoId and ap.numero =(select max(apb.numero) from ActoPublicitario apb where apb.acto.id=:actoId)")
	public ActoPublicitario findByLastNumero(@Param("actoId") Long  actoId);
	
	Optional<ActoPublicitario> findFirstByActoId(Long actoId);
	
	Optional<ActoPublicitario> findOneByNumeroAndOficinaId(Integer numero,Long oficinaId);
	
	@Query(value="select ap from ActoPublicitario ap where  ap.acto.tipoActo.id=:tipoActoId")
	public List<ActoPublicitario> findAllByTipoActoId(@Param("tipoActoId") Long  tipoActoId);
	
	@Query(value="SELECT ap FROM ActoPublicitario ap WHERE ap.numero =:numeroActoPublicitario AND ap.oficina.id=:oficinaId")
	ActoPublicitario findActoPublicitarioByOficinaIdAndNumActoPublicitario( @Param("oficinaId")Long oficinaId, @Param("numeroActoPublicitario")Integer numeroActoPublicitario);

	@Query(value="SELECT ap FROM ActoPublicitario ap WHERE ap.num_folio_real =:noFolio AND ap.oficina.id=:oficinaId")
	List<ActoPublicitario> findActoPublicitarioHis(@Param("oficinaId")Long oficinaId, @Param("noFolio")Integer noFolio);

}
