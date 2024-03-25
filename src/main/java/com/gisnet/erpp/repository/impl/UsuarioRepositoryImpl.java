package com.gisnet.erpp.repository.impl;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.LinkedHashSet;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.support.QueryDslRepositorySupport;
import org.springframework.transaction.annotation.Transactional;

import com.gisnet.erpp.domain.UsuarioAreaRol;
import com.gisnet.erpp.domain.UsuarioRol;
import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.AreaRol;
import com.gisnet.erpp.domain.QUsuario;
import com.gisnet.erpp.domain.QRol;
import com.gisnet.erpp.domain.QAreaRol;
import com.gisnet.erpp.domain.QUsuarioRol;
import com.gisnet.erpp.domain.QUsuarioArea;
import com.gisnet.erpp.domain.QUsuarioAreaRol;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.UsuarioRepositoryCustom;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.core.Tuple;


import com.gisnet.erpp.security.SecurityUtils;


@Transactional(readOnly = true)
public class UsuarioRepositoryImpl extends QueryDslRepositorySupport implements UsuarioRepositoryCustom{
	private final Logger log = LoggerFactory.getLogger(this.getClass());

    UsuarioRepositoryImpl() {
        super(Usuario.class);
    }
    

    private JPQLQuery<Usuario> getQueryFrom(QUsuario qEntity) {
        return from(qEntity);
    }

	public Page<Usuario> findAllByNombre(String paterno, String materno, String nombre, Long tipoUsuarioId, Pageable pageable, Set<Area> areaSet, Long oficinaId, Integer maxRolValue,Long usuarioRolId) {
        QUsuario usuario = QUsuario.usuario;
        QUsuarioArea usuarioArea  = QUsuarioArea.usuarioArea;
        QUsuarioAreaRol usuarioAreaRol = QUsuarioAreaRol.usuarioAreaRol;
        QUsuarioRol usuarioRol = QUsuarioRol.usuarioRol;
        QAreaRol areaRol = QAreaRol.areaRol;
        QRol rol = QRol.rol;
        
        JPQLQuery<Usuario> query = getQueryFrom(usuario);
        JPQLQuery<Usuario> query2 = getQueryFrom(usuario);
        JPQLQuery subQuery = from(usuario);
        
        BooleanBuilder where = new BooleanBuilder();
        BooleanBuilder whereUsers = new BooleanBuilder();
        if (paterno != null && paterno.length()>0) {
            where.and(usuario.persona.paterno.containsIgnoreCase(paterno));
        }
        
        if (oficinaId != null) {
            where.and(usuario.oficina.id.eq(oficinaId));
        }
        
        if (materno != null && materno.length()>0) {
            where.and(usuario.persona.materno.containsIgnoreCase(materno));
        }

        if (nombre != null && nombre.length()>0) {
            where.and(usuario.persona.nombre.containsIgnoreCase(nombre));
        }
        
        if (usuarioRolId!=null){
        	query.leftJoin(usuario.usuarioRolesParaUsuarios, usuarioRol);
        	query.leftJoin(usuarioRol.rol, rol);
        	where.and(rol.id.eq(usuarioRolId));
        }
        
        if (tipoUsuarioId!=null){
        	where.and(usuario.tipoUsuario.id.eq(tipoUsuarioId));
        }
        if(areaSet.size() > 0){
        	areaSet.forEach(System.out::println);
					query.leftJoin(usuario.usuarioAreasRolesParaUsuarios,  usuarioAreaRol);
					query.leftJoin(usuarioAreaRol.areaRol, areaRol);
        	where.and((areaRol.area.in(areaSet)).or(areaRol.area.id.isNull()));
        }

        query.where(where);
        Long totalFound = query.fetchCount();
        
        setOrder(query, pageable, usuario);
        List<Usuario> values = query.fetch();
        
        values.forEach(System.out::println);
        System.out.println(totalFound);
        
        List<Usuario> rolesNiveles = new ArrayList<>();
        List<Usuario> filtered = new ArrayList<>();
        Set<Usuario>results = new LinkedHashSet<>();
        
        
        
        if (maxRolValue != null) {
        	final Integer var = maxRolValue;
        	subQuery.leftJoin(usuario.usuarioRolesParaUsuarios, usuarioRol);
            subQuery.leftJoin(usuarioRol.rol, rol);
            subQuery.select(rol.nivel.min(), usuario.id);
            if(tipoUsuarioId != null) {
            	subQuery.where(usuario.tipoUsuario.id.eq(tipoUsuarioId));
            }
            subQuery.groupBy(usuario.id);
            List<Tuple> niveles = subQuery.fetch();
            
            niveles = niveles.stream().filter(x -> x.get(rol.nivel.min()) != null ? (x.get(rol.nivel.min())>= var ) : true)
        		.collect(Collectors.toList());
            for(Usuario value: values) {
            	for(Tuple tupla : niveles) {
            		if (value.getId().equals(tupla.get(usuario.id))){
            			results.add(value);
            		}	
            	}
            	
            }
            values.clear();
            values.addAll(results);
            if(totalFound.compareTo(Long.parseLong(String.valueOf(niveles.size()))) < 0) {
            	return new PageImpl<Usuario>(values, pageable, totalFound);	
        	}
            else {
            	return new PageImpl<Usuario>(values, pageable, Long.parseLong(String.valueOf(niveles.size())));
            }
                
        }
        else {
        	results.clear();
        	results.addAll(values);
        	values.clear();
        	values.addAll(results);
        }
   
        return new PageImpl<Usuario>(values, pageable, totalFound);
    }
    
