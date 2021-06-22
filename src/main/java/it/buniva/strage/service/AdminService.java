package it.buniva.strage.service;

import it.buniva.strage.entity.Admin;
import it.buniva.strage.entity.compositeatributte.PersonalData;
import it.buniva.strage.exception.admin.AdminNotFoundException;
import it.buniva.strage.exception.admin.EmptyAdminListException;
import it.buniva.strage.exception.role.RoleNotFoundException;
import it.buniva.strage.exception.student.DuplicatePersonalDataException;
import it.buniva.strage.exception.user.DuplicateUsernameException;
import it.buniva.strage.exception.user.UserNotFoundException;
import it.buniva.strage.payload.request.AdminRequest;
import it.buniva.strage.payload.request.PersonalDataRequest;

import javax.mail.MessagingException;
import java.util.List;

public interface AdminService {

    // ============================= CREATE ===============================
    Admin registerAdmin(AdminRequest adminRequest) throws DuplicateUsernameException, UserNotFoundException, RoleNotFoundException, AdminNotFoundException, DuplicatePersonalDataException, MessagingException;

    // ============================== READ ================================
    Admin getAdminByIdAndEnabledTrueAndDeletedFalse(Long adminId) throws AdminNotFoundException;

    Admin getAdminByUserIdAndEnabledTrueAndDeletedFalse(Long userId) throws AdminNotFoundException;

    Admin getAdminByUserIdAndDeletedFalse(Long userId) throws AdminNotFoundException;

    Admin getAdminByIdAndDeletedFalse(Long adminId) throws AdminNotFoundException;

    Admin getAdminByEmail(String email) throws AdminNotFoundException;

    List<Admin> getAllAdminsByEnabledTrueAndDeletedFalse() throws EmptyAdminListException;

    void existsAlreadyAdminByPersonalData(PersonalData personalData) throws DuplicatePersonalDataException;

    void existsAlreadyAdminByPersonalDataOnUpdate(Long adminId, PersonalData personalData) throws DuplicatePersonalDataException;


    // ============================ UPDATE =================================
    Admin updatePersonalData(Long adminId, PersonalDataRequest personalDataRequest) throws AdminNotFoundException, DuplicatePersonalDataException;

    Admin enableDisableAdmin(Long adminId) throws AdminNotFoundException, UserNotFoundException;

    Admin updateEnabled(Long adminId, boolean enabled) throws AdminNotFoundException;

    // ============================= DELETE ================================
    void deleteAdmin(Long adminId) throws AdminNotFoundException, UserNotFoundException;

    // ============================== SAVE ==================================
    Admin saveAdmin(Admin admin);


    // ============================== OTHER ==================================

}
