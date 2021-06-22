package it.buniva.strage.service;

import com.sun.mail.smtp.SMTPTransport;
import it.buniva.strage.constant.MailConstant;
import it.buniva.strage.payload.request.SendMailRequest;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;

import static javax.mail.Message.RecipientType.CC;
import static javax.mail.Message.RecipientType.TO;

@Service
public class MailService {

    public void sendEmail(SendMailRequest mailRequest) throws MessagingException {

        Message message = createEmail(mailRequest);

        SMTPTransport smtpTransport = (SMTPTransport) getEmailSession().getTransport(MailConstant.SIMPLE_MAIL_TRANSFER_PROTOCOL);
        smtpTransport.connect(MailConstant.GMAIL_SMTP_SERVER, MailConstant.USERNAME, MailConstant.PASSWORD);
        smtpTransport.sendMessage(message, message.getAllRecipients());
        smtpTransport.close();
    }


    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================

    private Message createEmail(SendMailRequest mailRequest) throws MessagingException {

        Message message = new MimeMessage(getEmailSession());

        message.setFrom(new InternetAddress(MailConstant.FROM_EMAIL));
        message.setRecipients(TO, InternetAddress.parse(mailRequest.getEmailTo(), false));
        message.setRecipients(CC, InternetAddress.parse(MailConstant.CC_EMAIL, false));

        message.setSubject(mailRequest.getMailObject().getMailObject());
        message.setText(mailRequest.getBody());

        message.setSentDate(new Date());
        message.saveChanges();

        return message;
    }

    private Session getEmailSession() {
        Properties properties = System.getProperties();
        properties.put(MailConstant.SMTP_HOST, MailConstant.GMAIL_SMTP_SERVER);
        properties.put(MailConstant.SMTP_AUTH, true);
        properties.put(MailConstant.SMTP_PORT, MailConstant.DEFAULT_PORT);

        // This parameters are to have an secure connexion
        properties.put(MailConstant.SMTP_STARTTLS_ENABLE, true);
        properties.put(MailConstant.SMTP_STARTTLS_REQUIRED, true);

        // We return the session we need to send an email
        return Session.getInstance(properties, null);
    }
}
