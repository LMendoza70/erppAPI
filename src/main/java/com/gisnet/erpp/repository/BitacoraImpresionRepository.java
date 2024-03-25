package com.gisnet.erpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.Bitacora;
import com.gisnet.erpp.domain.BitacoraImpresion;
import com.gisnet.erpp.domain.Usuario;

@SuppressWarnings("unused")
public interface BitacoraImpresionRepository extends JpaRepository<BitacoraImpresion,Long> {
	
	@Query(value="SELECT BI.* FROM BITACORA_IMPRESION BI WHERE BI.PRELACION_ID =:prelacionId AND BI.USUARIO_LOGEADO_ID = :usuario AND BI.VERSION=(SELECT MAX(BI.VERSION) FROM BITACORA_IMPRESION BI WHERE BI.PRELACION_ID =:prelacionId AND BI.USUARIO_LOGEADO_ID = :usuario )",nativeQuery=true) 
	BitacoraImpresion findBitacoraImpresionAndUserLogged(@Param("prelacionId")Long prelacionId, @Param("usuario") Usuario usuario);
	
	@Query(value="SELECT BI.* FROM BITACORA_IMPRESION BI WHERE BI.PRELACION_ID =:prelacionId AND BI.VERSION=(SELECT MAX(BI.VERSION) FROM BITACORA_IMPRESION BI WHERE BI.PRELACION_ID =:prelacionId)",nativeQuery=true) 
	BitacoraImpresion findBitacoraByPrelacionAndVersion(@Param("prelacionId")Long prelacionId);
}
