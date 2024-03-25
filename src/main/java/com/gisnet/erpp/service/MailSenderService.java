package com.gisnet.erpp.service;

import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.MimeMessage;

import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.InputStreamResource;
import org.springframework.mail.MailException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import com.gisnet.erpp.repository.ParametroRepository;
import com.gisnet.erpp.util.Constantes;

import javax.mail.internet.InternetAddress;
import javax.mail.Message.RecipientType;

import java.util.List;
import java.util.Properties;

import javax.mail.Address;

@Service
public class MailSenderService {
	private final Logger log = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	JavaMailSender mailSender;
	
    @Autowired
	private TemplateEngine templateEngine;
	
	@Autowired
	ParametroRepository parametroRepository;

	//ORM
    public void sendMailWithListAttachment(String from, String to, String subject, String template, Context context, List<byte[]> attachment) throws MessagingException {		
		Properties props =  System.getProperties();
		props.put("mail.transport.protocol", parametroRepository.findByCve("MAIL_TRASPORT_PROTOCOL").getValor());
		props.put("mail.smtp.port", Integer.parseInt(parametroRepository.findByCve("MAIL_PORT").getValor())); 
		props.put("mail.smtp.starttls.enable", parametroRepository.findByCve("MAIL_SMTP_STARTTLS").getValor());
		props.put("mail.smtp.auth", parametroRepository.findByCve("MAIL_SMTP_AUTH").getValor());

		Session session = Session.getDefaultInstance(props);

		MimeMessage message = new MimeMessage(session);		 	       

		try
		{
			
			MimeMessage msg = new MimeMessage(session);		 	       
			 
			 MimeMessageHelper messageHelper = new MimeMessageHelper(msg, true);
			messageHelper.setFrom(from);
			messageHelper.setTo(to);
			messageHelper.setSubject(subject);
			
		   String[] toIds = to.split(",");

		   Address[] ia = new InternetAddress[toIds.length];
			  int i = 0;
			  for (String address : toIds) {
				  ia[i] = new InternetAddress(address);
				  i++;
			  }

			  message.addRecipients(RecipientType.TO, ia);

			
			String content =  templateEngine.process(template, context);
			
			
			messageHelper.setText(content, true);
			messageHelper.addInline("header", new ClassPathResource("/static/img/menu_1.png"), "image/png");
			 
			 // Create a transport.
			 Transport transport = session.getTransport();
			
			System.out.println("Sending...");
			// Connect to Amazon SES using the SMTP username and password you specified above.
			transport.connect(parametroRepository.findByCve("MAIL_HOST").getValor(), 
							  parametroRepository.findByCve("USER_ACCOUNT").getValor(), 
							  parametroRepository.findByCve("MAIL_PASSWORD").getValor());
			
			if(attachment != null){
				int distincion=0;
				for (byte[] archivo :attachment ) {
					distincion+=1;
					messageHelper.addAttachment(subject+"_"+distincion, new ByteArrayResource(archivo), "application/pdf");	
				}
				
			}	        	
			
			
			// Send the email.
			transport.sendMessage(msg, msg.getAllRecipients());
			System.out.println("Email sent!");
		}
		catch (Exception ex) {
			System.out.println("The email was not sent.");
			System.out.println("Error message: " + ex.getMessage());
		}

	}

	public void sendMailWithAttachment(String from, String to, String subject, String template, Context context, byte[] attachment) throws MessagingException {
		MimeMessage message = mailSender.createMimeMessage();

		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom(from);
		//helper.setTo(to);
		helper.setSubject(subject);	
		String[] toIds = to.split(",");

		Address[] ia = new InternetAddress[toIds.length];
            int i = 0;
            for (String address : toIds) {
                ia[i] = new InternetAddress(address);
                i++;
            }

            message.addRecipients(RecipientType.TO, ia);



		String content = templateEngine.process(template, context);
		
		helper.setText(content, true);
		
        helper.addInline("escudo", new ClassPathResource("/static/img/escudo.png"), "image/png");
        
        helper.addInline("face", new ClassPathResource("/static/img/face.png"), "image/png");
        helper.addInline("rpp", new ClassPathResource("/static/img/rpp.png"), "image/png");
        
        if(attachment != null){
        	helper.addAttachment(subject, new ByteArrayResource(attachment), "application/pdf");
        }
		

		mailSender.send(message);
		
		log.debug("se envio el mail a " + to);

	}
	
	public void sendMail(String from, String to, String subject, String template, Context context) throws MessagingException {
		 MimeMessagePreparator messagePreparator = mimeMessage -> {
	            MimeMessageHelper messageHelper = new MimeMessageHelper(mimeMessage, true);
	            messageHelper.setFrom(from);
	            messageHelper.setTo(to);
	            messageHelper.setSubject(subject);
	            
		        String content = templateEngine.process(template, context);
		        
	            messageHelper.setText(content, true);
	            
	            messageHelper.addInline("escudo", new ClassPathResource("/static/img/escudo.png"), "image/png");
	            
	            messageHelper.addInline("face", new ClassPathResource("/static/img/face.png"), "image/png");
	            messageHelper.addInline("rpp", new ClassPathResource("/static/img/rpp.png"), "image/png");

		 };
	        try {	        	
	            mailSender.send(messagePreparator);
	            log.debug("se envio el mail a " + to);
	        } catch (MailException e) {
	            e.printStackTrace();
	        }

	}
	


}
