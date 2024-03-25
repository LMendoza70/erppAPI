package com.gisnet.erpp.repository;

import com.gisnet.erpp.domain.*;
import com.gisnet.erpp.vo.LibroVO;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LibroRepositoryCustom {
	//public abstract Libro findOneByArgumentos(Antecedente ante,Integer libro, String librobis, SeccionesPorOficina seccionesporOficina);

	public abstract Libro findLibroByAntePredio(String doc, String docBis, Integer anio, String volumen, String libro,
												String librobis, Long seccionId, Long oficinaId);

	public abstract PredioAnte findOnePredioAnteByAnteLibro(String numero, String documentoBis, String documento, Integer anio, String volumen,
    		String libro, String librobis, 
			Long seccionId, Long oficinaId);
	public abstract List<PredioAnte> findAllPredioAnteByAnteLibro(String numero, String documentoBis, String documento, Integer anio, String volumen,
    		String libro, String librobis, 
    		Long seccionId, Long oficinaId);
	public abstract AuxiliarAnte findAuxiliarAnteByAnteLibro(String numero, String documentoBis, String documento, Integer anio, 
    		String libro, String librobis, 
    		Long seccionId, Long oficinaId);
	public abstract MuebleAnte findMuebleAnteByAnteLibro(String numero, String documentoBis, String documento,  Integer anio, 
    		String libro, String librobis, 
    		Long seccionId, Long oficinaId);
	public abstract PjAnte findPjAnteByAnteLibro(String numero, String documentoBis, String documento,  Integer anio, 
    		String libro, String librobis, 
    		Long seccionId, Long oficinaId);
	
    public abstract Libro findLibroBy(String numLibro, String libroBis, Long seccionId, Long oficinaId, Integer anio, String volumen);
    
    public abstract List<Libro> findLibrosBy(String numLibro, String libroBis, Long seccionId, Long oficinaId, Integer anio, String volumen);
	//public abstract Libro findLibroByAmplia(Long oficinaId,Long seccionId,Integer anio,Integer documento);
	public abstract List<Libro> findBy(String libro, String libroBis, Long seccionPorOficinaId);
	
	public Page<Libro> findAllPageable(String numLibro, String libroBis, String oficinaId, String seccionId, String tipoDoc, Pageable pageable);
		
	public Libro findLibroUploadFile(LibroVO libroVO);
	
    public Libro findLibroUploadFileValiation(LibroVO libroVo);


}
