package it.buniva.strage.controller;

import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.entity.Configuration;
import it.buniva.strage.exception.answer.AnswerTrueAlreadyExistException;
import it.buniva.strage.exception.answer.EmptyAnswerListException;
import it.buniva.strage.exception.argument.ArgumentNotFoundException;
import it.buniva.strage.exception.argument.EmptyArgumentListException;
import it.buniva.strage.exception.configuration.*;
import it.buniva.strage.exception.question.EmptyQuestionListException;
import it.buniva.strage.exception.question.InvalidAnswerMarkException;
import it.buniva.strage.exception.question.QuestionNotFoundException;
import it.buniva.strage.exception.subject.SubjectNotFoundException;
import it.buniva.strage.payload.request.ConfigurationRequest;
import it.buniva.strage.payload.response.ConfigurationResponse;
import it.buniva.strage.service.ConfigurationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/configurations")
public class ConfigurationController extends ConfigurationExceptionHandling {

    @Autowired
    private ConfigurationService configurationService;


    // ============================= CREATE ===============================
    @PostMapping(value = "/add-configuration-by-subject")
//    @PreAuthorize("hasAuthority('question:create')")
    public ResponseEntity<ApiResponseCustom> addConfigurationBySubject(
            @RequestBody @Valid ConfigurationRequest configurationRequest,
            HttpServletRequest request) throws DuplicateConfigurationNameException, SubjectNotFoundException, EmptyAnswerListException, EmptyArgumentListException, ConfigurationCreationFailedException, AnswerTrueAlreadyExistException, EmptyQuestionListException, ArgumentNotFoundException, InvalidAnswerMarkException {

        Configuration configuration = configurationService.addConfigurationInSubject(configurationRequest);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 201,
                HttpStatus.CREATED, "", ConfigurationResponse.createFromConfiguration(configuration), request.getRequestURI()),
                HttpStatus.CREATED);
    }

    // ============================== READ ================================
    @GetMapping(value = "/get-configuration-by-code/{configurationCode}")
    // @PreAuthorize("hasAuthority('question:read')")
    public ResponseEntity<ApiResponseCustom> getConfigurationByCode(
            @PathVariable("configurationCode") String configurationCode,
            HttpServletRequest request) throws ConfigurationNotFoundException {

        Configuration configuration = configurationService.getConfigurationByCodeAndEnabledTrueAndDeletedFalse(configurationCode);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", ConfigurationResponse.createFromConfiguration(configuration), request.getRequestURI()),
                HttpStatus.OK);
    }

    @GetMapping(value = "/get-all-configurations-by-subject/{subjectCode}")
    // @PreAuthorize("hasAuthority('question:read') and hasAuthority('answer:read')")
    public ResponseEntity<ApiResponseCustom> getAllConfigurationBySubjectCode(
            @PathVariable("subjectCode") String subjectCode,
            HttpServletRequest request) throws  EmptyConfigurationListException {

        List<Configuration> configurationList = configurationService.getAllConfigurationBySubject(subjectCode);
        List<ConfigurationResponse> configurationResponseList = configurationList.stream()
                .map(ConfigurationResponse::createFromConfiguration)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", configurationResponseList, request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================ UPDATE =================================
    @PutMapping(value = "/update-configuration/{configurationCode}")
//    @PreAuthorize("hasAuthority('question:update')")
    public ResponseEntity<ApiResponseCustom> updateConfiguration(
            @PathVariable String configurationCode,
            @RequestBody @Valid ConfigurationRequest configurationRequest,
            HttpServletRequest request)
            throws SubjectNotFoundException, DuplicateConfigurationNameException,
            ConfigurationNotFoundException, EmptyAnswerListException, ConfigurationCreationFailedException,
            EmptyArgumentListException, AnswerTrueAlreadyExistException, EmptyQuestionListException,
            ArgumentNotFoundException, InvalidAnswerMarkException {

        Configuration configuration = configurationService.updateConfiguration(configurationCode, configurationRequest);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.CREATED, "", ConfigurationResponse.createFromConfiguration(configuration), request.getRequestURI()),
                HttpStatus.CREATED);
    }

    @PutMapping(value = "/enable-disable/{configurationCode}")
//    @PreAuthorize("hasAuthority('question:update')")
    public ResponseEntity<ApiResponseCustom> enableDisableConfiguration(
            @PathVariable String configurationCode,
            HttpServletRequest request) throws  ConfigurationNotFoundException {

        Configuration configuration = configurationService.enableDisableConfiguration(configurationCode);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.CREATED, "", ConfigurationResponse.createFromConfiguration(configuration), request.getRequestURI()),
                HttpStatus.CREATED);
    }
    // ============================= DELETE ================================
    @PutMapping(value = "/delete-configuration/{configurationCode}")
//    @PreAuthorize("hasAuthority('professor:delete')")
    public ResponseEntity<ApiResponseCustom> delete(
            @PathVariable String configurationCode, HttpServletRequest request)
            throws QuestionNotFoundException, ConfigurationNotFoundException {

        configurationService.deleteConfiguration(configurationCode);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", "Configuration successfully deleted", request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================== OTHER ==================================





    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================


}
