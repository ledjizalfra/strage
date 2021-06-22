package it.buniva.strage.constant;

public class UserConstant {

    public static final int LENGTH_PASSWORD_GENERATED = 8;

    public static final int EXPIRATION = 30; // In minutes

    public static final String USER_FOUND_MSG = "Returning found user with username: [%s].";

    public static final String USER_NOT_FOUND_BY_USERNAME_MSG = "No user found with username: [%s].";

    public static final String USERNAME_ALREADY_EXIST_MSG = "The username: [%s] is already taken.";

    public static final String USER_NOT_FOUND_BY_ID_MSG = "No user found with userId: [%s].";

    public static final String EMPTY_USER_LIST_MSG = "User list is empty.";

    public static final String PASSWORD_NO_MATCHES_MSG = "Old password no matches.";

    public static final String INVALID_OLD_PASSWORD_FORMAT_MSG = "Invalid old password format. Must be at least 8 characters long, must have at least 1 digit, 1 lower case, 1 upper case, 1 of this character '@,#,$,%,&,-,+,=,(,)'.";

    public static final String INVALID_NEW_PASSWORD_FORMAT_MSG = "Invalid new password format. Must be at least 8 characters long, must have at least 1 digit, 1 lower case, 1 upper case, 1 of this character '@,#,$,%,&,-,+,=,(,)'.";

}
