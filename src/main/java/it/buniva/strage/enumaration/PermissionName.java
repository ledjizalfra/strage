package it.buniva.strage.enumaration;

import lombok.Getter;

@Getter
public enum PermissionName {

    // DEFAULT PERMISSION
    ROLE_ADMIN("ADMIN"),
    ROLE_PROFESSOR("PROFESSOR"),
    ROLE_STUDENT("STUDENT"),

    //ROLE
    ROLE_READ("role:read"),

    //PERMISSION
    PERMISSION_READ("permission:read"),

    //USER
    USER_CREATE("user:create"),
    USER_READ("user:read"),
    USER_UPDATE("user:update"),
    USER_DELETE("user:delete"),

    //ADMIN
    ADMIN_CREATE("admin:create"),
    ADMIN_READ("admin:read"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete"),

    //PROFESSOR
    PROFESSOR_CREATE("professor:create"),
    PROFESSOR_READ("professor:read"),
    PROFESSOR_UPDATE("professor:update"),
    PROFESSOR_DELETE("professor:delete"),

    //STUDENT
    STUDENT_CREATE("student:create"),
    STUDENT_READ("student:read"),
    STUDENT_UPDATE("student:update"),
    STUDENT_DELETE("student:delete"),

    //CLASSROOM
    CLASSROOM_CREATE("classroom:create"),
    CLASSROOM_READ("classroom:read"),
    CLASSROOM_UPDATE("classroom:update"),
    CLASSROOM_DELETE("classroom:delete"),

    //SUBJECT
    SUBJECT_CREATE("subject:create"),
    SUBJECT_READ("subject:read"),
    SUBJECT_UPDATE("subject:update"),
    SUBJECT_DELETE("subject:delete"),

    //ARGUMENT
    ARGUMENT_CREATE("argument:create"),
    ARGUMENT_READ("argument:read"),
    ARGUMENT_UPDATE("argument:update"),
    ARGUMENT_DELETE("argument:delete"),

    //QUESTION
    QUESTION_CREATE("question:create"),
    QUESTION_READ("question:read"),
    QUESTION_UPDATE("question:update"),
    QUESTION_DELETE("question:delete"),

    //ANSWER
    ANSWER_CREATE("answer:create"),
    ANSWER_READ("answer:read"),
    ANSWER_UPDATE("answer:update"),
    ANSWER_DELETE("answer:delete"),

    //CONFIGURATION
    CONFIGURATION_CREATE("configuration:create"),
    CONFIGURATION_READ("configuration:read"),
    CONFIGURATION_UPDATE("configuration:update"),
    CONFIGURATION_DELETE("configuration:delete");

    private final String permission;

    PermissionName(String permission){
        this.permission = permission;
    }
}
