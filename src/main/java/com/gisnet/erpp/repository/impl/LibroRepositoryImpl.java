package com.gisnet.erpp.repository.impl;

import com.gisnet.erpp.domain.*;
import com.gisnet.erpp.repository.AreaRepository;
import com.gisnet.erpp.repository.LibroRepositoryCustom;
import com.gisnet.erpp.util.Constantes;
import com.gisnet.erpp.vo.LibroVO;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;
import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.LocalDateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Set;

@Transactional(readOnly = true)
public class LibroRepositoryImpl extends QueryDslRepositorySupport implements LibroRepositoryCustom {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

    LibroRepositoryImpl() {
        super(Prelacion.class);
    }

    private JPQLQuery<Libro> getQueryFrom(QLibro qEntity) {
        return from(qEntity);
    }
    
    @Transactional(readOnly = true)
    public Libro findLibroBy(String numLibro, String libroBis, Long seccionId, Long oficinaId, Integer anio, String volumen) {
        QLibro qLibro = QLibro.libro;
        QSeccionPorOficina qSeccion = QSeccionPorOficina.seccionPorOficina;

        JPQLQuery<Libro> query = getQueryFrom(qLibro);
        BooleanBuilder where = new BooleanBuilder();

        query.innerJoin(qLibro.seccionesPorOficina, qSeccion);
        where   .and(qLibro.numLibro.eq(numLibro) )
        		.and(qLibro.libroBis.eq(libroBis) )
                .and(qSeccion.oficina.id.eq(oficinaId))
                .and(qSeccion.seccion.id.eq(seccionId))
                .and(qLibro.anio.eq(anio))
                .and(qLibro.volumen.eq(volumen));

        query.where(where);
        
        List <Libro> librosResult = query.fetch();
        if (librosResult == null || librosResult.size()==0)
            return null;
        if (librosResult.size() > 1)
            throw new NonUniqueResultException("Hay mas de un libro con esas características");
        return librosResult.get(0);
    }
    
    @Transactional(readOnly = true)
    public List<Libro> findLibrosBy(String numLibro, String libroBis, Long seccionId, Long oficinaId, Integer anio, String volumen) {
        QLibro qLibro = QLibro.libro;
        QSeccionPorOficina qSeccion = QSeccionPorOficina.seccionPorOficina;

        JPQLQuery<Libro> query = getQueryFrom(qLibro);
        BooleanBuilder where = new BooleanBuilder();

        query.innerJoin(qLibro.seccionesPorOficina, qSeccion);
        where   .and(qSeccion.oficina.id.eq(oficinaId))
        		.and(qSeccion.seccion.id.eq(seccionId))
        		.and(qLibro.anio.eq(anio));
        		if(numLibro != null && numLibro.trim().length() > 0 ) {
        			where.and(qLibro.numLibro.eq(numLibro) );
        		}
        		if(libroBis != null && libroBis.trim().length() > 0 ) {
        			where.and(qLibro.libroBis.eq(libroBis) );
        		}
        		if(volumen != null && volumen.trim().length() > 0 ) {
        			where.and(qLibro.volumen.eq(volumen));
        		}

        query.where(where);
        
        List <Libro> librosResult = query.fetch();
        if (librosResult == null || librosResult.size()==0)
            return null;
        
//        if (librosResult.size() > 1)
//            throw new NonUniqueResultException("Hay mas de un libro con esas características");
        return librosResult;
    }

    @Transactional(readOnly = true)
    public Libro findLibroByAntePredio(String doc, String docBis, Integer anio,  String volumen, String libro,
                                       String librobis, Long seccionId, Long oficinaId) {

        QPredioAnte qPredioAnte = QPredioAnte.predioAnte;
        QAntecedente qAnte = QAntecedente.antecedente;
        BooleanBuilder where = new BooleanBuilder();

        JPQLQuery<Libro> query = this.getBaseQuery(doc, docBis, anio, volumen ,libro, librobis, seccionId, oficinaId);

        // Join con el tipo de consulta
        query.innerJoin(qAnte.predioAntesParaAntecedentes,qPredioAnte);


        List <Libro> librosResult = query.fetch();
        if (librosResult == null )
            return null;
        if (librosResult.size() > 1)
            throw new NonUniqueResultException("Hay mas de un libro o antecedente con esas características");
        return librosResult.get(0);

    }


