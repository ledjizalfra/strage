package it.buniva.strage.service;

import it.buniva.strage.entity.Professor;
import it.buniva.strage.entity.compositeatributte.PersonalData;
import it.buniva.strage.exception.professor.EmptyProfessorListException;
import it.buniva.strage.exception.professor.ProfessorNotFoundException;
import it.buniva.strage.exception.role.RoleNotFoundException;
import it.buniva.strage.exception.student.DuplicatePersonalDataException;
import it.buniva.strage.exception.user.DuplicateUsernameException;
import it.buniva.strage.exception.user.UserNotFoundException;
import it.buniva.strage.payload.request.PersonalDataRequest;
import it.buniva.strage.payload.request.ProfessorRequest;

import javax.mail.MessagingException;
import java.util.List;

public interface ProfessorService {

    // ============================= CREATE ===============================
    Professor registerProfessor(ProfessorRequest professorRequest) throws DuplicateUsernameException, UserNotFoundException, RoleNotFoundException, ProfessorNotFoundException, DuplicatePersonalDataException, MessagingException;

    // ============================== READ ================================
    Professor getProfessorByIdAndEnabledTrueAndDeletedFalse(Long professorId) throws ProfessorNotFoundException;

    Professor getProfessorByUserIdAndEnabledTrueAndDeletedFalse(Long userId) throws ProfessorNotFoundException;

    Professor getProfessorByUserIdAndDeletedFalse(Long userId) throws ProfessorNotFoundException;

    Professor getProfessorByIdAndDeletedFalse(Long professorId) throws ProfessorNotFoundException;

    Professor getProfessorByEmail(String email) throws ProfessorNotFoundException;

    List<Professor> getAllProfessorsByEnabledTrueAndDeletedFalse() throws EmptyProfessorListException;

    void existsAlreadyProfessorByPersonalData(PersonalData personalData) throws DuplicatePersonalDataException;

    void existsAlreadyProfessorByPersonalDataOnUpdate(Long professorId, PersonalData personalData) throws DuplicatePersonalDataException;


    // ============================ UPDATE =================================
    Professor updatePersonalData(Long professorId, PersonalDataRequest personalDataRequest) throws ProfessorNotFoundException, DuplicatePersonalDataException;

    Professor enableDisableProfessor(Long professorId) throws ProfessorNotFoundException, UserNotFoundException;

    Professor updateEnabled(Long professorId, boolean enabled) throws ProfessorNotFoundException;

    // ============================= DELETE ================================
    void deleteProfessor(Long professorId) throws ProfessorNotFoundException, UserNotFoundException;

    // ============================== SAVE ==================================
    Professor saveProfessor(Professor professor);

    // ============================== OTHER ==================================

}
