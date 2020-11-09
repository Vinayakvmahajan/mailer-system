MAILER APPLICATION

This demo is created to test the Java Mail Clients, to configure MailService 

1.1.RELEASE

Login to Gmail and visit Here to Turn On Access for less secure apps.

More Info here

Installation
$ git clone https://github.com/Vinayakvmahajan/mailer-system.git


Select the SMTP Server (GMAIL, YAHOO, OUTLOOK or ZOHO)

JavaMail API - JavaMail

JavaMail API is not part of standard JDK, so you will have to download it from it’s official website i.e JavaMail Home Page. Download the latest version of the JavaMail reference implementation and include it in your project build path. The jar file name will be javax.mail.jar.

<dependency>
	<groupId>com.sun.mail</groupId>
	<artifactId>javax.mail</artifactId>
	<version>1.5.5</version>
</dependency>

Java Program to send email contains following steps:

1.Creating javax.mail.Session object
2.Creating javax.mail.internet.MimeMessage object, we have to set different properties in this object such as recipient email address, Email Subject, Reply-To email, email body, attachments etc.
3.Using javax.mail.Transport to send the email message.

GMail SMTP - Gmail SMTP Settings.

Mock using TestNG 

Tools
The following tools are used to create this project :
STS
Git
License


Copyright (c) 2020. Vinayak Mahajan
