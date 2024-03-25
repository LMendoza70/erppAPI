package com.gisnet.erpp.config;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.stereotype.Component;

import com.gisnet.erpp.security.UserDetailsErpp;
import com.gisnet.erpp.service.AccesoService;

@Component
public class SessionCreatedListenerService implements HttpSessionListener {
	@Autowired
	AccesoService accesoService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Override
	public void sessionCreated(HttpSessionEvent httpSessionEvent) {
		
	}

	@Override
	public void sessionDestroyed(HttpSessionEvent httpSessionEvent) {
		HttpSession httpSession = httpSessionEvent.getSession();
		SecurityContext securityContext = (SecurityContextImpl) httpSession.getAttribute("SPRING_SECURITY_CONTEXT");
				
		logger.debug("SessionCreatedListenerService session.Id = {}", httpSession.getId());
		
		if (securityContext!=null && securityContext.getAuthentication()!=null){
			UserDetailsErpp user = (UserDetailsErpp) securityContext.getAuthentication().getPrincipal();
			boolean loggedOut=false;
			
			String l = (String) httpSession.getAttribute("LOGOUT");
			
			if (l!=null){
				loggedOut=true;
			} 	
			accesoService.logout(user.getId(), httpSession.getId(), loggedOut);
			}

	}
}
