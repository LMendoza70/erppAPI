package com.gisnet.erpp.web.api.usuario;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.gisnet.erpp.vo.*;
import com.google.gson.JsonObject;
import org.jfree.util.Log;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.util.Optional;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import javax.mail.MessagingException;
import javax.validation.Valid;

import java.net.URISyntaxException;

import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Rol;
import com.gisnet.erpp.domain.Telefono;
import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.AreaRol;
import com.gisnet.erpp.domain.Dependencia;
import com.gisnet.erpp.domain.Direccion;
import com.gisnet.erpp.domain.Funcion;
import com.gisnet.erpp.domain.FuncionRol;
import com.gisnet.erpp.domain.Notario;
import com.gisnet.erpp.domain.UsuarioArea;
import com.gisnet.erpp.domain.UsuarioClasifActoServicio;
import com.gisnet.erpp.domain.UsuarioOfiAreas;
import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.UsuarioActos;
import com.gisnet.erpp.domain.UsuarioRol;
import com.gisnet.erpp.domain.UsuarioServicios;
import com.gisnet.erpp.domain.Oficina;

import com.gisnet.erpp.service.UsuarioService;
import com.gisnet.erpp.service.UsuarioAreaService;
import com.gisnet.erpp.service.AreaRolService;

import com.gisnet.erpp.util.MessageObject;

import com.gisnet.erpp.web.api.account.UsuarioDTO;
import com.gisnet.erpp.web.api.usuario.*;
import org.springframework.web.client.RestTemplate;;
@RestController
@RequestMapping(value = "/api/usuario", produces = MediaType.APPLICATION_JSON_VALUE)
public class UsuarioRestController {
	
	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	UsuarioAreaService usuarioAreaService;
	
	@Autowired
	AreaRolService areaRolService;

	@Value("${spring.profiles.active:Unknown}")
	private String activeProfile;

