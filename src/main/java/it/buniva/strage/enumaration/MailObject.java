package it.buniva.strage.enumaration;

import lombok.Getter;

@Getter
public enum MailObject {

    CREDENTIALS_MAIL("Strage APP - Credentials"),
    RESET_PASSWORD_MAIL("Strage APP - Reset Password");

    private final String mailObject;

    MailObject(String mailObject){
        this.mailObject = mailObject;
    }
}
