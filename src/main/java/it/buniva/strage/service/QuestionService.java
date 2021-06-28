package it.buniva.strage.service;

import it.buniva.strage.entity.Argument;
import it.buniva.strage.entity.Question;
import it.buniva.strage.exception.argument.ArgumentNotFoundException;
import it.buniva.strage.exception.question.DuplicateQuestionContentException;
import it.buniva.strage.exception.question.QuestionNotFoundException;
import it.buniva.strage.payload.request.QuestionRequest;

public interface QuestionService {

    // ============================= CREATE ===============================
    Question addQuestionByArgument(QuestionRequest questionRequest) throws ArgumentNotFoundException, DuplicateQuestionContentException;

    // ============================== READ ================================
    Question getQuestionByCodeAndEnabledTrueAndDeletedFalse(String questionCode) throws QuestionNotFoundException;

   void existAlreadyQuestionByContentInArgument(String questionContent, Argument argument) throws DuplicateQuestionContentException;

    // ============================ UPDATE =================================
    boolean existsQuestionByCode(String questionCode);

    // ============================== SAVE ==================================
    Question saveQuestion(Question question);

    // ============================== READ ================================


    // ============================ UPDATE =================================


    // ============================= DELETE ================================


    // ============================== SAVE ==================================


    // ============================== OTHER ==================================

}
