package it.buniva.strage.controller;

import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.entity.Answer;
import it.buniva.strage.exception.answer.*;
import it.buniva.strage.exception.question.QuestionNotFoundException;
import it.buniva.strage.payload.request.AnswerRequest;
import it.buniva.strage.payload.request.AnswerUpdateRequest;
import it.buniva.strage.payload.response.AnswerResponse;
import it.buniva.strage.service.AnswerService;
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
@RequestMapping("/answers")

public class AnswerController extends AnswerExceptionHandling {

    @Autowired
    private AnswerService answerService;


    // ============================= CREATE ===============================
    @PostMapping(value = "/add-answer-in-question")
//    @PreAuthorize("hasAuthority('answer:create')")
    public ResponseEntity<ApiResponseCustom> addAnswerInQuestion(
            @RequestBody @Valid AnswerRequest answerRequest,
            HttpServletRequest request)
            throws InvalidAnswerRequestFormException, QuestionNotFoundException,
            AnswerTrueAlreadyExistException, DuplicateAnswerContentException,
            NoWantAnswerAlreadyExistException, DefaultAnswerAlreadyExistException {

        Answer answer = answerService.addAnswerInQuestion(answerRequest);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 201,
                HttpStatus.CREATED, "", AnswerResponse.createFromAnswer(answer), request.getRequestURI()),
                HttpStatus.CREATED);
    }

    // ============================== READ ================================


    @GetMapping(value = "/get-answer-by-code/{answerCode}")
//    @PreAuthorize("hasAuthority('answer:read')")
    public ResponseEntity<ApiResponseCustom> getAnswerByCode(
            @PathVariable String answerCode,
            HttpServletRequest request)
            throws AnswerNotFoundException {

        Answer answer = answerService.getAnswerByCodeAndEnabledTrueAndDeletedFalse(answerCode);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.CREATED, "", AnswerResponse.createFromAnswer(answer), request.getRequestURI()),
                HttpStatus.CREATED);
    }

    @GetMapping(value = "/get-all-answers-by-question/{questionCode}")
//    @PreAuthorize("hasAuthority('answer:read')")
    public ResponseEntity<ApiResponseCustom> getAnswersByQuestionCode(
            @PathVariable String questionCode,
            HttpServletRequest request)
            throws EmptyAnswerListException {

        List<Answer> answerList = answerService.getAllAnswerByQuestion(questionCode);

        List<AnswerResponse> answerResponseList = answerList.stream()
                .map(AnswerResponse::createFromAnswer)
                .collect(Collectors.toList());


        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.CREATED, "", answerResponseList, request.getRequestURI()),
                HttpStatus.CREATED);
    }

    // ============================ UPDATE =================================

    @PutMapping(value = "/update-answer/{answerCode}")
//    @PreAuthorize("hasAuthority('answer:update')")
    public ResponseEntity<ApiResponseCustom> updateAnswer(
            @PathVariable String answerCode,
            @RequestBody @Valid AnswerUpdateRequest answerUpdateRequest,
            HttpServletRequest request)
            throws AnswerNotFoundException, QuestionNotFoundException, DuplicateAnswerContentException {

        Answer answer = answerService.updateAnswer(answerCode, answerUpdateRequest);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.CREATED, "", AnswerResponse.createFromAnswer(answer), request.getRequestURI()),
                HttpStatus.CREATED);
    }

    @PutMapping(value = "enable-disable/{answerCode}")
//    @PreAuthorize("hasAuthority('answer:update')")
    public ResponseEntity<ApiResponseCustom> enableDisableAnswer(
            @PathVariable String answerCode,
            HttpServletRequest request) throws AnswerNotFoundException {

        Answer answer = answerService.enableDisableAnswer(answerCode);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.CREATED, "", AnswerResponse.createFromAnswer(answer), request.getRequestURI()),
                HttpStatus.CREATED);
    }

    // ============================= DELETE ================================

    @PutMapping(value = "/delete-answer/{answerCode}")
//    @PreAuthorize("hasAuthority('answer:delete')")
    public ResponseEntity<ApiResponseCustom> delete(
            @PathVariable String answerCode, HttpServletRequest request)
            throws AnswerNotFoundException {

        answerService.deleteAnswer(answerCode);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", "Answer successfully deleted", request.getRequestURI()),
                HttpStatus.OK);
    }


    // ============================== OTHER ==================================


    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================


}
