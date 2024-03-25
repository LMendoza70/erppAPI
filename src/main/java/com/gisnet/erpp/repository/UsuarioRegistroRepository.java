package com.gisnet.erpp.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.gisnet.erpp.domain.UsuarioRegistro;

/**
 * Spring Data JPA repository for the Usuario entity.
 */
@SuppressWarnings("unused")
public interface UsuarioRegistroRepository extends JpaRepository<UsuarioRegistro,Long> {
    Optional<UsuarioRegistro> findOneByEmail(String email);
    
    Optional<UsuarioRegistro> findOneByVerificacionToken(String token);
}
