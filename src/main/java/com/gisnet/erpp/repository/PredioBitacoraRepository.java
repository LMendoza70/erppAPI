package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.PredioBitacora;


public interface PredioBitacoraRepository extends JpaRepository<PredioBitacora,Long> {

	public PredioBitacora findOne(Long id);
	
	@Query(value="SELECT pb FROM PredioBitacora pb where pb.predio.id=:idPredio  order by pb.fechaAct desc")
	public List<PredioBitacora>findAllByPredioId(@Param("idPredio") Long idPredio);
	
	PredioBitacora findFirstByActoRectificacionId(Long actoId);
	
	List<PredioBitacora> findByPredioIdAndActoRectificacionIsNotNullOrderByIdDesc(Long predioId);
	
	@Query("SELECT pb FROM PredioBitacora pb WHERE pb.predio.id = :predioId ORDER BY pb.id DESC")
	List<PredioBitacora> findByPredio(@Param("predioId") Long predioId);
}
