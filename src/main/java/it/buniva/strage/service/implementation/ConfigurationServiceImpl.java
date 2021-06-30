package it.buniva.strage.service.implementation;

import it.buniva.strage.constant.AnswerConstant;
import it.buniva.strage.constant.ConfigurationConstant;
import it.buniva.strage.constant.QuestionConstant;
import it.buniva.strage.entity.*;
import it.buniva.strage.exception.answer.AnswerNotFoundException;
import it.buniva.strage.exception.answer.AnswerTrueAlreadyExistException;
import it.buniva.strage.exception.answer.EmptyAnswerListException;
import it.buniva.strage.exception.argument.ArgumentNotFoundException;
import it.buniva.strage.exception.argument.EmptyArgumentListException;
import it.buniva.strage.exception.configuration.ConfigurationCreationFailedException;
import it.buniva.strage.exception.configuration.ConfigurationNotFoundException;
import it.buniva.strage.exception.configuration.DuplicateConfigurationNameException;
import it.buniva.strage.exception.configuration.EmptyConfigurationListException;
import it.buniva.strage.exception.question.EmptyQuestionListException;
import it.buniva.strage.exception.question.InvalidAnswerMarkException;
import it.buniva.strage.exception.subject.SubjectNotFoundException;
import it.buniva.strage.payload.request.ConfigurationRequest;
import it.buniva.strage.repository.ConfigurationRepository;
import it.buniva.strage.repository.PermissionRepository;
import it.buniva.strage.service.*;
import it.buniva.strage.utility.MyStringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


@Service
@Transactional(rollbackFor = Exception.class)
public class ConfigurationServiceImpl implements ConfigurationService {

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private SubjectService subjectService;

    @Autowired
    private ArgumentService argumentService;

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;



    // ===================================================================
    // ======================== IMPLEMENTATIONS ==========================
    // ===================================================================

    // ============================= CREATE ===============================
    @Override
    public Configuration addConfigurationInSubject(ConfigurationRequest configurationRequest)
            throws SubjectNotFoundException, DuplicateConfigurationNameException,
            ConfigurationCreationFailedException {

        Subject subject = subjectService.getSubjectBySubjectCodeAndEnabledTrueAndDeletedFalse(configurationRequest.getSubjectCode());

        validateConfiguration(configurationRequest);

        return createNewConfiguration(configurationRequest, subject);
    }

    // ============================== READ ================================
    @Override
    public Configuration getConfigurationByCodeAndDeletedFalse(String configurationCode)
            throws ConfigurationNotFoundException {

        Configuration configuration =
                configurationRepository.findByConfigurationCodeAndDeletedFalse(configurationCode);

        if (configuration == null)
        {
            throw new ConfigurationNotFoundException(
                    String.format(
                            ConfigurationConstant.CONFIGURATION_NOT_FOUND_BY_CODE_MSG,
                            configurationCode));
        }

        return configuration;
    }

    @Override
    public Configuration getConfigurationByCodeAndEnabledTrueAndDeletedFalse(String configurationCode)
            throws ConfigurationNotFoundException {
        Configuration configuration =
                configurationRepository.findByConfigurationCodeAndEnabledTrueAndDeletedFalse(configurationCode);

        if (configuration == null) {
            throw new ConfigurationNotFoundException(
                    String.format(
                            ConfigurationConstant.CONFIGURATION_NOT_FOUND_BY_CODE_MSG,
                            configurationCode));
        }

        return configuration;
    }

    @Override
    public List<Configuration> getAllConfigurationBySubject(String subjectCode)
            throws EmptyConfigurationListException {

        List<Configuration> configurationList = configurationRepository.findAllBySubjectSubjectCode(subjectCode);

        if (configurationList.isEmpty()) {
            throw new EmptyConfigurationListException(ConfigurationConstant.EMPTY_CONFIGURATION_LIST_MSG);
        }

        return configurationList;
    }

    @Override
    public boolean existsConfigurationByCode(String configurationCode) {
        Configuration configuration = configurationRepository.findByConfigurationCode(configurationCode);
        return configuration != null;
    }

    @Override
    public boolean existsConfigurationByConfigurationName(String configurationName) {
        Configuration configuration = configurationRepository.findByConfigurationName(configurationName);
        return configuration != null;
    }


