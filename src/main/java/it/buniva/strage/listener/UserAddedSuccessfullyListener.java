package it.buniva.strage.listener;

import it.buniva.strage.constant.UserConstant;
import it.buniva.strage.entity.User;
import it.buniva.strage.event.UserAddedSuccessfullyEvent;
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
public class UserAddedSuccessfullyListener implements ApplicationListener<UserAddedSuccessfullyEvent>{

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private MailService mailService;

    @Autowired
    private SendMailListener sendMailListener;



    private boolean UserAddedSuccessfully = false;

    private SendMailRequest sendMailRequest = new SendMailRequest();

    private LinkedList<SendMailRequest> sendMailRequestsQueue = new LinkedList<>();


    @Override
    public void onApplicationEvent(UserAddedSuccessfullyEvent event) {
        // When the listener receive the message ...
        UserAddedSuccessfully = event.isUserAddedSuccessfully();
        sendMailRequestsQueue = sendMailListener.getSendMailRequestsQueue();
    }


    // Time in milliseconds
    @Scheduled(fixedDelay = 3000, zone = "Europe/Rome")
    public void sendingEmailToUserAfterRegistration() {

        if( !sendMailRequestsQueue.isEmpty() && UserAddedSuccessfully) {
            sendMailRequest = sendMailRequestsQueue.getFirst();
            User userByMail = new User();
            String email = sendMailRequest.getEmailTo();
            try {
                // Retrieve the user who receive the mail
                userByMail = userService.getUserByUsername(email);

                // Send the mail to the student
                mailService.sendEmail(sendMailRequest);

                userByMail.setMailSent(true);
                userService.saveUser(userByMail);

            } catch (UserNotFoundException e) {
                LOGGER.info(String.format(
                        "UserNotFoundException: " + UserConstant.USER_NOT_FOUND_BY_USERNAME_MSG, email));

                sendMailRequestsQueue.removeFirst();
                return;

            } catch ( MessagingException ex) {
                // catch exception without showing the message
                // Sending mail fail, user would not receive the mail
                LOGGER.info("An error occur during sending mail to: {}", email);
                userByMail.setMailSent(false);
                userService.saveUser(userByMail);

                sendMailRequestsQueue.removeFirst();
                return;
            }
            LOGGER.info("Email successfully sent to: {}", email);
            sendMailRequestsQueue.removeFirst();
        }
    }


    // GETTER AND SETTER
    public boolean isUserAddedSuccessfully() {
        return UserAddedSuccessfully;
    }

    public void setUserAddedSuccessfully(boolean userAddedSuccessfully) {
        UserAddedSuccessfully = userAddedSuccessfully;
    }

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
