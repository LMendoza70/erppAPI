package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.BitacoraDig;

public interface BitacoraDigRepository extends JpaRepository<BitacoraDig,Long> {

	List<BitacoraDig> findByLibroIdAndDocumentoIn(Long libroId,List<String> documentos);
}
