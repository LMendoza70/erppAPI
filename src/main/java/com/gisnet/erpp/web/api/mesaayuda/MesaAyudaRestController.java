package com.gisnet.erpp.web.api.mesaayuda;

import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.gisnet.erpp.domain.MesaTema;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.service.MesaTemaService;

@RestController
@RequestMapping(value = "/api/mesa-ayuda")
public class MesaAyudaRestController {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	MesaTemaService mesaTemaService;

	public static final HashMap<Long, SseEmitter> escritorioEmitters = new HashMap<Long, SseEmitter>();
	public static final HashMap<Long, ClienteEmitterDTO> clientesEmitters = new HashMap<Long, ClienteEmitterDTO>();
	
	@PostMapping(value = "/send-from-escritorio", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Long> sendFromEscriotio(@RequestBody RequestDTO requestDTO) {
		log.debug("sendFromEscriotio= {}", requestDTO );
		
		Long usuarioEscritorioId = SecurityUtils.getCurrentUserId();
		
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setAccion(requestDTO.getAccion());
		responseDTO.setUsuarioIdFrom(usuarioEscritorioId);
		responseDTO.setUsuarioNombreFrom(SecurityUtils.getCurrentUserNombreCompleto());
		responseDTO.setFecha(new Date());
		responseDTO.setMensaje(requestDTO.getMensaje());
		responseDTO.setUsuarioIdTo(requestDTO.getUsuarioIdTo());
		
		if (requestDTO.getAccion()==0 || requestDTO.getAccion()==ACCION.CREATE.accion){
			sendToCliente(responseDTO.getUsuarioIdTo(), true, responseDTO);
			sendToEscritorio(usuarioEscritorioId, false, responseDTO);
		} 	else {
			closeEscritorioEmmiter(usuarioEscritorioId);
		}

		return new ResponseEntity<>(1L, HttpStatus.OK);
	}
	
	private void sendToCliente(long usuarioClienteId, boolean incoming, ResponseDTO responseDTO){		
		ClienteEmitterDTO clienteEmitterDTO = clientesEmitters.get(usuarioClienteId);
		SseEmitter clienteEmitter = clienteEmitterDTO.getSsEmitter();	
		
		try {						
				responseDTO.setIncoming(incoming);
				clienteEmitter.send(dtoToJSON(responseDTO));
		} catch (IOException e) {
			e.printStackTrace();
			
			closeClienteEmitter(usuarioClienteId);
			
		} 

	}
	
	
	private void closeClienteEmitter(long usuarioClienteId){
		ClienteEmitterDTO clienteEmitterDTO = clientesEmitters.get(usuarioClienteId);
		SseEmitter clienteEmitter = clienteEmitterDTO.getSsEmitter();
		SseEmitter escritorioEmmiter = escritorioEmitters.get(clienteEmitterDTO.getUsuarioEscritorioId());
		
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setAccion(ACCION.CLOSE.accion);
		try {
			escritorioEmmiter.send(dtoToJSON(responseDTO));
		} catch(IOException e){
			log.error(e.getMessage(), e);
		}
		
		clienteEmitter.complete();	
		clientesEmitters.remove(usuarioClienteId);
	}
	
	@PostMapping(value = "/send-from-cliente", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Long> sendFromCliente(@RequestBody RequestDTO requestDTO) {
		log.debug("Enviando accion={}", requestDTO.getAccion());
		
		Long usuarioClienteId = SecurityUtils.getCurrentUserId();
		
		ResponseDTO responseDTO = new ResponseDTO();
		responseDTO.setAccion(requestDTO.getAccion());
		responseDTO.setUsuarioIdFrom(usuarioClienteId);
		responseDTO.setUsuarioNombreFrom(SecurityUtils.getCurrentUserNombreCompleto());
		responseDTO.setFecha(new Date());
		responseDTO.setMensaje(requestDTO.getMensaje());
		responseDTO.setUsuarioIdTo(usuarioClienteId);
		
		if (requestDTO.getAccion()==0 || requestDTO.getAccion()==ACCION.CREATE.accion){
			sendToEscritorio(clientesEmitters.get(usuarioClienteId).getUsuarioEscritorioId(), true, responseDTO);	
			sendToCliente(usuarioClienteId, false, responseDTO);
		} else {
			closeClienteEmitter(usuarioClienteId);
		}
		
		
		return new ResponseEntity<>(1L, HttpStatus.OK);
	}
	
	private void sendToEscritorio(long usuarioEscritorioId, boolean incoming, ResponseDTO responseDTO){		
		SseEmitter escritorioEmitter = escritorioEmitters.get(usuarioEscritorioId);

		try {					
				responseDTO.setAccion(ACCION.CREATE.accion);
				responseDTO.setFecha(new Date());
				responseDTO.setIncoming(incoming);
				escritorioEmitter.send(dtoToJSON(responseDTO));
		} catch (IOException e) {			
			e.printStackTrace();
			
			closeEscritorioEmmiter(usuarioEscritorioId);
			}

	}
	
	private void closeEscritorioEmmiter(long usuarioEscritorioId){
		SseEmitter escritorioEmitter = escritorioEmitters.get(usuarioEscritorioId);
		
		escritorioEmitter.complete();		

		Iterator<Map.Entry<Long, ClienteEmitterDTO>> it = clientesEmitters.entrySet().iterator();
		while (it.hasNext()) {
			  Map.Entry<Long, ClienteEmitterDTO> entry = it.next();
			  log.debug("sesionID={}, val{}", entry.getKey(), entry.getValue());
		    if (entry.getValue().getUsuarioEscritorioId()== usuarioEscritorioId) { 
		    	SseEmitter clienteEmitter = entry.getValue().getSsEmitter();
		    	
		    	clienteEmitter.complete();
		    	ResponseDTO responseDTO = new ResponseDTO();
				responseDTO.setAccion(ACCION.CLOSE.accion);
				try {
					clienteEmitter.send(dtoToJSON(responseDTO));
				} catch(IOException e){
					log.error(e.getMessage(), e);
				}
				
				clientesEmitters.remove(entry.getKey());
			} 
		}
	
		//close Escriotrio
		escritorioEmitters.remove(usuarioEscritorioId);
	}

	@GetMapping(value = "/escritorio/stream")
	public SseEmitter streamEscritorio() throws IOException {
		Long usuarioId = SecurityUtils.getCurrentUserId();
		SseEmitter emitter = escritorioEmitters.get(usuarioId);
		
		if (emitter==null){		
				emitter = new SseEmitter();
				escritorioEmitters.put(usuarioId, emitter);
				emitter.onCompletion(() -> {escritorioEmitters.remove(usuarioId);});
		}
		
		return emitter;
	}

	@GetMapping(value = "/cliente/stream")
	public SseEmitter streamCliente() throws IOException {
		Long usuarioId = SecurityUtils.getCurrentUserId();
		
		ClienteEmitterDTO clienteEmitterDTO = clientesEmitters.get(usuarioId);
		
		if (clienteEmitterDTO==null && escritorioEmitters.size()>0){
			SseEmitter emitter = new SseEmitter();
			clienteEmitterDTO = new ClienteEmitterDTO();
			clienteEmitterDTO.setSsEmitter(emitter);
			clienteEmitterDTO.setUsuarioEscritorioId(getEscritorioDisponible());
			clientesEmitters.put(usuarioId, clienteEmitterDTO);
			emitter.onCompletion(() -> clientesEmitters.remove(usuarioId));
					
		}
				
		return clienteEmitterDTO.getSsEmitter();
	}
	
	@GetMapping(value = "/disponibles", produces=MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Boolean> disponibles() {
		return new ResponseEntity<>(escritorioEmitters.size()>0, HttpStatus.OK);
	}
	
	
	
	private Long getEscritorioDisponible(){
		if (escritorioEmitters.size()>0){
			Random generator = new Random();
			Object[] ids = escritorioEmitters.keySet().toArray();
			return (Long) ids[generator.nextInt(escritorioEmitters.size())];
		}
		return null;
	}
	

	@GetMapping(value = "/mesa-tema")
	public ResponseEntity<List<MesaTema>> findByAllMAllMesasTemas() {
		return ResponseEntity.ok(mesaTemaService.findAll());
	}

	


	
    private String dtoToJSON(ResponseDTO dto){
    	ObjectMapper mapper = new ObjectMapper();
    	try {
			return mapper.writeValueAsString(dto);
		} catch (JsonProcessingException e) {
			log.error(e.getMessage(), e);
		}
    	
    	return "";
    }
    

    
	public enum ACCION{	CREATE(1), CLOSE(2);
	private int accion;
	private ACCION(int c){
		accion= c;
	}
	
	public int getIdClasifActo(){
		return accion;
	}
    
	@Override
	public String toString() {		
		return String.valueOf(accion);
	}	
	};


}