    @Transactional(readOnly = true)
    public PredioAnte findOnePredioAnteByAnteLibro(String numero, String documentoBis, String documento, Integer anio,  String volumen, 
    		String libro, String librobis, 
    		Long seccionId, Long oficinaId) {

        QPredioAnte qPredioAnte = QPredioAnte.predioAnte;
        QAntecedente qAnte = QAntecedente.antecedente;
        QLibro qLibro = QLibro.libro;
        QSeccionPorOficina qSeccion = QSeccionPorOficina.seccionPorOficina;
        JPQLQuery<PredioAnte> query = from (qPredioAnte);


        BooleanBuilder where = new BooleanBuilder();

        query.innerJoin(qPredioAnte.antecedente,qAnte);

        query.innerJoin(qAnte.libro,qLibro);
        query.innerJoin(qLibro.seccionesPorOficina, qSeccion);

        //query.innerJoin(qLibro.antecedentesParaLibros, qAnte);

        // Filtro de datos mínimos
        where   .and(qLibro.numLibro.eq(libro) )
                .and(qSeccion.oficina.id.eq(oficinaId))
                .and(qSeccion.seccion.id.eq(seccionId));

        // Filtro en Libro
        if (librobis != null && librobis != "")
            where.and( qLibro.libroBis.eq (librobis) );
        if (anio != null)
            where.and(qAnte.libro.anio.eq(anio));
        if (volumen != null)
            where.and(qAnte.libro.volumen.eq(volumen));

        // Filtro en antecedentes
        /*if (numero!= null )
            where.and(qAnte.numero.eq(numero)); */

        if (documento != null )
            where.and(qAnte.documento.eq(documento));
        if (documentoBis != null)
            where.and(qAnte.documentoBis.eq(documentoBis));


        query.where(where);

        List <PredioAnte> librosResult = query.fetch();
        if (librosResult == null )
            return null;
        if (librosResult.size() == 0)
        {
            return null;
        }
        if (librosResult.size() > 1)
            throw new NonUniqueResultException("Hay mas de un libro o antecedente con esas características");

        return librosResult.get(0);

    }

    @Transactional(readOnly = true)
    public List<PredioAnte> findAllPredioAnteByAnteLibro(String numero, String documentoBis, String documento, Integer anio,  String volumen, 
    		String libro, String librobis, 
    		Long seccionId, Long oficinaId) {

        QPredioAnte qPredioAnte = QPredioAnte.predioAnte;
        QAntecedente qAnte = QAntecedente.antecedente;
        QLibro qLibro = QLibro.libro;
        QSeccionPorOficina qSeccion = QSeccionPorOficina.seccionPorOficina;
        JPQLQuery<PredioAnte> query = from (qPredioAnte);


        BooleanBuilder where = new BooleanBuilder();

        query.innerJoin(qPredioAnte.antecedente,qAnte);

        query.innerJoin(qAnte.libro,qLibro);
        query.innerJoin(qLibro.seccionesPorOficina, qSeccion);

        //query.innerJoin(qLibro.antecedentesParaLibros, qAnte);

        // Filtro de datos mínimos
        //where   .and(qLibro.numLibro.eq(libro) )
         //       .and(qSeccion.oficina.id.eq(oficinaId))
         //       .and(qSeccion.seccion.id.eq(seccionId));
        if(libro != null){
            where.and(qLibro.numLibro.eq(libro));
        }
        if(oficinaId != null && oficinaId != 0){
            where.and(qSeccion.oficina.id.eq(oficinaId));
        }
        if(seccionId != null && seccionId != 0){
            where.and(qSeccion.seccion.id.eq(seccionId));
        }
        // Filtro en Libro
        if (librobis != null && librobis != "")
            where.and( qLibro.libroBis.eq (librobis) );
        if (anio != null){
        where.and(qAnte.libro.anio.eq(anio));
        where.or(qAnte.libro.anio.like("%"+anio+"%"));
        }
        if (volumen != null)
            where.and(qAnte.libro.volumen.eq(volumen));

        // Filtro en antecedentes
        /*if (numero!= null )
            where.and(qAnte.numero.eq(numero)); */

        if (documento != null ){
            where.and(qAnte.documento.equalsIgnoreCase(documento));
            where.or(qAnte.documento.like("%"+documento+"%"));
        }
        if (documentoBis != null)
            where.and(qAnte.documentoBis.eq(documentoBis));

        System.out.println(where);
        query.where(where);

        List <PredioAnte> librosResult = query.fetch();
        if (librosResult == null )
            return null;
        if (librosResult.size() == 0) {
            return null;
        }        
        return librosResult;
    }

