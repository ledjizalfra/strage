package it.buniva.strage.service.implementation;

import it.buniva.strage.constant.ArgumentConstant;
import it.buniva.strage.entity.Argument;
import it.buniva.strage.entity.Subject;
import it.buniva.strage.exception.argument.ArgumentNotFoundException;
import it.buniva.strage.exception.argument.DuplicateArgumentCodeException;
import it.buniva.strage.exception.argument.EmptyArgumentListException;
import it.buniva.strage.exception.subject.SubjectNotFoundException;
import it.buniva.strage.payload.request.ArgumentRequest;
import it.buniva.strage.repository.ArgumentRepository;
import it.buniva.strage.service.ArgumentService;
import it.buniva.strage.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class ArgumentServiceImpl implements ArgumentService {

    @Autowired
    private ArgumentRepository argumentRepository;

    @Autowired
    private SubjectService subjectService;


    // ===================================================================
    // ======================== IMPLEMENTATIONS ==========================
    // ===================================================================

    // ============================= CREATE ===============================

    @Override
    public Argument addArgument(ArgumentRequest argumentRequest) throws DuplicateArgumentCodeException, SubjectNotFoundException {

        Subject subject = subjectService.getSubjectBySubjectCodeAndEnabledTrueAndDeletedFalse(argumentRequest.getSubjectCode());

        return createNewArgument(argumentRequest, subject);
    }

    // ============================== READ ================================
    @Override
    public Argument getArgumentByArgumentCodeAndEnabledTrueAndDeletedFalse(String argumentCode) throws ArgumentNotFoundException
    {
        Argument argument = argumentRepository.findByArgumentCodeAndEnabledTrueAndDeletedFalse(argumentCode);
        if (argument == null)
        {
            throw new ArgumentNotFoundException(
                    String.format(ArgumentConstant.ARGUMENT_NOT_FOUND_BY_CODE_MSG, argumentCode));
        }
        return argument;
    }

    @Override
    public Argument getArgumentByIdAndDeletedFalse(Long argumentId) throws ArgumentNotFoundException
    {
        Argument argument = argumentRepository.findByIdAndDeletedFalse(argumentId);
        if (argument == null)
        {
            throw new ArgumentNotFoundException(
                    String.format(ArgumentConstant.ARGUMENT_NOT_FOUND_BY_ID_MSG, argumentId));
        }
        return argument;
    }

    @Override
    public Argument getArgumentByArgumentCodeAndDeletedFalse(String argumentCode) throws ArgumentNotFoundException
    {
        Argument argument = argumentRepository.findByArgumentCodeAndDeletedFalse(argumentCode);
        if (argument == null)
        {
            throw new ArgumentNotFoundException(
                    String.format(ArgumentConstant.ARGUMENT_NOT_FOUND_BY_CODE_MSG, argumentCode));
        }
        return argument;
    }

    @Override
    public Argument getArgumentByIdAndEnabledTrueAndDeletedFalse(Long argumentId) throws ArgumentNotFoundException
    {
        Argument argument = argumentRepository.findByIdAndEnabledTrueAndDeletedFalse(argumentId);
        if (argument == null)
        {
            throw new ArgumentNotFoundException(
                    String.format(ArgumentConstant.ARGUMENT_NOT_FOUND_BY_ID_MSG, argumentId));
        }
        return argument;
    }

    @Override
    public List<Argument> getAllArgumentsBySubjectAndEnabledTrueAndDeletedFalse(String subjectCode) throws EmptyArgumentListException, SubjectNotFoundException {

        Subject subject = subjectService.getSubjectBySubjectCodeAndEnabledTrueAndDeletedFalse(subjectCode);

        List<Argument> argumentList = argumentRepository.findAllBySubjectAndEnabledTrueAndDeletedFalse(subject);
        if (argumentList.isEmpty())
        {
            throw new EmptyArgumentListException(ArgumentConstant.EMPTY_ARGUMENT_LIST_MSG);
        }
        return argumentList;
    }

    @Override
    public List<Argument> getAllArguments() throws EmptyArgumentListException {
        List<Argument> argumentList = argumentRepository.findAllByEnabledTrueAndDeletedFalse();
        if (argumentList.isEmpty())
        {
            throw new EmptyArgumentListException(ArgumentConstant.EMPTY_ARGUMENT_LIST_MSG);
        }
        return argumentList;
    }

    @Override
    public void existsAlreadyArgumentByCode(String argumentCode) throws DuplicateArgumentCodeException {
        Argument argument = argumentRepository.findByArgumentCode(argumentCode);
        if (argument != null)
        {
            throw new DuplicateArgumentCodeException(
                    String.format(ArgumentConstant.DUPLICATE_ARGUMENT_CODE_MSG, argumentCode));
        }
    }

    // ============================ UPDATE =================================

    @Override
    public Argument enableDisableArgument(String argumentCode) throws ArgumentNotFoundException {
        Argument argument = getArgumentByArgumentCodeAndDeletedFalse(argumentCode);
        argument.setEnabled(!argument.isEnabled());
        return saveArgument(argument);
    }

    // ============================= DELETE ================================

    @Override
    public void deleteArgument(Long argumentId) throws ArgumentNotFoundException {
        Argument argument = getArgumentByIdAndDeletedFalse(argumentId);
        argument.setDeleted(true);
        argument.setEnabled(false);
        saveArgument(argument);
    }

    // ============================== SAVE ==================================

    @Override
    public Argument saveArgument(Argument argument) {
        return argumentRepository.save(argument);
    }



    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================

    private Argument createNewArgument(ArgumentRequest argumentRequest, Subject subject) throws DuplicateArgumentCodeException {

        Argument argument = new Argument();

        argument.setSubject(subject);
        argument.setArgumentName(argumentRequest.getArgumentName());
        argument.setArgumentCode(generateArgumentCode(argumentRequest));

        argument.setEnabled(true);
        argument.setDeleted(false);

        // Save in DB
        return saveArgument(argument);
    }
    private String generateArgumentCode(ArgumentRequest argumentRequest) throws DuplicateArgumentCodeException {

        String argumentCode = argumentRequest.getArgumentName().toUpperCase() + "-" + argumentRequest.getAnnuity();

        existsAlreadyArgumentByCode(argumentCode);

        return argumentCode;
    }
}
