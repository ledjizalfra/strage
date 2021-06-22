package it.buniva.strage.payload.response;

import it.buniva.strage.entity.User;
import it.buniva.strage.entity.compositeatributte.PersonalData;
import it.buniva.strage.exception.admin.AdminNotFoundException;
import it.buniva.strage.exception.professor.ProfessorNotFoundException;
import it.buniva.strage.exception.student.StudentNotFoundException;
import it.buniva.strage.exception.user.UserNotFoundException;
import it.buniva.strage.service.UserService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserLoggedResponse {

    private Long userId;

    private String username;

    private String name;

    private String surname;

    private String token;

    private String role;

    private Set<String> authorities;

    public static UserLoggedResponse createFrom(User user, String token, PersonalData personalData) {

        return new UserLoggedResponse(
                user.getUserId(),
                user.getUsername(),
                personalData.getName(),
                personalData.getSurname(),
                token,
                user.getRole().getRoleName().name(),
                user.getRole().getRoleName().getGrantedAuthorities().stream()
                        .map(SimpleGrantedAuthority::getAuthority)
                        .collect(Collectors.toSet())
        );
    }
}
