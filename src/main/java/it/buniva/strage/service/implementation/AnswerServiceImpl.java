package it.buniva.strage.service.implementation;

import it.buniva.strage.constant.AnswerConstant;
import it.buniva.strage.entity.Answer;
import it.buniva.strage.entity.Question;
import it.buniva.strage.exception.answer.*;
import it.buniva.strage.exception.question.QuestionNotFoundException;
import it.buniva.strage.payload.request.AnswerRequest;
import it.buniva.strage.payload.request.AnswerUpdateRequest;
import it.buniva.strage.repository.AnswerRepository;
import it.buniva.strage.service.AnswerService;
import it.buniva.strage.service.QuestionService;
import it.buniva.strage.utility.MyStringUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class AnswerServiceImpl implements AnswerService {

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionService questionService;


    // ===================================================================
    // ======================== IMPLEMENTATIONS ==========================
    // ===================================================================

    // ============================= CREATE ===============================
    @Override
    public Answer addAnswerInQuestion(AnswerRequest answerRequest)
            throws InvalidAnswerRequestFormException, QuestionNotFoundException,
            DuplicateAnswerContentException, AnswerTrueAlreadyExistException,
            DefaultAnswerAlreadyExistException, NoWantAnswerAlreadyExistException {

        // Validate the answer request form
        // We can add 3 type of answer:
        // 1 - The normal answer of a question
        // 2 - Only one default_answer
        // 3 - Only one No_want_to_answer
        // int typeOfAnswer = determineTypeOfAnswer(answerRequest);

        // Can throw exception
        validateAnswerRequest(answerRequest);

        Question questionByCode = new Question();
        String answerCode = "";
        if (answerRequest.isNormalAnswer()) {
            // If Question not exist throw an exception
            questionByCode = questionService.getQuestionByCodeAndEnabledTrueAndDeletedFalse(answerRequest.getQuestionCode());

            // If Answer already exist Throw exception: AnswerAlreadyExistException
            existAlreadyAnswerByContentByQuestion(answerRequest.getAnswerContent(), questionByCode);

            // For this question, we check if there is already a correct answer
            // If already exist throw an exception, we can just have on answer correct by question
            if (answerRequest.getCorrect()) {
                existAlreadyAnswerCorrectTrueForQuestion(questionByCode);
            }

            // Generate Answer code
            answerCode = generateAnswerCode(answerRequest);

        } else if (answerRequest.isDefaultAnswer()) {

            // Throw exception if already exist
            existsAlreadyDefaultAnswerByCode(AnswerConstant.DEFAULT_ANSWER_CODE);

        } else if (answerRequest.isNoWantToAnswer()) {
            // Throw exception if already exist
            existsAlreadyNoWantAnswerByCode(AnswerConstant.NO_WANT_ANSWER_CODE);
        }

        // create the new answer without saving in db
        return createNewAnswer(answerRequest, questionByCode, answerCode);
    }


    // ============================== READ ================================
    @Override
    public Answer getAnswerByCodeAndEnabledTrueAndDeletedFalse(String answerCode)
            throws AnswerNotFoundException {

        Answer answer = answerRepository.findByAnswerCodeAndEnabledTrueAndDeletedFalse(answerCode);

        if (answer == null) {
            throw new AnswerNotFoundException(
                    String.format(AnswerConstant.ANSWER_NOT_FOUND_BY_CODE_MSG, answerCode));
        }

        return answer;
    }

    @Override
    public Answer getAnswerByCodeAndDeletedFalse(String answerCode) throws AnswerNotFoundException {
        Answer answer = answerRepository.findByAnswerCodeAndDeletedFalse(answerCode);

        if (answer == null) {
            throw new AnswerNotFoundException(
                    String.format(AnswerConstant.ANSWER_NOT_FOUND_BY_CODE_MSG, answerCode));
        }

        return answer;
    }

    @Override
    public List<Answer> getAllAnswerByQuestion(String questionCode) throws EmptyAnswerListException {
        List<Answer> answerList = answerRepository.findAllByQuestionQuestionCodeAndEnabledTrueAndDeletedFalse(questionCode);

        if (answerList.isEmpty()) {
            throw new EmptyAnswerListException(AnswerConstant.EMPTY_ANSWER_LIST_MSG);
        }

        return answerList;
    }

    @Override
    public List<Answer> getAllAnswerByQuestionCode(String questionCode) throws EmptyAnswerListException {
        List<Answer> answers = answerRepository.findAllByQuestionQuestionCodeAndEnabledTrueAndDeletedFalse(questionCode);
        if (answers.isEmpty())
        {
            throw new EmptyAnswerListException(String.format(AnswerConstant.EMPTY_ANSWER_LIST_MSG, questionCode));
        }
        return answers;
    }

    @Override
    public Answer getCorrectAnswerInQuestion(String questionCode) throws QuestionNotFoundException {
        Question question = questionService.getQuestionByQuestionCodeAndEnabledTrueAndDeletedFalse(questionCode);
        return answerRepository.findByQuestionAndCorrectTrueAndDeletedFalse(question);
    }

    @Override
    public void existAlreadyAnswerByContentByQuestion(String aContent, Question question) throws DuplicateAnswerContentException {
        boolean existByContent = answerRepository
                .existsByAnswerContentAndQuestion(aContent, question);
        if (existByContent) {
            throw new DuplicateAnswerContentException(
                    String.format(
                            AnswerConstant.ANSWER_ALREADY_EXIST_IN_QUESTION_MSG,
                            aContent,
                            question.getQuestionCode()));
        }
    }

    @Override
    public void existAlreadyAnswerCorrectTrueForQuestion(Question question) throws AnswerTrueAlreadyExistException {

        boolean correctByQuestion = answerRepository.existsByQuestionAndCorrectTrue(question);
        if (correctByQuestion) {
            throw new AnswerTrueAlreadyExistException(
                    String.format(
                            AnswerConstant.ANSWER_TRUE_ALREADY_EXIST_IN_QUESTION_MSG,
                            question.getQuestionCode()));
        }
    }

    @Override
    public void existsAlreadyDefaultAnswerByCode(String defaultAnswerCode) throws DefaultAnswerAlreadyExistException {
        boolean existsAnswerByCode = existsAnswerByCode(defaultAnswerCode);
        if (existsAnswerByCode) {
            throw new DefaultAnswerAlreadyExistException(
                    AnswerConstant.DEFAULT_ANSWER_ALREADY_EXIST_MSG);
        }
    }

    @Override
    public void existsAlreadyNoWantAnswerByCode(String noWantAnswerCode) throws NoWantAnswerAlreadyExistException {
        boolean existsAnswerByCode = existsAnswerByCode(noWantAnswerCode);
        if (existsAnswerByCode) {
            throw new NoWantAnswerAlreadyExistException(
                    AnswerConstant.NO_WANT_ANSWER_ALREADY_EXIST_MSG);
        }
    }

    @Override
    public boolean existsAnswerByCode(String code) {
        return answerRepository.existsByAnswerCode(code);
    }

    // ============================ UPDATE =================================
    @Override
    public Answer updateAnswer(
            String answerCode,
            AnswerUpdateRequest answerUpdateRequest)
            throws AnswerNotFoundException, DuplicateAnswerContentException, QuestionNotFoundException {

        Answer answer = getAnswerByCodeAndEnabledTrueAndDeletedFalse(answerCode);
        Answer answerByContent = answerRepository.findByAnswerContentAndQuestion(answerUpdateRequest.getAnswerContent(), answer.getQuestion());
        if (answerByContent != null)
        {
            if (!answer.getId().equals(answerByContent.getId()))
            {
                throw new DuplicateAnswerContentException(String.format(
                        AnswerConstant.DUPLICATE_ANSWER_CONTENT_IN_QUESTION_MSG,
                        answerUpdateRequest.getAnswerContent(),
                        answer.getQuestion().getQuestionCode()));
            }
        }
        answer.setAnswerContent(answerUpdateRequest.getAnswerContent());
        if (answerUpdateRequest.isCorrect())
        {
            Answer oldCorrectAnswer = getCorrectAnswerInQuestion(answer.getQuestion().getQuestionCode());
            if (oldCorrectAnswer != null)
            {
                oldCorrectAnswer.setCorrect(false);
                saveAnswer(oldCorrectAnswer);
            }
        }
        answer.setCorrect(answerUpdateRequest.isCorrect());
        return saveAnswer(answer);
    }

    @Override
    public Answer enableDisableAnswer(String answerCode) throws AnswerNotFoundException {
        Answer answer = getAnswerByCodeAndDeletedFalse(answerCode);
        answer.setEnabled(!answer.isEnabled());
        return saveAnswer(answer);
    }

    // ============================= DELETE ================================
    @Override
    public void deleteAnswer(String answerCode) throws AnswerNotFoundException {
        Answer answer = getAnswerByCodeAndEnabledTrueAndDeletedFalse(answerCode);
        answer.setDeleted(true);
        answer.setEnabled(false);
        saveAnswer(answer);
    }

    // ============================== SAVE ==================================
    @Override
    public Answer saveAnswer(Answer answer) {
        return answerRepository.save(answer);
    }

    // ============================== OTHER ==================================





    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================

    private Answer createNewAnswer(AnswerRequest answerRequest, Question question, String answerCode) {

        Answer newAnswer = new Answer();
        if (answerRequest.isNormalAnswer()) {
            newAnswer.setAnswerCode(answerCode);

            newAnswer.setQuestion(question);
            newAnswer.setCorrect(answerRequest.getCorrect());

            newAnswer.setDefaultAnswer(false);
            newAnswer.setNoWantAnswerItem(false);

        } else { // Is default answer or no want to answer
            if (answerRequest.isDefaultAnswer()) {
                newAnswer.setAnswerCode(AnswerConstant.DEFAULT_ANSWER_CODE);
                newAnswer.setDefaultAnswer(true);
                newAnswer.setNoWantAnswerItem(false);

            } else if (answerRequest.isNoWantToAnswer()) {
                newAnswer.setAnswerCode(AnswerConstant.NO_WANT_ANSWER_CODE);
                newAnswer.setDefaultAnswer(false);
                newAnswer.setNoWantAnswerItem(true);
            }

            newAnswer.setQuestion(null);
            newAnswer.setCorrect(false);
        }

        newAnswer.setAnswerContent(answerRequest.getAnswerContent());

        newAnswer.setEnabled(true);
        newAnswer.setDeleted(false);

        return saveAnswer(newAnswer);
    }

    private void validateAnswerRequest(AnswerRequest answerRequest) throws InvalidAnswerRequestFormException {


        if (answerRequest.isNormalAnswer() &&
                !answerRequest.isDefaultAnswer() &&
                !answerRequest.isNoWantToAnswer()) {

            // IS NORMAL ANSWER
            if (!StringUtils.isNotBlank(answerRequest.getQuestionCode())) {
                throw new InvalidAnswerRequestFormException(
                        AnswerConstant.INVALID_VALUE_OF_QUESTION_CODE_MSG);
            }

        } else if (!answerRequest.isNormalAnswer() &&
                answerRequest.isDefaultAnswer() &&
                !answerRequest.isNoWantToAnswer()) {

            // IS DEFAULT ANSWER
            answerRequest.setQuestionCode(null);
            answerRequest.setCorrect(false);

        } else if (!answerRequest.isNormalAnswer() &&
                !answerRequest.isDefaultAnswer() &&
                answerRequest.isNoWantToAnswer()) {

            // IS NO WANT TO ANSWER
            answerRequest.setQuestionCode(null);
            answerRequest.setCorrect(false);

        } else { // OTHER CASE
            throw new InvalidAnswerRequestFormException(
                    AnswerConstant.INVALID_TYPE_OF_ANSWER_MSG);
        }
    }

    private String generateAnswerCode(AnswerRequest answerRequest) {

        // Get the Question code with Format: 'Q-xxxxx'
        // And replace the Q letter with A
        char[] codeChar = MyStringUtil.replace(
                answerRequest.getQuestionCode().toCharArray(), 0, 'A'); // A-xxxxx

        String code;
        do {
            code = new String(codeChar) + "-"
                    + MyStringUtil.generateRandomNumericString(AnswerConstant.LENGTH_ANSWER_CODE);
        } while (existsAnswerByCode(code));

        return code; // A-xxxxx-xx
    }
}
