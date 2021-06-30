package it.buniva.strage.service;

import it.buniva.strage.entity.Configuration;
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

import java.util.List;

public interface ConfigurationService {
    // ============================= CREATE ===============================
    Configuration addConfigurationInSubject(ConfigurationRequest configurationRequest) throws SubjectNotFoundException, DuplicateConfigurationNameException, ConfigurationCreationFailedException, EmptyQuestionListException, EmptyAnswerListException, AnswerTrueAlreadyExistException, EmptyArgumentListException, ArgumentNotFoundException, InvalidAnswerMarkException;

    // ============================== READ ================================
    Configuration getConfigurationByCodeAndDeletedFalse(String configurationCode) throws ConfigurationNotFoundException;

    Configuration getConfigurationByCodeAndEnabledTrueAndDeletedFalse(String configurationCode) throws ConfigurationNotFoundException;

    List<Configuration> getAllConfigurationBySubject(String subjectCode) throws EmptyConfigurationListException;

    boolean existsConfigurationByCode(String configurationCode);

    boolean existsConfigurationByConfigurationName(String configurationName);

    // ============================ UPDATE =================================
    Configuration updateConfiguration(String configurationCode, ConfigurationRequest configurationRequest) throws ConfigurationNotFoundException, SubjectNotFoundException, EmptyAnswerListException, ConfigurationCreationFailedException, EmptyArgumentListException, AnswerTrueAlreadyExistException, DuplicateConfigurationNameException, EmptyQuestionListException, ArgumentNotFoundException, InvalidAnswerMarkException;

    Configuration enableDisableConfiguration(String configurationCode) throws ConfigurationNotFoundException;


    // ============================= DELETE ================================
    void deleteConfiguration(String configurationCode) throws ConfigurationNotFoundException;


    // ============================== SAVE ==================================
    Configuration saveConfiguration(Configuration configuration);

    // ============================== OTHER ==================================

}
