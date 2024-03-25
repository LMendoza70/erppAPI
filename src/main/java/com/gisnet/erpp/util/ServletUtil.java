package com.gisnet.erpp.util;

import java.io.File;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

public class ServletUtil {
	public static String getContextURL(HttpServletRequest httpServletRequest){
		int indice = httpServletRequest.getRequestURL().toString().indexOf(httpServletRequest.getServletPath());
		String path = httpServletRequest.getRequestURL().toString().substring(0,indice);
		return path;
	}
	
	
	public static String getContextFilePath(HttpServletRequest httpServletRequest){
		ServletContext servletContext = httpServletRequest.getServletContext();
		String contextPath = servletContext.getRealPath(File.separator);
		return contextPath;
	}


}
