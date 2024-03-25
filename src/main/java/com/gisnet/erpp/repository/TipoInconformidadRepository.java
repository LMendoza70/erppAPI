package com.gisnet.erpp.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.TipoInconformidad;

public interface TipoInconformidadRepository extends JpaRepository<TipoInconformidad,Long>{
  TipoInconformidad findOneByNombre(String nombre);
}
