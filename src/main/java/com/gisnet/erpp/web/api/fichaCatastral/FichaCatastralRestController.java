package com.gisnet.erpp.web.api.fichaCatastral;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@RestController
@RequestMapping(value = "/api/ficha-catastral", produces = MediaType.APPLICATION_JSON_VALUE)
public class FichaCatastralRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Value("${catastrowebservice.uri}")
	private String CATASTRO_WEB_SERVICE_URI;

	@GetMapping("/{municipio}/{claveCuenta}/{tipo}")
	public ResponseEntity<?> findClaveCuenta(@PathVariable("municipio") String claveMunicipio, 
			@PathVariable("claveCuenta") String claveCuenta, @PathVariable("tipo") Integer tipo ){
		
		
		HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.set("Accept", MediaType.APPLICATION_JSON_VALUE);
        final String url = CATASTRO_WEB_SERVICE_URI; 
        HttpEntity<?> httpEntity  = new HttpEntity<>(httpHeaders); 
        RestTemplate restTemplate  = new RestTemplate();
        
        Map<String, String> params = new HashMap<>();
        params.put("municipio", claveMunicipio);
        if(tipo == 1) {
        	params.put("clave", claveCuenta);
        }else {
        	params.put("cuenta", claveCuenta);
        }
        
        UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl(url);
        for (Map.Entry<String, String> entry : params.entrySet()) {
            builder.queryParam(entry.getKey(), entry.getValue());
        }
        ResponseEntity<String> response = restTemplate.exchange(builder.toUriString(), HttpMethod.GET, httpEntity, String.class);
		
//        try {
        	response.getHeaders().getLocation();
        	response.getStatusCode();
        	String responseBody = response.getBody();
        	
        	return new ResponseEntity<>(responseBody, HttpStatus.OK);
        	//JSONObject obj = new JSONObject(responseBody);
        	
        	//System.out.println(obj);
        	
//        } catch (JSONException e) {
//        	throw new RuntimeException("JSONException occurred");
//        }
	}
}
