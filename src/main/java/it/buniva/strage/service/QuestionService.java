package it.buniva.strage.service;

import it.buniva.strage.entity.Argument;
import it.buniva.strage.entity.Question;
import it.buniva.strage.exception.argument.ArgumentNotFoundException;
import it.buniva.strage.exception.argument.ArgumentNotInSameSubjectException;
import it.buniva.strage.exception.question.DuplicateQuestionContentException;
import it.buniva.strage.exception.question.EmptyQuestionListException;
import it.buniva.strage.exception.question.InvalidAnswerMarkException;
import it.buniva.strage.exception.question.QuestionNotFoundException;
import it.buniva.strage.exception.subject.SubjectNotFoundException;
import it.buniva.strage.payload.request.QuestionAnswerMarkRequest;
import it.buniva.strage.payload.request.QuestionArgumentRequest;
import it.buniva.strage.payload.request.QuestionContentRequest;
import it.buniva.strage.payload.request.QuestionRequest;

import java.util.List;

public interface QuestionService {

    // ============================= CREATE ===============================
    Question addQuestionByArgument(QuestionRequest questionRequest) throws ArgumentNotFoundException, DuplicateQuestionContentException;

    // ============================== READ ================================
    Question getQuestionByCodeAndEnabledTrueAndDeletedFalse(String questionCode) throws QuestionNotFoundException;

    Question getQuestionByCodeAndDeletedFalse(String questionCode) throws QuestionNotFoundException;

    Question getQuestionByQuestionCodeAndEnabledTrueAndDeletedFalse(String questionCode) throws QuestionNotFoundException;

    List<Question> getAllQuestion() throws EmptyQuestionListException;

    List<Question> getAllQuestionByArgument(String argumentCode) throws EmptyQuestionListException, ArgumentNotFoundException;

    List<Question> getAllQuestionBySubject(String subjectCode) throws EmptyQuestionListException, SubjectNotFoundException;

    void existAlreadyQuestionByContentInArgument(String questionContent, Argument argument) throws DuplicateQuestionContentException;

    boolean existsQuestionByCode(String questionCode);


    // ============================ UPDATE =================================
    Question updateQuestionContent(
            String questionCode,
            QuestionContentRequest questionContentRequest)
            throws QuestionNotFoundException, DuplicateQuestionContentException;

    Question updateQuestionAnswerMark(
            String questionCode,
            QuestionAnswerMarkRequest questionAnswerMarkRequest)
            throws QuestionNotFoundException, InvalidAnswerMarkException;

    Question updateQuestionArgument(
            String questionCode,
            QuestionArgumentRequest questionArgumentRequest)
            throws QuestionNotFoundException, ArgumentNotFoundException,
            ArgumentNotInSameSubjectException;

    Question enableDisableQuestion(String questionCode) throws QuestionNotFoundException;

    // ============================= DELETE ================================
    void deleteQuestion(String questionCode) throws QuestionNotFoundException;

    // ============================== SAVE ==================================
    Question saveQuestion(Question question);


    // ============================== OTHER ==================================

}
