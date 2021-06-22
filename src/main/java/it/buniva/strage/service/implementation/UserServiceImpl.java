package it.buniva.strage.service.implementation;

import it.buniva.strage.constant.MailBodyConstant;
import it.buniva.strage.constant.PwdResetTokenConstant;
import it.buniva.strage.constant.UserConstant;
import it.buniva.strage.entity.*;
import it.buniva.strage.entity.compositeatributte.PersonalData;
import it.buniva.strage.enumaration.MailObject;
import it.buniva.strage.enumaration.RoleName;
import it.buniva.strage.exception.admin.AdminNotFoundException;
import it.buniva.strage.exception.professor.ProfessorNotFoundException;
import it.buniva.strage.exception.pwdresettoken.InvalidPwdResetTokenException;
import it.buniva.strage.exception.pwdresettoken.PwdResetTokenExpiredException;
import it.buniva.strage.exception.role.RoleNotFoundException;
import it.buniva.strage.exception.student.StudentNotFoundException;
import it.buniva.strage.exception.user.*;
import it.buniva.strage.payload.request.*;
import it.buniva.strage.repository.UserRepository;
import it.buniva.strage.security.UserPrincipal;
import it.buniva.strage.service.*;
import it.buniva.strage.utility.PasswordUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;


@Service
@Qualifier("userDetailsService")
@Transactional(rollbackFor = Exception.class)
public class UserServiceImpl implements UserService, UserDetailsService {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Value("${strage.baseUrl}")
    private String baseUrl;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordUtils passwordUtils;

    @Autowired
    private RoleService roleService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private PwdResetTokenService pwdResetTokenService;

    @Autowired
    private MailService mailService;


