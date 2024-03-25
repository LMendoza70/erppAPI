package com.gisnet.erpp.web.controller;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.ConceptoPago;
import com.gisnet.erpp.domain.Recibo;
import com.gisnet.erpp.service.ConceptoPagoService;
import com.gisnet.erpp.service.PagoService;
import com.gisnet.erpp.service.ReciboService;


@Controller
@RequestMapping(value = "/pago-en-linea")
public class PagoEnLineaController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PagoService pagoService;

	
	
	@PostMapping("/retorno")
	public void regreso (@RequestParam("xmlmultipago") String xmlmultipago, HttpServletResponse response) throws JAXBException, IOException{
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		logger.debug("xmlmultipago={}", xmlmultipago);
		
		long reciboId = pagoService.regreso(xmlmultipago);
		
		writer.println("<html>");
		writer.println("<script type=\"text/javascript\">");
		writer.println("if(localStorage){");
		writer.println("localStorage.setItem(\"jhi-pago_exitoso\", \""+ reciboId +"\");");
		writer.println("}");
		writer.println("</script>");
		writer.println("<body>");
		writer.println("<h1> Pago exitoso </h1>");
		writer.println("</body>");
		writer.println("</html>");

		
		
	}
		
	@GetMapping("/retorno/test/{reciboId}")
	public void regreso (@PathVariable("reciboId") Long reciboId, HttpServletResponse response) throws JAXBException, IOException{
		response.setContentType("text/html");
		PrintWriter writer = response.getWriter();
		
		String xmlmultipago= pagoService.regresoTest(reciboId);
		logger.debug("xmlmultipago={}", xmlmultipago);
		
		pagoService.regreso(xmlmultipago);
				
		writer.println("<html>");
		writer.println("<script type=\"text/javascript\">");
		writer.println("if(localStorage){");
		writer.println("localStorage.setItem(\"jhi-pago_exitoso\", \""+ reciboId +"\");");
		writer.println("}");
		writer.println("</script>");
		writer.println("<body>");
		writer.println("<h1> Pago exitoso</h1>");
		writer.println("</body>");
		writer.println("</html>");

	    
	
}

	

}
