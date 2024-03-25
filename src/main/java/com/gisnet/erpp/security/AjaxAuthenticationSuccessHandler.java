package com.gisnet.erpp.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

import com.gisnet.erpp.service.AccesoService;

/**
 * Spring Security success handler, specialized for Ajax requests.
 */
public class AjaxAuthenticationSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	private AccesoService accesoService;
	
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication)
        throws IOException, ServletException {
    	
    	logger.debug("AjaxAuthenticationSuccessHandler session.Id {}", request.getSession().getId());
    	
    	UserDetailsErpp user = (UserDetailsErpp) authentication.getPrincipal();
    	
    	int total = accesoService.countOpenByUsuarioId(user.getId());
    	
    	logger.debug("sessions for user {}", total);
    	
    	if (total>=1){
    		
    		SecurityContextHolder.getContext().setAuthentication(null);
    		response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
    		
    	} else {
    	
			if (user!=null){
				accesoService.login(user.getId(), request.getSession().getId());
			}
			
			response.setStatus(HttpServletResponse.SC_OK);
    	}
    	

        
    }
}