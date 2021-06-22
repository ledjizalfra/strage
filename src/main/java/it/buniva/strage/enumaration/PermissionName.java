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
    CLASSROOM_DELETE("classroom:delete");

    private final String permission;

    PermissionName(String permission){
        this.permission = permission;
    }
}
