package it.buniva.strage.exception.pwdresettoken;

public class PwdResetTokenExpiredException extends Exception {
    public PwdResetTokenExpiredException(String message) {
        super(message);
    }
}
