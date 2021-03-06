package com.mailer.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

//import com.mailer.configuration.MailConfig;

/**
 * Email service class
 * 
 * @version 1.0 06-NOV-20
 * @Author Vinayak Mahajan
 *
 **/
@Service
public class EmailService {
	
	private static Logger log=LoggerFactory.getLogger(EmailService.class);
	
	@Autowired
	private JavaMailSender javaMailSender;
	
	//@Autowired
	//private static MailConfig mailConfig;

	private static Map<String, String> credentials = new HashMap<>();

	private static List<String> recipientList = new ArrayList<String>();
	
	

	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
		
		log.info("Intilizing creadentials for sending mail");
		
		credentials.put("testing20201011@gmail.com", "dell@123");
		credentials.put("testing20201012@gmail.com", "dell@123");
		credentials.put("testing20201013@gmail.com", "dell@123");
		credentials.put("testing20201014@gmail.com", "dell@123");
		credentials.put("testing20201015@gmail.com", "dell@123");
		
		log.info("Intilizing recipient for sending mail");
		
		recipientList.add("testing20201011@yahoo.com");
		recipientList.add("testing20201012@yahoo.com");
		recipientList.add("testing20201013@yahoo.com");
		recipientList.add("testing20201014@yahoo.com");
		recipientList.add("testing20201015@yahoo.com");
		
		
	}

	/**
	 * Send mail
	 * 
	 * @throws MailException
	 * @throws InterruptedException
	 * @throws IOException 
	 * 
	 */
	@Async
	public static void sendMail() throws MailException, InterruptedException, IOException {

		log.info("Reading mail body from a file");
		
		ClassPathResource resource = new ClassPathResource("mailBody.txt");
		InputStream fis = resource.getInputStream();
		
		byte[] buffer = new byte[10];
		StringBuilder sb = new StringBuilder();
		while (fis.read(buffer) != -1) {
			sb.append(new String(buffer));
			buffer = new byte[10];
		}
		fis.close();

		String body = sb.toString();
		
		 Properties properties = new Properties();
	        properties.put("mail.smtp.host", "smtp.gmail.com");
	        properties.put("mail.smtp.socketFactory.port", "465");
	        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
	        properties.put("mail.smtp.auth", "true");
	        properties.put("mail.smtp.port", "465");

		int sCount = 0;
		int rCount = 0;

		for (Map.Entry<String, String> cread : credentials.entrySet()) {
			++sCount;
			rCount=0;
			for (String recipient : recipientList) {
				++rCount;
				try {
				// This will handle the complete authentication
				Session session = Session.getDefaultInstance(properties,

						new javax.mail.Authenticator() {
							@Override
							protected PasswordAuthentication getPasswordAuthentication() {

								return new PasswordAuthentication(cread.getKey(), cread.getKey());

							}

						});

				

					// Create object of MimeMessage class
					Message message = new MimeMessage(session);

					// Set the from address
					message.setFrom(new InternetAddress(cread.getKey()));
					log.info("Mail from-->{}",cread.getKey());

					// Set the recipient address
					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
					log.info("Mail to-->{}",recipient);
					
					String subLine= sCount + " VINAYAK VASANTRAO MAHAJAN " + rCount;
					
					log.info("Mail subject-->{}",subLine);
					
					// Add the subject link
					message.setSubject(subLine);

					writeFile(subLine);

					// Create object to add multimedia type content
					BodyPart messageBodyPart = new MimeBodyPart();

					// Set the body of email
					messageBodyPart.setText(body);
					
					// Create object of MimeMultipart class
					Multipart multipart = new MimeMultipart();

					// Part two is attachment
					messageBodyPart = new MimeBodyPart();
					String filename = "attachment.txt";
					DataSource source = new FileDataSource(filename);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(filename);

					multipart.addBodyPart(messageBodyPart);

					// set the content
					message.setContent(multipart);

					// finally send the email
					Transport.send(message);

					System.out.println("=====Email Sent=====");

				} catch (MessagingException e) {

					throw new RuntimeException(e);

				} 
			}
		}

	}

	/**
	 * Write the file with new content mail
	 * 
	 * @param content
	 */
	public static void writeFile(String content) {
		BufferedWriter bw = null;
		try {
			File file = new File("attachment.txt");

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file);
			bw = new BufferedWriter(fw);
			bw.write(content);
			System.out.println("File written Successfully");

		} catch (IOException ioe) {
			ioe.printStackTrace();
		} finally {
			try {
				if (bw != null)
					bw.close();
			} catch (Exception ex) {
				System.out.println("Error in closing the BufferedWriter" + ex);
			}
		}

	}

}
