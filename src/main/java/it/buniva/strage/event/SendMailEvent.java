package it.buniva.strage.event;

import it.buniva.strage.payload.request.SendMailRequest;
import org.springframework.context.ApplicationEvent;

public class SendMailEvent extends ApplicationEvent {

    private static final long serialVersionUID = 1L;


    private SendMailRequest sendMailRequest;



    public SendMailEvent(Object source, SendMailRequest sendMailRequest) {
        super(source);
        this.sendMailRequest = sendMailRequest;
    }

    public SendMailRequest getSendMailRequest() {
        return sendMailRequest;
    }

    public void setSendMailRequest(SendMailRequest sendMailRequest) {
        this.sendMailRequest = sendMailRequest;
    }
}
