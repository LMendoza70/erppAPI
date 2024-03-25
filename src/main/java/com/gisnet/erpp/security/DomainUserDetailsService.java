package com.gisnet.erpp.security;

import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.repository.UsuarioRepository;
import com.gisnet.erpp.service.UsuarioService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Authenticate a user from the database.
 */
@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final Logger log = LoggerFactory.getLogger(DomainUserDetailsService.class);

    private final UsuarioService usuarioService;

    public DomainUserDetailsService(UsuarioService usuarioService) {
        this.usuarioService = usuarioService;
    }

    @Override
    @Transactional
    public UserDetailsErpp loadUserByUsername(final String login) {
        log.debug("Authenticating {}", login);
        Optional<Usuario> usuarioFromDatabase = usuarioService.findOneWithRolesAnFuncionesByUserName(login);
        return usuarioFromDatabase.map(usuario -> {
            if (!usuario.isActivo()) {
                throw new UserNotActivatedException("Usuario " + login + " was not activated");
            }
            List<GrantedAuthority> grantedAuthorities = usuario.getUsuarioRolesParaUsuarios().stream().map(authority -> new SimpleGrantedAuthority(authority.getRol().getCve())).collect(Collectors.toList());
            		return new UserDetailsErpp(login, usuario.getContrasenia(), grantedAuthorities, usuario.getId(), usuario.getNombreCompleto());
        }).orElseThrow(() -> new UsernameNotFoundException("Usuario " + login + " Error de Login"));
    }
}
