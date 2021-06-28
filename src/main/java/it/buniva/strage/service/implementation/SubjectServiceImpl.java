package it.buniva.strage.service.implementation;

import it.buniva.strage.constant.SubjectConstant;
import it.buniva.strage.entity.Subject;
import it.buniva.strage.exception.subject.DuplicateSubjectCodeException;
import it.buniva.strage.exception.subject.EmptySubjectListException;
import it.buniva.strage.exception.subject.SubjectNotFoundException;
import it.buniva.strage.payload.request.SubjectRequest;
import it.buniva.strage.repository.SubjectRepository;
import it.buniva.strage.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class SubjectServiceImpl implements SubjectService {

    @Autowired
    private SubjectRepository subjectRepository;


    // ===================================================================
    // ======================== IMPLEMENTATIONS ==========================
    // ===================================================================

    // ============================= CREATE ===============================

    @Override
    public Subject addSubject(SubjectRequest subjectRequest) throws DuplicateSubjectCodeException {

        return  createNewSubject(subjectRequest);
    }

    // ============================== READ ================================

    @Override
    public Subject getSubjectBySubjectCodeAndEnabledTrueAndDeletedFalse(String subjectCode) throws SubjectNotFoundException
    {
        Subject subject = subjectRepository.findBySubjectCodeAndEnabledTrueAndDeletedFalse(subjectCode);
        if (subject == null)
        {
            throw new SubjectNotFoundException(
                    String.format(SubjectConstant.SUBJECT_NOT_FOUND_BY_CODE_MSG, subjectCode));
        }
        return subject;
    }

    @Override
    public Subject getSubjectByIdAndDeletedFalse(Long subjectId) throws SubjectNotFoundException
    {
        Subject subject = subjectRepository.findByIdAndDeletedFalse(subjectId);
        if (subject == null)
        {
            throw new SubjectNotFoundException(
                    String.format(SubjectConstant.SUBJECT_NOT_FOUND_BY_CODE_MSG, subjectId));
        }
        return subject;
    }

    @Override
    public Subject getSubjectBySubjectCodeAndDeletedFalse(String subjectCode) throws SubjectNotFoundException
    {
        Subject subject = subjectRepository.findBySubjectCodeAndDeletedFalse(subjectCode);
        if (subject == null)
        {
            throw new SubjectNotFoundException(
                    String.format(SubjectConstant.SUBJECT_NOT_FOUND_BY_CODE_MSG, subjectCode));
        }
        return subject;
    }

    @Override
    public Subject getSubjectByIdAndEnabledTrueAndDeletedFalse(Long subjectId) throws SubjectNotFoundException
    {
        Subject subject = subjectRepository.findByIdAndEnabledTrueAndDeletedFalse(subjectId);
        if (subject == null)
        {
            throw new SubjectNotFoundException(
                    String.format(SubjectConstant.SUBJECT_NOT_FOUND_BY_ID_MSG,subjectId));
        }
        return subject;
    }

    @Override
    public void existsAlreadySubjectByCode(String subjectCode) throws DuplicateSubjectCodeException {
        Subject subject = subjectRepository.findBySubjectCode(subjectCode);
        if (subject != null)
        {
            throw new DuplicateSubjectCodeException(
                    String.format(SubjectConstant.DUPLICATE_SUBJECT_CODE_MSG, subjectCode));
        }
    }

    @Override
    public List<Subject> getAllSubjects() throws EmptySubjectListException {
        List<Subject> subjectList = subjectRepository.findAllByDeletedFalseAndEnabledTrue();
        if (subjectList.isEmpty())
        {
            throw new EmptySubjectListException(SubjectConstant.EMPTY_SUBJECT_LIST_MSG);
        }
        return subjectList;
    }



    // ============================ UPDATE =================================

    @Override
    public Subject enableDisableSubject(String subjectCode) throws SubjectNotFoundException {
        Subject subject = getSubjectBySubjectCodeAndDeletedFalse(subjectCode);
        subject.setEnabled(!subject.isEnabled());
        return saveSubject(subject);
    }

    // ============================= DELETE ================================

    @Override
    public void deleteSubject(Long subjectId) throws SubjectNotFoundException {
        Subject subject = getSubjectByIdAndDeletedFalse(subjectId);
        subject.setDeleted(true);
        subject.setEnabled(false);
        saveSubject(subject);
    }

    // ============================== SAVE ==================================

    @Override
    public Subject saveSubject(Subject subject) {
        return subjectRepository.save(subject);
    }




    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================

    private Subject createNewSubject(SubjectRequest subjectRequest) throws DuplicateSubjectCodeException {

        Subject subject = new Subject();

        subject.setSubjectName(subjectRequest.getSubjectName());
        subject.setSubjectCode(generateSubjectCode(subjectRequest));

        subject.setEnabled(true);
        subject.setDeleted(false);

        // Save in DB
        return saveSubject(subject);
    }
    private String generateSubjectCode(SubjectRequest subjectRequest) throws DuplicateSubjectCodeException {
        String subjectCode = subjectRequest.getSubjectName().toUpperCase() + "-" + subjectRequest.getAnnuity();

        existsAlreadySubjectByCode(subjectCode);

        return subjectCode;
    }

}
