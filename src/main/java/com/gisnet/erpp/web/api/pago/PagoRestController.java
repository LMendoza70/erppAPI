package com.gisnet.erpp.web.api.pago;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.bind.JAXBException;

import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.ConceptoPago;
import com.gisnet.erpp.service.ConceptoPagoService;
import com.gisnet.erpp.service.PagoService;
import com.gisnet.erpp.vo.ConceptosPagosVO;

@RestController
@RequestMapping(value = "/api/pago", produces = MediaType.APPLICATION_JSON_VALUE)
public class PagoRestController {
	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PagoService pagoService;
	

	@RequestMapping(value = "/genera",method = RequestMethod.POST)
	@ResponseBody
	public String pago(@RequestBody ConceptosPagosVO conceptosPago, HttpServletRequest httpServletRequest) throws IOException, JAXBException {
		String xml = pagoService.pago(conceptosPago);
		return xml;
	}


	
	/*
	@GetMapping(value = "/{conceptoPagoId}")
	@ResponseBody
	public void pago(@PathVariable Long conceptoPagoId, HttpServletResponse httpResponse, OutputStream out) throws ClientProtocolException, IOException, JAXBException {
		CloseableHttpClient client = HttpClients.createDefault();
		HttpPost httpPost = new HttpPost("https://gobiernoenlinea1.jalisco.gob.mx/multipagos/CargaXML");

		List<NameValuePair> params = new ArrayList<NameValuePair>();

		String xml = pagoService.pago(conceptoPagoId);

		logger.debug("Enviando xml de pago = {}", xml);

		params.add(new BasicNameValuePair("xmlmultipago", xml));
		httpPost.setEntity(new UrlEncodedFormEntity(params));

		CloseableHttpResponse response = client.execute(httpPost);
		
		httpResponse.setContentType("text/html");

		logger.debug("" + response.getStatusLine());
		HttpEntity entity = response.getEntity();

		IOUtils.copy(response.getEntity().getContent(), out);
		
		String result = EntityUtils.toString(response.getEntity());

		client.close(); 

	} */

}
