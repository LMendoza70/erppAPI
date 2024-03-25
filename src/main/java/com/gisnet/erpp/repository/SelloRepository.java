package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.Sello;

public interface SelloRepository  extends JpaRepository<Sello,Long>{

	List<Sello> findBylibroIdAndDocumento(Long libroId,String documento);
}