    @Transactional(readOnly = true)
    public PjAnte findPjAnteByAnteLibro(String numero, String documentoBis, String documento, Integer anio, 
    		String libro, String librobis, 
    		Long seccionId, Long oficinaId) {

        QPjAnte qPjAnte = QPjAnte.pjAnte;
        QAntecedente qAnte = QAntecedente.antecedente;
        QLibro qLibro = QLibro.libro;
        QSeccionPorOficina qSeccion = QSeccionPorOficina.seccionPorOficina;
        JPQLQuery<PjAnte> query = from (qPjAnte);


        BooleanBuilder where = new BooleanBuilder();

        query.innerJoin(qPjAnte.antecedente,qAnte);

        query.innerJoin(qAnte.libro,qLibro);
        query.innerJoin(qLibro.seccionesPorOficina, qSeccion);

        //query.innerJoin(qLibro.antecedentesParaLibros, qAnte);

        // Filtro de datos mínimos
        where   .and(qLibro.numLibro.eq(libro) )
                .and(qSeccion.oficina.id.eq(oficinaId))
                .and(qSeccion.seccion.id.eq(seccionId));

        // Filtro en Libro
        if (librobis != null && librobis != "")
            where.and( qLibro.libroBis.eq (librobis) );

        // Filtro en antecedentes
        if (documento != null )
            where.and(qAnte.documento.eq(documento));
        if (documentoBis != null)
            where.and(qAnte.documentoBis.eq(documentoBis));
        if (anio != null)
            where.and(qAnte.libro.anio.eq(anio));

        query.where(where);

        List <PjAnte> librosResult = query.fetch();
        if (librosResult == null )
            return null;
        if (librosResult.size() == 0)
            return null;
        if (librosResult.size() > 1)
            throw new NonUniqueResultException("Hay mas de un libro o antecedente con esas características");
        return librosResult.get(0);

    }
    
    
    @Transactional(readOnly = true)
    public AuxiliarAnte findAuxiliarAnteByAnteLibro(String numero, String documentoBis, String documento, Integer anio, 
    		String libro, String librobis, 
    		Long seccionId, Long oficinaId) {

        QAuxiliarAnte qAuxiliarAnte = QAuxiliarAnte.auxiliarAnte;
        QAntecedente qAnte = QAntecedente.antecedente;
        QLibro qLibro = QLibro.libro;
        QSeccionPorOficina qSeccion = QSeccionPorOficina.seccionPorOficina;
        JPQLQuery<AuxiliarAnte> query = from (qAuxiliarAnte);


        BooleanBuilder where = new BooleanBuilder();

        query.innerJoin(qAuxiliarAnte.antecedente,qAnte);

        query.innerJoin(qAnte.libro,qLibro);
        query.innerJoin(qLibro.seccionesPorOficina, qSeccion);

        //query.innerJoin(qLibro.antecedentesParaLibros, qAnte);

        // Filtro de datos mínimos
        where   .and(qLibro.numLibro.eq(libro) )
                .and(qSeccion.oficina.id.eq(oficinaId))
                .and(qSeccion.seccion.id.eq(seccionId));

        // Filtro en Libro
        if (librobis != null && librobis != "")
            where.and( qLibro.libroBis.eq (librobis) );

        // Filtro en antecedentes
        if (documento != null )
            where.and(qAnte.documento.eq(documento));
        if (documentoBis != null)
            where.and(qAnte.documentoBis.eq(documentoBis));
        if (anio != null)
            where.and(qAnte.libro.anio.eq(anio));

        query.where(where);

        List <AuxiliarAnte> librosResult = query.fetch();
        if (librosResult == null )
            return null;
        if (librosResult.size() == 0)
            return null;
        if (librosResult.size() > 1)
            throw new NonUniqueResultException("Hay mas de un libro o antecedente con esas características");
        return librosResult.get(0);

    }
    
