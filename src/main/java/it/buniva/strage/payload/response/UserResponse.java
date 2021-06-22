package it.buniva.strage.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import it.buniva.strage.entity.Student;
import it.buniva.strage.entity.User;
import it.buniva.strage.entity.compositeatributte.PersonalData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserResponse {

    private Long userId;

    private String username;

    private boolean enabled;

    private String role;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Rome")
    private Date createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Rome")
    private Date updatedAt;

    public static UserResponse createFromUser(User user) {
        return new UserResponse(
                user.getUserId(),
                user.getUsername(),
                user.isEnabled(),
                user.getRole().getRoleName().name(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        );
    }
}
