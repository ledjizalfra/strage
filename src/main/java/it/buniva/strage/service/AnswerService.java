package it.buniva.strage.service;

import it.buniva.strage.entity.Answer;
import it.buniva.strage.entity.Question;
import it.buniva.strage.exception.answer.*;
import it.buniva.strage.exception.question.QuestionNotFoundException;
import it.buniva.strage.payload.request.AnswerRequest;

public interface AnswerService {

    // ============================= CREATE ===============================
    Answer addAnswerInQuestion(AnswerRequest answerRequest) throws InvalidAnswerRequestFormException, QuestionNotFoundException, DuplicateAnswerContentException, AnswerTrueAlreadyExistException, DefaultAnswerAlreadyExistException, NoWantAnswerAlreadyExistException;

    // ============================== READ ================================
    void existAlreadyAnswerByContentByQuestion(String aContent, Question question) throws DuplicateAnswerContentException;

    void existAlreadyAnswerCorrectTrueForQuestion(Question question) throws AnswerTrueAlreadyExistException;

    void existsAlreadyDefaultAnswerByCode(String defaultAnswerCode) throws DefaultAnswerAlreadyExistException, DefaultAnswerAlreadyExistException;

    void existsAlreadyNoWantAnswerByCode(String noWantAnswerCode) throws NoWantAnswerAlreadyExistException, NoWantAnswerAlreadyExistException;

    boolean existsAnswerByCode(String code);


    // ============================ UPDATE =================================


    // ============================= DELETE ================================


    // ============================== SAVE ==================================
    Answer saveAnswer(Answer answer);


    // ============================== OTHER ==================================

}