    // ============================ UPDATE =================================
    @Override
    public Configuration updateConfiguration(
            String configurationCode,
            ConfigurationRequest configurationRequest)
            throws ConfigurationNotFoundException, SubjectNotFoundException,
            ConfigurationCreationFailedException, DuplicateConfigurationNameException  {

        Subject subject = subjectService.getSubjectBySubjectCodeAndEnabledTrueAndDeletedFalse(configurationRequest.getSubjectCode());
        Configuration configuration = getConfigurationByCodeAndEnabledTrueAndDeletedFalse(configurationCode);

        validateConfiguration(configurationRequest);

        configuration.setConfigurationCode(generateConfigurationCode());
        configuration.setConfigurationName(configurationRequest.getConfigurationName());
        configuration.setSubject(subject);
        configuration.setNumberQuestion(configurationRequest.getNumberQuestion());
        configuration.setNumberAnswerByQuestion(configurationRequest.getNumberAnswerByQuestion());
        configuration.setMinScore(configurationRequest.getMinScore());
        configuration.setMinScoreToPass(configurationRequest.getMinScoreToPass());
        configuration.setMaxScore(configurationRequest.getMaxScore());
        configuration.setDuration(configurationRequest.getDuration());
        configuration.setDurationSced(configurationRequest.getDurationSced());
        configuration.setSameMarkForAllQuestions(configurationRequest.isSameMarkForAllQuestions());
        configuration.setCorrectAnswerMark(configurationRequest.getCorrectAnswerMark());
        configuration.setIncorrectAnswerMark(configurationRequest.getIncorrectAnswerMark());
        configuration.setRoundMark(configurationRequest.isRoundMark());
        configuration.setOneQuizForAllStud(configurationRequest.isOneQuizForAllStud());
        configuration.setRandomAnswer(configurationRequest.isRandomAnswer());
        configuration.setEnabledAutoCommit(configurationRequest.isEnabledAutoCommit());
        configuration.setEnabledNoWantAnswer(configurationRequest.isEnabledNoWantAnswer());
        configuration.setEnabledDefaultAns(configurationRequest.isEnabledDefaultAns());

        return configuration;
    }

    @Override
    public Configuration enableDisableConfiguration(String configurationCode)
            throws ConfigurationNotFoundException {

        Configuration configuration = getConfigurationByCodeAndEnabledTrueAndDeletedFalse(configurationCode);
        configuration.setEnabled(!configuration.isEnabled());
        return saveConfiguration(configuration);
    }


    // ============================= DELETE ================================

    @Override
    public void deleteConfiguration(String configurationCode) throws ConfigurationNotFoundException {
        Configuration configuration = getConfigurationByCodeAndEnabledTrueAndDeletedFalse(configurationCode);
        configuration.setDeleted(true);
        configuration.setEnabled(false);
    }

    // ============================== SAVE ==================================

    @Override
    public Configuration saveConfiguration(Configuration configuration) {
        return configurationRepository.save(configuration);
    }
    // ============================== OTHER ==================================





    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================

    private void validateConfiguration(ConfigurationRequest configurationRequest)
            throws DuplicateConfigurationNameException, ConfigurationCreationFailedException {

        if (existsConfigurationByConfigurationName(configurationRequest.getConfigurationName())) {
            throw new DuplicateConfigurationNameException(
                    String.format(
                            ConfigurationConstant.DUPLICATE_CONFIGURATION_NAME_MSG,
                            configurationRequest.getConfigurationName()));
        }

        /*List<Argument> argumentList = argumentService.getAllArgumentsBySubjectAndEnabledTrueAndDeletedFalse(configurationRequest.getSubjectCode());

        for (Argument argument : argumentList) {
            List<Question> questionList = new ArrayList<>();
            try{
                questionList = questionService.getAllQuestionByArgument(argument.getArgumentCode());
            } catch (EmptyQuestionListException e) { }

            if (questionList.size() < configurationRequest.getNumberQuestion()) {
                throw new ConfigurationCreationFailedException(String.format(ConfigurationConstant.ARGUMENT_NOT_CONTAINS_ENOUGH_QUESTIONS_MSG, argument.getArgumentCode()));
            }
            for (Question question : questionList) {
                List<Answer> answerList = new ArrayList<>();
                try {
                    answerList = answerService.getAllAnswerByQuestionCode(question.getQuestionCode());
                } catch (EmptyAnswerListException e) { }

                if (answerList.size() < configurationRequest.getNumberAnswerByQuestion()) {
                    throw new ConfigurationCreationFailedException(String.format(ConfigurationConstant.QUESTION_NOT_CONTAINS_ENOUGH_ANSWERS_MSG, question.getQuestionCode()));
                }
            }
        }*/

        if (configurationRequest.getMinScore() >= configurationRequest.getMaxScore()) {
            throw new ConfigurationCreationFailedException(
                    String.format(
                            ConfigurationConstant.MIN_SCORE_IS_BIGGER_THAN_MAX_SCORE_MSG,
                            configurationRequest.getMinScore(),
                            configurationRequest.getMaxScore()));
        }
        if (configurationRequest.getMinScoreToPass() < configurationRequest.getMinScore() ||
                configurationRequest.getMinScoreToPass() > configurationRequest.getMaxScore()) {

            throw new ConfigurationCreationFailedException(
                    String.format(
                            ConfigurationConstant.MIN_SCORE_TO_PASS_OUT_OF_IN_RANGE_MSG,
                            configurationRequest.getMinScoreToPass(),
                            configurationRequest.getMinScore(),
                            configurationRequest.getMaxScore()));
        }

        if (configurationRequest.isSameMarkForAllQuestions()) {
            if (configurationRequest.getCorrectAnswerMark() == null) {
                throw new ConfigurationCreationFailedException(
                        ConfigurationConstant.CORRECT_ANSWER_MARK_IS_NOT_SET_MSG);
            }
            if (configurationRequest.getIncorrectAnswerMark() == null) {
                throw new ConfigurationCreationFailedException(
                        ConfigurationConstant.INCORRECT_ANSWER_MARK_IS_NOT_SET_MSG);
            }

            validateQuestionMark(
                    configurationRequest.getCorrectAnswerMark(),
                    configurationRequest.getIncorrectAnswerMark());
        }
        /*else
        {
            List<Question> questionList = questionService.getAllQuestionBySubject(configurationRequest.getSubjectCode());

            for (Question question : questionList)
            {
                if (question.getCorrectAnswerMark() == null)
                {
                    throw new ConfigurationCreationFailedException(
                            String.format(
                                    ConfigurationConstant.QUESTION_CORRECT_ANSWER_MARK_IS_NOT_SET_MSG,
                                    question.getQuestionCode()));
                }
                if (question.getIncorrectAnswerMark() == null)
                {
                    throw new ConfigurationCreationFailedException(
                            String.format(
                                    ConfigurationConstant.QUESTION_INCORRECT_ANSWER_MARK_IS_NOT_SET_MSG,
                                    question.getQuestionCode()));
                }

                validateQuestionMark(question.getCorrectAnswerMark(), question.getIncorrectAnswerMark());
            }
        }*/

        if (configurationRequest.isEnabledDefaultAns())
        {
            try {
                answerService.getAnswerByCodeAndEnabledTrueAndDeletedFalse(AnswerConstant.DEFAULT_ANSWER_CODE);
            }
            catch (AnswerNotFoundException e) {
                throw new ConfigurationCreationFailedException(
                        ConfigurationConstant.DEFAULT_ANSWER_NOT_FOUND_MSG);
            }
        }

        if (configurationRequest.isEnabledNoWantAnswer())
        {
            try {
                answerService.getAnswerByCodeAndEnabledTrueAndDeletedFalse(AnswerConstant.NO_WANT_ANSWER_CODE);
            }
            catch (AnswerNotFoundException e) {
                throw new ConfigurationCreationFailedException(
                        ConfigurationConstant.NO_WANT_ANSWER_NOT_FOUND_MSG);
            }
        }
    }

