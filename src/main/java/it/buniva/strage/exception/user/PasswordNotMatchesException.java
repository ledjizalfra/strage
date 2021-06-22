package it.buniva.strage.exception.user;

public class PasswordNotMatchesException extends Exception {
    public PasswordNotMatchesException(String message) {
        super(message);
    }
}
