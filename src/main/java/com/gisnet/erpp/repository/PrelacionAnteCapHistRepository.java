package com.gisnet.erpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.PrelacionAnteCapHist;


/**
 * Spring Data JPA repository for the Antecedente entity.
 */
@SuppressWarnings("unused")
public interface PrelacionAnteCapHistRepository extends JpaRepository<PrelacionAnteCapHist,Long> , PrelacionAnteCapHistRepositoryCustom{
	
	public abstract PrelacionAnteCapHist findOneByLibroIdAndPrelacionIdAndDocumento(Long libroId, Long prelacionId, String doc);
	public PrelacionAnteCapHist findFirstByLibroIdAndDocumentoAndDocumentoBisOrderByNumeroPredioDesc(Long libroId,String documento,String documentoBis);
	@Modifying
	@Query(value="update prel_ante_cap_hist  set usuario_validacion_id=:id_usuario where  carga_trabajo = :carga_trabajo",nativeQuery=true)
	public  void updateValidador(@Param("id_usuario") Long idUsuario, @Param("carga_trabajo") String cargaTrabajo);
	public PrelacionAnteCapHist findByPredioIdAndTipoFolioId(Long predioId,Long tipoFolioId);

}
