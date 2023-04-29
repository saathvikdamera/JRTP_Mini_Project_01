package com.Damera.utils;

import java.io.File;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

@Component
public class EmailUtils {

	@Autowired
	private JavaMailSender emailSender;

	public void sendEmailWithAttachment(String to,
			String subject, 
			String text,
			File file) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();

		// use the true flag to indicate you need a multipart message
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setFrom("saathvikdamera@gmail.com");
		helper.setTo(to);
		helper.setSubject(subject);
		helper.setText(text);

		// attach the file
		// FileSystemResource file = new FileSystemResource(new File(pathToAttachment));
		helper.addAttachment("Plans", file);

		emailSender.send(message);
		System.out.println("Mail with attachment sent successfully");


	}

}
