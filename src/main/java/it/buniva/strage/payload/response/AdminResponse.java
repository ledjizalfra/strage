package it.buniva.strage.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import it.buniva.strage.entity.Admin;
import it.buniva.strage.entity.compositeatributte.PersonalData;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdminResponse {

    private Long adminId;

    private PersonalData personalData;

    private boolean enabled;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Rome")
    private Date createdAt;

    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss", timezone = "Europe/Rome")
    private Date updatedAt;

    public static AdminResponse createFromAdmin(Admin admin) {
        return new AdminResponse(
                admin.getId(),
                admin.getPersonalData(),
                admin.isEnabled(),
                admin.getCreatedAt(),
                admin.getUpdatedAt()
        );
    }
}