	@GetMapping("/usuarios")
	public ResponseEntity<Page<Usuario>> getAllUsuarios(Pageable pageable, String paterno, String materno, String nombre, Long tipoUsuarioId, String areasRoles, Boolean filterPermissions, Long usuarioRolId) {
		Set<Area> areaSet = new HashSet<>();
		if(areasRoles != null) {
			if (areasRoles.length()>0){
				String[] separatedAreas = areasRoles.split("[,]+");
				for( String area: separatedAreas) {
					areaSet.add(areaRolService.findOne(Long.parseLong(area)).getArea());
				}
			}
		}
		Page<Usuario> page = usuarioService.findAllByNombre(paterno, materno, nombre, tipoUsuarioId, pageable, areaSet, filterPermissions,usuarioRolId);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	@RequestMapping(value = "/email/", method = RequestMethod.GET)
	public ResponseEntity<Usuario> getUsuarioByMail(@RequestParam("email") String email) {
			System.out.println("Direccion de correo electronico "+email);
			Usuario user=usuarioService.findByEmail(email);
		
		return new ResponseEntity<>(user, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/rol/{rolId}", method = RequestMethod.GET)
	public ResponseEntity<List<Usuario>> getUsuarioByRolId(@PathVariable("rolId") Long rolId) {
			List<Usuario> result = usuarioService.findByRolId(rolId);
			return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@RequestMapping(value = "/clave-rol/{clave}", method = RequestMethod.GET)
	public ResponseEntity<List<Usuario>> getUsuarioByRolClave(@PathVariable("clave") String clave) {
			List<Usuario> result = usuarioService.findByRolCve(clave);
			return new ResponseEntity<>(result, HttpStatus.OK);
	}
	
	@GetMapping("/{id}/permisos")
	public ResponseEntity<UsuarioDTO> getUsuarioPermisos(@PathVariable("id") Long id) {
		return Optional.ofNullable(usuarioService.findOneWithRolesAndFuncionesById(id))
				.map(user -> new ResponseEntity<>(new UsuarioDTO(user), HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	@GetMapping("/{id}/roles")
	public ResponseEntity<UsuarioDTO> getUsuarioRoles(@PathVariable("id") Long id) {
		return Optional.ofNullable(usuarioService.findOneWithRolesById(id))
				.map(user -> new ResponseEntity<>(new UsuarioDTO(user), HttpStatus.OK))
				.orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
	}
	
	/*esta funcion realmente regresa un listado de roles y no un usuario*/
	@GetMapping("/{id}/roles-enserio")
	public ResponseEntity<Set<Rol>> getUsuarioRolesEnserio(@PathVariable("id") Long id) {
		Set<Rol> roles = new HashSet<>();
		Set<UsuarioRol> usuRoles;
		usuRoles = Optional.ofNullable(usuarioService.findOneWithRolesById(id))
				.map(user-> user.getUsuarioRolesParaUsuarios())
				.orElse(new HashSet<>());
		
		if(usuRoles.size() > 0) {
			usuRoles.forEach(x -> roles.add(x.getRol()));
		}
		
		return new ResponseEntity<>(roles, HttpStatus.OK);
	}
	
	@GetMapping("/{id}/areas-roles")
	public ResponseEntity<Set<AreaRol>> getUsuarioAreas(@PathVariable("id") Long id) {
		Set<AreaRol> areasRoles = new HashSet<>();
		usuarioService.findOneWithRolesById(id).getUsuarioAreasRolesParaUsuarios().forEach(x -> areasRoles.add(x.getAreaRol()));
		return new ResponseEntity<>(areasRoles, HttpStatus.OK);
	}
	
	@GetMapping("/{id}/oficina")
	public ResponseEntity<Oficina> getUsuarioOficina(@PathVariable("id") Long id) {
		Oficina oficina = usuarioService.findOne(id).getOficina();
		return new ResponseEntity<>(oficina, HttpStatus.OK);
	}
	
	@GetMapping("/oficina")
	public ResponseEntity<?> getUsuarioOficina() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		String username = authentication.getName();
		if( username == null ) {
			return new ResponseEntity<>("{\"mensaje\":\"El usuario no esta logeado.\"}", HttpStatus.NOT_FOUND);
		}
		
		Oficina oficina = usuarioService.findByNombre(username).getOficina();
		if( oficina != null ) {
			return new ResponseEntity<>(oficina, HttpStatus.OK);
		} else {
			return new ResponseEntity<>("{\"mensaje\":\"No existe oficina para el usuario: "+username+"\"}", HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/save")
	public ResponseEntity<?> createUsuario(@RequestBody UsuarioVO usuario) throws URISyntaxException {
		Usuario newUsuario=null;
		log.debug("Creando usuario_registro");							
		try {	
			if(usuarioService.findOneByUserName(usuario.getUserName()).isPresent()) {
				System.out.println("here we found it");
				return new ResponseEntity<>(new MessageObject("El Nombre de Usuario se Encuentra en Uso"), new HttpHeaders(), HttpStatus.BAD_REQUEST); 
			}
			newUsuario=usuario.transformIntoUsuario();
			newUsuario.setId(null);
			newUsuario = usuarioService.save(newUsuario, usuario.getAreasRoles(), usuario.getRoles());
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(new MessageObject(e.getMessage()) ,new HttpHeaders(),HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<>(newUsuario,new HttpHeaders(),HttpStatus.CREATED);	
	}
	
	@PostMapping("/update-usuario")
	public ResponseEntity<?> updateUsuario(@RequestBody UsuarioVO usuario) throws URISyntaxException {
		Usuario newUsuario=null;
		log.debug("Actualizando usuario");							
		try {	
			newUsuario=usuario.transformIntoUsuario();
			newUsuario = usuarioService.update(newUsuario, usuario.getAreasRoles(), usuario.getRoles());
		} catch (Exception e) {
			log.error(e.getMessage());
			return new ResponseEntity<>(new MessageObject(e.getMessage()) ,new HttpHeaders(),HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<>(newUsuario,new HttpHeaders(),HttpStatus.CREATED);	
	}
	
	@PutMapping("/update-perfiles")
	public ResponseEntity<?> updatePerfiles(@RequestBody UsuarioVO usuario) throws URISyntaxException {
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			String json = ow.writeValueAsString(usuario);
			System.out.println(json);
		}
		catch (Exception except) {
			System.out.println(except.toString());
		}
			
		Usuario newUsuario=null;
		
		try {
			newUsuario = usuarioService.updatePerfiles(usuario.transformIntoUsuario(), usuario.getAreasRoles(), usuario.getRoles());
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			return new ResponseEntity<>(new MessageObject(e.getMessage()) ,new HttpHeaders(),HttpStatus.EXPECTATION_FAILED);
		}
		return new ResponseEntity<>(newUsuario,new HttpHeaders(),HttpStatus.CREATED);	
	}
	
	@PostMapping("/update")
	public ResponseEntity<Usuario> updateUsuarioPermisos(@RequestBody UsuarioVO2 usuario) throws URISyntaxException {
		System.out.println(usuario.transformIntoUsuario());
		Usuario actual = usuarioService.update( usuario);
		return new ResponseEntity<>(actual, HttpStatus.OK);
	}

	@GetMapping ("/{id}")
	public ResponseEntity<Usuario> getUsuario (@PathVariable long id ) {
		Usuario user=usuarioService.findOne(id);
		return new ResponseEntity<Usuario>(user, HttpStatus.OK);

	}
	
	@GetMapping("/suggest-email")
	public ResponseEntity<?> suggestEmail(String nombre, String paterno, String materno) {
		String res, suggestedEmail1="", suggestedEmail2="";
		suggestedEmail1 = usuarioService.createSuggestedEmail(nombre, paterno);
		res = suggestedEmail1;
		if (!usuarioService.isNewValidEmail(suggestedEmail1)){
			suggestedEmail2 =  usuarioService.createSuggestedEmail(nombre, materno);
			res = suggestedEmail2;
			if(!usuarioService.isNewValidEmail(suggestedEmail2)) {
				return new ResponseEntity<>(new MessageObject(suggestedEmail1 + " y " + suggestedEmail2 + " no se encuentran disponibles"), new HttpHeaders(), HttpStatus.CONFLICT);
			}
		}
		return new ResponseEntity<EmailDTO>(new EmailDTO(res), new HttpHeaders(), HttpStatus.OK);
	}
	
	@PostMapping("/validate-email")
	public ResponseEntity<?> validateEmail(@RequestBody String email) {
	
		if (usuarioService.isNewValidEmail(email)){
			return new ResponseEntity<>(new MessageObject( email + " esta disponible"), new HttpHeaders(), HttpStatus.OK);
		}
		return new ResponseEntity<>(new MessageObject(email + " no se encuentra disponible!"), new HttpHeaders(), HttpStatus.CONFLICT);
	}
	
	@GetMapping("/{id}/funciones-padre")
	public ResponseEntity<Set<Funcion>> findAllFuncionesPadre(@PathVariable Long id) throws URISyntaxException {
		Set<Funcion> padres = new HashSet<Funcion>(); 
				padres = usuarioService.getFuncionesPadre(id);
		return new ResponseEntity<>(padres, HttpStatus.OK);
	}
	
	@GetMapping("/{id}/funciones-hijas")
	public ResponseEntity<Set<Funcion>> findAllFuncionesHijas(@PathVariable Long id) throws URISyntaxException {
		Set<Funcion> hijas = new HashSet<Funcion>(); 
				hijas = usuarioService.getFuncionesHijas(id);
		return new ResponseEntity<>(hijas, HttpStatus.OK);
	}
	
	@GetMapping("/{id}/funciones")
	public ResponseEntity<Set<FuncionRol>> findAllFunciones(@PathVariable("id") Long id) throws URISyntaxException {
		return new ResponseEntity<>(usuarioService.findEnabledFunctions(id), HttpStatus.OK);
	}	
	
	@GetMapping("/{usuarioId}/roles/{rolId}/funciones")
	public ResponseEntity<Set<FuncionRol>> findAllFuncionesByUsuarioIdAndRolId(@PathVariable("usuarioId") Long usuarioId, @PathVariable("rolId") Long rolId) throws URISyntaxException {
		return new ResponseEntity<>(usuarioService.findEnabledFunctionsByUsuarioIdAndRolId(usuarioId, rolId), HttpStatus.OK);
	}
	
	@GetMapping("/email-domain")
	public ResponseEntity<?> getEmailDomain() {
		String emailDomain = usuarioService.getEmailDomain();
		return new ResponseEntity<EmailDTO>(new EmailDTO(emailDomain), new HttpHeaders(), HttpStatus.OK);
	}
	
	@PostMapping("/update-funciones")
	public ResponseEntity<Usuario> updateRolFunctions(@RequestBody UsuarioVO usuario) throws URISyntaxException {
		return new ResponseEntity<>(usuarioService.updateFunciones(usuario.transformIntoUsuario(), usuario.getFuncionesRolesParaUsuarios()), HttpStatus.OK);
	}
	
	@PostMapping("/update-funciones-by-rol")
	public ResponseEntity<Usuario> updateRolFunctionsByRol(@RequestBody UsuarioVO usuario) throws URISyntaxException {
		Rol rol = usuario.getRoles().stream().findFirst().orElse(null);
		return new ResponseEntity<>(usuarioService.updateFuncionesByRol(usuario.transformIntoUsuario(), usuario.getFuncionesRolesParaUsuarios(), rol), HttpStatus.OK);
	}
	
	@PostMapping(path = "/save-notario-titular", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<?> postRegisterSaveNotarioTitular(@Valid 
			@RequestParam("persona") String sPersona,
			@RequestParam("notario") String sNotario,
			@RequestParam("direccion") String sDireccion,
			@RequestParam("telefono") String sTelefono,
			@RequestParam("usuario") String sUsuario
		) throws MessagingException {
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
			usuario = usuarioService.saveNotarioTitular(persona, notario, direccion, telefono, usuario);
			return new ResponseEntity<>(usuario, HttpStatus.OK);
		}
		catch (Exception e ) {
			log.debug(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping(path = "/save-dependencia", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<?> postRegisterSaveDependencia(@Valid 
			@RequestParam("dependencia") String sDependencia,
			@RequestParam("direccion") String sDireccion,
			@RequestParam("telefono") String sTelefono,
			@RequestParam("usuario") String sUsuario
		) throws MessagingException {
		Dependencia dependencia = new Dependencia();
		Direccion direccion = new Direccion();
		Telefono telefono = new Telefono();
		Usuario usuario = new Usuario();
		
		try {
			
			dependencia = new ObjectMapper().readValue(sDependencia, Dependencia.class);
			direccion = new ObjectMapper().readValue(sDireccion, Direccion.class);
			telefono = new ObjectMapper().readValue(sTelefono, Telefono.class);
			usuario = new ObjectMapper().readValue(sUsuario, Usuario.class);
			
			usuario = usuarioService.saveDependencia(dependencia, direccion, telefono, usuario);
			return new ResponseEntity<>(usuario, HttpStatus.OK);	
		}
		catch (Exception e ) {
			log.debug(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	
	@PostMapping(path = "/save-publico-general", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<?> postRegisterPublicoPublicoGeneral(@Valid 
			@RequestParam("persona") String sPersona,
			@RequestParam("direccion") String sDireccion,
			@RequestParam("telefono") String sTelefono,
			@RequestParam("usuario") String sUsuario
		) throws MessagingException {
		Persona persona = new Persona();
		Direccion direccion = new Direccion();
		Telefono telefono = new Telefono();
		Usuario usuario = new Usuario();
		
		try {
			
			persona = new ObjectMapper().readValue(sPersona, Persona.class);
			direccion = new ObjectMapper().readValue(sDireccion, Direccion.class);
			telefono = new ObjectMapper().readValue(sTelefono, Telefono.class);
			usuario = new ObjectMapper().readValue(sUsuario, Usuario.class);
			
			
			usuario = usuarioService.savePublicoGeneral(persona, direccion, telefono, usuario);
			return new ResponseEntity<>(usuario, HttpStatus.OK);	
		}
		catch (Exception e ) {
			log.debug(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PostMapping(path = "/save-gestor", produces = { MediaType.APPLICATION_JSON_VALUE, MediaType.TEXT_PLAIN_VALUE })
	public ResponseEntity<?> postRegisterGestor(@Valid 
			@RequestParam("persona") String sPersona,
			@RequestParam("direccion") String sDireccion,
			@RequestParam("telefono") String sTelefono,
			@RequestParam("usuario") String sUsuario,
			@RequestParam("notarias") String sNotarias
		) throws MessagingException {
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
			usuario = usuarioService.saveGestor(persona, direccion, telefono, usuario, notarias);
			return new ResponseEntity<>(usuario, HttpStatus.OK);	
		}
		catch (Exception e ) {
			log.debug(e.getMessage());
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
		
	}
	@GetMapping ("/{idArea}/usuario-areas")
	public ResponseEntity<com.gisnet.erpp.web.api.usuario.UsuarioDTO> getAreasIdByIdUser (@PathVariable ("idArea")Long idArea){
		return ResponseEntity.ok(usuarioService.findAreaIdByUsuarioId(idArea));
	}
	
	@PostMapping("/update-usuarioOfiA")
	public ResponseEntity<List<UsuarioOfiAreas>> updateUsuarioOfiA( @RequestBody List <UsuarioOfiAreas> usuario ){
		return ResponseEntity.ok(usuarioService.updateUsuarioOfiA(usuario));
	}
	
	@GetMapping("/usuario-actos/{id}")
	public ResponseEntity<List<UsuarioActos>> getUsuarioActos(@PathVariable("id") Long id) {
		return new ResponseEntity<>(usuarioService.getUsuarioActos(id), HttpStatus.OK); 
	}
	
	@GetMapping("/usuario-servicios/{id}")
	public ResponseEntity<List<UsuarioServicios>> getUsuarioServicios(@PathVariable("id") Long id) {
		return new ResponseEntity<>(usuarioService.getUsuarioServicios(id), HttpStatus.OK); 
	}
	
	@PostMapping("/update-usuario-actos/")
	public ResponseEntity<List<UsuarioActos>> updateUsuarioActos(@RequestBody List<UsuarioActos> usuario) throws URISyntaxException {
		return new ResponseEntity<>(usuarioService.updateUsuarioActos(usuario), HttpStatus.OK);
	}
	
	@PostMapping("/update-usuario-servicios/")
	public ResponseEntity<List<UsuarioServicios>> updateUsuarioServicios(@RequestBody List<UsuarioServicios> usuario) throws URISyntaxException {
		return new ResponseEntity<>(usuarioService.updateUsuarioServicios(usuario), HttpStatus.OK);
	}
	
	@GetMapping ("/usuarios-consulta")
	public ResponseEntity<Page<Usuario>> getUsuarios(Pageable pageable, String paterno, String materno, String nombre, Long tipoUsuarioId, String areasRoles, Boolean filterPermissions) {
		Set<Area> areaSet = new HashSet<>();
		
		tipoUsuarioId = 8L; // RPP5
		ArrayList<Long> listaRoles = new ArrayList<Long>();
		
		Long rolRegistradorId = 19L;   // Rol : Registrador
		Long rolValidadorId = 19L;   // Rol : Validador
		
		listaRoles.add(rolRegistradorId);
		listaRoles.add(rolValidadorId);
		
		Page<Usuario> page = usuarioService.findAllByTipoAndRolAndNombre(tipoUsuarioId, listaRoles, paterno, materno, nombre, pageable, areaSet, filterPermissions);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}
	@GetMapping("/usuarios-consulta-CyVF")
	public ResponseEntity<Page<Usuario>> getUsuariosCyVF(Pageable pageable, String paterno, String materno, String nombre, Long tipoUsuarioId, String areasRoles, Boolean filterPermissions){
		Set<Area> areaSet = new HashSet<>();
		
		tipoUsuarioId = 8L; // RPP5
		System.out.println("usuarios-consulta-CyVF " +  tipoUsuarioId );
		ArrayList<Long> listaRoles = new ArrayList<Long>();
		Long rolValidadorDeFolios = 5L;   // Rol : ValidadorDeFolios			
		listaRoles.add(rolValidadorDeFolios);	
		Page<Usuario> page = usuarioService.findAllByTipoAndRolAndNombreCyVF(tipoUsuarioId, listaRoles, paterno, materno, nombre, pageable, areaSet, filterPermissions);
;
		return new ResponseEntity<>(page, HttpStatus.OK);
	}

	@GetMapping ("/usuarios-consulta-solicitante")
	public ResponseEntity<Page<Usuario>> getUsuariosSolicitante(Pageable pageable, String paterno, String materno, String nombre, Long tipoUsuarioId, String areasRoles, Boolean filterPermissions) {
		Set<Area> areaSet = new HashSet<>();
		
		
		Page<Usuario> page = usuarioService.findAllByTipoSolicitanteAndNombre(tipoUsuarioId, paterno, materno, nombre, pageable, areaSet, filterPermissions);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}
	
	@GetMapping ("/usuarios-consulta-coordinador")
	public ResponseEntity<Page<Usuario>> getUsuariosCoordinador(Pageable pageable, String paterno, String materno, String nombre, Long tipoUsuarioId, String areasRoles, Boolean filterPermissions) {
		Set<Area> areaSet = new HashSet<>();
		
		ArrayList<Long> listaRoles = new ArrayList<Long>();
		
		Long rolCoordinadorId = 8L;   // Rol : Coordinador
		
		listaRoles.add(rolCoordinadorId);
		
		Page<Usuario> page = usuarioService.findAllByTipoAndRolAndNombre(tipoUsuarioId, listaRoles, paterno, materno, nombre, pageable, areaSet, filterPermissions);
		return new ResponseEntity<>(page, HttpStatus.OK);
	}
	
	@GetMapping("/valida-acto-servicios/{idUsuario}")
	public ResponseEntity<Long> getActosServicios(@PathVariable("idUsuario") Long id) {
		return new ResponseEntity<>(usuarioService.getActosServicios(id), HttpStatus.OK); 
	}
	
	@GetMapping("/genera-token")
	public ResponseEntity<String> getToken() {
		return new ResponseEntity<>(usuarioService.getToken(), HttpStatus.OK); 
	}
	
	@GetMapping("/findUsuarioByRol")
	public ResponseEntity<UsuarioRol> getUsuarioRol(){
		return ResponseEntity.ok(usuarioService.getUsuarioRol());
	}

	@GetMapping("/findUsuario")
	public ResponseEntity<Usuario> getUsuario(){
		return ResponseEntity.ok(usuarioService.getUsuario());
	}

	@PostMapping("/logs")
	public ResponseEntity<?> setLogs(@RequestBody LogsVO logs) throws URISyntaxException {
		String baseUrl = "https://s3.iwebsapp.com/logs";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JsonObject requestBody = new JsonObject();
		requestBody.addProperty("date", logs.getDate());
		requestBody.addProperty("time", logs.getTime());
		requestBody.addProperty("user", "");
		requestBody.addProperty("device", logs.getDevice());
		requestBody.addProperty("os", logs.getOs());
		requestBody.addProperty("page", logs.getPage());
		requestBody.addProperty("method", logs.getMethod());
		requestBody.addProperty("endpoint", logs.getEndpoint());
		requestBody.addProperty("params", logs.getParams());
		requestBody.addProperty("response", "");
		requestBody.addProperty("proyect", "erpp");
		requestBody.addProperty("environment", this.activeProfile);
		log.debug("requestBody " + requestBody);

		try {
			HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
			ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.POST, requestEntity, String.class);
			String responseBody = response.getBody();
			return new ResponseEntity<>(responseBody, HttpStatus.OK);
		} catch(Exception e) {
			log.error("Error en /api/usuario/logs : {}", e.getMessage());
		}

		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}

	@PostMapping("/sms")
	public ResponseEntity<?> setLogs(@RequestBody smsVO sms) throws URISyntaxException {
		String baseUrl = "https://sms.iwebsapp.com";
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		JsonObject requestBody = new JsonObject();
		requestBody.addProperty("phone", sms.getPhone());
		requestBody.addProperty("message", sms.getMessage());
		log.debug("requestBody " + requestBody);

		try {
			HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);
			ResponseEntity<String> response = restTemplate.exchange(baseUrl, HttpMethod.POST, requestEntity, String.class);
			String responseBody = response.getBody();
			return new ResponseEntity<>(responseBody, HttpStatus.OK);
		} catch(Exception e) {
			log.error("Error en /api/usuario/sms : {}", e.getMessage());
		}

		return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
	}


	@GetMapping("/logs/{date}")
	public ResponseEntity<?> setLogs(@PathVariable("date") String date) throws URISyntaxException {
		String baseUrl = "https://api.monit.iclouds.mx/erpp/" + date + "/" + this.activeProfile;
		//String baseUrl = "https://api.monit.iclouds.mx/axio/2024-02-22/release";
		RestTemplate restTemplate = new RestTemplate();

		ResponseEntity<String> response = restTemplate.getForEntity(baseUrl, String.class);

		if (response.getStatusCode().is2xxSuccessful()) {
			String responseBody = response.getBody();
			System.out.println("Respuesta del servidor: " + responseBody);
			return new ResponseEntity<>(responseBody, HttpStatus.OK);
		} else {
			System.err.println("Error al consumir el endpoint. Código de estado: " + response.getStatusCodeValue());
			return new ResponseEntity<>(null, HttpStatus.BAD_REQUEST);
		}

	}

}
