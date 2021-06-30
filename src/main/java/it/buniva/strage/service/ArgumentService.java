package it.buniva.strage.service;

import it.buniva.strage.entity.Argument;
import it.buniva.strage.entity.Subject;
import it.buniva.strage.exception.argument.ArgumentNotFoundException;
import it.buniva.strage.exception.argument.DuplicateArgumentCodeException;
import it.buniva.strage.exception.argument.EmptyArgumentListException;
import it.buniva.strage.exception.subject.SubjectNotFoundException;
import it.buniva.strage.payload.request.ArgumentRequest;

import java.util.List;

public interface ArgumentService {

    // ============================= CREATE ===============================

    Argument addArgument(ArgumentRequest argumentRequest) throws ArgumentNotFoundException, DuplicateArgumentCodeException, SubjectNotFoundException;

    // ============================== READ ================================
    Argument getArgumentByArgumentCodeAndEnabledTrueAndDeletedFalse(String argumentCode) throws ArgumentNotFoundException;

    Argument getArgumentByIdAndDeletedFalse(Long argumentId) throws ArgumentNotFoundException;

    Argument getArgumentByArgumentCodeAndDeletedFalse(String argumentCode) throws ArgumentNotFoundException;

    Argument getArgumentByIdAndEnabledTrueAndDeletedFalse(Long argumentId) throws ArgumentNotFoundException;

    List<Argument> getAllArgumentsBySubjectAndEnabledTrueAndDeletedFalse(String subjectCode) throws EmptyArgumentListException, SubjectNotFoundException;

    List<Argument> getAllArguments() throws EmptyArgumentListException;

    void existsAlreadyArgumentByCode(String argumentCode) throws DuplicateArgumentCodeException;

    // ============================ UPDATE =================================

    Argument enableDisableArgument(String argumentCode) throws ArgumentNotFoundException;

    // ============================= DELETE ================================

    void deleteArgument(Long argumentId) throws ArgumentNotFoundException;

    // ============================== SAVE ==================================

    Argument saveArgument(Argument argument);

    // ============================== OTHER ==================================

}
