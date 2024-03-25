package com.gisnet.erpp.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import java.lang.IllegalArgumentException;

import javax.mail.MessagingException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thymeleaf.context.Context;

import com.gisnet.erpp.domain.Usuario;
import com.gisnet.erpp.domain.UsuarioRegistro;
import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.repository.UsuarioRegistroRepository;
import com.gisnet.erpp.repository.UsuarioRolRepository;

@Service
public class UsuarioRegistroService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
			
	@Autowired
	UsuarioRolRepository usuarioRolRepository;
	
	
	
	@Autowired
	UsuarioRegistroRepository usuarioRegistroRepository;
		
	@Autowired
	MailSenderService mailSenderService;
	
	@Autowired
	ParametroRepository parametroRepository;
	
	
	@Transactional
	public UsuarioRegistro creaUsuarioRegistro(String email) throws MessagingException{
		String token = UUID.randomUUID().toString();
		UsuarioRegistro usuarioRegistro = null;
		Optional<UsuarioRegistro> us = usuarioRegistroRepository.findOneByEmail(email);
		if (us.isPresent()){
			usuarioRegistro = us.get();			
		} else {
			usuarioRegistro = new UsuarioRegistro();
		}
		usuarioRegistro.setEmail(email);
		usuarioRegistro.setVerificacionToken(token);
		usuarioRegistro.setVerificacionFechaExp(calculaFechaExpiracionToken(Integer.parseInt(parametroRepository.findByCve("TOKEN_REGISTRO_MINUTOS_VIGENCIA").getValor())));
		
		usuarioRegistroRepository.save(usuarioRegistro);
		
        Context context = new Context();
        context.setVariable("url", parametroRepository.findByCve("QR_BASE_URI").getValor() + "/#/nuevo-post-registro/"+usuarioRegistro.getVerificacionToken());
        context.setVariable("minutos", Integer.parseInt(parametroRepository.findByCve("TOKEN_REGISTRO_MINUTOS_VIGENCIA").getValor()));
		
		mailSenderService.sendMail(parametroRepository.findByCve("MAIL_USERNAME").getValor(), email, "Proceso de registro, nuevo usuario", "usuarioRegistroTemplate" , context);
		
		return usuarioRegistro;		
	}
	
	@Transactional
	public UsuarioRegistro creaRegistroConfirmacion(String email , String nombre) throws MessagingException{
		String token = UUID.randomUUID().toString();
		UsuarioRegistro usuarioRegistro = null;
		Optional<UsuarioRegistro> us = usuarioRegistroRepository.findOneByEmail(email);
		if (us.isPresent()){
			usuarioRegistro = us.get();			
		} else {
			usuarioRegistro = new UsuarioRegistro();
		}
		usuarioRegistro.setEmail(email);
		usuarioRegistro.setVerificacionToken(token);
		usuarioRegistro.setVerificacionFechaExp(calculaFechaExpiracionToken(Integer.parseInt(parametroRepository.findByCve("TOKEN_CONFIRMACION_MINUTOS_VIGENCIA").getValor())));
		
		usuarioRegistroRepository.save(usuarioRegistro);
		
        Context context = new Context();
        context.setVariable("url", parametroRepository.findByCve("QR_BASE_URI").getValor() + "/#/complete-register/"+usuarioRegistro.getVerificacionToken());
        context.setVariable("horas", Integer.parseInt(parametroRepository.findByCve("TOKEN_CONFIRMACION_MINUTOS_VIGENCIA").getValor())/60);
		
		mailSenderService.sendMail(parametroRepository.findByCve("MAIL_USERNAME").getValor(), email, "Confirmacio de registro, nuevo usuario", "usuarioConfirmacionTemplate" , context);
		
		return usuarioRegistro;		
	}
	
	@Transactional
	public UsuarioRegistro requestPasswordReset(Usuario usuario) throws MessagingException{
		String token = UUID.randomUUID().toString();
		UsuarioRegistro usuarioRegistro = null;
		String email = usuario.getEmail();
		Optional<UsuarioRegistro> us = usuarioRegistroRepository.findOneByEmail(email);
		if (us.isPresent()){
			usuarioRegistro = us.get();			
		} else {
			usuarioRegistro = new UsuarioRegistro();
		}
		usuarioRegistro.setEmail(email);
		usuarioRegistro.setVerificacionToken(token);
		usuarioRegistro.setVerificacionFechaExp(calculaFechaExpiracionToken(Integer.parseInt(parametroRepository.findByCve("TOKEN_REGISTRO_MINUTOS_VIGENCIA").getValor())));
		
		usuarioRegistroRepository.save(usuarioRegistro);
		
        Context context = new Context();
        context.setVariable("nombre", usuario.getNombreCompleto());
        context.setVariable("url", parametroRepository.findByCve("QR_BASE_URI").getValor() + "/#/reset-password/"+usuarioRegistro.getVerificacionToken());
        context.setVariable("minutos", Integer.parseInt(parametroRepository.findByCve("TOKEN_REGISTRO_MINUTOS_VIGENCIA").getValor()));
		
		mailSenderService.sendMail(parametroRepository.findByCve("MAIL_USERNAME").getValor(), email, "Restablecer contraseña", "resetPasswordTemplate" , context);
		
		return usuarioRegistro;		
	}
	
	
	
	public boolean validaToken(String token){
		Optional<UsuarioRegistro> us = usuarioRegistroRepository.findOneByVerificacionToken(token);
		if(us.isPresent() && us.get().getVerificacionFechaExp().after(new Date())){
			return true;
		} 
		return false;
	}
	
	public String getEmailFromToken(String token) throws IllegalArgumentException{
		Optional<UsuarioRegistro> us = usuarioRegistroRepository.findOneByVerificacionToken(token);
		if(us.isPresent() && us.get().getVerificacionFechaExp().after(new Date())){
			return us.get().getEmail();
		} 
		throw new IllegalArgumentException();
	}
	
	
	
	private Date calculaFechaExpiracionToken(Integer minutos) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(new Timestamp(cal.getTime().getTime()));
        cal.add(Calendar.MINUTE,minutos);
        return new Date(cal.getTime().getTime());
    }
	
	
	

	
}
