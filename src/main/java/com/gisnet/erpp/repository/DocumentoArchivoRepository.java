package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.ActoAnte;
import com.gisnet.erpp.domain.DocumentoArchivo;

@SuppressWarnings("unused")
public interface DocumentoArchivoRepository extends JpaRepository<DocumentoArchivo,Long> {
   
	List<DocumentoArchivo> findByDocumentoId(Long documentoId);
	DocumentoArchivo findFirstByDocumentoIdOrderByIdDesc(Long documentoId);//CV
	DocumentoArchivo findFirstByArchivoIdOrderByIdDesc(Long archivoId);
}