    @Transactional(readOnly = true)
    public MuebleAnte findMuebleAnteByAnteLibro(String numero, String documentoBis, String documento, Integer anio, 
    		String libro, String librobis, 
    		Long seccionId, Long oficinaId) {

        QMuebleAnte qMuebleAnte = QMuebleAnte.muebleAnte;
        QAntecedente qAnte = QAntecedente.antecedente;
        QLibro qLibro = QLibro.libro;
        QSeccionPorOficina qSeccion = QSeccionPorOficina.seccionPorOficina;
        JPQLQuery<MuebleAnte> query = from (qMuebleAnte);


        BooleanBuilder where = new BooleanBuilder();

        query.innerJoin(qMuebleAnte.antecedente,qAnte);

        query.innerJoin(qAnte.libro,qLibro);
        query.innerJoin(qLibro.seccionesPorOficina, qSeccion);

        //query.innerJoin(qLibro.antecedentesParaLibros, qAnte);

        // Filtro de datos mínimos
        where   .and(qLibro.numLibro.eq(libro) )
                .and(qSeccion.oficina.id.eq(oficinaId))
                .and(qSeccion.seccion.id.eq(seccionId));

        // Filtro en Libro
        if (librobis != null && librobis != "")
            where.and( qLibro.libroBis.eq (librobis) );

        // Filtro en antecedentes
        if (documento != null )
            where.and(qAnte.documento.eq(documento));
        if (documentoBis != null)
            where.and(qAnte.documentoBis.eq(documentoBis));
        if (anio != null)
            where.and(qAnte.libro.anio.eq(anio));

        query.where(where);

        List <MuebleAnte> librosResult = query.fetch();
        if (librosResult == null )
            return null;
        if (librosResult.size() == 0)
            return null;
        if (librosResult.size() > 1)
            throw new NonUniqueResultException("Hay mas de un libro o antecedente con esas características");
        return librosResult.get(0);

    }

