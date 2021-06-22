package it.buniva.strage.payload.request;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClassroomListRequest {

    @NotNull
    private List<ClassroomRequest> classroomRequestList;
}
