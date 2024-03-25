package com.gisnet.erpp.service;

import static org.assertj.core.api.Assertions.setAllowComparingPrivateFields;

//import static org.mockito.Matchers.intThat;

import java.text.Normalizer;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.HashSet;
import java.util.List;

import java.lang.RuntimeException;
import java.sql.Timestamp;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.lang3.time.DateUtils;
import org.thymeleaf.context.Context;

import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.UsuarioActos;
import com.gisnet.erpp.domain.GestorNotario;
import com.gisnet.erpp.domain.UsuarioArea;
import com.gisnet.erpp.domain.UsuarioRegistro;
import com.gisnet.erpp.domain.UsuarioRol;
import com.gisnet.erpp.domain.UsuarioServicios;
import com.gisnet.erpp.domain.Dependencia;
import com.gisnet.erpp.domain.Area;
import com.gisnet.erpp.domain.AreaClasifActo;
import com.gisnet.erpp.domain.Rol;
import com.gisnet.erpp.domain.Servicio;
import com.gisnet.erpp.domain.Funcion;
import com.gisnet.erpp.domain.AreaRol;
import com.gisnet.erpp.domain.FuncionRolUsu;
import com.gisnet.erpp.domain.Notario;
import com.gisnet.erpp.domain.Oficina;
import com.gisnet.erpp.domain.UsuarioAreaRol;
import com.gisnet.erpp.domain.UsuarioClasifActoServicio;
import com.gisnet.erpp.domain.UsuarioOfiAreas;
import com.gisnet.erpp.domain.Direccion;
import com.gisnet.erpp.domain.FuncionRol;
import com.gisnet.erpp.domain.Persona;
import com.gisnet.erpp.domain.Telefono;
import com.gisnet.erpp.domain.TipoActo;
import com.gisnet.erpp.domain.TipoUsuario;

import com.gisnet.erpp.repository.UsuarioRepository;
import com.gisnet.erpp.repository.AreaClasifActoRepository;
import com.gisnet.erpp.repository.AreaRolRepository;
import com.gisnet.erpp.repository.UsuarioRolRepository;
import com.gisnet.erpp.repository.UsuarioServicioRepository;
import com.gisnet.erpp.repository.DireccionRepository;
import com.gisnet.erpp.repository.NacionalidadRepository;
import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.repository.PersonaRepository;
import com.gisnet.erpp.repository.RolRepository;
import com.gisnet.erpp.repository.ServicioRepository;
import com.gisnet.erpp.repository.FuncionRolUsuRepository;
import com.gisnet.erpp.repository.UsuarioAreaRolRepository;
import com.gisnet.erpp.repository.UsuarioClasifActoServicioRepository;
import com.gisnet.erpp.repository.UsuarioOfiAreasRepository;
import com.gisnet.erpp.repository.DependenciaRepository;
import com.gisnet.erpp.repository.DiaInhabilRepository;
import com.gisnet.erpp.repository.GestorNotarioRepository;
import com.gisnet.erpp.repository.TelefonoRepository;
import com.gisnet.erpp.repository.TipoActoRepository;
import com.gisnet.erpp.repository.NotarioRepository;
import com.gisnet.erpp.repository.TipoPersonaRepository;
import com.gisnet.erpp.repository.TipoUsuarioRepository;
import com.gisnet.erpp.repository.UsuNotarioRepository;
import com.gisnet.erpp.repository.UsuarioActosRepository;
import com.gisnet.erpp.repository.UsuarioAreaRepository;
import com.gisnet.erpp.repository.UsuarioRegistroRepository;
import com.gisnet.erpp.vo.UsuarioVO2;
import com.gisnet.erpp.web.api.account.PostRegistroDTO;
import com.gisnet.erpp.web.api.usuario.UsuarioDTO;
import com.google.common.base.Throwables;
import com.gisnet.erpp.security.PasswordGenerator;
import com.gisnet.erpp.security.SecurityUtils;
import com.gisnet.erpp.util.UtilFecha;