    private void validateQuestionMark(Double correctAnswerMark, Double incorrectAnswerMark) throws ConfigurationCreationFailedException {
        if (correctAnswerMark < incorrectAnswerMark*-1) {
            throw new ConfigurationCreationFailedException(String.format(
                    ConfigurationConstant.INCORRECT_ANSWER_MARK_MUST_BE_LEAST_THAN_CORRECT_ANSWER_MARK_MSG,
                    incorrectAnswerMark*-1, correctAnswerMark));
        }
    }

    private Configuration createNewConfiguration(ConfigurationRequest configurationRequest, Subject subject) {
        Configuration configuration = new Configuration();

        configuration.setConfigurationCode(generateConfigurationCode());
        configuration.setConfigurationName(configurationRequest.getConfigurationName());
        configuration.setSubject(subject);
        configuration.setNumberQuestion(configurationRequest.getNumberQuestion());
        configuration.setNumberAnswerByQuestion(configurationRequest.getNumberAnswerByQuestion());
        configuration.setMinScore(configurationRequest.getMinScore());
        configuration.setMinScoreToPass(configurationRequest.getMinScoreToPass());
        configuration.setMaxScore(configurationRequest.getMaxScore());
        configuration.setDuration(configurationRequest.getDuration());
        configuration.setDurationSced(configurationRequest.getDurationSced());
        configuration.setSameMarkForAllQuestions(configurationRequest.isSameMarkForAllQuestions());
        configuration.setCorrectAnswerMark(configurationRequest.getCorrectAnswerMark());
        configuration.setIncorrectAnswerMark(configurationRequest.getIncorrectAnswerMark());
        configuration.setRoundMark(configurationRequest.isRoundMark());
        configuration.setOneQuizForAllStud(configurationRequest.isOneQuizForAllStud());
        configuration.setRandomAnswer(configurationRequest.isRandomAnswer());
        configuration.setEnabledAutoCommit(configurationRequest.isEnabledAutoCommit());
        configuration.setEnabledNoWantAnswer(configurationRequest.isEnabledNoWantAnswer());
        configuration.setEnabledDefaultAns(configurationRequest.isEnabledDefaultAns());

        return saveConfiguration(configuration);
    }

    private String generateConfigurationCode() {
        String code;
        do{
            code = "C-" + MyStringUtil.generateRandomNumericString(ConfigurationConstant.LENGTH_CONFIGURATION_CODE);
        }while(existsConfigurationByCode(code));
        return code;
    }
}
