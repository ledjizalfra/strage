package it.buniva.strage.service.implementation;

import it.buniva.strage.constant.QuestionConstant;
import it.buniva.strage.entity.Argument;
import it.buniva.strage.entity.Question;
import it.buniva.strage.exception.argument.ArgumentNotFoundException;
import it.buniva.strage.exception.question.DuplicateQuestionContentException;
import it.buniva.strage.exception.question.QuestionNotFoundException;
import it.buniva.strage.payload.request.QuestionRequest;
import it.buniva.strage.repository.QuestionRepository;
import it.buniva.strage.service.ArgumentService;
import it.buniva.strage.service.QuestionService;
import it.buniva.strage.utility.MyStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(rollbackFor = Exception.class)
public class QuestionServiceImpl implements QuestionService {

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private ArgumentService argumentService;



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

    // ============================= DELETE ================================


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
