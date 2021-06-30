package it.buniva.strage.service;

import it.buniva.strage.entity.Answer;
import it.buniva.strage.entity.Question;
import it.buniva.strage.exception.answer.*;
import it.buniva.strage.exception.question.QuestionNotFoundException;
import it.buniva.strage.payload.request.AnswerRequest;
import it.buniva.strage.payload.request.AnswerUpdateRequest;

import java.util.List;

public interface AnswerService {

    // ============================= CREATE ===============================
    Answer addAnswerInQuestion(AnswerRequest answerRequest)
            throws InvalidAnswerRequestFormException, QuestionNotFoundException,
            DuplicateAnswerContentException, AnswerTrueAlreadyExistException,
            DefaultAnswerAlreadyExistException, NoWantAnswerAlreadyExistException;

    // ============================== READ ================================
    Answer getAnswerByCodeAndDeletedFalse(String answerCode) throws AnswerNotFoundException;

    List<Answer> getAllAnswerByQuestion(String questionCode) throws EmptyAnswerListException;

    List<Answer> getAllAnswerByQuestionCode(String questionCode) throws EmptyAnswerListException;

    Answer getCorrectAnswerInQuestion(String questionCode) throws QuestionNotFoundException;

    Answer getAnswerByCodeAndEnabledTrueAndDeletedFalse(String answerCode) throws AnswerNotFoundException;

    void existAlreadyAnswerByContentByQuestion(String aContent, Question question) throws DuplicateAnswerContentException;

    void existAlreadyAnswerCorrectTrueForQuestion(Question question) throws AnswerTrueAlreadyExistException;

    void existsAlreadyDefaultAnswerByCode(String defaultAnswerCode) throws DefaultAnswerAlreadyExistException, DefaultAnswerAlreadyExistException;

    void existsAlreadyNoWantAnswerByCode(String noWantAnswerCode) throws NoWantAnswerAlreadyExistException, NoWantAnswerAlreadyExistException;

    boolean existsAnswerByCode(String code);


    // ============================ UPDATE =================================


    // ============================ UPDATE =================================
    Answer updateAnswer(
            String answerCode,
            AnswerUpdateRequest answerUpdateRequest)
            throws AnswerNotFoundException, DuplicateAnswerContentException, QuestionNotFoundException;

    Answer enableDisableAnswer(String answerCode) throws AnswerNotFoundException;

    // ============================= DELETE ================================
    void deleteAnswer(String answerCode) throws AnswerNotFoundException;

    // ============================== SAVE ==================================
    Answer saveAnswer(Answer answer);




    // ============================== OTHER ==================================

}
