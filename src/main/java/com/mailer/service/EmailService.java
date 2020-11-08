package com.mailer.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
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

import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

/**
 * Email service class
 * 
 * @version 1.0 06-NOV-20
 * @Author Vinayak Mahajan
 *
 **/
@Service
public class EmailService {

	private JavaMailSender javaMailSender;

	private Map<String, String> credentials = new HashMap<>();

	private List<String> recipientList = new ArrayList<String>();

	public EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;
		credentials.put("testing20201011@gmail.com", "dell@123");
		credentials.put("testing20201012@gmail.com", "dell@123");
		credentials.put("testing20201013@gmail.com", "dell@123");
		credentials.put("testing20201014@gmail.com", "dell@123");
		credentials.put("testing20201015@gmail.com", "dell@123");
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
	public void sendMail() throws MailException, InterruptedException, IOException {

		FileInputStream fis = new FileInputStream("mailBody.txt");
		byte[] buffer = new byte[10];
		StringBuilder sb = new StringBuilder();
		while (fis.read(buffer) != -1) {
			sb.append(new String(buffer));
			buffer = new byte[10];
		}
		fis.close();

		String body = sb.toString();
		
		// Create object of Property file
		Properties props = new Properties();

		// this will set host of server- you can change based on your requirement
		props.put("mail.smtp.host", "smtp.gmail.com");

		// set the port of socket factory
		props.put("mail.smtp.socketFactory.port", "465");

		// set socket factory
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");

		// set the authentication to true
		props.put("mail.smtp.auth", "true");

		// set the port of SMTP server
		props.put("mail.smtp.port", "465");

		int sCount = 0;
		int rCount = 0;

		for (Map.Entry<String, String> cread : credentials.entrySet()) {
			++sCount;
			rCount=0;
			for (String recipient : recipientList) {
				++rCount;
				// This will handle the complete authentication
				Session session = Session.getDefaultInstance(props,

						new javax.mail.Authenticator() {

							protected PasswordAuthentication getPasswordAuthentication() {

								return new PasswordAuthentication(cread.getKey(), cread.getKey());

							}

						});

				try {

					// Create object of MimeMessage class
					Message message = new MimeMessage(session);

					// Set the from address
					message.setFrom(new InternetAddress("mukeshotwani.50@gmail.com"));

					// Set the recipient address
					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse("mukesh.otwani50@gmail.com"));

					// Add the subject link
					message.setSubject(sCount + "VINAYAK VASANTRAO MAHAJAN" + rCount);

					writeFile(sCount + "VINAYAK VASANTRAO MAHAJAN" + rCount);

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
	public void writeFile(String content) {
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
