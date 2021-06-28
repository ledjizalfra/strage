package it.buniva.strage.listener;

import it.buniva.strage.constant.UserConstant;
import it.buniva.strage.entity.User;
import it.buniva.strage.event.SendMailEvent;
import it.buniva.strage.exception.user.UserNotFoundException;
import it.buniva.strage.payload.request.SendMailRequest;
import it.buniva.strage.service.MailService;
import it.buniva.strage.service.StudentService;
import it.buniva.strage.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import java.util.LinkedList;

@Component
public class SendMailListener implements ApplicationListener<SendMailEvent>{

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private MailService mailService;



    private SendMailRequest sendMailRequest = new SendMailRequest();

    private LinkedList<SendMailRequest> sendMailRequestsQueue = new LinkedList<>();



    @Override
    public void onApplicationEvent(SendMailEvent event) {
        // When the listener receive the message ...
        sendMailRequestsQueue.addLast(event.getSendMailRequest());
    }


    // GETTER AND SETTER

    public SendMailRequest getSendMailRequest() {
        return sendMailRequest;
    }

    public void setSendMailRequest(SendMailRequest sendMailRequest) {
        this.sendMailRequest = sendMailRequest;
    }

    public LinkedList<SendMailRequest> getSendMailRequestsQueue() {
        return sendMailRequestsQueue;
    }

    public void setSendMailRequestsQueue(LinkedList<SendMailRequest> sendMailRequestsQueue) {
        this.sendMailRequestsQueue = sendMailRequestsQueue;
    }
}
