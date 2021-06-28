package it.buniva.strage.controller;

import io.swagger.annotations.ApiOperation;
import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.entity.Admin;
import it.buniva.strage.event.UserAddedSuccessfullyEvent;
import it.buniva.strage.exception.admin.AdminExceptionHandling;
import it.buniva.strage.exception.admin.AdminNotFoundException;
import it.buniva.strage.exception.admin.EmptyAdminListException;
import it.buniva.strage.exception.role.RoleNotFoundException;
import it.buniva.strage.exception.student.DuplicatePersonalDataException;
import it.buniva.strage.exception.user.DuplicateUsernameException;
import it.buniva.strage.exception.user.UserNotFoundException;
import it.buniva.strage.payload.request.AdminRequest;
import it.buniva.strage.payload.request.PersonalDataRequest;
import it.buniva.strage.payload.response.AdminResponse;
import it.buniva.strage.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admins")
public class AdminController extends AdminExceptionHandling {

    @Autowired
    private AdminService adminService;

    @Autowired
    private ApplicationEventPublisher publisher;


    // ============================= CREATE ===============================
    @ApiOperation(value = "RegisterAdmin", notes = "Allows to register a new Admin, "
            + "but also providing the general user info (email and authorities).\n"
            + "response messages:\n"
            + " => Model(AdminResponse): in case of success,\n"
            + " => Otherwise return an error message")
    @PostMapping(value = "/register-admin")
//    @PreAuthorize("hasAuthority('admin:create')")
    public ResponseEntity<ApiResponseCustom> registerAdmin(
            @RequestBody @Valid AdminRequest adminRequest,
            HttpServletRequest request)
            throws DuplicateUsernameException, UserNotFoundException, RoleNotFoundException,
            AdminNotFoundException, DuplicatePersonalDataException, MessagingException {

        Admin admin = adminService.registerAdmin(adminRequest);

        // Publish an event to notifier the send mail Scheduling that it can start sending mail
        publisher.publishEvent(new UserAddedSuccessfullyEvent(this, true));

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 201,
                HttpStatus.CREATED, "", AdminResponse.createFromAdmin(admin), request.getRequestURI()),
                HttpStatus.CREATED);
    }

    // ============================== READ ================================
    @GetMapping(value = "/get-admin-by-id/{adminId}")
//    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponseCustom> getById(
            @PathVariable("adminId") Long adminId,
            HttpServletRequest request) throws AdminNotFoundException {

        Admin admin = adminService.getAdminByUserIdAndEnabledTrueAndDeletedFalse(adminId);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", AdminResponse.createFromAdmin(admin), request.getRequestURI()),
                HttpStatus.OK);
    }

    @GetMapping(value = "/get-all-admins")
//    @PreAuthorize("hasAuthority('admin:read')")
    public ResponseEntity<ApiResponseCustom> getAllAdmins(HttpServletRequest request) throws EmptyAdminListException {

        List<Admin> adminList = adminService.getAllAdminsByEnabledTrueAndDeletedFalse();
        List<AdminResponse> adminResponseList = new ArrayList<>();

        for (Admin admin : adminList) {
            adminResponseList.add(AdminResponse.createFromAdmin(admin));
        }

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", adminResponseList, request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================ UPDATE =================================
    @PutMapping(value = "/enable-disable/{adminId}")
//    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponseCustom> enableDisableAdmin(
            @PathVariable Long adminId,
            HttpServletRequest request) throws AdminNotFoundException, UserNotFoundException {

        Admin admin = adminService.enableDisableAdmin(adminId);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", AdminResponse.createFromAdmin(admin), request.getRequestURI()),
                HttpStatus.OK);
    }

    @PutMapping(value = "/update-admin-personal-data/{adminId}")
//    @PreAuthorize("hasAuthority('admin:update')")
    public ResponseEntity<ApiResponseCustom> updatePersonalData(
            @PathVariable Long adminId,
            @RequestBody @Valid PersonalDataRequest personalDataRequest,
            HttpServletRequest request) throws AdminNotFoundException, DuplicatePersonalDataException {

        Admin admin = adminService.updatePersonalData(adminId, personalDataRequest);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", AdminResponse.createFromAdmin(admin), request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================= DELETE ================================
    @PutMapping(value = "/delete-admin/{adminId}")
//    @PreAuthorize("hasAuthority('admin:delete')")
    public ResponseEntity<ApiResponseCustom> setDeleted(
            @PathVariable Long adminId,
            HttpServletRequest request) throws UserNotFoundException, AdminNotFoundException {

        adminService.deleteAdmin(adminId);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", "Admin successfully deleted", request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================== OTHER ==================================





    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================


}
