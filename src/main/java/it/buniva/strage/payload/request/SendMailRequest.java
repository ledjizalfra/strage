package it.buniva.strage.payload.request;

import it.buniva.strage.constant.MailBodyConstant;
import it.buniva.strage.entity.Admin;
import it.buniva.strage.entity.Professor;
import it.buniva.strage.entity.Student;
import it.buniva.strage.enumaration.MailObject;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SendMailRequest {

    private String emailTo;

    private MailObject mailObject;

    private String body;



    public static SendMailRequest createFromStudent(Student student,MailObject mailObject, String password) {
        return new SendMailRequest(
                student.getPersonalData().getEmail(),
                mailObject,
                String.format(
                        MailBodyConstant.STUDENT_CREDENTIALS_MAIL_BODY,
                        student.getPersonalData().getName(),
                        student.getPersonalData().getEmail(),
                        password
                )
        );
    }

    public static SendMailRequest createFromProfessor(Professor professor, MailObject mailObject, String password) {
        return new SendMailRequest(
                professor.getPersonalData().getEmail(),
                mailObject,
                String.format(
                        MailBodyConstant.PROFESSOR_CREDENTIALS_MAIL_BODY,
                        professor.getPersonalData().getName(),
                        professor.getPersonalData().getEmail(),
                        password
                )
        );
    }

    public static SendMailRequest createFromAdmin(Admin admin, MailObject mailObject, String password) {
        return new SendMailRequest(
                admin.getPersonalData().getEmail(),
                mailObject,
                String.format(
                        MailBodyConstant.ADMIN_CREDENTIALS_MAIL_BODY,
                        admin.getPersonalData().getName(),
                        admin.getPersonalData().getEmail(),
                        password
                )
        );
    }

}
