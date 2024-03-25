package com.gisnet.erpp.repository;

import java.util.List;
import java.util.Set;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.Acto;
import com.gisnet.erpp.domain.ActoDocumento;
import com.gisnet.erpp.domain.Prelacion;

/**
 * Spring Data JPA repository for the ActoDocumento entity.
 */
@SuppressWarnings("unused")
public interface ActoDocumentoRepository extends JpaRepository<ActoDocumento,Long> {

	public List<ActoDocumento> getAllByActoId(Long actoId);
	
	@Query(value="select ad.* FROM DOCUMENTO d "+
	" INNER JOIN ACTO_DOCUMENTO ad on d.id=ad.documento_id "+
	" where ad.VERSION =(select max(version) as v from  ACTO_DOCUMENTO WHERE ACTO_ID=:actoId) "+
	" and ad.ACTO_ID=:actoId",  
		nativeQuery=true
	)
	public List<ActoDocumento> getAllActoDocumentoByActoId(@Param("actoId") Long id);

	
	@Query(value="with docs as ( " +
			" select doc.id from acto ac inner join tipo_doc_tipo_acto tdoac on tdoac.tipo_acto_id = ac.tipo_acto_id " +
			" inner join documento doc on doc.tipo_documento_id  = tdoac.tipo_documento_id " +
			" where ac.id =:actoId and doc.id in ( " +
					" select doc.id from documento doc " +
					" inner join acto_documento adoc on adoc.documento_id = doc.id " +
					" inner join acto ac on ac.id = adoc.acto_id " +
					" where ac.prelacion_id = (select pre.id from prelacion pre inner join acto ac2 on ac2.prelacion_id = pre.id where ac2.id = :actoId)) " +
			" ) select adoc.* from acto_documento adoc inner join docs on docs.id = adoc.documento_id " + 
				" inner join (select acdoc.acto_id, max(acdoc.version) as version from acto_documento acdoc inner join docs on docs.id = acdoc.documento_id " +
	            " group  by acto_id) nfiltro on nfiltro.acto_id = adoc.acto_id and nfiltro.version = adoc.version ",  
			nativeQuery=true
		)
		public Set<ActoDocumento> getAllActoAssignableDocuments(@Param("actoId") Long id);

	@Query(value=" select ac.* from ACTO_DOCUMENTO ac  join  DOCUMENTO d on d.ID=ac.DOCUMENTO_ID "+
				" join ARCHIVO a on a.ID=d.ARCHIVO_ID  where  a.ID=:archivoId",  
		nativeQuery=true
	)
	public ActoDocumento getByArchivo(@Param("archivoId") Long archivoId);
	
	
	 @Query( value=" select ac.* from acto_documento ac, acto a, "+
			 	   " ( select ac.ACTO_ID, max(ac.VERSION) as version from acto_documento ac  group by ac.ACTO_ID ) q1 "+
			       " where ac.ACTO_ID = q1.ACTO_ID "+ 
			       " and ac.VERSION = q1.VERSION " + 
			       " and ac.ACTO_ID = a.id " +
			       " and  (a.vf is null or a.vf = false) " +
			       " and a.PRELACION_ID = :id", nativeQuery=true)
	 public List<ActoDocumento> getAllActoDocumentoByActoPrelacionId(@Param("id") Long id);
	 
	 @Query(value="SELECT ad FROM ActoDocumento ad "+
		 " where ad.acto.prelacion.id = :prelacionId "+
		 " and ad.acto.motivo.id is not null")
	 public List<ActoDocumento> getAllDocumentoRechazoByActoPrelacionId(@Param("prelacionId") Long id);
	 
	 @Query(value="SELECT a FROM ActoDocumento a where a.documento.id in(:ids) ")
	 public List<ActoDocumento> getAllByDocumentoId(@Param("ids") List<Long> id);
	 
	 @Query(value="SELECT a FROM ActoDocumento a where a.acto in(:actos) ")
	 public List<ActoDocumento> getAllByActo(@Param("actos") List <Acto> acto);
	 
	 public Long deleteByActoIdAndVersionNot(Long actoId,Integer version);

	 @Query(value="SELECT ad.* FROM ACTO_DOCUMENTO  ad where ad.ACTO_ID in(:listActo2) AND ad.VERSION = (SELECT MAX(ad.VERSION) FROM ACTO_DOCUMENTO ad WHERE ad.ACTO_ID in(:listActo2))",nativeQuery=true) 
	 public List<ActoDocumento> findAllByListActo(@Param("listActo2")List<Acto> listActo2);
	 
	 // el mejorcito en su momento
   /* @Query(value="SELECT ad FROM ActoDocumento ad where ad.acto in(:listActo2)") 
	 public List<ActoDocumento> findAllByListActo(@Param("listActo2")List<Acto> listActo2);*/
		 
	 @Query(value="SELECT ad.acto FROM ActoDocumento ad where ad.acto.prelacion.id =:prelacionId") 
	 public List<Acto> findActosByPrelacionId(@Param("prelacionId") Long  prelacionId);
	 
	 @Query(value="SELECT ad.acto FROM ActoDocumento ad where ad.documento.id =:idDocumento") 
	 public Acto  findActoByIdDocument(@Param("idDocumento") Long  idDocumento);
	 
	 @Query(value="SELECT ad.* FROM ACTO_DOCUMENTO ad where ad.ACTO_ID =:actoId AND ad.DOCUMENTO_ID=(SELECT MAX(ad.DOCUMENTO_ID) FROM ACTO_DOCUMENTO ad WHERE ad.ACTO_ID=:actoId) AND ad.version=(SELECT MAX(ad.version) FROM ACTO_DOCUMENTO ad WHERE ad.ACTO_ID=:actoId) ", nativeQuery=true)
	 public ActoDocumento findActoDocumentoByActoId(@Param("actoId") Long  actoId);

	 public ActoDocumento  findTop1ActoDocumentoByActoIdOrderByIdDesc(Long  actoId);

	 public Optional<ActoDocumento> findFirstByActoIdOrderByVersionDesc(Long ActoId);
	 public Optional<ActoDocumento> findFirstByActoIdOrderByIdDesc(Long ActoId);
	 
	 @Query(value=" select ac.* from ACTO_DOCUMENTO ac  join  DOCUMENTO d on d.ID=ac.DOCUMENTO_ID "+
				" join ARCHIVO a on a.ID=d.ARCHIVO_ID  where  a.ID=:archivoId",  
		nativeQuery=true
	)
	public List<ActoDocumento> getByAllArchivo(@Param("archivoId") Long archivoId);
}
