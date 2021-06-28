package it.buniva.strage.service;

import it.buniva.strage.entity.Subject;
import it.buniva.strage.exception.subject.DuplicateSubjectCodeException;
import it.buniva.strage.exception.subject.EmptySubjectListException;
import it.buniva.strage.exception.subject.SubjectNotFoundException;
import it.buniva.strage.payload.request.SubjectRequest;

import java.util.List;

public interface SubjectService {

    // ============================= CREATE ===============================

    Subject addSubject(SubjectRequest subjectRequest) throws SubjectNotFoundException, DuplicateSubjectCodeException;

    // ============================== READ ================================

    Subject getSubjectBySubjectCodeAndEnabledTrueAndDeletedFalse(String subjectCode) throws SubjectNotFoundException;

    Subject getSubjectByIdAndDeletedFalse(Long subjectId) throws SubjectNotFoundException;

    Subject getSubjectBySubjectCodeAndDeletedFalse(String subjectCode) throws SubjectNotFoundException;

    Subject getSubjectByIdAndEnabledTrueAndDeletedFalse(Long subjectId) throws SubjectNotFoundException;

    void existsAlreadySubjectByCode(String subjectCode) throws DuplicateSubjectCodeException, SubjectNotFoundException;

    List<Subject> getAllSubjects() throws EmptySubjectListException;

    // ============================ UPDATE =================================

    Subject enableDisableSubject(String subjectCode) throws SubjectNotFoundException;

    // ============================= DELETE ================================

    void deleteSubject(Long subjectId) throws SubjectNotFoundException;

    // ============================== SAVE ==================================

    Subject saveSubject(Subject subject);

    // ============================== OTHER ==================================

}
