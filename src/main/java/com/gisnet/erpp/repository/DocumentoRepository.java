package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.Documento;

import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Set;

/**
 * Spring Data JPA repository for the Documento entity.
 */
@SuppressWarnings("unused")
public interface DocumentoRepository extends JpaRepository<Documento,Long> {
	
	List <Documento> findAllByNotarioId(Long id);
	
	@Query(value="	select "
			+ "		distinct documento.* "
			+ "	from acto_documento "
			+ "    	inner join ("
			+ "        	select "
			+ "            	documento.id "
			+ "        	from acto "
			+ "            	inner join tipo_doc_tipo_acto on tipo_doc_tipo_acto.tipo_acto_id = acto.tipo_acto_id "
			+ "            	inner join documento on documento.tipo_documento_id  = tipo_doc_tipo_acto.tipo_documento_id "
			+ "            	inner join ("
			+ "                	select "
			+ "                    	documento.id "
			+ "                	from documento "
			+ "                    	inner join acto_documento acto_documento on acto_documento.documento_id = documento.id "
			+ "                    	inner join acto on acto.id = acto_documento.acto_id "
			+ "                    	inner join ("
			+ "                        	select "
			+ "                            	prelacion.id "
			+ "                        	from prelacion "
			+ "                            	inner join acto on acto.prelacion_id = prelacion.id where acto.id = :actoId and (acto.clon is null 	or acto.clon = false)"
			+ "                    	) id_prelacion on id_prelacion.id = acto.prelacion_id "
			+ "            	)documentos_prelacion on documentos_prelacion.id = documento.id "
			+ "        	where acto.id = :actoId and (acto.clon is null 	or acto.clon = false) "
			+ "    	)id_documentos on id_documentos.id = acto_documento.documento_id "
			+ "    	inner join ( "
			+ " 		select "
			+ " 			acto_documento.acto_id, "
            + " 			max(acto_documento.version) as version " 
            + " 		from acto_documento "
            + " 		group  by acto_documento.acto_id "
            + " 	)nfiltro on nfiltro.version = acto_documento.version and nfiltro.acto_id = acto_documento.acto_id "
            + " 	inner join documento on documento.id = id_documentos.id",  
	nativeQuery=true)
	public Set<Documento> getAllActoAssignableDocuments(@Param("actoId") Long id);
	
	public Documento findFirstByArchivoIdAndActoDocumentosParaDocumentosActoVfOrderByIdDesc(Long archivoId, Boolean vf);
	
}
