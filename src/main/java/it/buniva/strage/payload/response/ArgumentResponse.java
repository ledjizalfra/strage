package it.buniva.strage.payload.response;

import it.buniva.strage.entity.Argument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArgumentResponse {

    private String argumentName;

    private String argumentCode;

    private String subjectCode;

    private boolean enabled;


    public static ArgumentResponse createFromArgument(Argument argument) {

        return new ArgumentResponse(
                argument.getArgumentName(),
                argument.getArgumentCode(),
                argument.getSubject().getSubjectCode(),
                argument.isEnabled()
        );
    }
}
