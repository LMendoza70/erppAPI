package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.PrelacionAnte;
import com.gisnet.erpp.domain.Antecedente;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the PrelacionAnte entity.
 */
@SuppressWarnings("unused")
public interface PrelacionAnteRepository extends JpaRepository<PrelacionAnte,Long> {

       List<PrelacionAnte>  findByPrelacionId(Long id);
       
       List<PrelacionAnte>  findByLibroAndLibroBisAndSeccionIdAndOficinaIdAndAnioAndVolumenAndDocumentoAndTipoFolioIdAndPrelacionStatusIdInAndPrelacionUsuarioCapValIsNotNullAndPrelacionConsecutivoLessThanAndPrelacionAnio(String libro,
    		   String libroBis,Long seccionId,Long oficinaId,Integer anio,String volumen,String documento,Long tipoFolioId,List <Long> status,Integer consecutivo,Integer pAnio); 
       
       List<PrelacionAnte>  findByLibroAndLibroBisAndSeccionIdAndOficinaIdAndAnioAndVolumenAndDocumentoAndTipoFolioIdAndPrelacionStatusIdInAndPrelacionUsuarioCapValIsNotNullAndPrelacionAnioLessThan(String libro,
    		   String libroBis,Long seccionId,Long oficinaId,Integer anio,String volumen,String documento,Long tipoFolioId,List <Long> status,Integer pAnio); 

     //  PrelacionAnte findByPrelacionIdAndAntecedenteId(long prelacionId, long anteId);
     List<PrelacionAnte>  findByPredioId(Long id);
}
