package com.gisnet.erpp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.Antecedente;
import com.gisnet.erpp.domain.Libro;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.Seccion;
import com.gisnet.erpp.vo.AntecedenteVO;

/**
 * Spring Data JPA repository for the Antecedente entity.
 */
@SuppressWarnings("unused")
public interface AntecedenteRepository extends JpaRepository<Antecedente,Long> {

	@Query(value="SELECT A.* FROM ANTECEDENTE A INNER JOIN PJ_ANTE pa ON (a.ID = pa.ANTECEDENTE_ID) INNER JOIN PERSONA_JURIDICA pj ON (pa.PERSONA_JURIDICA_ID = pj.ID) WHERE pj.id = :idPersonaJuridica", nativeQuery=true)
	Antecedente findAntecedenteByPersonaJuridicaId(@Param("idPersonaJuridica") Long idPersonaJuridica);

	@Query(value="SELECT A.* FROM ANTECEDENTE A INNER JOIN AUXILIAR_ANTE aa ON (a.ID = aa.ANTECEDENTE_ID) INNER JOIN FOLIO_SECCION_AUXILIAR fsa ON (aa.FOLIO_SECCION_AUXILIAR_ID = fsa.ID) WHERE fsa.id = :idFolioSeccionAuxiliar", nativeQuery=true)
	Antecedente findAntecedenteByFolioSeccionAuxiliarId(@Param("idFolioSeccionAuxiliar") Long idFolioSeccionAuxiliar);

	@Query(value="SELECT A.* FROM ANTECEDENTE A INNER JOIN MUEBLE_ANTE ma ON (a.ID = ma.ANTECEDENTE_ID) INNER JOIN MUEBLE mu ON (ma.MUEBLE_ID = mu.ID) WHERE mu.id = :idMueble", nativeQuery=true)
	Antecedente findAntecedenteByMuebleId(@Param("idMueble") Long idMueble);

	@Query(value="SELECT A.* FROM ANTECEDENTE A INNER JOIN PREDIO_ANTE pa ON (a.ID = pa.ANTECEDENTE_ID) INNER JOIN predio pre ON (pa.PREDIO_ID = pre.ID) WHERE pre.id = :idPredio limit 1 ", nativeQuery=true)
	Antecedente findAntecedenteByPredioId(@Param("idPredio") Long idPredio);

/*
	@Query("SELECT a FROM Antecedente a INNER JOIN a.prelacionAntesParaAntecedentes pa INNER JOIN pa.prelacion pre WHERE pre.id = :idPrelacion")
	Antecedente findByIdPrelacion(@Param("idPrelacion") Long idPrelacion);
*/



	@Query(value=" select a.* from antecedente a "+
					"join  libro  l on l.id=a.libro_id " +
					"join  libro_bis lb on lb.id =a.libro_bis_id "+
					"join  seccion s on s.id=a.seccion_id "+
					"join  oficina o on  o.id=a.oficina_id "+
					"where a.documento=:doc and a.documento_bis=:docbis and  l.num_libro=:libro and lb.nombre=:librobis and s.nombre = :seccion and o.num_oficina=:oficina ",
		nativeQuery=true
	)
	public Antecedente findByDatosAntecedente(@Param("doc") String doc ,@Param("docbis") String docbis , @Param("libro") Integer libro, @Param("librobis") String librobis, @Param("seccion") String seccion, @Param("oficina") String oficina);
	
	public abstract Optional<Antecedente> findOneBylibroAndDocumentoAndDocumentoBis(Libro libro, String documento, String documentoBis);

    /*
	public Antecedente findBylibroAndSeccionAndOficina(Libro libro, Seccion seccion, Oficina oficina );

	public Antecedente findBylibroAndSeccionAndOficinaAndLibroBis(Libro libro, Seccion seccion, Oficina oficina, LibroBis libroBis );

	public Antecedente findBylibroAndSeccionAndOficinaAndDocumento(Libro libro, Seccion seccion, Oficina oficina, String documento);

	public Antecedente findBylibroAndSeccionAndOficinaAndDocumentoBis(Libro libro, Seccion seccion, Oficina oficina, String documentoBis);

	public Antecedente findBylibroAndSeccionAndOficinaAndLibroBisAndDocumento(Libro libro, Seccion seccion, Oficina oficina, LibroBis libroBis, String documento);

	public Antecedente findBylibroAndSeccionAndOficinaAndLibroBisAndDocumentoBis(Libro libro, Seccion seccion, Oficina oficina, LibroBis libroBis, String documentoBis);

	public Antecedente findBylibroAndSeccionAndOficinaAndDocumentoAndDocumentoBis(Libro libro, Seccion seccion, Oficina oficina, String documento, String documentoBis);

	public Antecedente findByLibroAndLibroBisAndSeccionAndOficinaAndDocumentoAndDocumentoBis(Libro libro,LibroBis libroBis,Seccion seccion , Oficina oficina, String documento, String documentoBis);
	*/

	//public List<Antecedente> findByPrelacionAntesParaAntecedentesPrelacionId(Long idPrelacion);
	@Query(value="SELECT a from Antecedente a WHERE a.libro.id =:idLib and a.documento =:documento " )
	List<Antecedente> findAntecedenteByLibroIdAndDocumento(@Param("idLib") Long idLibro,@Param("documento") String documento);
	
	@Query(value="SELECT a from Antecedente a where a.libro.id in "
			+ "(SELECT id FROM Libro l WHERE anio=:anio AND seccionesPorOficina.id in "
			+ "(SELECT id FROM SeccionPorOficina so WHERE oficina.id=:oficinaId AND seccion.id=:seccionId))and documento=:documentoId")
	List<Antecedente> findAntecedenteAmpliaByOficinaAndSeccionAndAnioAndLibro(@Param("oficinaId") Long oficinaId,@Param("seccionId") Long seccionId, @Param("anio") Integer anio,@Param("documentoId") String documentoId );
	@Query(value="SELECT A.* FROM ANTECEDENTE A INNER JOIN LIBRO L ON (A.libro_id = L.ID) WHERE L.id = :idLibro AND A.documento = :documento", nativeQuery=true)
	Antecedente findAntecedenteByLibroId(@Param("idLibro") Long idLibro, @Param("documento") String documento);
}
