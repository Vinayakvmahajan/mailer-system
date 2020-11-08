package com.mailer.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.mailer.service.EmailService;

@RestController
class MailController {
	
	@Autowired
	private EmailService emailService;

	@GetMapping("/mail")
	public void send() throws MailException, InterruptedException {
			emailService.sendMail();
	}
}