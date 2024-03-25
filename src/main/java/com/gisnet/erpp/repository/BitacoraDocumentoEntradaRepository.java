package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.gisnet.erpp.domain.BitacoraDocumentoEntrada;

public interface BitacoraDocumentoEntradaRepository  extends JpaRepository<BitacoraDocumentoEntrada,Long> {

    List<BitacoraDocumentoEntrada> findAllByPrelacionId(Long prelacionId);

}