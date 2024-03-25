package com.gisnet.erpp.web.api.account;

import java.util.List;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Optional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import com.gisnet.erpp.domain.Acceso;
import com.gisnet.erpp.domain.ActoDocumento;
import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.ClasifActo;
import com.gisnet.erpp.domain.Documento;
import com.gisnet.erpp.domain.Estado;
import com.gisnet.erpp.domain.Municipio;
import com.gisnet.erpp.domain.TipoUsuario;
import com.gisnet.erpp.domain.TipoNotario;

import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Notario;
import com.gisnet.erpp.domain.Direccion;
import com.gisnet.erpp.domain.Telefono;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.domain.Ambito;
import com.gisnet.erpp.domain.Dependencia;



import com.gisnet.erpp.service.EstadoService;
import com.gisnet.erpp.service.MunicipioService;
import com.gisnet.erpp.service.TipoUsuarioService;
import com.gisnet.erpp.service.TipoNotarioService;
import com.gisnet.erpp.service.UsuarioRegistroService;
import com.gisnet.erpp.service.UsuarioService;
import com.gisnet.erpp.service.AccesoService;
import com.gisnet.erpp.service.AmbitoService;
import com.gisnet.erpp.service.NotarioService;
import com.gisnet.erpp.service.NotificacionService;
import com.gisnet.erpp.util.MessageObject;


@RestController
@RequestMapping("/api")
public class AccountResource {

	private final Logger log = LoggerFactory.getLogger(AccountResource.class);

	private final UsuarioService userService;
	
	private final UsuarioRegistroService usuarioRegistroService;
	
	private final TipoUsuarioService tipoUsuarioService;
	
	private final MunicipioService municipioService;
	
	private final AmbitoService ambitoService;
	
	private final EstadoService estadoService;
	
	private final AccesoService accesoService;
	
	private final NotarioService notarioService;
	
	private final TipoNotarioService tipoNotarioService;
	
	private final NotificacionService notificacionService;	

	public AccountResource(UsuarioService userService, UsuarioRegistroService usuarioRegistroService, TipoUsuarioService tipoUsuarioService, EstadoService estadoService, MunicipioService municipioService, AmbitoService ambitoService, NotarioService notarioService, TipoNotarioService tipoNotarioService,AccesoService accesoService, NotificacionService notificacionService) {
		this.userService = userService;
		this.usuarioRegistroService = usuarioRegistroService;
		this.tipoUsuarioService = tipoUsuarioService;
		this.estadoService = estadoService;
		this.municipioService = municipioService;
		this.ambitoService = ambitoService;
		this.notarioService = notarioService;
		this.tipoNotarioService = tipoNotarioService;
		this.accesoService =  accesoService;
		this.notificacionService = notificacionService;
	}

	@GetMapping("/authenticate")
	public String isAuthenticated(HttpServletRequest request) {
		log.debug("REST request to check if the current user is authenticated");
		return request.getRemoteUser();
	}

	@GetMapping("/account")
	public ResponseEntity<UsuarioDTO> getAccount() {
		return Optional.ofNullable(userService.findOneWithRolesAnFuncionesByUserName()).map(user -> new ResponseEntity<>(new UsuarioDTO(user, notificacionService.findByUserMenu()), HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR));
	}
	
	@PostMapping(value = "/error-logout")
    public ResponseEntity<Boolean> errorLogout(HttpServletRequest request)
	{
		try 
		{
		    String sessionId = request.getSession().getId();
		    accesoService.setLogoutError(sessionId);
		    return new ResponseEntity<>(true, HttpStatus.OK);
		}
		catch(Exception e) {
			return new ResponseEntity<>(false, HttpStatus.OK);	
		}
	}
	
