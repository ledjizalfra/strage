package it.buniva.strage.controller;

import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.entity.Answer;
import it.buniva.strage.entity.Question;
import it.buniva.strage.exception.answer.*;
import it.buniva.strage.exception.argument.ArgumentNotFoundException;
import it.buniva.strage.exception.question.DuplicateQuestionContentException;
import it.buniva.strage.exception.question.QuestionNotFoundException;
import it.buniva.strage.payload.request.AnswerRequest;
import it.buniva.strage.payload.request.QuestionRequest;
import it.buniva.strage.payload.response.AnswerResponse;
import it.buniva.strage.payload.response.QuestionResponse;
import it.buniva.strage.service.AnswerService;
import it.buniva.strage.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;

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


    // ============================ UPDATE =================================


    // ============================= DELETE ================================


    // ============================== OTHER ==================================





    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================


}
