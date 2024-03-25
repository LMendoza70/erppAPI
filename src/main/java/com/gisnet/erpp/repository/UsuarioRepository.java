package com.gisnet.erpp.repository;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.UsuarioRol;

/**
 * Spring Data JPA repository for the Usuario entity.
 */
@SuppressWarnings("unused")
public interface UsuarioRepository extends JpaRepository<Usuario,Long>, UsuarioRepositoryCustom {
    @EntityGraph(attributePaths = "usuarioRolesParaUsuarios.rol")
    Optional<Usuario> findOneWithRolesByUserName(String userName);

    @EntityGraph(attributePaths = "usuarioRolesParaUsuarios.rol.funcionRolesParaRols.funcion")
    Optional<Usuario> findOneWithRolesAnFuncionesByUserName(String userName);
    
    Usuario findOneByUsuarioRolesParaUsuariosRolIdAndOficinaId(Long rolId, Long oficinaId);

    @Query(value="SELECT u.* FROM USUARIO u INNER JOIN PERSONA p ON (p.ID = u.PERSONA_ID)  WHERE p.email = :email", nativeQuery=true)
    Usuario findByEmailPersona(@Param("email") String email );

    Usuario findByUserName(String userName);

    @EntityGraph(attributePaths = "usuarioRolesParaUsuarios.rol.funcionRolesParaRols.funcion")
    Optional<Usuario> findOneWithRolesAndFuncionesById(Long id);

    List<Usuario> findByOficinaIdAndUsuarioRolesParaUsuariosRolIdAndActivo(Long idOficina,Long idRol,Boolean activo);

    @EntityGraph(attributePaths = "usuarioRolesParaUsuarios.rol")
    Optional<Usuario> findOneWithRolesById(Long id);
    
  //TODO borrar el metodo de arriba,
    @Query(value="SELECT u FROM Usuario u where u.userName = :userName")
    Optional<Usuario> findOneByUserNameOptional(@Param("userName") String userName);
    
    Optional<Usuario> findOneByEmail(String email);
    
    Optional<Usuario> findOneByEmailIgnoreCase(String email);
    
    List<Usuario>findByUsuarioRolesParaUsuariosRolId(Long rolId);
    
    List<Usuario>findByUsuarioRolesParaUsuariosRolCve(String clave);

    @EntityGraph(attributePaths = "persona")
    Usuario findByEmail (String email);
    
   // List <Usuario> findAllByUsuarioAreasRolesParaUsuarios(Set<UsuarioAreaRol> UsuarioArea);
    
    @Query(value="SELECT u FROM Usuario u where u.id in(:usuarios) AND u.oficina.id= :oficina")
    List<Usuario> findAllByUsuariosId(@Param("usuarios") List<Long> idUsuarios, @Param("oficina") Long oficina);

    @Query(value="SELECT u FROM Usuario u where u.tipoUsuario.id=8 AND u.activo=1")
    List<Usuario> findAllByTipoUsuarioAndActivo();
}
