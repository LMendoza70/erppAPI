package com.gisnet.erpp.web.api.notificacion;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

import com.gisnet.erpp.domain.EstructuraOrg;
import com.gisnet.erpp.domain.Notificacion;
import com.gisnet.erpp.domain.TipoNotificacion;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.UsuarioOfiAreas;
import com.gisnet.erpp.service.NotificacionService;
import com.gisnet.erpp.web.api.prelacionPredio.PrelacionPredioDTO;


@RestController
@RequestMapping(value = "/api/notificacion", produces = MediaType.APPLICATION_JSON_VALUE)
public class NotificacionRestController {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	NotificacionService notificacionService;
	
	@GetMapping("/notificacion-usuario-logeado/")
	public ResponseEntity<List<Notificacion>> getNotificacionByUserLogged() {
		return new ResponseEntity<>(notificacionService.findByUserLogged(), HttpStatus.OK);
	}
	
	@GetMapping("/notificacion-usuario-menu/")
	public ResponseEntity<Long> getNotificacionByUserMenu() {
		return new ResponseEntity<>(notificacionService.findByUserMenu(), HttpStatus.OK);
	}
	
	@PostMapping("/notificacion-vista/")
	public ResponseEntity<Long> notificacionVista(@RequestBody Notificacion notificacion) {
		return new ResponseEntity<>(notificacionService.notificacionVista(notificacion), HttpStatus.OK);
	}
	
	@GetMapping("/genera-notificacion/{msg}/{tipo}/{destinatario}/{remitente}")
	public ResponseEntity<Long> generaNotificacion(@PathVariable("msg")String msg , @PathVariable("tipo")Long tipo , @PathVariable("destinatario")Long destinatario, @PathVariable("remitente")Long remitente) {
		return new ResponseEntity<>(notificacionService.generaNotificacion(msg, tipo, destinatario, remitente), HttpStatus.OK);
	}
	
	@GetMapping ("/obtener-destinatarios/{destinatario}")
	public ResponseEntity<List<Usuario>> obtenerDestinatarios(@PathVariable("destinatario")Long destinatario){
		return ResponseEntity.ok(notificacionService.obtenerDestinatarios(destinatario));
	}	
	@PostMapping("/enviar-comunicado/{msg}/{tipoNotificacion}/{tipoDestinatario}")
	public ResponseEntity<Long> enviarComunicado(@PathVariable("msg")String msg , @PathVariable("tipoNotificacion")Long tipoNotificacion , @PathVariable("tipoDestinatario")Long tipoDestinatario, @RequestBody List <Usuario> destinatarioSelect){
		return ResponseEntity.ok(notificacionService.enviarComunicado(msg, tipoNotificacion, tipoDestinatario,destinatarioSelect));
	}
	 
	@PostMapping("/subordinados")
	public ResponseEntity<List<Usuario>> findSubordinados( @RequestBody List <Usuario> usuario ){
		return ResponseEntity.ok(notificacionService.findSubordinados(usuario));
	}
	
	@PostMapping("/superiores")
	public ResponseEntity<List<Usuario>> findSuperiores( @RequestBody List <Usuario> usuario ){
		return ResponseEntity.ok(notificacionService.findSuperiores(usuario));
	}
}
