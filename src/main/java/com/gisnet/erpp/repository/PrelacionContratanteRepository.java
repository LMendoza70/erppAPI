package com.gisnet.erpp.repository;

import java.util.List;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Predio;
import com.gisnet.erpp.domain.Prelacion;
import com.gisnet.erpp.domain.PrelacionContratante;

public interface PrelacionContratanteRepository 
	extends JpaRepository<PrelacionContratante, Long>, PrelacionContratanteRepositoryCustom {

	public PrelacionContratante findByPrelacion(Prelacion prelacion);

	public PrelacionContratante findByActoAndOrden(Acto acto, Integer orden);

	public List<PrelacionContratante> findAllByPrelacionAndPredioOrderByOrdenAsc(Prelacion prelacion,Predio predio);
	
	public List<PrelacionContratante> findAllByPrelacionAndActoInOrderByOrdenAsc(Prelacion prelacion,Set<Acto> actos);

	public List<PrelacionContratante> findAllByPredioOrderByOrdenAsc(Predio predio);
	
	public Long deleteByActoId(long actoId);
	
	@Query(value="SELECT pc FROM PrelacionContratante pc where pc.nombre =:nombre AND pc.paterno=:paterno AND pc.materno=:materno") 
	public PrelacionContratante findByNombreAndApaternoAndAmaterno(@Param("nombre") String nombre, @Param("paterno") String paterno, @Param("materno") String materno);

}
