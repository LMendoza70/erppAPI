package com.gisnet.erpp.config;

import java.util.Properties;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import com.gisnet.erpp.repository.ParametroRepository;

@Configuration  
public class MailConfiguration {
	@Autowired
	ParametroRepository parametroRepository;
	
	@Bean
    public JavaMailSender mailSender(){
		JavaMailSenderImpl mailSender = new JavaMailSenderImpl();
		mailSender.setHost(parametroRepository.findByCve("MAIL_HOST").getValor());
	    mailSender.setPort(Integer.parseInt(parametroRepository.findByCve("MAIL_PORT").getValor()));
	     
	    mailSender.setUsername(parametroRepository.findByCve("MAIL_USERNAME").getValor());
	    mailSender.setPassword(parametroRepository.findByCve("MAIL_PASSWORD").getValor());
	     
	    Properties props = mailSender.getJavaMailProperties();
	    props.put("mail.transport.protocol", parametroRepository.findByCve("MAIL_TRASPORT_PROTOCOL").getValor());
	    props.put("mail.smtp.auth", parametroRepository.findByCve("MAIL_SMTP_AUTH").getValor());
	    props.put("mail.smtp.starttls.enable", parametroRepository.findByCve("MAIL_SMTP_STARTTLS").getValor());
	    	   	    
	    return mailSender;
	}
}
