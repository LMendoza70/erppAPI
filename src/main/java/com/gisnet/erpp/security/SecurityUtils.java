package com.gisnet.erpp.security;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;


public final class SecurityUtils {

    private SecurityUtils() {
    }

    public static String getCurrentUserLogin() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String userName = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails springSecurityUser = (UserDetails) authentication.getPrincipal();
                userName = springSecurityUser.getUsername();
            } else if (authentication.getPrincipal() instanceof String) {
                userName = (String) authentication.getPrincipal();
            }
        }
        return userName;
    }
    
    public static Long getCurrentUserId() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        Long id = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetailsErpp) {
            	UserDetailsErpp springSecurityUser = (UserDetailsErpp) authentication.getPrincipal();
                id = springSecurityUser.getId();
            } 
        }
        return id;
    }
    
    public static String getCurrentUserNombreCompleto() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        String nombreCompleto = null;
        if (authentication != null) {
            if (authentication.getPrincipal() instanceof UserDetailsErpp) {
            	UserDetailsErpp springSecurityUser = (UserDetailsErpp) authentication.getPrincipal();
                nombreCompleto = springSecurityUser.getNombreCompleto();
            } 
        }
        return nombreCompleto;
    }


    public static boolean isAuthenticated() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();       
        return authentication != null;
    }

    public static boolean isCurrentUserInRole(String authority) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        Authentication authentication = securityContext.getAuthentication();
        if (authentication != null) {
            return authentication.getAuthorities().stream()
                .anyMatch(grantedAuthority -> grantedAuthority.getAuthority().equals(authority));
        }
        return false;
    }
}