	public Page<Usuario> findAllByTipoAndRolAndNombre(Long tipoUsuarioId, ArrayList<Long> listaRoles, String paterno, String materno, String nombre, Pageable pageable, Set<Area> areaSet, Long oficinaId, Integer maxRolValue) {
		QUsuario usuario = QUsuario.usuario;
        QUsuarioArea usuarioArea  = QUsuarioArea.usuarioArea;
        QUsuarioAreaRol usuarioAreaRol = QUsuarioAreaRol.usuarioAreaRol;
        QUsuarioRol usuarioRol = QUsuarioRol.usuarioRol;
        QAreaRol areaRol = QAreaRol.areaRol;
        QRol rol = QRol.rol;
        
        JPQLQuery<Usuario> query = getQueryFrom(usuario);
        JPQLQuery subQuery = from(usuario);
        
        BooleanBuilder where = new BooleanBuilder();
        if (paterno != null && paterno.length()>0) {
            where.and(usuario.persona.paterno.containsIgnoreCase(paterno));
        }
        
        if (oficinaId != null) {
            where.and(usuario.oficina.id.eq(oficinaId));
        }
        
        if (materno != null && materno.length()>0) {
            where.and(usuario.persona.materno.containsIgnoreCase(materno));
        }

        if (nombre != null && nombre.length()>0) {
            where.and(usuario.persona.nombre.containsIgnoreCase(nombre));
        }
        
        if (tipoUsuarioId!=null){
        	where.and(usuario.tipoUsuario.id.eq(tipoUsuarioId));
        }
        
        if (listaRoles != null && listaRoles.size() > 0 ) {
        	query.leftJoin(usuario.usuarioRolesParaUsuarios,  usuarioRol);
			query.leftJoin(usuarioRol.rol, rol);
			where.and(rol.id.in(listaRoles));
        }
        
        query.where(where);
        Long totalFound = query.fetchCount();
        
        setOrder(query, pageable, usuario);
        List<Usuario> values = query.fetch();
        
        values.forEach(System.out::println);
        System.out.println(totalFound);
        
        List<Usuario> rolesNiveles = new ArrayList<>();
        List<Usuario> filtered = new ArrayList<>();
        Set<Usuario>results = new LinkedHashSet<>();
        
        
        
        	results.clear();
        	results.addAll(values);
        	values.clear();
        	values.addAll(results);
        
   
        return new PageImpl<Usuario>(values, pageable, totalFound);
	}
	
    private void setOrder(JPQLQuery<Usuario> query, Pageable pageable, QUsuario usuario){    	    	
    	if (pageable != null) {
            if (pageable.getSort() != null) {
                for (Sort.Order order : pageable.getSort()) {
                    Order direction = order.isAscending() ? Order.ASC : Order.DESC;
                    log.debug("order.getProperty()="+order.getProperty());
                    switch(((order.getProperty()))) {
                        case "persona.paterno":
                            query.orderBy(new OrderSpecifier<String>(direction, usuario.persona.paterno));
                            break;
                        case "persona.materno":
                            query.orderBy(new OrderSpecifier<String>(direction, usuario.persona.materno));
                            break;
                        case "persona.nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, usuario.persona.nombre));
                            break;                            
                        case "tipoUsuario.nombre":
                            query.orderBy(new OrderSpecifier<String>(direction, usuario.tipoUsuario.nombre));
                            break;
                        case "userName":
                            query.orderBy(new OrderSpecifier<String>(direction, usuario.userName));
                            break;
                    }
                }
            }
            query.offset(pageable.getOffset()).limit(pageable.getPageSize());
        }
    } 
    public Page<Usuario> findAllByTipoSolicitanteAndNombre(Long tipoUsuarioId, String paterno, String materno, String nombre, Pageable pageable, Set<Area> areaSet, Long oficinaId, Integer maxRolValue) {
		QUsuario usuario = QUsuario.usuario;
        QUsuarioArea usuarioArea  = QUsuarioArea.usuarioArea;
        QUsuarioAreaRol usuarioAreaRol = QUsuarioAreaRol.usuarioAreaRol;
        QUsuarioRol usuarioRol = QUsuarioRol.usuarioRol;
        QAreaRol areaRol = QAreaRol.areaRol;
        QRol rol = QRol.rol;
        
        JPQLQuery<Usuario> query = getQueryFrom(usuario);
        
        BooleanBuilder where = new BooleanBuilder();
        if (paterno != null && paterno.length()>0) {
            where.and(usuario.persona.paterno.containsIgnoreCase(paterno));
        }
        
        if (oficinaId != null) {
            where.and(usuario.oficina.id.eq(oficinaId));
        }
        
        if (materno != null && materno.length()>0) {
            where.and(usuario.persona.materno.containsIgnoreCase(materno));
        }

        if (nombre != null && nombre.length()>0) {
            where.and(usuario.persona.nombre.containsIgnoreCase(nombre));
        }
        
        where.and(usuario.tipoUsuario.id.notIn(8));
        if (tipoUsuarioId!=null){
        	where.and(usuario.tipoUsuario.id.eq(tipoUsuarioId));
        	
        }
        
        
        
        query.where(where);
        Long totalFound = query.fetchCount();
        
        setOrder(query, pageable, usuario);
        List<Usuario> values = query.fetch();
        
        values.forEach(System.out::println);
        System.out.println(totalFound);
        
        List<Usuario> rolesNiveles = new ArrayList<>();
        List<Usuario> filtered = new ArrayList<>();
        Set<Usuario>results = new LinkedHashSet<>();
        
        
        
        	results.clear();
        	results.addAll(values);
        	values.clear();
        	values.addAll(results);
        
   
        return new PageImpl<Usuario>(values, pageable, totalFound);
	}
	
}
