package it.buniva.strage.controller;

import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.constant.SecurityConstant;
import it.buniva.strage.entity.User;
import it.buniva.strage.exception.admin.AdminNotFoundException;
import it.buniva.strage.exception.professor.ProfessorNotFoundException;
import it.buniva.strage.exception.pwdresettoken.InvalidPwdResetTokenException;
import it.buniva.strage.exception.pwdresettoken.PwdResetTokenExpiredException;
import it.buniva.strage.exception.student.StudentNotFoundException;
import it.buniva.strage.exception.user.*;
import it.buniva.strage.payload.request.AuthenticationRequest;
import it.buniva.strage.payload.request.ForgotPasswordRequest;
import it.buniva.strage.payload.request.PasswordRequest;
import it.buniva.strage.payload.request.PwdResetRequest;
import it.buniva.strage.payload.response.UserLoggedResponse;
import it.buniva.strage.payload.response.UserResponse;
import it.buniva.strage.security.UserPrincipal;
import it.buniva.strage.service.UserService;
import it.buniva.strage.utility.JWTTokenProvider;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController extends UserExceptionHandling {

    private final Logger LOGGER = LoggerFactory.getLogger(getClass());

    @Autowired
    private UserService userService;

    @Autowired
    private JWTTokenProvider jwtTokenProvider;

    @Autowired
    private AuthenticationManager authenticationManager;



    @PostMapping("/login")
    public ResponseEntity<ApiResponseCustom> authentication(
            @RequestBody @Valid AuthenticationRequest authenticationRequest,
            HttpServletResponse response,
            HttpServletRequest request)
            throws UserNotFoundException, AdminNotFoundException, ProfessorNotFoundException,
            StudentNotFoundException, DisabledException, LockedException, BadCredentialsException {

        String emailAsUsername = authenticationRequest.getEmail().toLowerCase().trim();
        String password = authenticationRequest.getPassword();

        authenticate(emailAsUsername, password);

        User loggedUser = userService.getUserByUsername(emailAsUsername);
        UserPrincipal userPrincipal = new UserPrincipal(loggedUser);

        // Add the token to the HEADER
        // HttpHeaders jwtHeader = getJwtHeader(userPrincipal);
        String token = jwtTokenProvider.generateJwtToken(userPrincipal);
        response.setHeader(SecurityConstant.JWT_TOKEN_HEADER, token);


        UserLoggedResponse userLoggedResponse = UserLoggedResponse.createFrom(
                loggedUser,
                token,
                userService.getUserPersonalData(loggedUser.getUserId())
        );

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, StringUtils.EMPTY, userLoggedResponse, request.getRequestURI()),
                HttpStatus.OK);
    }


    // ============================= CREATE ===============================


    // ============================== READ ================================
    @GetMapping(value = "/get-user-by-id/{userId}")
//    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<ApiResponseCustom> getUserById(
            @PathVariable("userId") Long userId,
            HttpServletRequest request) throws UserNotFoundException {

        User user = userService.getUserByIdAndEnabledTrueAndDeletedFalse(userId);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", UserResponse.createFromUser(user), request.getRequestURI()),
                HttpStatus.OK);
    }

    @GetMapping(value = "/get-all-users")
//    @PreAuthorize("hasAuthority('user:read')")
    public ResponseEntity<ApiResponseCustom> getAllUsers(HttpServletRequest request) throws EmptyUserListException {
        List<User> userList = userService.getAllUsersByEnabledTrueAndDeletedFalse();

        List<UserResponse> userResponseList = new ArrayList<>();
        for (User user : userList) {
            userResponseList.add(UserResponse.createFromUser(user));
        }

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", userResponseList, request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================ UPDATE =================================
    @PutMapping(value = "/enable-disable/{userId}")
//    @PreAuthorize("hasAuthority('user:update')")
    public ResponseEntity<ApiResponseCustom> enableDisableUser(
            @PathVariable Long userId,
            HttpServletRequest request)
            throws UserNotFoundException, AdminNotFoundException, ProfessorNotFoundException,
            StudentNotFoundException {

        User user = userService.enableDisableUser(userId);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", UserResponse.createFromUser(user), request.getRequestURI()),
                HttpStatus.OK);
    }

    @PutMapping(value = "/change-password")
    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR', 'STUDENT')")
    public ResponseEntity<ApiResponseCustom> changePassword(
            @RequestBody @Valid PasswordRequest passwordRequest,
            HttpServletRequest request)
            throws UserNotFoundException, PasswordNotMatchesException, InvalidPasswordFormatException {

        // User must logged
        userService.changePassword(passwordRequest);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", "Password has been successfully changed", request.getRequestURI()),
                HttpStatus.OK);
    }

    @PostMapping(value = "/forgot-password")
    public ResponseEntity<ApiResponseCustom> sendMailPasswordRecover(
            @Valid @RequestBody ForgotPasswordRequest forgotRequest,
            HttpServletRequest request) throws UserNotFoundException, MessagingException {

        userService.forgotPassword(forgotRequest, request);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", "Email successfully sent", request.getRequestURI()),
                HttpStatus.OK);
    }

    @PostMapping(value = "/password-reset/{token}")
    public ResponseEntity<ApiResponseCustom> resetPasswordForm(
            @PathVariable String token,
            @Valid @RequestBody PwdResetRequest pwdResetRequest,
            HttpServletRequest request)
            throws InvalidPwdResetTokenException, InvalidPasswordFormatException,
            PwdResetTokenExpiredException {

        userService.passwordReset(pwdResetRequest, token);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", "Password successfully reset", request.getRequestURI()),
                HttpStatus.OK);
    }


    // ============================= DELETE ================================
    @PutMapping(value = "/delete-user/{userId}")
//    @PreAuthorize("hasAuthority('user:delete')")
    public ResponseEntity<ApiResponseCustom> setDeleted(
            @PathVariable Long userId,
            HttpServletRequest request) throws UserNotFoundException {

        userService.deleteUser(userId);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", "User successfully deleted", request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================== OTHER ==================================




    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================

    // =====================================================
    // == PRIVATE METHODS
    // =====================================================

    /**
     * Set the token in the Header with the key: JWT_TOKEN_HEADER="Jwt-Token"
     * @param user
     * @return
     */
    private HttpHeaders getJwtHeader(UserPrincipal user) {
        HttpHeaders header = new HttpHeaders();
        header.add(SecurityConstant.JWT_TOKEN_HEADER, jwtTokenProvider.generateJwtToken(user));

        return header;
    }

    private void authenticate(String username, String password)
            throws DisabledException, LockedException, BadCredentialsException {

        final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(username, password));

        LOGGER.info(authentication.toString());
    }
}
