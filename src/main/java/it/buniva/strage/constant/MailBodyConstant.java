package it.buniva.strage.constant;

public class MailBodyConstant {

    public static final String STUDENT_CREDENTIALS_MAIL_BODY =
             "Hi %s ,\n"
            + "you have been register on the Strage app as a Student\n\n"
            + "Here you can find your credentials:\n"
            + "email: %s \n"
            + "password: %s \n\n"
            + "You must set a new password during the first access.\n"
            + "Have a nice day and good luck with the study,\n"
            + "Our team.";

    public static final String PROFESSOR_CREDENTIALS_MAIL_BODY =
            "Hi %s ,\n"
            + "you have been register on the Strage app as a Professor\n\n"
            + "Here you can find your credentials:\n"
            + "email: %s \n"
            + "password: %s \n\n"
            + "You must set a new password during the first access.\n"
            + "Have a nice day and good work,\n"
            + "Our team.";

    public static final String ADMIN_CREDENTIALS_MAIL_BODY =
            "Hi %s ,\n"
            + "you have been register on the Strage app as an Admin\n\n"
            + "Here you can find your credentials:\n"
            + "email: %s \n"
            + "password: %s \n\n"
            + "You must set a new password during the first access.\n"
            + "Have a nice day and good work,\n"
            + "Our team.";


    public static final String RESET_PASSWORD_MAIL_BODY =
            "Here is the link to reset your password.\n\n"
            + "%s \n\n"
            + "The link is valid for: %d minutes\n";
}
