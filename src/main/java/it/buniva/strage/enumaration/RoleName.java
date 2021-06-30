package it.buniva.strage.enumaration;

import com.google.common.collect.Sets;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static it.buniva.strage.enumaration.PermissionName.*;


@Getter
public enum RoleName {

    ADMIN(Sets.newHashSet(
            ROLE_ADMIN,
            ROLE_READ,
            PERMISSION_READ,
            USER_CREATE, USER_READ, USER_UPDATE, USER_DELETE,
            ADMIN_CREATE, ADMIN_READ, ADMIN_UPDATE, ADMIN_DELETE,
            PROFESSOR_CREATE, PROFESSOR_READ, PROFESSOR_UPDATE, PROFESSOR_DELETE,
            STUDENT_CREATE, STUDENT_READ, STUDENT_UPDATE, STUDENT_DELETE,
            CLASSROOM_CREATE, CLASSROOM_READ, CLASSROOM_UPDATE, CLASSROOM_DELETE,
            SUBJECT_CREATE, SUBJECT_READ, SUBJECT_UPDATE, SUBJECT_DELETE
    )),
    PROFESSOR(Sets.newHashSet(
            ROLE_PROFESSOR,
            STUDENT_CREATE, STUDENT_READ,
            CLASSROOM_CREATE, CLASSROOM_READ,
            SUBJECT_CREATE, SUBJECT_READ, SUBJECT_UPDATE, SUBJECT_DELETE,
            ARGUMENT_CREATE, ARGUMENT_READ, ARGUMENT_UPDATE, ARGUMENT_DELETE,
            QUESTION_CREATE, QUESTION_READ, QUESTION_UPDATE, QUESTION_DELETE,
            ANSWER_CREATE, ANSWER_READ, ANSWER_UPDATE, ANSWER_DELETE,
            CONFIGURATION_CREATE, CONFIGURATION_READ, CONFIGURATION_UPDATE, CONFIGURATION_DELETE
    )),
    STUDENT(Sets.newHashSet(
            ROLE_STUDENT
    ));

    private final Set<PermissionName> permissions;

    RoleName(Set<PermissionName> permissions) {
        this.permissions = permissions;
    }

    /**
     * Define authority (permission) who will be add to the user through spring security
     * @return a set of permissions ( authorities )
     */
    public Set<SimpleGrantedAuthority> getGrantedAuthorities(){
        Set<SimpleGrantedAuthority> permissions = getPermissions().stream()
                .filter(
                        e ->  Pattern.compile("^[a-z]+:(create|read|update|delete)$")
                        .matcher(e.getPermission())
                        .matches() ) // filter only those who have this syntax: (ex: user:read, student:read, ...), excluding ROLE_[role]
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .collect(Collectors.toSet());
        permissions.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return permissions;
    }

}
