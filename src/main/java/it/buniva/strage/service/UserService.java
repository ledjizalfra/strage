package it.buniva.strage.service;

import it.buniva.strage.entity.User;
import it.buniva.strage.entity.compositeatributte.PersonalData;
import it.buniva.strage.exception.admin.AdminNotFoundException;
import it.buniva.strage.exception.professor.ProfessorNotFoundException;
import it.buniva.strage.exception.pwdresettoken.InvalidPwdResetTokenException;
import it.buniva.strage.exception.pwdresettoken.PwdResetTokenExpiredException;
import it.buniva.strage.exception.role.RoleNotFoundException;
import it.buniva.strage.exception.student.StudentNotFoundException;
import it.buniva.strage.exception.user.*;
import it.buniva.strage.payload.request.ForgotPasswordRequest;
import it.buniva.strage.payload.request.PasswordRequest;
import it.buniva.strage.payload.request.PwdResetRequest;
import it.buniva.strage.payload.request.UserRequest;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface UserService {

    // ============================= CREATE ===============================
    User registerUser(UserRequest userRequest)throws DuplicateUsernameException, UserNotFoundException, RoleNotFoundException;

    // ============================== READ ================================
    User getAuthenticatedUser() throws UserNotFoundException;

    User getUserByUsername(String username) throws UserNotFoundException;

    User getUserByIdAndEnabledTrueAndDeletedFalse(Long userId) throws UserNotFoundException;

    User getUserByIdAndDeletedFalse(Long userId) throws UserNotFoundException;

    List<User> getAllUsersByEnabledTrueAndDeletedFalse() throws EmptyUserListException;

    PersonalData getUserPersonalData(Long userId) throws UserNotFoundException, StudentNotFoundException, ProfessorNotFoundException, AdminNotFoundException;

    void existsAlreadyUserByUsername(String username) throws DuplicateUsernameException;

    // ============================ UPDATE =================================
    User enableDisableUser(Long userId) throws UserNotFoundException, StudentNotFoundException, ProfessorNotFoundException, AdminNotFoundException;

    User updateEnable(Long userId, boolean enabled) throws UserNotFoundException;

    void changePassword(PasswordRequest passwordRequest) throws UserNotFoundException, PasswordNotMatchesException, InvalidPasswordFormatException;

    // ============================= DELETE ================================
    void deleteUser(Long userId) throws UserNotFoundException;

    // ============================== SAVE ==================================
    User saveUser(User user);


    // ============================== OTHER ==================================
    void forgotPassword(ForgotPasswordRequest forgotRequest, HttpServletRequest request) throws UserNotFoundException, MessagingException;

    void passwordReset(PwdResetRequest passwordRequest, String token) throws InvalidPwdResetTokenException, PwdResetTokenExpiredException, InvalidPasswordFormatException;

}
