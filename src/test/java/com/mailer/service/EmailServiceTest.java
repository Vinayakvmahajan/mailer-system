package com.mailer.service;

import static org.mockito.Mockito.when;

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

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.testng.Reporter;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

/**
 * Email service class test cases
 * 
 * @version 1.0 06-NOV-20
 * @Author Vinayak Mahajan
 *
 **/
@Service
public class EmailServiceTest extends AbstractTestNGSpringContextTests {

	@Mock
	private JavaMailSender javaMailSender;
	
	@InjectMocks
	private EmailService emailService;
	
	private MockMvc mockMvc;
	
	@Mock
	private Properties props;

	@Mock
	Message message;
	
	@Mock
	private Map<String, String> credentials = new HashMap<>();

	@Mock
	private List<String> recipientList = new ArrayList<String>();
	
	@BeforeTest
    public void init() {
        MockitoAnnotations.initMocks(this);
               mockMvc = MockMvcBuilders.standaloneSetup(emailService).build();         
    }

	@BeforeTest
	public void initEmail() {

		Reporter.log("Initilizing creadentials for sending mail");
		credentials.put("testing20201011@gmail.com", "dell@123");
		credentials.put("testing20201012@gmail.com", "dell@123");
		credentials.put("testing20201013@gmail.com", "dell@123");
		credentials.put("testing20201014@gmail.com", "dell@123");
		credentials.put("testing20201015@gmail.com", "dell@123");
		
		Reporter.log("Initilizing recipient");
		recipientList.add("testing20201011@yahoo.com");
		recipientList.add("testing20201012@yahoo.com");
		recipientList.add("testing20201013@yahoo.com");
		recipientList.add("testing20201014@yahoo.com");
		recipientList.add("testing20201015@yahoo.com");
		 
		props = new Properties();

		Reporter.log("set properties");
		props.put("mail.smtp.host", "smtp.gmail.com");
		props.put("mail.smtp.socketFactory.port", "465");
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "465");
	}

	
	@Test
	public void sendMailTest() throws MailException, InterruptedException, IOException {

		String body = "Testing body";
		Reporter.log("Mail body-->"+body);

		int sCount = 0;
		int rCount = 0;
		Reporter.log("Sending mail for from each creadentials to 5 recipient");
		for (Map.Entry<String, String> cread : credentials.entrySet()) {
			++sCount;
			rCount=0;
			for (String recipient : recipientList) {
				++rCount;
				Reporter.log("setting authentication");
				Session session = Session.getDefaultInstance(props,

						new javax.mail.Authenticator() {

							protected PasswordAuthentication getPasswordAuthentication() {

								return new PasswordAuthentication(cread.getKey(), cread.getKey());

							}

						});

				try {


					message = new MimeMessage(session);

					message.setFrom(new InternetAddress(cread.getKey()));
					Reporter.log("Mail from-->"+cread.getKey());
					message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recipient));
					Reporter.log("Mail to-->"+recipient);
					
					String subLine=sCount + "VINAYAK VASANTRAO MAHAJAN" + rCount;
					message.setSubject(subLine);
					Reporter.log("Subject line-->"+subLine);

					when(writeFile(subLine)).thenReturn(true);
					BodyPart messageBodyPart = new MimeBodyPart();

					messageBodyPart.setText(body);

					Reporter.log("setting attachment");
					String filename = "attachment.txt";
					DataSource source = new FileDataSource(filename);
					messageBodyPart.setDataHandler(new DataHandler(source));
					messageBodyPart.setFileName(filename);

					Multipart multipart = new MimeMultipart();
					multipart.addBodyPart(messageBodyPart);

					message.setContent(multipart);
					Transport.send(message);
					
					
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
	public Boolean writeFile(String content) {
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
			return true;
		} catch (IOException ioe) {
			
			ioe.printStackTrace();
			return false;
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