    private JPQLQuery<Libro> getBaseQuery (String anteDocumento, String anteDocumentoBis, Integer anio,  String volumen, String libro,
                                           String librobis, Long seccionId, Long oficinaId) {

        QLibro qLibro = QLibro.libro;
        QAntecedente qAnte = QAntecedente.antecedente;
        QSeccionPorOficina qSeccion = QSeccionPorOficina.seccionPorOficina;
        JPQLQuery<Libro> query = getQueryFrom(qLibro);
        BooleanBuilder where = new BooleanBuilder();

        query.innerJoin(qLibro.seccionesPorOficina, qSeccion);
        query.innerJoin(qLibro.antecedentesParaLibros, qAnte);

        // Filtro de datos mínimos
        where   .and(qLibro.numLibro.eq(libro) )
                .and(qSeccion.oficina.id.eq(oficinaId))
                .and(qSeccion.seccion.id.eq(seccionId));

        // Filtro en Libro
        if (librobis != null && librobis != "")
            where.and( qLibro.libroBis.eq (librobis) );
        if (anio != null)
            where.and(qAnte.libro.anio.eq(anio));
        if (volumen != null)
            where.and(qAnte.libro.volumen.eq(volumen));

        // Filtro en antecedentes
        if (anteDocumento != null )
            where.and(qAnte.documento.eq(anteDocumento));
        if (anteDocumentoBis != null)
            where.and(qAnte.documentoBis.eq(anteDocumentoBis));
       


        query.where(where);

        return query;

    }
    @Transactional(readOnly = true)
    public List<Libro> findBy(String numLibro, String libroBis, Long seccionPorOficinaId) {
        QLibro libro = QLibro.libro;
        JPQLQuery<Libro> query = getQueryFrom(libro);
        BooleanBuilder where = new BooleanBuilder();
        if (numLibro != null) {
            where.and(libro.numLibro.eq(numLibro));
        }
        
        if (libroBis != null && libroBis.length()>0) {
            where.and(libro.libroBis.eq(libroBis));
        }

        if (seccionPorOficinaId != null ) {
            where.and(libro.seccionesPorOficina.id.eq(seccionPorOficinaId));
        }
        
        query.where(where);
        query.orderBy(libro.numLibro.asc());
        return query.fetch();
    }
    
    public Page<Libro> findAllPageable(String numLibro, String libroBis, String oficinaId, String seccionId, String tipoDoc, Pageable pageable) {
        QLibro libro = QLibro.libro;
        JPQLQuery<Libro> query = getQueryFrom(libro);
        BooleanBuilder where = new BooleanBuilder();
        if (numLibro != null  && numLibro.length()>0) {
            where.and(libro.numLibro.eq(numLibro));
        }
        
        if (libroBis != null && libroBis.length()>0) {
            where.and(libro.libroBis.containsIgnoreCase(libroBis));
        }
       
        if (oficinaId!=null && oficinaId.length()>0){
        	where.and(libro.seccionesPorOficina.oficina.id.eq(Long.valueOf(oficinaId)));
		}
        if (seccionId!=null && seccionId.length()>0){
        	where.and(libro.seccionesPorOficina.seccion.id.eq(Long.valueOf(seccionId)));
		}
        
        if (tipoDoc!=null && tipoDoc.length()>0) {
        	where.and(libro.tipoDoc.eq(Boolean.valueOf(tipoDoc)));
        }

        query.where(where);
        long totalFound = query.fetchCount();
        
        setOrder(query, pageable, libro);
        
        query.orderBy(libro.id.asc());
        List<Libro> results = query.fetch();
        return new PageImpl<Libro>(results, pageable, totalFound);
    }
    
    private void setOrder(JPQLQuery<Libro> query, Pageable pageable, QLibro libro){    	    	
    	if (pageable != null) {
            if (pageable.getSort() != null) {
                for (Sort.Order order : pageable.getSort()) {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    log.debug("order.getProperty()="+order.getProperty());
                    switch(((order.getProperty()))) {
                        case "numLibro":
                            query.orderBy(new OrderSpecifier<String>(direction, libro.numLibro));
                            break;
                        case "libroBis":
                            query.orderBy(new OrderSpecifier<String>(direction, libro.libroBis));
                            break;
                        case "seccionesPorOficina.oficina.nombre":
                    		query.orderBy(new OrderSpecifier<String>(direction, libro.seccionesPorOficina.oficina.nombre));
                        	break;
                        case "seccionesPorOficina.seccion.nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, libro.seccionesPorOficina.seccion.nombre));
                            break;
                        case "tipoDoc":
                            query.orderBy(new OrderSpecifier<Boolean>(direction, libro.tipoDoc));
                            break;
                    }
                }
            }
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }

        
    } 

