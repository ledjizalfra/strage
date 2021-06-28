package it.buniva.strage.controller;

import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.entity.Question;
import it.buniva.strage.exception.argument.ArgumentNotFoundException;
import it.buniva.strage.exception.question.DuplicateQuestionContentException;
import it.buniva.strage.exception.question.QuestionExceptionHandling;
import it.buniva.strage.payload.request.QuestionRequest;
import it.buniva.strage.payload.response.QuestionResponse;
import it.buniva.strage.service.QuestionService;
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
@RequestMapping("/questions")
public class QuestionController extends QuestionExceptionHandling {

    @Autowired
    private QuestionService questionService;


    // ============================= CREATE ===============================
    @PostMapping(value = "/add-question-by-argument")
//    @PreAuthorize("hasAuthority('question:create')")
    public ResponseEntity<ApiResponseCustom> addQuestionByArgument(
            @RequestBody @Valid QuestionRequest questionRequest,
            HttpServletRequest request) throws ArgumentNotFoundException, DuplicateQuestionContentException {

        Question question = questionService.addQuestionByArgument(questionRequest);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 201,
                HttpStatus.CREATED, "", QuestionResponse.createFromQuestion(question), request.getRequestURI()),
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
