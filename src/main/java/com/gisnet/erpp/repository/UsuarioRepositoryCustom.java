package com.gisnet.erpp.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.Set;

import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.Area;

public interface UsuarioRepositoryCustom {
	public abstract Page<Usuario> findAllByNombre(String paterno, String materno, String nombre, Long tipoUsuarioId, Pageable pageable, Set<Area> areaSet, Long oficinaId, Integer maxRolValue,Long usuarioRolId);
	public abstract Page<Usuario> findAllByTipoAndRolAndNombre(Long tipoUsuarioId, ArrayList<Long> listaRoles, String paterno, String materno, String nombre, Pageable pageable, Set<Area> areaSet, Long oficinaId, Integer maxRolValue);
	public abstract Page<Usuario> findAllByTipoSolicitanteAndNombre(Long tipoUsuarioId, String paterno, String materno, String nombre, Pageable pageable, Set<Area> areaSet, Long oficinaId, Integer maxRolValue);

}