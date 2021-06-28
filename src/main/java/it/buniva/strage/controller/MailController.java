package it.buniva.strage.controller;

import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.constant.MailBodyConstant;
import it.buniva.strage.constant.UserConstant;
import it.buniva.strage.entity.Admin;
import it.buniva.strage.entity.Professor;
import it.buniva.strage.entity.Student;
import it.buniva.strage.entity.User;
import it.buniva.strage.enumaration.MailObject;
import it.buniva.strage.enumaration.RoleName;
import it.buniva.strage.exception.admin.AdminNotFoundException;
import it.buniva.strage.exception.professor.ProfessorNotFoundException;
import it.buniva.strage.exception.student.StudentNotFoundException;
import it.buniva.strage.exception.user.UserNotFoundException;
import it.buniva.strage.payload.request.SendMailRequest;
import it.buniva.strage.service.*;
import it.buniva.strage.utility.PasswordUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.time.Instant;


@RestController
@RequestMapping("/mails")
public class MailController {

    @Autowired
    private MailService mailService;

    @Autowired
    private UserService userService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private PasswordUtils passwordUtils;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;


    @PostMapping(path = "/send-credentials/{userId}")
    public ResponseEntity<ApiResponseCustom> sendCredentials(
            @PathVariable("userId") Long userId,
            HttpServletRequest request)
            throws UserNotFoundException, ProfessorNotFoundException,
            StudentNotFoundException, AdminNotFoundException, MessagingException {

        User user = userService.getUserByIdAndEnabledTrueAndDeletedFalse(userId);

        String newPassword = passwordUtils.generatePassword(UserConstant.LENGTH_PASSWORD_GENERATED);
        user.setPassword(passwordEncoder.encode(newPassword));
        userService.saveUser(user);

        String mailBody = null;
        String email = null;

        if (user.getRole().getRoleName().equals(RoleName.STUDENT)) {
            Student student = studentService.getStudentByUserIdAndDeletedFalse(user.getUserId());
            email = student.getPersonalData().getEmail();
            mailBody = String.format(
                    MailBodyConstant.STUDENT_CREDENTIALS_MAIL_BODY,
                    student.getPersonalData().getName(),
                    student.getPersonalData().getEmail(),
                    newPassword
            );

        } else if (user.getRole().getRoleName().equals(RoleName.PROFESSOR)) {
            Professor professor = professorService.getProfessorByUserIdAndDeletedFalse(user.getUserId());
            email = professor.getPersonalData().getEmail();
            mailBody = String.format(
                    MailBodyConstant.PROFESSOR_CREDENTIALS_MAIL_BODY,
                    professor.getPersonalData().getName(),
                    professor.getPersonalData().getEmail(),
                    newPassword
            );

        } else if (user.getRole().getRoleName().equals(RoleName.ADMIN)) {
            Admin admin = adminService.getAdminByUserIdAndDeletedFalse(user.getUserId());
            email = admin.getPersonalData().getEmail();
            mailBody = String.format(
                    MailBodyConstant.ADMIN_CREDENTIALS_MAIL_BODY,
                    admin.getPersonalData().getName(),
                    admin.getPersonalData().getEmail(),
                    newPassword
            );
        }
        mailService.sendEmail(new SendMailRequest(
                email,
                MailObject.CREDENTIALS_MAIL,
                mailBody
        ));

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, StringUtils.EMPTY, "Mail successfully send", request.getRequestURI()),
                HttpStatus.OK);
    }
}