	@PostMapping(value = "/busca-logout-error" ,produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<Boolean> getLogoutError(HttpServletRequest request)
	{
		try 
		{
			log.info(SecurityUtils.getCurrentUserId().toString());
			Acceso obj = accesoService.getLogoutError(SecurityUtils.getCurrentUserId());
			log.info("ID  ACCESO:"+obj.getId().toString());
		    return  new ResponseEntity<>(obj.getLogoutError(), HttpStatus.OK );
		}
		catch(Exception e) {
			log.info("ID  ACCESO ERROR:" + e.getMessage());
			return new ResponseEntity<>(false, HttpStatus.OK);	
		}
	}
	
	@PostMapping(path = "/user-unlock", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<Boolean> userUnlock(@Valid @RequestBody UsuarioUnlockDTO userUnlock )
	{
		log.info(userUnlock.getJpassword() + "   "+userUnlock.getJusername());
		try
		{
			   Usuario usuario = userService.findOneByUserName(userUnlock.getJusername()).get();
			   BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
				if(usuario==null) 
				{
					log.info("NO EXISTE EL USUARIO");
				   return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
				}
		        else
		        {
		        	//VALIDA CONTRASEÑAS
		        	if(encoder.matches(userUnlock.getJpassword(),usuario.getContrasenia()))
		        	{
		        		log.info("=)");
			             accesoService.userUnlock(usuario.getId()); // UNLOCK USER
			             return new ResponseEntity<>(true, HttpStatus.OK);
		        	}else 
		        	{
		        		log.info("CONTRASEÑA INCORRECTA");
		        		 return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		        	}
		        }
		}catch(Exception e) {
		   log.info(e.getMessage());
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping(path = "/register", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity registerAccount(@Valid @RequestBody RegistroDTO registroDTO) {

		HttpHeaders textPlainHeaders = new HttpHeaders();
		textPlainHeaders.setContentType(MediaType.TEXT_PLAIN);
		
		return userService.findOneByEmail(registroDTO.getEmail()).map(user -> new ResponseEntity<>("email ya esta siendo usado", textPlainHeaders, HttpStatus.BAD_REQUEST))
						.orElseGet(() -> {
							log.debug("Creando usuario_registro");							
							try {
								usuarioRegistroService.creaUsuarioRegistro(registroDTO.getEmail());
							} catch (MessagingException e) {
								log.error(e.getMessage(), e);
							}
							return new ResponseEntity<>(HttpStatus.CREATED);
						});
	}
	
	@GetMapping(value = "/post-register/{tok}")
    public ResponseEntity<Boolean> validateToken(@PathVariable("tok") String token) {		
		return new ResponseEntity<>(usuarioRegistroService.validaToken(token), HttpStatus.OK);
	}
	
	@GetMapping(value = "/post-register/{tok}/email", produces = { MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<?> getEmailFromToken(@PathVariable("tok") String token) {	
		try {
			return new ResponseEntity<>(usuarioRegistroService.getEmailFromToken(token), HttpStatus.OK);	
		}
		catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	@PostMapping(path = "/post-register", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<Boolean> postRegisterAccount(@Valid @RequestBody PostRegistroDTO postRegistroDTO) throws MessagingException {
		
		
		
		log.debug(postRegistroDTO.toString());
		try {
			userService.postRegistro(postRegistroDTO);
		}
		catch (Exception e) {
			return new ResponseEntity<>(false, HttpStatus.EXPECTATION_FAILED);	
		}
				
		return new ResponseEntity<>(true, HttpStatus.OK);	
	}
	
	@PostMapping(path = "/post-register/save-notario-titular", produces = { MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<?> postRegisterSaveNotarioTitular(@Valid 
			@RequestParam("persona") String sPersona,
			@RequestParam("notario") String sNotario,
			@RequestParam("direccion") String sDireccion,
			@RequestParam("telefono") String sTelefono,
			@RequestParam("usuario") String sUsuario,
			@RequestParam("token") String token
		) throws MessagingException {
		
		if(!usuarioRegistroService.validaToken(token)) {
			return new ResponseEntity<>("email no registrado", HttpStatus.BAD_REQUEST);
		}
		
		Persona persona = new Persona();
		Notario notario = new Notario();
		Direccion direccion = new Direccion();
		Telefono telefono = new Telefono();
		Usuario usuario = new Usuario();
		
		try {
			persona = new ObjectMapper().readValue(sPersona, Persona.class);
			notario = new ObjectMapper().readValue(sNotario, Notario.class);
			direccion = new ObjectMapper().readValue(sDireccion, Direccion.class);
			telefono = new ObjectMapper().readValue(sTelefono, Telefono.class);
			usuario = new ObjectMapper().readValue(sUsuario, Usuario.class);
			if(userService.postRegisterSaveNotarioTitular(persona, notario, direccion, telefono, usuario, token)) {
				return new ResponseEntity<>(HttpStatus.OK);	
			}
			else {
				return new ResponseEntity<>("no se ha podido registrar el usuario", HttpStatus.BAD_REQUEST);
			}
			
		}
		catch (Exception e ) {
			log.debug(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping(path = "/post-register/save-dependencia", produces = { MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<?> postRegisterSaveDependencia(@Valid 
			@RequestParam("dependencia") String sDependencia,
			@RequestParam("direccion") String sDireccion,
			@RequestParam("telefono") String sTelefono,
			@RequestParam("usuario") String sUsuario,
			@RequestParam("token") String token
		) throws MessagingException {
		
		if(!usuarioRegistroService.validaToken(token)) {
			return new ResponseEntity<>("email no registrado", HttpStatus.BAD_REQUEST);
		}
		
		
		Dependencia dependencia = new Dependencia();
		Direccion direccion = new Direccion();
		Telefono telefono = new Telefono();
		Usuario usuario = new Usuario();
		
		try {
			
			dependencia = new ObjectMapper().readValue(sDependencia, Dependencia.class);
			direccion = new ObjectMapper().readValue(sDireccion, Direccion.class);
			telefono = new ObjectMapper().readValue(sTelefono, Telefono.class);
			usuario = new ObjectMapper().readValue(sUsuario, Usuario.class);
			if(userService.postRegisterSaveDependencia(dependencia, direccion, telefono, usuario, token)) {
				return new ResponseEntity<>(HttpStatus.OK);	
			}
			else {
				return new ResponseEntity<>("no se ha podido registrar el usuario", HttpStatus.BAD_REQUEST);
			}
			
		}
		catch (Exception e ) {
			log.debug(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@GetMapping(value = "/post-register/municipio/{municipioId}/notarios", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
    public ResponseEntity<?> getNotariosByMunicipio(@PathVariable("municipioId") Long municipioId) {	
		try {
			return new ResponseEntity<>(notarioService.findAllByMunicipioId(municipioId), HttpStatus.OK);	
		}
		catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	@PostMapping(path = "/post-register/save-publico-general", produces = { MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<?> postRegisterPublicoPublicoGeneral(@Valid 
			@RequestParam("persona") String sPersona,
			@RequestParam("direccion") String sDireccion,
			@RequestParam("telefono") String sTelefono,
			@RequestParam("usuario") String sUsuario,
			@RequestParam("token") String token
		) throws MessagingException {
		
		if(!usuarioRegistroService.validaToken(token)) {
			return new ResponseEntity<>("email no registrado", HttpStatus.BAD_REQUEST);
		}
		
		
		Persona persona = new Persona();
		Direccion direccion = new Direccion();
		Telefono telefono = new Telefono();
		Usuario usuario = new Usuario();
		
		try {
			
			persona = new ObjectMapper().readValue(sPersona, Persona.class);
			direccion = new ObjectMapper().readValue(sDireccion, Direccion.class);
			telefono = new ObjectMapper().readValue(sTelefono, Telefono.class);
			usuario = new ObjectMapper().readValue(sUsuario, Usuario.class);
			if(userService.postRegisterSavePublicoGeneral(persona, direccion, telefono, usuario, token)) {
				return new ResponseEntity<>(HttpStatus.OK);	
			}
			else {
				return new ResponseEntity<>("no se ha podido registrar el usuario", HttpStatus.BAD_REQUEST);
			}
			
		}
		catch (Exception e ) {
			log.debug(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping(path = "/post-register/save-gestor", produces = { MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<?> postRegisterGestor(@Valid 
			@RequestParam("persona") String sPersona,
			@RequestParam("direccion") String sDireccion,
			@RequestParam("telefono") String sTelefono,
			@RequestParam("usuario") String sUsuario,
			@RequestParam("notarias") String sNotarias,
			@RequestParam("token") String token
		) throws MessagingException {
		
				
		if(!usuarioRegistroService.validaToken(token)) {
			return new ResponseEntity<>("email no registrado", HttpStatus.BAD_REQUEST);
		}
				
		Persona persona = new Persona();
		Direccion direccion = new Direccion();
		Telefono telefono = new Telefono();
		Usuario usuario = new Usuario();
		List<Notario> notarias = new ArrayList<>();
		
		try {
			notarias = new ObjectMapper().readValue(sNotarias, new TypeReference<List<Notario>>(){});	
			persona = new ObjectMapper().readValue(sPersona, Persona.class);
			direccion = new ObjectMapper().readValue(sDireccion, Direccion.class);
			telefono = new ObjectMapper().readValue(sTelefono, Telefono.class);
			usuario = new ObjectMapper().readValue(sUsuario, Usuario.class);
			if(userService.postRegisterSaveGestor(persona, direccion, telefono, usuario, token, notarias)) {
				return new ResponseEntity<>(HttpStatus.OK);	
			}
			else {
				return new ResponseEntity<>("no se ha podido registrar el usuario", HttpStatus.BAD_REQUEST);
			}
			
		}
		catch (Exception e ) {
			log.debug(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	
	
	

		

	
	@PostMapping(path = "/complete-register", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<Boolean> completeRegisterAccount(@Valid @RequestBody PostRegistroDTO postRegistroDTO) {
		log.debug(postRegistroDTO.toString());
		userService.resetPassword(postRegistroDTO);		
		return new ResponseEntity<>(true, HttpStatus.OK);	
	}
	
	@PostMapping(path = "/account/reset-password/finish", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<Boolean> resetPasswordFinish(@Valid @RequestBody PostRegistroDTO postRegistroDTO) {
		log.debug(postRegistroDTO.toString());
		userService.resetPassword(postRegistroDTO);		
		return new ResponseEntity<>(true, HttpStatus.OK);	
	}
	
	 @PostMapping(path = "/account/reset-password/init", produces = MediaType.TEXT_PLAIN_VALUE)
	 public ResponseEntity requestPasswordReset(@RequestBody String mail) {
		 return userService.findOneByEmail(mail)
         .map(user -> {
        	 try {
        		 usuarioRegistroService.requestPasswordReset(user);
        		 return new ResponseEntity<>("email se envio", HttpStatus.OK);
        	 } catch (MessagingException e) {
					log.error(e.getMessage(), e);
					return new ResponseEntity<>("email no registrado", HttpStatus.BAD_REQUEST);
			}        	            
         }).orElse(new ResponseEntity<>("email no registrado", HttpStatus.BAD_REQUEST));
 }
	
	@GetMapping(value = "/post-register/tipousuarios")
    public ResponseEntity<List<TipoUsuario>> listTiposUsuario() {		
		return ResponseEntity.ok(tipoUsuarioService.findAllSelfRegister());
	}
	
	@GetMapping(value = "/post-register/tipos-notario")
    public ResponseEntity<List<TipoNotario>> listTiposNotario() {		
		return ResponseEntity.ok(tipoNotarioService.findAll());
	}
	
	@GetMapping(value = "/post-register/ambitos")
    public ResponseEntity<List<Ambito>> listAmbitos() {		
		return ResponseEntity.ok(ambitoService.findAll());
	}
	
	@GetMapping(value = "/post-register/municipio/estado/{id}")
	public ResponseEntity<List<Municipio>> findMunicipiosbyEstadoId(@PathVariable("id") Long id) {
		return ResponseEntity.ok(municipioService.findAllbyEstadoId(id));
	}
	
	@GetMapping(value = "/post-register/estado")
	public ResponseEntity<List<Estado>> findEstados() {
		return ResponseEntity.ok(estadoService.findAll());
	}
	
	@GetMapping("/post-register/notarios")
	public ResponseEntity<Page<Notario>> getAllJueces(Pageable pageable, String paterno, String materno, String nombre, Integer noNotario, Long estadoId, Long municipioId) {
		log.debug("paterno=" + paterno);
		log.debug("materno=" + materno);
		log.debug("nombre=" + nombre);
		log.debug("estadoId=" + estadoId);
		log.debug("municipioId=" + estadoId);
		Page<Notario> page = notarioService.findAllByNombre(municipioId, estadoId, paterno, materno, nombre, noNotario, pageable);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}




}
