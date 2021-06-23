package it.buniva.strage.service.implementation;

import it.buniva.strage.constant.AdminConstant;
import it.buniva.strage.constant.UserConstant;
import it.buniva.strage.entity.Admin;
import it.buniva.strage.entity.User;
import it.buniva.strage.entity.compositeatributte.PersonalData;
import it.buniva.strage.enumaration.MailObject;
import it.buniva.strage.enumaration.RoleName;
import it.buniva.strage.event.SendMailEvent;
import it.buniva.strage.exception.admin.AdminNotFoundException;
import it.buniva.strage.exception.admin.EmptyAdminListException;
import it.buniva.strage.exception.role.RoleNotFoundException;
import it.buniva.strage.exception.student.DuplicatePersonalDataException;
import it.buniva.strage.exception.user.DuplicateUsernameException;
import it.buniva.strage.exception.user.UserNotFoundException;
import it.buniva.strage.payload.request.*;
import it.buniva.strage.repository.AdminRepository;
import it.buniva.strage.service.AdminService;
import it.buniva.strage.service.MailService;
import it.buniva.strage.service.UserService;
import it.buniva.strage.utility.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private PasswordUtils passwordUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    @Autowired
    private ApplicationEventPublisher publisher;




    // ===================================================================
    // ======================== IMPLEMENTATIONS ==========================
    // ===================================================================

    // ============================= CREATE ===============================
    @Override
    public Admin registerAdmin(AdminRequest adminRequest) throws DuplicateUsernameException, UserNotFoundException, RoleNotFoundException, AdminNotFoundException, DuplicatePersonalDataException, MessagingException {
        PersonalData personalData = new PersonalData(
                adminRequest.getEmail(),
                adminRequest.getName(),
                adminRequest.getSurname()
        );

        // Throw an exception if exist
        existsAlreadyAdminByPersonalData(personalData);

        // Generate the password
        String password = passwordUtils.generatePassword(UserConstant.LENGTH_PASSWORD_GENERATED);

        // Insert one user
        User newUser = userService.registerUser(new UserRequest(
                adminRequest.getEmail(),
                password,
                RoleName.ADMIN.name()
        ));

        // Create a new admin and save in DB
        Admin newAdmin = createNewAdmin(adminRequest, newUser, personalData);

        // Prepare the sendMailRequest
        SendMailRequest sendMailRequest = SendMailRequest.createFromAdmin(
                newAdmin,
                MailObject.CREDENTIALS_MAIL,
                password
        );

        // Publish an event, the listener would get it an send the mail to the user
        // we send the email:username and password
        publisher.publishEvent(new SendMailEvent(this, sendMailRequest));

        return getAdminByIdAndEnabledTrueAndDeletedFalse(newAdmin.getId());
    }

    // ============================== READ ================================
    @Override
    public Admin getAdminByIdAndEnabledTrueAndDeletedFalse(Long adminId) throws AdminNotFoundException {
        Admin admin = adminRepository.findByIdAndEnabledTrueAndDeletedFalse(adminId);

        if (admin == null) {
            throw new AdminNotFoundException(
                    String.format(AdminConstant.ADMIN_NOT_FOUND_BY_ID_MSG, adminId));
        }

        return admin;
    }

    @Override
    public Admin getAdminByUserIdAndEnabledTrueAndDeletedFalse(Long userId) throws AdminNotFoundException {
        Admin admin = adminRepository.findByUserUserIdAndEnabledTrueAndDeletedFalse(userId);

        if (admin == null) {
            throw new AdminNotFoundException(
                    String.format(AdminConstant.ADMIN_NOT_FOUND_BY_USER_ID_MSG, userId));
        }

        return admin;
    }

    @Override
    public Admin getAdminByUserIdAndDeletedFalse(Long userId) throws AdminNotFoundException {
        Admin admin = adminRepository.findByUserUserIdAndDeletedFalse(userId);

        if (admin == null) {
            throw new AdminNotFoundException(
                    String.format(AdminConstant.ADMIN_NOT_FOUND_BY_USER_ID_MSG, userId));
        }

        return admin;
    }

    @Override
    public Admin getAdminByIdAndDeletedFalse(Long adminId) throws AdminNotFoundException {
        Admin admin = adminRepository.findByIdAndDeletedFalse(adminId);

        if (admin == null) {
            throw new AdminNotFoundException(
                    String.format(AdminConstant.ADMIN_NOT_FOUND_BY_ID_MSG, adminId));
        }

        return admin;
    }

    @Override
    public Admin getAdminByEmail(String email) throws AdminNotFoundException {
        Admin admin = adminRepository.findByPersonalDataEmailAndEnabledTrueAndDeletedFalse(email);
        if(admin == null) {
            throw new AdminNotFoundException(
                    String.format(AdminConstant.ADMIN_NOT_FOUND_BY_EMAIL_MSG, email));
        }
        return admin;
    }

    @Override
    public List<Admin> getAllAdminsByEnabledTrueAndDeletedFalse() throws EmptyAdminListException {
        List<Admin> adminList = adminRepository.findAllByEnabledTrueAndDeletedFalse();

        if (adminList.isEmpty()) {
            throw new EmptyAdminListException(AdminConstant.EMPTY_ADMIN_LIST_MSG);
        }

        return adminList;
    }

    @Override
    public void existsAlreadyAdminByPersonalData(PersonalData personalData) throws DuplicatePersonalDataException {
        boolean existAdmin = adminRepository.existsByPersonalData(personalData);
        if(existAdmin) {
            throw new DuplicatePersonalDataException(
                    String.format(AdminConstant.DUPLICATE_PERSONAL_DATA_MSG, personalData.toString()));
        }
    }

    @Override
    public void existsAlreadyAdminByPersonalDataOnUpdate(Long adminId, PersonalData personalData) throws DuplicatePersonalDataException {
        Admin admin = adminRepository.findAdminByPersonalData(personalData);
        if(admin!=null && !admin.getId().equals(adminId)) {
            // If another admin is found with the same personal data, an exception is throw
            throw new DuplicatePersonalDataException(
                    String.format(AdminConstant.DUPLICATE_PERSONAL_DATA_MSG, personalData.toString()));
        }
    }

    // ============================ UPDATE =================================
    @Override
    public Admin updatePersonalData(Long adminId, PersonalDataRequest personalDataRequest) throws AdminNotFoundException, DuplicatePersonalDataException {
        Admin admin = getAdminByIdAndEnabledTrueAndDeletedFalse(adminId);

        PersonalData personalData = new PersonalData(
                admin.getPersonalData().getEmail(),
                personalDataRequest.getName(),
                personalDataRequest.getSurname());

        // Throw an exception if already exist
        existsAlreadyAdminByPersonalDataOnUpdate(adminId, personalData);

        admin.setPersonalData(personalData);

        return saveAdmin(admin);
    }

    @Override
    public Admin enableDisableAdmin(Long adminId) throws AdminNotFoundException, UserNotFoundException {
        Admin admin = getAdminByIdAndDeletedFalse(adminId);
        admin.setEnabled(!admin.isEnabled());

        userService.updateEnable(admin.getUser().getUserId(), admin.isEnabled());

        return saveAdmin(admin);
    }

    @Override
    public Admin updateEnabled(Long adminId, boolean enabled) throws AdminNotFoundException {
        Admin admin = getAdminByIdAndDeletedFalse(adminId);
        admin.setEnabled(enabled);

        return saveAdmin(admin);
    }

    // ============================= DELETE ================================
    @Override
    public void deleteAdmin(Long adminId) throws AdminNotFoundException, UserNotFoundException {
        Admin admin = getAdminByIdAndDeletedFalse(adminId);
        admin.setDeleted(true);
        saveAdmin(admin);

        userService.deleteUser(admin.getUser().getUserId());
    }

    // ============================== SAVE ==================================
    @Override
    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    // ============================== OTHER ==================================





    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================

    private Admin createNewAdmin(AdminRequest adminRequest, User user, PersonalData personalData) {

        Admin newAdmin = new Admin();
        newAdmin.setPersonalData(personalData);
        newAdmin.setUser(user);

        newAdmin.setEnabled(true);
        newAdmin.setDeleted(false);

        // Save on DB
        return saveAdmin(newAdmin);
    }
}
