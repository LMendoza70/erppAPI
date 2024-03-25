package com.gisnet.erpp.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.Libro;

/**
 * Spring Data JPA repository for the Libro entity.
 */
@SuppressWarnings("unused")
public interface LibroRepository extends JpaRepository<Libro,Long>, LibroRepositoryCustom{
	
	public Libro findById(Long id);

    public Libro findByNumLibro(String nombre);
    
    public Libro findOneByNumLibro(Integer numLibro);
            
    public List<Libro> findAllByNumLibroLike(String numLibro);

    public Libro findByNombre(String nombreLibro);
    
    public Optional<Libro> findOneByNumLibroAndLibroBisAndSeccionesPorOficinaIdAndAnioAndActivoAndVolumen(String numeroLibro, String libroBis,Long seccionPorOficinaId, Integer anio, Boolean isActivo, String volumen);
    	
	public Libro findByNombreAndLibroBis(String nombreLibro,String tipo);
	


}