    // ===================================================================
    // ======================== IMPLEMENTATIONS ==========================
    // ===================================================================

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsernameAndDeletedFalse(username);
        if (user == null) {
            LOGGER.error(String.format(UserConstant.USER_NOT_FOUND_BY_USERNAME_MSG, username));
            throw new UsernameNotFoundException(
                    String.format(UserConstant.USER_NOT_FOUND_BY_USERNAME_MSG, username));

        } else { // The User is found, the Authentication can process

            // UserPrincipal implement the UserDetails
            UserPrincipal userPrincipal = new UserPrincipal(user);
            LOGGER.info(String.format(UserConstant.USER_FOUND_MSG, username));
            LOGGER.info(String.format("List of authorities:'%s'", userPrincipal.getAuthorities().toString()));

            return userPrincipal;
        }
    }

    // ============================= CREATE ===============================
    @Override
    public User registerUser(UserRequest userRequest) throws DuplicateUsernameException, UserNotFoundException, RoleNotFoundException {

        // Control if the User already exists by username
        // if exist throw an exception
        existsAlreadyUserByUsername(userRequest.getUsername());

        // Control if the role exist by name
        // if not exist throw an exception
        Role roleByRoleName = roleService.getRoleByRoleName(
                getRoleNameFromString(userRequest.getRole()));

       /* // TODO... After we will generate password directly in Student, Professor or Admin service
        String password = passwordUtils.generatePassword(UserConstant.LENGTH_PASSWORD_GENERATED);
        userRequest.setPassword(password);*/

        // Create and save the user
        User userSaved = createNewUser(userRequest, roleByRoleName);

        LOGGER.info(String.format("EMAIL: %s", userRequest.getUsername()));
        LOGGER.info(String.format("PASSWORD: %s", userRequest.getPassword()));

        return getUserByIdAndEnabledTrueAndDeletedFalse(userSaved.getUserId());
    }

    // ============================== READ ================================
    @Override
    public User getAuthenticatedUser() throws UserNotFoundException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        LOGGER.info("USER AUTHENTICATED: " + authentication.getName());
        return getUserByUsername(authentication.getName());
    }

    @Override
    public User getUserByUsername(String username) throws UserNotFoundException {
        User userByUsername = userRepository.findUserByUsernameAndDeletedFalse(username);
        if (userByUsername == null) {
            throw new UserNotFoundException(
                    String.format(UserConstant.USER_NOT_FOUND_BY_USERNAME_MSG, username));
        }
        return userByUsername;
    }

    @Override
    public User getUserByIdAndEnabledTrueAndDeletedFalse(Long userId) throws UserNotFoundException {
        Optional<User> userOptional = userRepository.findByUserIdAndEnabledTrueAndDeletedFalse(userId);
        // Optional<User> userOptional = userRepository.findByUserIdAndEnabledTrueAndDeletedFalseQuery(userId);

        if (!userOptional.isPresent()) {
            throw new UserNotFoundException(
                    String.format(UserConstant.USER_NOT_FOUND_BY_ID_MSG, userId));
        }
        return userOptional.get();
    }

    @Override
    public User getUserByIdAndDeletedFalse(Long userId) throws UserNotFoundException {
        User userById = userRepository.findUserByUserIdAndDeletedFalse(userId);
        if (userById == null) {
            throw new UserNotFoundException(
                    String.format(UserConstant.USER_NOT_FOUND_BY_ID_MSG, userId));
        }
        return userById;
    }

    @Override
    public List<User> getAllUsersByEnabledTrueAndDeletedFalse() throws EmptyUserListException {
        List<User> userList = userRepository.findAllByEnabledTrueAndDeletedFalse();

        if (userList.isEmpty()) {
            throw new EmptyUserListException(UserConstant.EMPTY_USER_LIST_MSG);
        }

        return userList;
    }

    @Override
    public PersonalData getUserPersonalData(Long userId) throws UserNotFoundException, StudentNotFoundException, ProfessorNotFoundException, AdminNotFoundException {
        User user = getUserByIdAndEnabledTrueAndDeletedFalse(userId);

        if (user.getRole().getRoleName().equals(RoleName.PROFESSOR)) {
            Professor professor = professorService.getProfessorByUserIdAndEnabledTrueAndDeletedFalse(user.getUserId());
            return professor.getPersonalData();

        } else if (user.getRole().getRoleName().equals(RoleName.ADMIN)) {
            Admin admin = adminService.getAdminByUserIdAndEnabledTrueAndDeletedFalse(user.getUserId());
            return admin.getPersonalData();

        } else {
            Student student = studentService.getStudentByUserIdAndEnabledTrueAndDeletedFalse(user.getUserId());
            return student.getPersonalData();
        }
    }

    @Override
    public void existsAlreadyUserByUsername(String username) throws DuplicateUsernameException {
        boolean existsByUsername = userRepository.existsByUsername(username);
        if (existsByUsername) {
            // If the username is already taken by another person
            throw new DuplicateUsernameException(
                    String.format(UserConstant.USERNAME_ALREADY_EXIST_MSG, username));
        }
    }

    // ============================ UPDATE =================================
    @Override
    public User enableDisableUser(Long userId) throws UserNotFoundException, StudentNotFoundException, ProfessorNotFoundException, AdminNotFoundException {
        User user = getUserByIdAndDeletedFalse(userId);
        user.setEnabled(!user.isEnabled());

        saveUser(user);

        // Update the profile with this userId
        if (user.getRole().getRoleName().equals(RoleName.STUDENT)) {
            Student student = studentService.getStudentByUserIdAndDeletedFalse(user.getUserId());
            studentService.updateEnabled(student.getId(), user.isEnabled());

        } else if (user.getRole().getRoleName().equals(RoleName.PROFESSOR)) {
            Professor professor = professorService.getProfessorByUserIdAndDeletedFalse(user.getUserId());
            professorService.updateEnabled(professor.getId(), user.isEnabled());

        } else if (user.getRole().getRoleName().equals(RoleName.ADMIN)) {
            Admin admin = adminService.getAdminByUserIdAndDeletedFalse(user.getUserId());
            adminService.updateEnabled(admin.getId(), user.isEnabled());
        }

        return user;
    }

    @Override
    public User updateEnable(Long userId, boolean enabled) throws UserNotFoundException {
        User user = getUserByIdAndDeletedFalse(userId);
        user.setEnabled(enabled);

        return saveUser(user);
    }

    @Override
    public void changePassword(PasswordRequest passwordRequest)
            throws UserNotFoundException, PasswordNotMatchesException, InvalidPasswordFormatException {

        // Get the user authenticated
        User user = getAuthenticatedUser();

        //control the pass is valid
        if(!passwordUtils.isPasswordValid(passwordRequest.getOldPassword())) {
            throw new InvalidPasswordFormatException(UserConstant.INVALID_OLD_PASSWORD_FORMAT_MSG);
        }
        if(!passwordUtils.isPasswordValid(passwordRequest.getNewPassword())) {
            throw new InvalidPasswordFormatException(UserConstant.INVALID_NEW_PASSWORD_FORMAT_MSG);
        }

        // check if old password matches with the actual password before we set the new password
        if(!passwordUtils.passwordMatches(passwordRequest.getOldPassword(), user)) {
            throw new PasswordNotMatchesException(UserConstant.PASSWORD_NO_MATCHES_MSG);
        }

        user.setPassword(passwordUtils.encodePassword(passwordRequest.getNewPassword()));

        saveUser(user);
    }

    // ============================= DELETE ================================
    @Override
    public void deleteUser(Long userId) throws UserNotFoundException {
        User user = getUserByIdAndDeletedFalse(userId);
        user.setEnabled(false);
        user.setDeleted(true);

        saveUser(user);
    }

    // ============================== SAVE ==================================
    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }


    // ============================== OTHER ==================================
    @Override
    public void forgotPassword(
            ForgotPasswordRequest forgotRequest,
            HttpServletRequest request) throws UserNotFoundException, MessagingException {

        // TODO ...
        // If user not found throw an exception
        User user = getUserByUsername(forgotRequest.getEmail());

        PwdResetToken pwdResetToken = pwdResetTokenService.createPasswordResetToken(user);

        // Send the mail to the user
        String url = baseUrl + "/users/password-reset/" + pwdResetToken.getToken();

        mailService.sendEmail(new SendMailRequest(
                user.getUsername(),
                MailObject.RESET_PASSWORD_MAIL,
                String.format(
                        MailBodyConstant.RESET_PASSWORD_MAIL_BODY,
                        url,
                        PwdResetTokenConstant.EXPIRATION
                )
        ));

    }

    @Override
    public void passwordReset(
            PwdResetRequest pwdResetRequest,
            String token)
            throws InvalidPwdResetTokenException, PwdResetTokenExpiredException,
            InvalidPasswordFormatException {

        // TODO ...

        // Get the passwordResetToken by token, if not found throw an exception
        PwdResetToken pwdResetToken = pwdResetTokenService.getPwdResetTokenByTokenAndActivatedTrue(token);

        // Check if the new password have a valid format
        if(!passwordUtils.isPasswordValid(pwdResetRequest.getNewPassword())) {
            throw new InvalidPasswordFormatException(UserConstant.INVALID_NEW_PASSWORD_FORMAT_MSG);
        }

        User user = pwdResetToken.getUser();
        user.setPassword(passwordUtils.encodePassword(pwdResetRequest.getNewPassword()));

        saveUser(user);

        // deactivated the passwordResetToken
        pwdResetToken.setActivated(false);
        pwdResetTokenService.savePasswordResetToken(pwdResetToken);
    }



    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================

    private User createNewUser(UserRequest userRequest, Role role) throws RoleNotFoundException {

        User user = new User();

        String encodedPassword = passwordUtils.encodePassword(userRequest.getPassword());

        user.setUsername(userRequest.getUsername());
        user.setPassword(encodedPassword);

        user.setDeleted(false);
        user.setEnabled(true);

        // The user did not receive the mail yet
        user.setMailSent(false);

        user.setRole(role);

        // Save on data base
        return saveUser(user);
    }

    private RoleName getRoleNameFromString(String role) {
        RoleName roleNameDefault = RoleName.STUDENT;
        for (RoleName roleName : RoleName.values()) {
            if (role.equals(roleName.name())) {
                return roleName;
            }
        }
        return roleNameDefault;
    }
}