    @Transactional(readOnly = true)
    @Override
    public Libro findLibroUploadFile(LibroVO libroVO) {
      
        QAntecedente qAnte = QAntecedente.antecedente;
        QLibro qLibro = QLibro.libro;
        QSeccionPorOficina qSeccion = QSeccionPorOficina.seccionPorOficina;
        JPQLQuery<Libro> query = from (qLibro);


        BooleanBuilder where = new BooleanBuilder();
       

        query.innerJoin(qLibro.antecedentesParaLibros, qAnte);
        query.innerJoin(qLibro.seccionesPorOficina, qSeccion);      

        //NombreLibro nombreLibroEntity = nombreLibroService.findOne(libroVO.getLibro());
        //TipoLibro tipoLibroEntity = tipoLibroService.findOne(libroVO.getTipoLibro());

        
         // .and(qLibro.nombreLibro.eq(nombreLibroEntity) )
         //       .and(qLibro.tipoLibro.eq(tipoLibroEntity))
          where .and(qSeccion.oficina.id.eq(libroVO.getOficina()))               
                .and(qSeccion.seccion.id.eq(libroVO.getSeccion()))
                .and(qLibro.anio.eq(libroVO.getAnio()))
                .and(qLibro.numLibro.eq(libroVO.getTomo()))
                .and(qLibro.libroBis.eq(libroVO.getLibrobis()))
                .and(qLibro.volumen.eq(libroVO.getVolumen()));   
                
                ;
                System.out.println("query: "+libroVO.getNumeroInscripcion());
      
        where.and(qAnte.documento.eq(libroVO.getNumeroInscripcion()));

        query.where(where);
        System.out.println("query: "+query.toString());
        
        List <Libro> librosResult = query.fetch();

        System.out.println("librosResult "+librosResult);
    
        if (librosResult == null )
            return null;
        if (librosResult.size() == 0)
        {
            return null;
        }        
        if (librosResult.size() > 1) {
                throw new NonUniqueResultException("Hay mas de un libro y antecedente con esas características");
        }

        return librosResult.get(0);
       
    }
    
    @Transactional(readOnly = true)
    @Override
    public Libro findLibroUploadFileValiation(LibroVO libroVO) {
              
        QLibro qLibro = QLibro.libro;
        QSeccionPorOficina qSeccion = QSeccionPorOficina.seccionPorOficina;
        JPQLQuery<Libro> query = from (qLibro);


        BooleanBuilder where = new BooleanBuilder();
       
        
        query.innerJoin(qLibro.seccionesPorOficina, qSeccion);      

      //  NombreLibro nombreLibroEntity = nombreLibroService.findOne(libroVO.getLibro());
      //  TipoLibro tipoLibroEntity = tipoLibroService.findOne(libroVO.getTipoLibro());

        
          // .and(qLibro.nombreLibro.eq(nombreLibroEntity) )
           //     .and(qLibro.tipoLibro.eq(tipoLibroEntity))
          where.and(qSeccion.oficina.id.eq(libroVO.getOficina()))
          .and(qLibro.anio.eq(libroVO.getAnio()))
          .and(qSeccion.seccion.id.eq(libroVO.getSeccion()))
          .and(qLibro.numLibro.eq(libroVO.getTomo()))
          .and(qLibro.libroBis.eq(libroVO.getLibrobis()))
          .and(qLibro.volumen.eq(libroVO.getVolumen()));         
        
        
        query.where(where);
        
        List <Libro> librosResult = query.fetch();
        if (librosResult == null )
            return null;
        if (librosResult.size() == 0)
        {
            return null;
        }        
        if (librosResult.size() > 1)
            throw new NonUniqueResultException("Hay mas de un libro con esas características");

        return librosResult.get(0);
       
    }
    
    

    
    
}
