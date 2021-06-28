package it.buniva.strage.constant;

import java.util.Locale;

public class ProfessorConstant {

    public static final String PROFESSOR_NOT_FOUND_BY_ID_MSG = "Professor no found with professorId: [%s].";

    public static final String PROFESSOR_NOT_FOUND_BY_USER_ID_MSG = "Professor no found with userId: [%s].";

    public static final String PROFESSOR_NOT_FOUND_BY_EMAIL_MSG = "Professor no found with email: [%s].";

    public static final String EMPTY_PROFESSOR_LIST_MSG = "Empty professor list.";

    public static final String DUPLICATE_PERSONAL_DATA_MSG = "Duplicate personal data: [%s].";

    public static final String SUBJECT_ASSIGNED_TO_PROFESSOR_MSG =
            "Subject with code: [%s] successfully assigned " +
                    "to professor with email: [%s].";

    public static final String SUBJECT_ALREADY_EXIST_FOR_PROFESSOR_MSG =
            "Subject with code: [%s] already exists " +
                    "for the professor with email: [%s].";
}
