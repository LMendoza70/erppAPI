package com.gisnet.erpp.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.ServiciosCotizador;

public interface ServiciosCotizadorRepository extends JpaRepository<ServiciosCotizador,Long>{
	List<ServiciosCotizador> findAllByOrderByLeyendaTipoServicio();

} 
