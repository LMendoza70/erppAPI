package com.gisnet.erpp.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AbstractAuthenticationTargetUrlRequestHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

/**
 * Spring Security logout handler, specialized for Ajax requests.
 */
public class AjaxLogoutSuccessHandler extends AbstractAuthenticationTargetUrlRequestHandler
    implements LogoutSuccessHandler {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
        Authentication authentication)
        throws IOException, ServletException {
    	
    	logger.debug("AjaxLogoutSuccessHandler");;
    	request.getSession().setAttribute("LOGOUT", "LOGOUT");
    	
        response.setStatus(HttpServletResponse.SC_OK);
    }
}