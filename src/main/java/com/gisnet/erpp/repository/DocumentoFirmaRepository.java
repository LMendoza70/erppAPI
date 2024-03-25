package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.DocumentoFirma;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
* Spring Data JPA repository for the DocumentoFirma entity.
*/
@SuppressWarnings("unused")
public interface DocumentoFirmaRepository extends JpaRepository<DocumentoFirma,Long> {

}
