package it.buniva.strage.service.implementation;

import it.buniva.strage.constant.QuestionConstant;
import it.buniva.strage.entity.Argument;
import it.buniva.strage.entity.Question;
import it.buniva.strage.entity.Subject;
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
import it.buniva.strage.repository.QuestionRepository;
import it.buniva.strage.service.ArgumentService;
import it.buniva.strage.service.QuestionService;
import it.buniva.strage.service.SubjectService;
import it.buniva.strage.utility.MyStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ArgumentService argumentService;

    @Autowired
    private SubjectService subjectService;

    // ===================================================================
    // ======================== IMPLEMENTATIONS ==========================
    // ===================================================================

    // ============================= CREATE ===============================
    @Override
    public Question addQuestionByArgument(QuestionRequest questionRequest) throws ArgumentNotFoundException, DuplicateQuestionContentException {
        Argument argument =
                argumentService.getArgumentByArgumentCodeAndEnabledTrueAndDeletedFalse(
                        questionRequest.getArgumentCode());

        // Control if the question exist by content
        // throw exception if not exist
        existAlreadyQuestionByContentInArgument(questionRequest.getQuestionContent(),argument);

        return  createNewQuestion(questionRequest,argument);
    }


    // ============================== READ ================================

    @Override
    public Question getQuestionByCodeAndEnabledTrueAndDeletedFalse(String questionCode) throws QuestionNotFoundException {

        Question question = questionRepository.findByQuestionCodeAndEnabledTrueAndDeletedFalse(questionCode);
        if(question == null) {
            throw new QuestionNotFoundException(
                    String.format(QuestionConstant.QUESTION_NOT_FOUND_BY_CODE_MSG, questionCode));
        }

        return question;
    }

    @Override
    public Question getQuestionByCodeAndDeletedFalse(String questionCode) throws QuestionNotFoundException {

        Question question = questionRepository.findByQuestionCodeAndDeletedFalse(questionCode);
        if(question == null) {
            throw new QuestionNotFoundException(
                    String.format(QuestionConstant.QUESTION_NOT_FOUND_BY_CODE_MSG, questionCode));
        }

        return question;
    }


    @Override
    public Question getQuestionByQuestionCodeAndEnabledTrueAndDeletedFalse(String questionCode) throws QuestionNotFoundException {
        Question question = questionRepository.findByQuestionCodeAndEnabledTrueAndDeletedFalse(questionCode);
        if(question == null) {
            throw new QuestionNotFoundException(
                    String.format(QuestionConstant.QUESTION_NOT_FOUND_BY_CODE_MSG, questionCode));
        }
        return question;
    }

    @Override
    public List<Question> getAllQuestion() throws EmptyQuestionListException {
        List<Question> question = questionRepository.findAllByEnabledTrueAndDeletedFalse();
        if(question == null) {
            throw new EmptyQuestionListException(QuestionConstant.EMPTY_QUESTION_LIST_MSG);
        }
        return question;
    }

    @Override
    public List<Question> getAllQuestionByArgument(String argumentCode) throws EmptyQuestionListException, ArgumentNotFoundException {
        Argument argument = argumentService.getArgumentByArgumentCodeAndEnabledTrueAndDeletedFalse(argumentCode);
        List<Question> questionList = questionRepository.findAllByArgumentAndEnabledTrueAndDeletedFalse(argument);
        if(questionList.isEmpty()) {
            throw new EmptyQuestionListException(QuestionConstant.EMPTY_QUESTION_LIST_MSG);
        }
        return questionList;
    }

    @Override
    public List<Question> getAllQuestionBySubject(String subjectCode) throws EmptyQuestionListException, SubjectNotFoundException {

        Subject subject = subjectService.getSubjectBySubjectCodeAndEnabledTrueAndDeletedFalse(subjectCode);
        List<Question> questionList = questionRepository.findAllByArgumentSubjectAndEnabledTrueAndDeletedFalse(subject);
        if(questionList.isEmpty()) {
            throw new EmptyQuestionListException(QuestionConstant.EMPTY_QUESTION_LIST_MSG);
        }
        return questionList;
    }

    @Override
    public void existAlreadyQuestionByContentInArgument(String questionContent, Argument argument) throws DuplicateQuestionContentException {

        boolean existsQuestion = questionRepository.existsByQuestionContentAndArgument(questionContent, argument);
        if(existsQuestion) {
            throw new DuplicateQuestionContentException(
                    String.format(
                            QuestionConstant.DUPLICATE_QUESTION_CONTENT_IN_ARGUMENT_MSG,
                            questionContent,
                            argument.getArgumentCode())
            );
        }
    }


    // ============================ UPDATE =================================

    @Override
    public boolean existsQuestionByCode(String questionCode) {
        return questionRepository.existsByQuestionCode(questionCode);
    }

    @Override
    public Question updateQuestionContent(
            String questionCode,
            QuestionContentRequest questionContentRequest)
            throws QuestionNotFoundException, DuplicateQuestionContentException {

        Question question = getQuestionByCodeAndEnabledTrueAndDeletedFalse(questionCode);
        Question questionByContent = questionRepository.findByQuestionContentAndArgument(questionContentRequest.getQuestionContent(), question.getArgument());
        if (questionByContent != null) {
            if (!question.getId().equals(questionByContent.getId())) {
                throw new DuplicateQuestionContentException(String.format(
                        QuestionConstant.DUPLICATE_QUESTION_CONTENT_IN_ARGUMENT_MSG,
                        questionContentRequest.getQuestionContent(),
                        question.getArgument().getArgumentCode()));
            }
            else {
                return question;
            }
        }
        question.setQuestionContent(questionContentRequest.getQuestionContent());
        return saveQuestion(question);
    }

    @Override
    public Question updateQuestionAnswerMark(
            String questionCode,
            QuestionAnswerMarkRequest questionAnswerMarkRequest)
            throws QuestionNotFoundException, InvalidAnswerMarkException {

        if (questionAnswerMarkRequest.getCorrectAnswerMark()
                < questionAnswerMarkRequest.getIncorrectAnswerMark()*-1) {
            throw new InvalidAnswerMarkException(String.format(
                    QuestionConstant.INCORRECT_ANSWER_MARK_BIGGER_THAN_CORRECT_ANSWER_MARK_MSG,
                    questionAnswerMarkRequest.getIncorrectAnswerMark(),
                    questionAnswerMarkRequest.getCorrectAnswerMark()));
        }

        Question question = getQuestionByCodeAndEnabledTrueAndDeletedFalse(questionCode);
        question.setCorrectAnswerMark(questionAnswerMarkRequest.getCorrectAnswerMark());
        question.setIncorrectAnswerMark(questionAnswerMarkRequest.getIncorrectAnswerMark());
        return saveQuestion(question);
    }

    @Override
    public Question updateQuestionArgument(
            String questionCode,
            QuestionArgumentRequest questionArgumentRequest)
            throws QuestionNotFoundException, ArgumentNotFoundException,
            ArgumentNotInSameSubjectException {

        Question question = getQuestionByCodeAndEnabledTrueAndDeletedFalse(questionCode);

        Argument newArgument = argumentService.getArgumentByArgumentCodeAndEnabledTrueAndDeletedFalse(questionArgumentRequest.getArgumentCode());
        if (!question.getArgument().getSubject().getSubjectCode().equals(newArgument.getSubject().getSubjectCode())) {
            throw new ArgumentNotInSameSubjectException(
                    String.format(
                        QuestionConstant.ARGUMENT_NOT_IN_SAME_SUBJECT_MSG,
                        question.getArgument().getArgumentCode(),
                            newArgument.getArgumentCode()
                    ));
        }

        try {
            existAlreadyQuestionByContentInArgument(question.getQuestionContent(), newArgument);
        } catch (DuplicateQuestionContentException e) {
           return question;
        }
        question.setArgument(newArgument);
        return saveQuestion(question);
    }

    @Override
    public Question enableDisableQuestion(String questionCode) throws QuestionNotFoundException {
        Question question = getQuestionByCodeAndDeletedFalse(questionCode);
        question.setEnabled(!question.isEnabled());
        return saveQuestion(question);
    }

    // ============================= DELETE ================================

    @Override
    public void deleteQuestion(String questionCode) throws QuestionNotFoundException {
        Question question = getQuestionByCodeAndEnabledTrueAndDeletedFalse(questionCode);
        question.setEnabled(false);
        question.setDeleted(true);
        saveQuestion(question);
    }

    // ============================== SAVE ==================================
    @Override
    public Question saveQuestion(Question question) {
        return questionRepository.save(question);
    }

    // ============================== OTHER ==================================


    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================

    private Question createNewQuestion(QuestionRequest questionRequest, Argument argument) {

        Question question = new Question();
        question.setQuestionCode(generateQuestionCode());
        question.setQuestionContent(questionRequest.getQuestionContent());
        question.setArgument(argument);

        question.setEnabled(true);
        question.setDeleted(false);

        return saveQuestion(question);
    }

    private String generateQuestionCode() {
        String code;
        do{
            code = "Q-" + MyStringUtil.generateRandomNumericString(QuestionConstant.LENGTH_QUESTION_CODE);
        }while(existsQuestionByCode(code));
        return code;
    }
}
