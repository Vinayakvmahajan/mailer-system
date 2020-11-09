package com.mailer;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.mail.MailException;
import org.springframework.scheduling.annotation.EnableAsync;

import com.mailer.service.EmailService;


/**
 * Mailer application main class 
 * 
 * @version 1.0 06-NOV-20
 * @Author Vinayak Mahajan
 *
 **/
@SpringBootApplication
@ComponentScan(basePackages = "com.mailer")
@EnableAsync
public class MailerApplication {

	public static void main(String[] args) throws MailException, InterruptedException, IOException {
		SpringApplication.run(MailerApplication.class, args);
		EmailService.sendMail();
		
	}

}