import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class UsuarioService {

	public static final Pattern DIACRITICS = Pattern.compile("[\\p{InCombiningDiacriticalMarks}\\p{IsLm}\\p{IsSk}]+");

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	UsuarioRepository usuarioRepository;

	@Autowired
	GestorNotarioRepository gestorNotarioRepository;

	@Autowired
	EstadoService estadoService;

	@Autowired
	MunicipioService municipioService;

	@Autowired
	UsuarioRolRepository usuarioRolRepository;

	@Autowired
	UsuarioAreaRepository usuarioAreaRepository;

	@Autowired
	UsuNotarioRepository usuNotarioRepository;

	@Autowired
	MailSenderService mailSenderService;

	@Autowired
	ParametroRepository parametroRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	@Autowired
	UsuarioRegistroRepository usuarioRegistroRepository;

	@Autowired
	PersonaRepository personaRepository;

	@Autowired
	DireccionRepository direccionRepository;

	@Autowired
	TelefonoRepository telefonoRepository;

	@Autowired
	TipoPersonaRepository tipoPersonaRepository;

	@Autowired
	NacionalidadRepository nacionalidadRepository;

	@Autowired
	DependenciaRepository dependenciaRepository;

	@Autowired
	RolService rolService;

	@Autowired
	RolRepository rolRepository;

	@Autowired
	AreaRolRepository areaRolRepository;

	@Autowired
	FuncionRolUsuRepository funcionRolUsuRepository;

	@Autowired
	UsuarioAreaRolRepository usuarioAreaRolRepository;

	@Autowired
	TipoUsuarioRepository tipoUsuarioRepository;

	@Autowired
	NotarioRepository notarioRepository;

	@Autowired
	UsuarioRegistroService usuarioRegistroService;

	@Autowired
	UsuarioOfiAreasRepository usuarioOfiAreasRepository;

	@Autowired
	UsuarioClasifActoServicioRepository usuarioClasifActoServicioRepository;

	@Autowired
	UsuarioActosRepository usuarioActosRepository;

	@Autowired
	AreaClasifActoRepository areaClasifActoRepository;

	@Autowired
	UsuarioServicioRepository usuarioServicioRepository;
	
	@Autowired
	TipoActoRepository tipoActoRepository;
	
	@Autowired
	ServicioRepository servicioRepository;
	
	@Autowired
	UsuarioAreaService usuarioAreaService;
	
	@Autowired
	DiaInhabilRepository diaInhabilRepository;
	
	@Transactional
	public Usuario save(Usuario usuario, Set<Long> areasRoles, Set<Rol> roles) throws Exception {
		if (usuario.getEmail() == null)
			usuario.setEmail(usuario.getPersona().getEmail());

		usuarioRepository.saveAndFlush(usuario);

		roles.forEach((x) -> {
			Rol therol = rolRepository.findOne(x.getId());
			UsuarioRol urol = new UsuarioRol();
			urol.setUsuario(usuario);
			urol.setRol(therol);
			usuarioRolRepository.save(urol);
			therol.getFuncionRolesParaRols().forEach(y -> {
				FuncionRolUsu furolu = new FuncionRolUsu();
				furolu.setFuncionRol(y);
				furolu.setUsuario(usuario);
				furolu.setIsActivo(true);
				funcionRolUsuRepository.save(furolu);
			});
		});

		areasRoles.forEach(x -> {
			UsuarioAreaRol uAreaRol = new UsuarioAreaRol();
			UsuarioArea uArea = new UsuarioArea();
			AreaRol aRol = areaRolRepository.findOne(x);
			uAreaRol.setAreaRol(aRol);
			uAreaRol.setUsuario(usuario);
			usuarioAreaRolRepository.save(uAreaRol);
			uArea.setArea(aRol.getArea());
			uArea.setUsuario(usuario);

			try {
				usuarioAreaRepository.saveAndFlush(uArea);
				usuarioOfiAreasRepository.saveAndFlush(new UsuarioOfiAreas().activo(true).area(aRol.getArea())
						.oficina(usuario.getOficina()).usuario(usuario));
			} catch (Exception e) {
				log.debug(e.getMessage());
				throw Throwables.propagate(e);
			}
		});

		usuario.getUsuNotariosParaUsuarios().forEach(x -> usuNotarioRepository.save(x));

		usuarioRolRepository.flush();
		funcionRolUsuRepository.flush();
		usuarioAreaRolRepository.flush();
		usuNotarioRepository.flush();
		try {
			////////// cambio que se realizó desde la semana pasada indicado con una rayita////////
			usuarioRegistroService.creaRegistroConfirmacion(usuario.getPersona().getEmail(),
					usuario.getPersona().getNombre() + "  " + usuario.getPersona().getPaterno());
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw new RuntimeException("no se ha podido enviar el email");
		}
		return usuario;
	}
	
	@Transactional
	public Usuario update(Usuario userInfo, Set<Long> areasRoles, Set<Rol> roles) throws Exception {
		
		Usuario usuario = usuarioRepository.findOne(userInfo.getId());
		if (usuario.getEmail() == null)
			usuario.setEmail(usuario.getPersona().getEmail());
		usuario.setNumEmpleado(userInfo.getNumEmpleado());
		usuario.setOficina(userInfo.getOficina());
		usuarioRepository.saveAndFlush(usuario);
		log.debug(usuario.toString());
		usuario.getUsuarioRolesParaUsuarios().forEach((x) -> {
			usuarioRolRepository.delete(x.getId());
		});
		roles.forEach((x) -> {
			UsuarioRol urol = new UsuarioRol();
			urol.setUsuario(usuario);
			urol.setRol(x);
			usuarioRolRepository.save(urol);
			x.getFuncionRolesParaRols().forEach(y -> {
				FuncionRolUsu furolu = new FuncionRolUsu();
				furolu.setFuncionRol(y);
				furolu.setUsuario(usuario);
				furolu.setIsActivo(true);
				funcionRolUsuRepository.save(furolu);
			});
		});
		usuarioRolRepository.flush();
		funcionRolUsuRepository.flush();
		
		usuario.getUsuarioAreasParaUsuarios().forEach((x) -> {
			usuarioAreaRepository.delete(x.getId());
		});
		usuario.getUsuarioAreasRolesParaUsuarios().forEach((x) -> {
			usuarioAreaRolRepository.delete(x.getId());
		});
		usuarioAreaRepository.flush();
		usuarioAreaRolRepository.flush();
		
		areasRoles.forEach((aRolId) -> {
			AreaRol aRol = areaRolRepository.findOne(aRolId);
			UsuarioAreaRol uAreaRol = new UsuarioAreaRol();
			UsuarioArea uArea = new UsuarioArea();
			
			uAreaRol.setAreaRol(aRol);
			uAreaRol.setUsuario(usuario);
			usuarioAreaRolRepository.save(uAreaRol);
			uArea.setArea(aRol.getArea());
			uArea.setUsuario(usuario);
			usuarioAreaRepository.save(uArea);
		});
		
		usuarioAreaRepository.flush();
		usuarioAreaRolRepository.flush();
		
		return usuario;
	}
	
	

	@Transactional
	public Usuario updatePerfiles(Usuario usuario, Set<Long> areasRoles, Set<Rol> roles) throws Exception {
		Usuario updatableUser = usuarioRepository.findOne(usuario.getId());

		updatableUser.setActivo(usuario.isActivo());
		usuarioRepository.saveAndFlush(updatableUser);
		List<Rol> rolesAsignables = rolService.findAllRppRolesAssignableByUserName();
		for (Rol rolA : rolesAsignables) {
			if (!roles.contains(rolA)) {
				UsuarioRol urol = usuarioRolRepository.findOneByRolIdAndUsuarioId(rolA.getId(), updatableUser.getId())
						.orElse(null);

				if (urol != null) {
					rolA.getFuncionRolesParaRols().forEach(x -> {
						FuncionRolUsu frolu = null;
						frolu = funcionRolUsuRepository.findOneByUsuarioIdAndFuncionRolId(updatableUser.getId(),
								x.getId());
						if (frolu != null) {
							funcionRolUsuRepository.delete(frolu);
						}
					});
					usuarioRolRepository.delete(urol);
				}
			} else {
				UsuarioRol urol = usuarioRolRepository.findOneByRolIdAndUsuarioId(rolA.getId(), updatableUser.getId())
						.orElse(null);
				if (urol == null) {
					urol = new UsuarioRol();
					urol.setUsuario(usuario);
					urol.setRol(rolA);
					usuarioRolRepository.save(urol);
					rolA.getFuncionRolesParaRols().forEach(y -> {
						FuncionRolUsu furolu = new FuncionRolUsu();
						furolu.setFuncionRol(y);
						furolu.setUsuario(updatableUser);
						furolu.setIsActivo(true);
						funcionRolUsuRepository.save(furolu);
					});

				}
			}
		}

		usuarioRolRepository.flush();
		funcionRolUsuRepository.flush();
		Set<AreaRol> areasRolesLogguedUser = new HashSet<>();
		this.findOneWithRolesById(SecurityUtils.getCurrentUserId()).getUsuarioAreasRolesParaUsuarios()
				.forEach(x -> areasRolesLogguedUser.add(x.getAreaRol()));

		for (Long areaRolId : areasRoles) {

			UsuarioAreaRol uAreaRol = new UsuarioAreaRol();
			UsuarioArea uArea = new UsuarioArea();
			AreaRol aRol = areaRolRepository.findOne(areaRolId);
			if (areasRolesLogguedUser.contains(aRol)) {
				uAreaRol = usuarioAreaRolRepository.findOneByUsuarioIdAndAreaRolId(updatableUser.getId(), aRol.getId())
						.orElse(null);

				if (uAreaRol == null) {
					uAreaRol = new UsuarioAreaRol();
					uAreaRol.setAreaRol(aRol);
					uAreaRol.setUsuario(usuario);
					usuarioAreaRolRepository.save(uAreaRol);
					uArea.setArea(aRol.getArea());
					uArea.setUsuario(usuario);
					try {
						usuarioAreaRepository.saveAndFlush(uArea);
					} catch (Exception e) {
					}
				}
				areasRolesLogguedUser.remove(aRol);
			}
		}
		usuarioAreaRepository.flush();

		for (AreaRol areaRol : areasRolesLogguedUser) {
			areaRol.getArea().getUsuarioAreasParaAreas().forEach(x -> {
				usuarioAreaRepository.delete(x);
				usuarioAreaRepository.flush();
			});
			UsuarioAreaRol uarol = usuarioAreaRolRepository
					.findOneByUsuarioIdAndAreaRolId(updatableUser.getId(), areaRol.getId()).orElse(null);
			if (uarol != null) {
				usuarioAreaRolRepository.delete(uarol);
			}
		}
		areaRolRepository.flush();
		usuarioAreaRolRepository.flush();
		return updatableUser;
	}

	@Transactional
	public Usuario update(UsuarioVO2 usuario) {
		System.out.println("/////////////////////////////");
		System.out.println(usuario);
		System.out.println(usuario.getUsuarioAreasParaUsuarios());
		System.out.println(usuario.getUsuarioRolesParaUsuarios());
		Usuario theUser = usuario.transformIntoUsuario();
		Usuario old = usuarioRepository.findOne(theUser.getId());
		old.getUsuarioRolesParaUsuarios().forEach(x -> usuarioRolRepository.delete(x.getId()));
		old.getUsuarioAreasParaUsuarios().forEach(x -> usuarioAreaRepository.delete(x.getId()));
		usuarioRolRepository.flush();
		usuarioAreaRepository.flush();

		Usuario actual = usuarioRepository.findOne(theUser.getId());

		for (UsuarioRol rol : theUser.getUsuarioRolesParaUsuarios()) {
			try {
				if (rol.getRol().getId() == null) {
					continue;
				}
			} catch (Exception x) {
				continue;
			}
			UsuarioRol newRol = new UsuarioRol();
			newRol.setRol(rol.getRol());
			newRol.setUsuario(actual);
			actual.addUsuarioRolesParaUsuario(newRol);
			usuarioRolRepository.save(newRol);
		}
		usuarioRolRepository.flush();

		for (UsuarioArea area : theUser.getUsuarioAreasParaUsuarios()) {
			try {
				if (area.getArea().getId() == null) {
					continue;
				}
			} catch (Exception x) {
				continue;
			}

			UsuarioArea newArea = new UsuarioArea();
			newArea.setArea(area.getArea());
			newArea.setUsuario(actual);
			actual.addUsuarioAreasParaUsuario(newArea);
			usuarioAreaRepository.save(newArea);
		}
		usuarioAreaRepository.flush();

		return actual;
	}

	@Transactional(readOnly = true)
	public Page<Usuario> findAllByNombre(String paterno, String materno, String nombre, Long tipoUsuarioId,
			Pageable pageable, Set<Area> areaSet, Boolean validateOficina,Long usuarioRolId) {
		Long oficinaId = null;
		Usuario user = null;
		Integer nivel = null;

		if (validateOficina != null) {
			if (validateOficina) {
				user = usuarioRepository.findOne(SecurityUtils.getCurrentUserId());
				oficinaId = user.getOficina().getId();
				nivel = this.getMinRolLevelFromLogguedUser();
			}
		}
		System.out.println("472:oficina id " + oficinaId + " nivel " + nivel + " user " + user+ " tipoUsuario " + tipoUsuarioId);
		return usuarioRepository.findAllByNombre(paterno, materno, nombre, tipoUsuarioId, pageable, areaSet, oficinaId,
				nivel,usuarioRolId);
	}

	@Transactional(readOnly = true)
	public Usuario findOneWithRolesAnFuncionesByUserName() {

		return usuarioRepository.findOneWithRolesAnFuncionesByUserName(SecurityUtils.getCurrentUserLogin())
				.orElse(null);
	}

	@Transactional(readOnly = true)
	public Integer getMinRolLevelFromLogguedUser() {
		Set<UsuarioRol> usuarioRoles = usuarioRepository
				.findOneWithRolesAnFuncionesByUserName(SecurityUtils.getCurrentUserLogin()).orElse(null)
				.getUsuarioRolesParaUsuarios();
		int minval = 999999999;
		int tmp = 0;
		for (UsuarioRol urol : usuarioRoles) {
			try {
				tmp = urol.getRol().getNivel();
				if (tmp < minval) {
					minval = tmp;
				}
				tmp = 0;
			} catch (Exception x) {
			}
			tmp = 0;
		}
		return minval+1;
	}

	@Transactional(readOnly = true)
	public Optional<Usuario> findOneWithRolesAnFuncionesByUserName(String userName) {
		return usuarioRepository.findOneWithRolesAnFuncionesByUserName(userName);
	}

	@Transactional(readOnly = true)
	public Usuario findOneWithRolesAndFuncionesById(Long id) {
		return usuarioRepository.findOneWithRolesAndFuncionesById(id).orElse(null);
	}

	@Transactional(readOnly = true)
	public Usuario findOneWithRolesById(Long id) {
		return usuarioRepository.findOneWithRolesById(id).orElse(null);
	}

	@Transactional(readOnly = true)
	public Usuario findByNombre(String userName) {
		return usuarioRepository.findByUserName(userName);
	}

	@Transactional(readOnly = true)
	public Optional<Usuario> findOneByUserName(String userName) {
		return usuarioRepository.findOneByUserNameOptional(userName);
	}

	@Transactional(readOnly = true)
	public Usuario findByEmailPersona(String email) {
		return usuarioRepository.findByEmailPersona(email);
	}

	@Transactional(readOnly = true)
	public Optional<Usuario> findOneByEmail(String email) {
		return usuarioRepository.findOneByEmailIgnoreCase(email);
	}

	@Transactional
	public Usuario postRegistro(PostRegistroDTO postRegistro) throws Exception {
		Optional<UsuarioRegistro> us = usuarioRegistroRepository.findOneByVerificacionToken(postRegistro.getToken());
		if (!us.isPresent()) {
			throw new IllegalArgumentException();
		}

		String email = us.get().getEmail();

		PasswordGenerator passwordGenerator = new PasswordGenerator();
		String password = passwordGenerator.generate();

		UsuarioRegistro usuarioRegistro = us.get();
		usuarioRegistro.setVerificacionFechaExp(new Date());
		usuarioRegistroRepository.save(usuarioRegistro);

		Persona persona = new Persona();
		persona.setTipoPersona(tipoPersonaRepository.findOne(1L));
		persona.setActivo(true);
		persona.setNacionalidad(nacionalidadRepository.findOne(1L));
		persona.setNombre(postRegistro.getNombre());
		persona.setPaterno(postRegistro.getPaterno());
		persona.setMaterno(postRegistro.getMaterno());
		persona.setFechaNac(postRegistro.getFechaNac());
		persona.setRfc(postRegistro.getRfc());
		persona.setCurp(postRegistro.getCurp());
		persona.setEmail(email);

		personaRepository.save(persona);

		Telefono telefono = new Telefono();
		telefono.setNumTelefono(postRegistro.getNumTelefono());
		telefono.setPrincipal(true);
		telefono.setPersona(persona);
		telefonoRepository.save(telefono);

		Direccion direccion = new Direccion();

		direccion.setCalle(postRegistro.getCalle());
		direccion.setNumExt(postRegistro.getNumExt());
		direccion.setNumInt(postRegistro.getNumInt());
		direccion.setCp(postRegistro.getCp());
		direccion.setEstado(postRegistro.getEstado());
		direccion.setMunicipio(postRegistro.getMunicipio());
		direccion.setActivo(true);
		direccion.setPersona(persona);

		direccionRepository.save(direccion);

		Usuario usuario = new Usuario();
		TipoUsuario tipoUsuario = tipoUsuarioRepository.findOne(postRegistro.getTipoUsuarioId());
		usuario.setTipoUsuario(tipoUsuario);
		usuario.setActivo(true);
		usuario.setUserName(email);
		usuario.setEmail(email);
		usuario.setPersona(persona);
		usuario.setActivo(true);
		usuario.setContrasenia(passwordEncoder.encode(password));
		usuarioRepository.save(usuario);

		if (tipoUsuario.getRol() == null) {
			throw new IllegalArgumentException("El rol del tipo Usuario es nulo");
		}

		UsuarioRol usuarioRol = new UsuarioRol();
		usuarioRol.setUsuario(usuario);
		usuarioRol.setRol(tipoUsuario.getRol());

		usuarioRolRepository.save(usuarioRol);

		Context context = new Context();
		context.setVariable("persona", persona);
		context.setVariable("password", password);

		try {
			mailSenderService.sendMail(parametroRepository.findByCve("MAIL_USERNAME").getValor(), email, "Constraseña",
					"newPasswordTemplate", context);
		} catch (Exception e) {
			throw new RuntimeException("no se ha podido enviar el email");
		}
		return usuario;
	}

	@Transactional
	public Usuario completeRegistro(PostRegistroDTO postRegistro) {
		Optional<UsuarioRegistro> us = usuarioRegistroRepository.findOneByVerificacionToken(postRegistro.getToken());
		if (!us.isPresent()) {
			throw new IllegalArgumentException();
		}

		String email = us.get().getEmail();

		UsuarioRegistro usuarioRegistro = us.get();
		usuarioRegistro.setVerificacionFechaExp(new Date());
		usuarioRegistroRepository.save(usuarioRegistro);

		Usuario usuario = usuarioRepository.findByEmail(email);
		usuario.setActivo(true);
		usuario.setUserName(email);
		usuario.setEmail(email);

		usuario.setContrasenia(passwordEncoder.encode(postRegistro.getPassword()));
		usuarioRepository.save(usuario);

		UsuarioRol usuarioRol = new UsuarioRol();
		usuarioRol.setUsuario(usuario);
		usuarioRol.setRol(rolRepository.findOne(21L));

		usuarioRolRepository.save(usuarioRol);

		return usuario;
	}

	@Transactional
	public Usuario resetPassword(PostRegistroDTO postRegistro) {
		Optional<UsuarioRegistro> us = usuarioRegistroRepository.findOneByVerificacionToken(postRegistro.getToken());
		if (!us.isPresent()) {
			throw new IllegalArgumentException();
		}

		String email = us.get().getEmail();

		UsuarioRegistro usuarioRegistro = us.get();
		usuarioRegistro.setVerificacionFechaExp(new Date());
		usuarioRegistroRepository.save(usuarioRegistro);

		Usuario usuario = usuarioRepository.findByEmail(email);
		usuario.setActivo(true);
		usuario.setContrasenia(passwordEncoder.encode(postRegistro.getPassword()));
		usuarioRepository.saveAndFlush(usuario);

		return usuario;
	}

	@Transactional(readOnly = true)
	public Usuario findByEmail(String email) {

		return usuarioRepository.findByEmail(email);
	}

	@Transactional(readOnly = true)
	public List<Usuario> findByRolId(Long rolId) {

		return usuarioRepository.findByUsuarioRolesParaUsuariosRolId(rolId);
	}
	
	@Transactional(readOnly = true)
	public List<Usuario> findByRolCve(String clave) {

		return usuarioRepository.findByUsuarioRolesParaUsuariosRolCve(clave);
	}

	@Transactional(readOnly = true)
	public Usuario findOne(long id) {
		return usuarioRepository.findOne(id);
	}

	@Transactional(readOnly = true)
	public Usuario getLoguedUser() throws Exception {

		Usuario loguedUser = findOneWithRolesAnFuncionesByUserName();
		if (loguedUser != null) {
			return loguedUser;
		}
		throw new Exception("No existe un usuario autenticado");

	}

	@Transactional
	public String getEmailDomain() {
		try {
			return parametroRepository.findByCve("DOMINIO").getValor();
		} catch (Exception x) {
			return "";
		}

	}

	public String createSuggestedEmail(String nombre1, String nombre2) {
		String suggestedEmail = "";
		String domain = this.getEmailDomain();
		if (nombre1 == null || nombre2 == null) {
			return "";
		}
		nombre1 = cleanSpecialCharacters(getFirstWordOnly(nombre1));
		nombre2 = cleanSpecialCharacters(nombre2);
		suggestedEmail = nombre1 + "." + nombre2 + domain;
		return suggestedEmail.toLowerCase();
	}

	public Boolean isNewValidEmail(String email) {
		if (email.length() > 0) {
			Optional<Usuario> user = this.findOneByUserName(email);
			return !user.isPresent();
		}
		return false;

	}

	private static String getFirstWordOnly(String str) {
		if (str.contains(" ")) {
			str = str.substring(0, str.indexOf(" "));
		}
		return str;
	}

	private static String cleanSpecialCharacters(String str) {
		str = Normalizer.normalize(str, Normalizer.Form.NFD);
		str = DIACRITICS.matcher(str).replaceAll("");
		str = str.replaceAll("\\s", "");
		return str;
	}

	public Set<Funcion> getFuncionesPadre(Long id) {
		Usuario user = usuarioRepository.findOne(id);
		Set<Rol> roles = new HashSet<>();
		Set<Funcion> funciones = new HashSet<>();
		user.getUsuarioRolesParaUsuarios().forEach(x -> roles.add(x.getRol()));
		for (Rol rol : roles) {
			for (FuncionRol frol : rol.getFuncionRolesParaRols()) {
				funciones.add(frol.getFuncion());
			}
		}

		funciones = funciones.stream().filter(x -> x.getFuncionPadre() == null).collect(Collectors.toSet());

		return funciones;
	}

	public Set<Funcion> getFuncionesHijas(Long id) {
		Usuario user = usuarioRepository.findOne(id);
		Set<Rol> roles = new HashSet<>();
		Set<Funcion> funciones = new HashSet<>();
		user.getUsuarioRolesParaUsuarios().forEach(x -> roles.add(x.getRol()));
		for (Rol rol : roles) {
			for (FuncionRol frol : rol.getFuncionRolesParaRols()) {
				funciones.add(frol.getFuncion());
			}
		}

		funciones = funciones.stream().filter(x -> x.getFuncionPadre() != null).collect(Collectors.toSet());

		return funciones;
	}

	public Set<FuncionRol> findEnabledFunctions(Long id) {

		Set<FuncionRol> funrol = new HashSet<>();
		Set<FuncionRolUsu> frolu = funcionRolUsuRepository.findAllByActivoTrueAndUsuarioId(id);
		frolu.forEach(x -> funrol.add(x.getFuncionRol()));

		return funrol;
	}

	public Set<FuncionRol> findEnabledFunctionsByUsuarioIdAndRolId(Long usuarioId, Long rolId) {
		Usuario user = usuarioRepository.findOne(usuarioId);
		Set<UsuarioRol> usuRoles = user.getUsuarioRolesParaUsuarios();
		Set<FuncionRol> funRol = new HashSet<>();
		Set<FuncionRol> filtro = new HashSet<>();
		Set<FuncionRolUsu> frolusuario = funcionRolUsuRepository.findAllByActivoTrueAndUsuarioId(usuarioId);

		for (UsuarioRol usuRol : usuRoles) {
			if (usuRol.getRol().getId().equals(rolId)) {
				funRol = usuRol.getRol().getFuncionRolesParaRols();
			}
		}

		for (FuncionRol fro : funRol) {
			for (FuncionRolUsu frolusu : frolusuario) {
				if (frolusu.getFuncionRol().getId().equals(fro.getId())) {
					filtro.add(fro);
				}
			}
		}
		return filtro;
	}

	@Transactional
	public Usuario updateFunciones(Usuario usuario, Set<Long> funcionesRoles) {
		Usuario user = usuarioRepository.getOne(usuario.getId());

		Set<FuncionRolUsu> fRolUsu = user.getFuncionesRolesParaUsuarios();

		fRolUsu.forEach(x -> {
			funcionRolUsuRepository.delete(x);
		});
		funcionRolUsuRepository.flush();
		FuncionRolUsu newFRolUsu = new FuncionRolUsu();
		Set<FuncionRol> fRoles = new HashSet<>();
		for (UsuarioRol uRol : user.getUsuarioRolesParaUsuarios()) {

			fRoles = uRol.getRol().getFuncionRolesParaRols();
			for (FuncionRol fRol : fRoles) {
				newFRolUsu = new FuncionRolUsu();
				newFRolUsu.setFuncionRol(fRol);
				newFRolUsu.setUsuario(user);
				if (funcionesRoles.contains(fRol.getId())) {
					funcionRolUsuRepository.save(newFRolUsu.Activo(true));
				} else {
					funcionRolUsuRepository.save(newFRolUsu.Activo(false));
				}
			}
		}
		funcionRolUsuRepository.flush();

		return user;
	}

	@Transactional
	public Usuario updateFuncionesByRol(Usuario usuario, Set<Long> funcionesRoles, Rol rol) {
		Usuario user = usuarioRepository.getOne(usuario.getId());
		Rol theRol = rolRepository.findOne(rol.getId());

		user.getFuncionesRolesParaUsuarios().forEach(x -> {
			if (x.getFuncionRol().getRol().getId().equals(theRol.getId())) {
				x.setIsActivo(false);
				funcionRolUsuRepository.save(x);
			}
		});
		funcionRolUsuRepository.flush();

		theRol.getFuncionRolesParaRols().forEach(x -> {
			FuncionRolUsu fRolUsu = null;
			if (funcionesRoles.contains(x.getId())) {
				fRolUsu = funcionRolUsuRepository.findOneByUsuarioIdAndFuncionRolId(user.getId(), x.getId());
				if (fRolUsu != null) {
					fRolUsu.setIsActivo(true);
				} else {
					fRolUsu = new FuncionRolUsu();
					fRolUsu.setFuncionRol(x);
					fRolUsu.setUsuario(user);
					fRolUsu.setIsActivo(true);
				}
				funcionRolUsuRepository.save(fRolUsu);
			}
		});
		funcionRolUsuRepository.flush();

		return user;
	}

	@Transactional
	public Boolean postRegisterSaveNotarioTitular(Persona persona, Notario notario, Direccion direccion,
			Telefono telefono, Usuario usuario, String token) throws Exception {
		Optional<UsuarioRegistro> us = usuarioRegistroRepository.findOneByVerificacionToken(token);
		if (!us.isPresent()) {
			throw new IllegalArgumentException("La solicitud ha expirado o no existe");
		}

		String email = us.get().getEmail();
		int addMinuteTime = 60;
		Date targetTime = new Date(); // now
		targetTime = DateUtils.addMinutes(targetTime, addMinuteTime);

		UsuarioRegistro usuarioRegistro = us.get();
		usuarioRegistro.setVerificacionFechaExp(targetTime);
		usuarioRegistroRepository.save(usuarioRegistro);
		usuario.setEmail(email);
		try {
			usuario = saveNotarioTitular(persona, notario, direccion, telefono, usuario);
			return true;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Transactional
	public Usuario saveNotarioTitular(Persona persona, Notario notario, Direccion direccion, Telefono telefono,
			Usuario usuario) throws Exception {
		PasswordGenerator passwordGenerator = new PasswordGenerator();
		String password = passwordGenerator.generate();

		UsuarioRol rolU = new UsuarioRol();
		rolU.setRol(rolService.findOneById(10L));

		log.debug(notario.toString());
		try {
			personaRepository.saveAndFlush(persona);
			usuario.setPersona(persona);
			usuario.setContrasenia(passwordEncoder.encode(password));
			notario.setPersona(persona);
			notarioRepository.saveAndFlush(notario);
			usuario.setNotario(notario);
			usuarioRepository.saveAndFlush(usuario);
			rolU.setUsuario(usuario);
			usuarioRolRepository.saveAndFlush(rolU);
			direccion.setNotario(notario);
			direccion.setPersona(persona);
			telefono.setNotario(notario);
			direccionRepository.saveAndFlush(direccion);
			telefonoRepository.saveAndFlush(telefono);
			this.setUsuarioFuncionesRoles(rolU.getRol(), usuario);
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw new Exception("no se pudo guardar el usuario");
		}
		Context context = new Context();
		context.setVariable("persona", persona);
		context.setVariable("password", password);

		try {
			mailSenderService.sendMail(parametroRepository.findByCve("MAIL_USERNAME").getValor(), usuario.getEmail(),
					"Constraseña", "newPasswordTemplate", context);
		} catch (Exception e) {
			throw new RuntimeException("no se ha podido enviar el email");
		}
		return usuario;
	}

	@Transactional
	public Boolean postRegisterSaveDependencia(Dependencia dependencia, Direccion direccion, Telefono telefono,
			Usuario usuario, String token) throws Exception {
		Optional<UsuarioRegistro> us = usuarioRegistroRepository.findOneByVerificacionToken(token);
		if (!us.isPresent()) {
			throw new IllegalArgumentException("La solicitud ha expirado o no existe");
		}

		String email = us.get().getEmail();
		int addMinuteTime = 60;
		Date targetTime = new Date(); // now
		targetTime = DateUtils.addMinutes(targetTime, addMinuteTime);

		UsuarioRegistro usuarioRegistro = us.get();
		usuarioRegistro.setVerificacionFechaExp(targetTime);
		usuarioRegistroRepository.save(usuarioRegistro);
		usuario.setEmail(email);

		try {
			usuario = saveDependencia(dependencia, direccion, telefono, usuario);
			return true;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Transactional
	public Usuario saveDependencia(Dependencia dependencia, Direccion direccion, Telefono telefono, Usuario usuario)
			throws Exception {

		PasswordGenerator passwordGenerator = new PasswordGenerator();
		String password = passwordGenerator.generate();
		Persona persona = new Persona();
		
		UsuarioRol rolU = new UsuarioRol();
		Long d  = dependencia.getTipoDependencia().getId();
		int dep = new Long(d).intValue();
		
		switch(dep) {
		case 1:
			rolU.setRol(rolService.findOneById(14L));
			break;
		case 2:
			rolU.setRol(rolService.findOneById(13L));
			break;
		case 3:
			rolU.setRol(rolService.findOneById(9L));
			break;
		case 4:
			rolU.setRol(rolService.findOneById(11L));
			break;
		case 5:
			rolU.setRol(rolService.findOneById(22L));
			break;
		case 6:
			rolU.setRol(rolService.findOneById(23L));
			break;
		case 7:
			rolU.setRol(rolService.findOneById(24L));
			break;
			}
		
	
		System.out.println("RolU  : " +rolU.getRol());
		try {

			persona.setActivo(true);
			persona.setNacionalidad(this.nacionalidadRepository.findOne(87L));
			persona.setTipoPersona(tipoPersonaRepository.findOne(1L));
			persona.setNombre(dependencia.getNombre());
			persona.setEmail(usuario.getUserName());
			personaRepository.saveAndFlush(persona);
			usuario.setPersona(persona);
			usuario.setContrasenia(passwordEncoder.encode(password));
			dependenciaRepository.saveAndFlush(dependencia);
			usuario.setDependencia(dependencia);
			usuarioRepository.saveAndFlush(usuario);
			rolU.setUsuario(usuario);
			usuarioRolRepository.saveAndFlush(rolU);
			direccion.setPersona(persona);
			direccion.setDependencia(dependencia);
			telefono.setDependencia(dependencia);
			direccionRepository.saveAndFlush(direccion);
			telefonoRepository.saveAndFlush(telefono);
			this.setUsuarioFuncionesRoles(rolU.getRol(), usuario);
		} catch (Exception e) {
			e.printStackTrace();
			log.debug(e.getMessage());
			throw new Exception("no se pudo guardar el usuario");
		}
		Context context = new Context();
		context.setVariable("persona", persona);
		context.setVariable("password", password);

		try {
			mailSenderService.sendMail(parametroRepository.findByCve("MAIL_USERNAME").getValor(), usuario.getEmail(),
					"Constraseña", "newPasswordTemplate", context);
		} catch (Exception e) {
			throw new RuntimeException("no se ha podido enviar el email");
		}
		return usuario;
	}

	@Transactional
	public Boolean postRegisterSavePublicoGeneral(Persona persona, Direccion direccion, Telefono telefono,
			Usuario usuario, String token) throws Exception {
		Optional<UsuarioRegistro> us = usuarioRegistroRepository.findOneByVerificacionToken(token);
		if (!us.isPresent()) {
			throw new IllegalArgumentException("La solicitud ha expirado o no existe");
		}

		String email = us.get().getEmail();
		int addMinuteTime = 60;
		Date targetTime = new Date(); // now
		targetTime = DateUtils.addMinutes(targetTime, addMinuteTime);

		UsuarioRegistro usuarioRegistro = us.get();
		usuarioRegistro.setVerificacionFechaExp(targetTime);
		usuarioRegistroRepository.save(usuarioRegistro);
		usuario.setEmail(email);

		try {
			usuario = savePublicoGeneral(persona, direccion, telefono, usuario);
			return true;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Transactional
	public Usuario savePublicoGeneral(Persona persona, Direccion direccion, Telefono telefono, Usuario usuario)
			throws Exception {

		PasswordGenerator passwordGenerator = new PasswordGenerator();
		String password = passwordGenerator.generate();
		UsuarioRol rolU = new UsuarioRol();
		rolU.setRol(rolService.findOneById(21L));

		try {
			personaRepository.saveAndFlush(persona);
			usuario.setPersona(persona);
			usuario.setContrasenia(passwordEncoder.encode(password));
			usuarioRepository.saveAndFlush(usuario);
			rolU.setUsuario(usuario);
			usuarioRolRepository.saveAndFlush(rolU);
			direccion.setPersona(persona);
			telefono.setPersona(persona);
			direccionRepository.saveAndFlush(direccion);
			telefonoRepository.saveAndFlush(telefono);
			this.setUsuarioFuncionesRoles(rolU.getRol(), usuario);
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw new Exception("no se pudo guardar el usuario");
		}
		Context context = new Context();
		context.setVariable("persona", persona);
		context.setVariable("password", password);

		try {
			mailSenderService.sendMail(parametroRepository.findByCve("MAIL_USERNAME").getValor(), usuario.getEmail(),
					"Constraseña", "newPasswordTemplate", context);
		} catch (Exception e) {
			throw new RuntimeException("no se ha podido enviar el email");
		}
		return usuario;
	}

	@Transactional
	public Boolean postRegisterSaveGestor(Persona persona, Direccion direccion, Telefono telefono, Usuario usuario,
			String token, List<Notario> notarias) throws Exception {
		Optional<UsuarioRegistro> us = usuarioRegistroRepository.findOneByVerificacionToken(token);
		if (!us.isPresent()) {
			throw new IllegalArgumentException("La solicitud ha expirado o no existe");
		}

		String email = us.get().getEmail();
		usuario.setEmail(email);
		int addMinuteTime = 60;
		Date targetTime = new Date(); // now
		targetTime = DateUtils.addMinutes(targetTime, addMinuteTime);

		UsuarioRegistro usuarioRegistro = us.get();
		usuarioRegistro.setVerificacionFechaExp(targetTime);
		usuarioRegistroRepository.save(usuarioRegistro);
		try {
			usuario = saveGestor(persona, direccion, telefono, usuario, notarias);
			return true;
		} catch (Exception e) {
			throw new Exception(e.getMessage());
		}
	}

	@Transactional
	public Usuario saveGestor(Persona persona, Direccion direccion, Telefono telefono, Usuario usuario,
			List<Notario> notarias) throws Exception {

		PasswordGenerator passwordGenerator = new PasswordGenerator();
		String password = passwordGenerator.generate();
		UsuarioRol rolU = new UsuarioRol();
		rolU.setRol(rolService.findOneById(12L));

		try {
			personaRepository.saveAndFlush(persona);
			usuario.setPersona(persona);
			usuario.setContrasenia(passwordEncoder.encode(password));
			usuarioRepository.saveAndFlush(usuario);
			direccion.setPersona(persona);
			telefono.setPersona(persona);
			direccionRepository.saveAndFlush(direccion);
			telefonoRepository.saveAndFlush(telefono);
			rolU.setUsuario(usuario);
			usuarioRolRepository.saveAndFlush(rolU);
			this.setUsuarioFuncionesRoles(rolU.getRol(), usuario);
			notarias.forEach(x -> {
				GestorNotario gNotario = new GestorNotario();
				gNotario.setNotario(x);
				gNotario.setUsuario(usuario);
				gestorNotarioRepository.save(gNotario);
			});
			gestorNotarioRepository.flush();
		} catch (Exception e) {
			log.debug(e.getMessage());
			throw new Exception("no se pudo guardar el usuario");
		}
		Context context = new Context();
		context.setVariable("persona", persona);
		context.setVariable("password", password);

		try {
			mailSenderService.sendMail(parametroRepository.findByCve("MAIL_USERNAME").getValor(), usuario.getEmail(),
					"Constraseña", "newPasswordTemplate", context);
			for (Notario x : notarias) {
				context.setVariable("persona", x.getPersona());
				context.setVariable("password", "");
				mailSenderService.sendMail(parametroRepository.findByCve("MAIL_USERNAME").getValor(), x.getEmail(),
						"Proceso de registro", "usuarioRegistroTemplate", context);
			}

		} catch (Exception e) {
			throw new RuntimeException("no se ha podido enviar el email");
		}
		return usuario;
	}

	public UsuarioDTO findAreaIdByUsuarioId(Long idArea) {

		Usuario user = findByNombre(SecurityUtils.getCurrentUserLogin());
	    System.out.println("Usuario  " + user.getId());
		Long oficina = user.getOficina().getId();
		List<Long> area = new ArrayList<Long>();
		area.add(1L);
		area.add(3L);
		area.add(5L);
		UsuarioDTO usuario = new UsuarioDTO();
		
		List<UsuarioAreaRol> usuarioAreaRol = new ArrayList<UsuarioAreaRol>();
		List<AreaRol> areaRol = new ArrayList<AreaRol>();
		UsuarioAreaRol usuarioAR = new UsuarioAreaRol();
		List<Area> areas = new ArrayList<Area>();
		List<UsuarioAreaRol> uAreaRol = new ArrayList<UsuarioAreaRol>();
		
		List<UsuarioArea> usuarioArea = usuarioAreaRepository.findAllByUsuario(user);
		if(usuarioArea.size()== 0L) {
			return usuario;
		}
		
		Rol rol = new Rol();
		rol.setId(19L);

		if (idArea.equals(0L)) {
			System.out.println("entre");
			for (UsuarioArea us : usuarioArea) {
				areas.add(us.getArea());
			}
			areaRol = areaRolRepository.findAllByRolAndArea(areas, rol);
		} else {
			System.out.println("entre con IdArea");
			areaRol = areaRolRepository.findAllByRolAndAreaId(idArea, rol);
		}

		System.out.println("Areast " + areaRol.size());

		List<Long> iDs = new ArrayList<Long>();

		uAreaRol.clear();
		if (areaRol.size() == 0L) {
			return usuario;
		}
		uAreaRol = usuarioAreaRolRepository.findAllByAreaRol(areaRol);

		if(uAreaRol.size()==0) {
			return usuario;
		}
		for (UsuarioAreaRol uar : uAreaRol) {
			iDs.add(uar.getUsuario().getId());
			System.err.println("ID : " + uar.getId());
		}

		
		
		try {
			usuario.setUsuario(usuarioRepository.findAllByUsuariosId(iDs, oficina));
			if (idArea.equals(0L)) {
				usuario.setUsuarioOfiAreas(usuarioOfiAreasRepository.findAllByUsuarios(iDs, oficina, area));
			} else {
				usuario.setUsuarioOfiAreas(usuarioOfiAreasRepository.findAllByUsuariosIds(iDs, idArea, oficina));
			}
		} catch (Exception e) {
			System.out.println("Error " + e);
		}

		return usuario;
	}
	public List<UsuarioOfiAreas> updateUsuarioOfiA(List<UsuarioOfiAreas> usuario) {

		System.out.println("Estatus : " + usuario.get(0).getActivo());

		usuarioOfiAreasRepository.save(usuario);

		return usuario;
	}

	public UsuarioClasifActoServicio getUsuarioClasifActoServicio(Long id, Long clasifActo) {

		return usuarioClasifActoServicioRepository.findAllByUsuarioIdAndClasifActoId(id, clasifActo);
	}

	public List<UsuarioActos> getUsuarioActos(Long id) {

		return usuarioActosRepository.findAllByUsuarioIdOrderByTipoActoClasifActoNombre(id);
	}

	public List<UsuarioServicios> getUsuarioServicios(Long id) {

		return usuarioServicioRepository.findAllByUsuarioIdOrderByServicioNombre(id);

	}

	public List<UsuarioActos> updateUsuarioActos(List<UsuarioActos> usuActos) {

		usuarioActosRepository.save(usuActos);

		return usuarioActosRepository
				.findAllByUsuarioIdOrderByTipoActoClasifActoNombre(usuActos.get(0).getUsuario().getId());

	}

	public List<UsuarioServicios> updateUsuarioServicios(List<UsuarioServicios> usuServicios) {

		usuarioServicioRepository.save(usuServicios);

		return usuarioServicioRepository
				.findAllByUsuarioIdOrderByServicioNombre(usuServicios.get(0).getUsuario().getId());

	}

	// FIXME : Metodo para llenar actos segun seu area
	public void updateActosUsuariosAreas(Long idUsuario, Long areaId) {

		List<AreaClasifActo> clasifActo = areaClasifActoRepository.findAllByAreaId(areaId);

		System.out.println("Clasificaciones : " + clasifActo.size());

		for (AreaClasifActo c : clasifActo) {

			System.out.println("Actos : " + c.getClasifActo().getTipoActosParaClasifActos().size());

		}

	}

	@Transactional(readOnly = true)
	public Page<Usuario> findAllByTipoAndRolAndNombre(Long tipoUsuarioId, ArrayList<Long> listaRoles, String paterno, String materno,
			String nombre, Pageable pageable, Set<Area> areaSet, Boolean validateOficina) {
		Long oficinaId = null;
		Usuario user = null;
		Integer nivel = null;

		//System.out.println("1327:oficina id " + oficinaId + " nivel " + nivel + " user " + user);
		System.out.println("1328: tipoUsuarioId " +  tipoUsuarioId + " listaRoles " + listaRoles 
		+ " paterno " + paterno+" nombre " +nombre);
		
		
		return usuarioRepository.findAllByTipoAndRolAndNombre(tipoUsuarioId, listaRoles, paterno, materno, nombre, pageable,
				areaSet, oficinaId, nivel);
	}

	@Transactional(readOnly = true)
	public Page<Usuario> findAllByTipoAndRolAndNombreCyVF
	(
		Long tipoUsuarioId,
		ArrayList<Long> listaRoles,
		String paterno,
	    String materno,
		String nombre,
		Pageable pageable,
		Set<Area> areaSet,
		 Boolean validateOficina
	) {

		Long oficinaId = null;
		Usuario user = null;
		Integer nivel = null;
		//System.out.println("1327:oficina id " + oficinaId + " nivel " + nivel + " user " + user);
		System.out.println("1328: tipoUsuarioId " +  tipoUsuarioId + " listaRoles " + listaRoles 
		+ " paterno " + paterno+" nombre " +nombre);
		Usuario solicitante = getUsuario();
		Oficina oficinaSolicitante = solicitante.getOficina();
		System.out.println("oficinaSolicitante " +  oficinaSolicitante );
		oficinaId = oficinaSolicitante.getId();		
		return usuarioRepository.findAllByTipoAndRolAndNombre(tipoUsuarioId, listaRoles, paterno, materno, nombre, pageable,
				areaSet, oficinaId, nivel);
	}

	@Transactional(readOnly = true)
	public Page<Usuario> findAllByTipoSolicitanteAndNombre(Long tipoUsuarioId, String paterno, String materno,
			String nombre, Pageable pageable, Set<Area> areaSet, Boolean validateOficina) {
		Long oficinaId = null;
		Usuario user = null;
		Integer nivel = null;

		System.out.println("1339:oficina id " + oficinaId + " nivel " + nivel + " user " + user);
		return usuarioRepository.findAllByTipoSolicitanteAndNombre(tipoUsuarioId, paterno, materno, nombre, pageable,
				areaSet, oficinaId, nivel);
	}

	public Long getActosServicios(Long idUsuario) {

		Usuario usuario = usuarioRepository.getOne(idUsuario);
		List<UsuarioActos> UA = usuarioActosRepository.findAllByUsuario(usuario);

		if (UA == null || UA.size() == 0) {

			List<UsuarioOfiAreas> usuarioOfiArea = usuarioOfiAreasRepository.findAllByUsuario(usuario);
			List<Long> idAreas = new ArrayList<Long>();

			for (UsuarioOfiAreas usuOfiArea : usuarioOfiArea) {
				idAreas.add(usuOfiArea.getArea().getId());
			}

			List<AreaClasifActo> areaClasifActo = areaClasifActoRepository.findAllByAreaIds(idAreas);
			List<Long> clasificaciones = new ArrayList<Long>();
			List<UsuarioActos> usuarioActos = new ArrayList<UsuarioActos>();
			List<UsuarioServicios> usuarioServicios = new ArrayList<UsuarioServicios>();

			for (AreaClasifActo aCla : areaClasifActo) {
				clasificaciones.add(aCla.getClasifActo().getId());
			}

			List<TipoActo> tipoActo = tipoActoRepository.findAllByClasif(clasificaciones,Boolean.TRUE);
			List<Servicio> servicios = servicioRepository.findAllByClasif(clasificaciones,Boolean.TRUE);

			for (TipoActo tipos : tipoActo) {
				UsuarioActos usuAct = new UsuarioActos();
				usuAct.setIndRegistrar(false);
				usuAct.setIndTurnado(false);
				usuAct.setTipoActo(tipos);
				usuAct.setUsuario(usuario);
				usuarioActos.add(usuAct);
			}

			for (Servicio serv : servicios) {
				UsuarioServicios usuServ = new UsuarioServicios();
				usuServ.setIndRegistrar(false);
				usuServ.setIndTurnado(false);
				usuServ.setServicio(serv);
				usuServ.setUsuario(usuario);
				usuarioServicios.add(usuServ);
			}

			usuarioActosRepository.save(usuarioActos);
			usuarioServicioRepository.save(usuarioServicios);

		}

		return 1L;
	}
	
	private void setUsuarioFuncionesRoles(Rol rol, Usuario usuario) throws Exception{
		try {
			rol.getFuncionRolesParaRols().forEach(y -> {
				FuncionRolUsu furolu = new FuncionRolUsu();
				furolu.setFuncionRol(y);
				furolu.setUsuario(usuario);
				furolu.setIsActivo(true);
				funcionRolUsuRepository.save(furolu);
			});
			funcionRolUsuRepository.flush();
		}
		catch (Exception e) {
			log.debug(e.getMessage());
			throw new Exception("Error al guardar las funciones del usuario");
		}
	}
	
	public String getToken() {
		
		
		Usuario user = findByNombre(SecurityUtils.getCurrentUserLogin());
		
		String token = UUID.randomUUID().toString();
		
		UsuarioRegistro usuarioRegistro = new UsuarioRegistro();
		
		usuarioRegistro.setEmail(user.getEmail());
		usuarioRegistro.setVerificacionToken(token);
		usuarioRegistro.setVerificacionFechaExp(calculaFechaExpiracionToken(Integer.parseInt(parametroRepository.findByCve("TOKEN_REGISTRO_MINUTOS_VIGENCIA").getValor())));

		usuarioRegistroRepository.save(usuarioRegistro);
		
		return token;
		
	}
	
	private Date calculaFechaExpiracionToken(Integer minutos) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE,minutos);
        return new Date(cal.getTime().getTime());
    }

	public UsuarioRol getUsuarioRol() {
		Usuario user = findByNombre(SecurityUtils.getCurrentUserLogin());
		Long userId = user.getId();
		UsuarioRol usRol = usuarioRolRepository.findUsuarioRolByUsuarioId(userId);
		
		return usRol;
	}
	
	public List<Usuario> getUsuariosByAreAndOffice(Long idUser, Long officeId) {
		List<Usuario> usuarios = new ArrayList<Usuario>();
		List<UsuarioArea> usuarioAreas = usuarioAreaService.findAllByUsuarioId(idUser);
		if(usuarioAreas.size()>0) {
			List<Area> areas = new ArrayList<Area>();
			for(UsuarioArea usAreas : usuarioAreas) {
				areas.add(usAreas.getArea());
			}
				List<UsuarioOfiAreas> usuariosOfiAreas = usuarioOfiAreasRepository.findAllByAreaAndOficce(areas,officeId);
				if(usuariosOfiAreas.size()>0) {
					for(UsuarioOfiAreas usOfiArea : usuariosOfiAreas) {
						usuarios.add(usOfiArea.getUsuario());
					}
				}else {
					return  usuarios;
				}
		}else {
			return  usuarios;
		}
		
		
		return  usuarios;
		
	}

	public Usuario getUsuario() {
		Usuario us = findByNombre(SecurityUtils.getCurrentUserLogin());
		return us;

	}
	
	public boolean validaUsuarioPortal() {
		String horaInicio =  parametroRepository.findByCve("HORA_INICIO_PORTAL").getValor();
		String horaFin =  parametroRepository.findByCve("HORA_FIN_PORTAL").getValor();
		
		Calendar diaHoraActual = Calendar.getInstance();
		Calendar diaHoraInicio = getDateSetTime(horaInicio);
		Calendar diaHoraFin = getDateSetTime(horaFin);
		
		if(diaHoraActual.before(diaHoraInicio))
			return false;
		
		if(diaHoraActual.after(diaHoraFin))
			return false;
		int anio = diaHoraActual.get(Calendar.YEAR);
		int mes = diaHoraActual.get(Calendar.MONTH);
		int dia = diaHoraActual.get(Calendar.DAY_OF_MONTH);
		Long diaInhabil =  diaInhabilRepository.countByAnioMesDia(anio,mes,dia);
		if(diaInhabil > 0)
			return false;
		
		return UtilFecha.isDiaHabil(new Date());
	}
	
	/**
	 * Obtiene dia actual con la hora de argumento
	 * @param hora HH:mm:ss
	 * @return Calendar
	 */
	private Calendar getDateSetTime(String _hora) {
		int hora =  Integer.parseInt(_hora.split(":")[0]);
		int minutos =  Integer.parseInt(_hora.split(":")[1]);
		Calendar dia = Calendar.getInstance();
		dia.set(Calendar.HOUR_OF_DAY, hora);
		dia.set(Calendar.MINUTE, minutos);
		return dia;
	}
	
}
