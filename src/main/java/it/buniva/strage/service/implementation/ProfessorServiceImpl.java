package it.buniva.strage.service.implementation;

import it.buniva.strage.constant.ProfessorConstant;
import it.buniva.strage.constant.UserConstant;
import it.buniva.strage.entity.Professor;
import it.buniva.strage.entity.User;
import it.buniva.strage.entity.compositeatributte.PersonalData;
import it.buniva.strage.enumaration.MailObject;
import it.buniva.strage.enumaration.RoleName;
import it.buniva.strage.exception.professor.EmptyProfessorListException;
import it.buniva.strage.exception.professor.ProfessorNotFoundException;
import it.buniva.strage.exception.role.RoleNotFoundException;
import it.buniva.strage.exception.student.DuplicatePersonalDataException;
import it.buniva.strage.exception.user.DuplicateUsernameException;
import it.buniva.strage.exception.user.UserNotFoundException;
import it.buniva.strage.payload.request.PersonalDataRequest;
import it.buniva.strage.payload.request.ProfessorRequest;
import it.buniva.strage.payload.request.SendMailRequest;
import it.buniva.strage.payload.request.UserRequest;
import it.buniva.strage.repository.ProfessorRepository;
import it.buniva.strage.service.MailService;
import it.buniva.strage.service.ProfessorService;
import it.buniva.strage.service.UserService;
import it.buniva.strage.utility.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class ProfessorServiceImpl implements ProfessorService {

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private PasswordUtils passwordUtils;

    @Autowired
    private UserService userService;

    @Autowired
    private MailService mailService;

    // ===================================================================
    // ======================== IMPLEMENTATIONS ==========================
    // ===================================================================

    // ============================= CREATE ===============================
    @Override
    public Professor registerProfessor(ProfessorRequest professorRequest) throws DuplicateUsernameException, UserNotFoundException, RoleNotFoundException, ProfessorNotFoundException, DuplicatePersonalDataException, MessagingException {
        PersonalData personalData = new PersonalData(
                professorRequest.getEmail(),
                professorRequest.getName(),
                professorRequest.getSurname()
        );

        // Throw an exception if exist
        existsAlreadyProfessorByPersonalData(personalData);

        // Generate the password
        String password = passwordUtils.generatePassword(UserConstant.LENGTH_PASSWORD_GENERATED);

        // Insert one user
        User newUser = userService.registerUser(new UserRequest(
                professorRequest.getEmail(),
                password,
                RoleName.PROFESSOR.name()
        ));

        // Create a new professor and save in DB
        Professor newProfessor = createNewProfessor(professorRequest, newUser, personalData);

        // TODO... Send mail to the new user
        mailService.sendEmail(SendMailRequest.createFromProfessor(
                newProfessor,
                MailObject.CREDENTIALS_MAIL,
                password
        ));

        return getProfessorByIdAndEnabledTrueAndDeletedFalse(newProfessor.getId());
    }

    // ============================== READ ================================
    @Override
    public Professor getProfessorByIdAndEnabledTrueAndDeletedFalse(Long professorId) throws ProfessorNotFoundException {
        Professor professor = professorRepository.findByIdAndEnabledTrueAndDeletedFalse(professorId);

        if (professor == null) {
            throw new ProfessorNotFoundException(
                    String.format(ProfessorConstant.PROFESSOR_NOT_FOUND_BY_ID_MSG, professorId));
        }

        return professor;
    }

    @Override
    public Professor getProfessorByUserIdAndEnabledTrueAndDeletedFalse(Long userId) throws ProfessorNotFoundException {
        Professor professor = professorRepository.findByUserUserIdAndEnabledTrueAndDeletedFalse(userId);

        if (professor == null) {
            throw new ProfessorNotFoundException(
                    String.format(ProfessorConstant.PROFESSOR_NOT_FOUND_BY_USER_ID_MSG, userId));
        }

        return professor;
    }

    @Override
    public Professor getProfessorByUserIdAndDeletedFalse(Long userId) throws ProfessorNotFoundException {
        Professor professor = professorRepository.findByUserUserIdAndDeletedFalse(userId);

        if (professor == null) {
            throw new ProfessorNotFoundException(
                    String.format(ProfessorConstant.PROFESSOR_NOT_FOUND_BY_USER_ID_MSG, userId));
        }

        return professor;
    }

    @Override
    public Professor getProfessorByIdAndDeletedFalse(Long professorId) throws ProfessorNotFoundException {
        Professor professor = professorRepository.findByIdAndDeletedFalse(professorId);

        if (professor == null) {
            throw new ProfessorNotFoundException(
                    String.format(ProfessorConstant.PROFESSOR_NOT_FOUND_BY_ID_MSG, professorId));
        }

        return professor;
    }

    @Override
    public Professor getProfessorByEmail(String email) throws ProfessorNotFoundException {
        Professor professor = professorRepository.findByPersonalDataEmailAndEnabledTrueAndDeletedFalse(email);
        if(professor == null) {
            throw new ProfessorNotFoundException(
                    String.format(ProfessorConstant.PROFESSOR_NOT_FOUND_BY_EMAIL_MSG, email));
        }
        return professor;
    }

    @Override
    public List<Professor> getAllProfessorsByEnabledTrueAndDeletedFalse() throws EmptyProfessorListException {
        List<Professor> professorList = professorRepository.findAllByEnabledTrueAndDeletedFalse();

        if (professorList.isEmpty()) {
            throw new EmptyProfessorListException(ProfessorConstant.EMPTY_PROFESSOR_LIST_MSG);
        }

        return professorList;
    }

    @Override
    public void existsAlreadyProfessorByPersonalData(PersonalData personalData) throws DuplicatePersonalDataException {
        boolean existProfessor = professorRepository.existsByPersonalData(personalData);
        if(existProfessor) {
            throw new DuplicatePersonalDataException(
                    String.format(ProfessorConstant.DUPLICATE_PERSONAL_DATA_MSG, personalData.toString()));
        }
    }

    @Override
    public void existsAlreadyProfessorByPersonalDataOnUpdate(Long professorId, PersonalData personalData) throws DuplicatePersonalDataException {
        Professor professor = professorRepository.findProfessorByPersonalData(personalData);
        if(professor!=null && !professor.getId().equals(professorId)) {
            // If another professor is found with the same personal data, an exception is throw
            throw new DuplicatePersonalDataException(
                    String.format(ProfessorConstant.DUPLICATE_PERSONAL_DATA_MSG, personalData.toString()));
        }
    }

    // ============================ UPDATE =================================
    @Override
    public Professor updatePersonalData(Long professorId, PersonalDataRequest personalDataRequest) throws ProfessorNotFoundException, DuplicatePersonalDataException {
        Professor professor = getProfessorByIdAndEnabledTrueAndDeletedFalse(professorId);

        PersonalData personalData = new PersonalData(
                professor.getPersonalData().getEmail(),
                personalDataRequest.getName(),
                personalDataRequest.getSurname());

        // Throw an exception if already exist
        existsAlreadyProfessorByPersonalDataOnUpdate(professorId, personalData);

        professor.setPersonalData(personalData);

        return saveProfessor(professor);
    }

    @Override
    public Professor enableDisableProfessor(Long professorId) throws ProfessorNotFoundException, UserNotFoundException {
        Professor professor = getProfessorByIdAndDeletedFalse(professorId);
        professor.setEnabled(!professor.isEnabled());

        userService.updateEnable(professor.getUser().getUserId(), professor.isEnabled());

        return saveProfessor(professor);
    }

    @Override
    public Professor updateEnabled(Long professorId, boolean enabled) throws ProfessorNotFoundException {
        Professor professor = getProfessorByIdAndDeletedFalse(professorId);
        professor.setEnabled(enabled);

        return saveProfessor(professor);
    }

    // ============================= DELETE ================================
    @Override
    public void deleteProfessor(Long professorId) throws ProfessorNotFoundException, UserNotFoundException {
        Professor professor = getProfessorByIdAndDeletedFalse(professorId);
        professor.setDeleted(true);
        saveProfessor(professor);

        userService.deleteUser(professor.getUser().getUserId());
    }

    // ============================== SAVE ==================================
    @Override
    public Professor saveProfessor(Professor professor) {
        return professorRepository.save(professor);
    }

    // ============================== OTHER ==================================





    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================

    private Professor createNewProfessor(ProfessorRequest professorRequest, User user, PersonalData personalData) {

        Professor newProfessor = new Professor();
        newProfessor.setPersonalData(personalData);
        newProfessor.setUser(user);

        newProfessor.setEnabled(true);
        newProfessor.setDeleted(false);


        // Save on DB
        return saveProfessor(newProfessor);
    }
}
