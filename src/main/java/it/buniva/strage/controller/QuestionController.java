package it.buniva.strage.controller;

import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.entity.Question;
import it.buniva.strage.exception.answer.AnswerTrueAlreadyExistException;
import it.buniva.strage.exception.answer.EmptyAnswerListException;
import it.buniva.strage.exception.argument.ArgumentNotFoundException;
import it.buniva.strage.exception.argument.ArgumentNotInSameSubjectException;
import it.buniva.strage.exception.question.*;
import it.buniva.strage.exception.subject.SubjectNotFoundException;
import it.buniva.strage.payload.request.QuestionAnswerMarkRequest;
import it.buniva.strage.payload.request.QuestionArgumentRequest;
import it.buniva.strage.payload.request.QuestionContentRequest;
import it.buniva.strage.payload.request.QuestionRequest;
import it.buniva.strage.payload.response.AnswerResponse;
import it.buniva.strage.payload.response.QuestionResponse;
import it.buniva.strage.payload.response.QuestionWithAnswersResponse;
import it.buniva.strage.service.AnswerService;
import it.buniva.strage.service.QuestionService;
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
@RequestMapping("/questions")
public class QuestionController extends QuestionExceptionHandling {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private AnswerService answerService;


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
    @GetMapping(value = "/get-question-by-code/{questionCode}")
    // @PreAuthorize("hasAuthority('question:read')")
    public ResponseEntity<ApiResponseCustom> getQuestionByCode(
            @PathVariable("questionCode") String questionCode,
            HttpServletRequest request) throws QuestionNotFoundException {

        Question question = questionService.getQuestionByQuestionCodeAndEnabledTrueAndDeletedFalse(questionCode);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", QuestionResponse.createFromQuestion(question), request.getRequestURI()),
                HttpStatus.OK);
    }

    @GetMapping(value = "/get-question-with-all-answers-by-code/{questionCode}")
    // @PreAuthorize("hasAuthority('question:read') and hasAuthority('answer:read')")
    public ResponseEntity<ApiResponseCustom> getQuestionWithAllAnswerByCode(
            @PathVariable("questionCode") String questionCode,
            HttpServletRequest request) throws QuestionNotFoundException, AnswerTrueAlreadyExistException, EmptyAnswerListException {

        Question question = questionService.getQuestionByQuestionCodeAndEnabledTrueAndDeletedFalse(questionCode);
        List<AnswerResponse> answerResponseList =
                answerService.getAllAnswerByQuestionCode(questionCode).stream()
                        .map(AnswerResponse::createFromAnswer).collect(Collectors.toList());
        QuestionWithAnswersResponse questionWithAnswersResp = new QuestionWithAnswersResponse(
                QuestionResponse.createFromQuestion(question),
                answerResponseList);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", questionWithAnswersResp,
                request.getRequestURI()),
                HttpStatus.OK);
    }

    @GetMapping(value = "/get-all-questions-by-argument/{argumentCode}")
//    @PreAuthorize("hasAuthority('question:read') and hasAuthority('argument:read')")
    public ResponseEntity<ApiResponseCustom> getAllQuestionsByArgument(
            @PathVariable("argumentCode") String argumentCode,
            HttpServletRequest request) throws EmptyQuestionListException, ArgumentNotFoundException {

        List<Question> questionList = questionService.getAllQuestionByArgument(argumentCode);
        List<QuestionResponse> questionResponseList = questionList.stream().map(QuestionResponse::createFromQuestion).collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", questionResponseList, request.getRequestURI()),
                HttpStatus.OK);
    }

    @GetMapping(value = "/get-all-questions-by-subject/{subjectCode}")
//    @PreAuthorize("hasAuthority('question:create') and hasAuthority('subject:read')")
    public ResponseEntity<ApiResponseCustom> getAllQuestionsBySubject(
            @PathVariable("subjectCode") String subjectCode,
            HttpServletRequest request) throws EmptyQuestionListException, SubjectNotFoundException {

        List<Question> questionList = questionService.getAllQuestionBySubject(subjectCode);
        List<QuestionResponse> questionResponseList = questionList.stream().map(QuestionResponse::createFromQuestion).collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", questionResponseList, request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================ UPDATE =================================

    @PutMapping(value = "/update-question-content/{questionCode}")
//    @PreAuthorize("hasAuthority('question:update')")
    public ResponseEntity<ApiResponseCustom> updateQuestionContent(
            @PathVariable String questionCode,
            @RequestBody @Valid QuestionContentRequest questionContentRequest,
            HttpServletRequest request) throws QuestionNotFoundException, DuplicateQuestionContentException {

        Question question = questionService.updateQuestionContent(questionCode, questionContentRequest);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.CREATED, "", QuestionResponse.createFromQuestion(question), request.getRequestURI()),
                HttpStatus.CREATED);
    }

    @PutMapping(value = "/update-answer-mark/{questionCode}")
//    @PreAuthorize("hasAuthority('question:update')")
    public ResponseEntity<ApiResponseCustom> updateQuestionAnswerMark(
            @PathVariable String questionCode,
            @RequestBody @Valid QuestionAnswerMarkRequest questionAnswerMarkRequest,
            HttpServletRequest request) throws QuestionNotFoundException, InvalidAnswerMarkException {

        Question question = questionService.updateQuestionAnswerMark(questionCode, questionAnswerMarkRequest);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.CREATED, "", QuestionResponse.createFromQuestion(question), request.getRequestURI()),
                HttpStatus.CREATED);
    }

    @PutMapping(value = "/update-question-argument/{questionCode}")
//    @PreAuthorize("hasAuthority('question:update') and hasAuthority('argument:update')")
    public ResponseEntity<ApiResponseCustom> updateQuestionArgument(
            @PathVariable String questionCode,
            @RequestBody @Valid QuestionArgumentRequest questionArgumentRequest,
            HttpServletRequest request)
            throws QuestionNotFoundException, ArgumentNotFoundException, ArgumentNotInSameSubjectException {

        Question question = questionService.updateQuestionArgument(questionCode, questionArgumentRequest);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.CREATED, "", QuestionResponse.createFromQuestion(question), request.getRequestURI()),
                HttpStatus.CREATED);
    }

    @PutMapping(value = "/enable-disable/{questionCode}")
//    @PreAuthorize("hasAuthority('question:update')")
    public ResponseEntity<ApiResponseCustom> enableDisableQuestion(
            @PathVariable String questionCode,
            HttpServletRequest request) throws QuestionNotFoundException {

        Question question = questionService.enableDisableQuestion(questionCode);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.CREATED, "", QuestionResponse.createFromQuestion(question), request.getRequestURI()),
                HttpStatus.CREATED);
    }


    // ============================= DELETE ================================
    @PutMapping(value = "/delete-question/{questionCode}")
//    @PreAuthorize("hasAuthority('professor:delete')")
    public ResponseEntity<ApiResponseCustom> delete(
            @PathVariable String questionCode, HttpServletRequest request)
            throws  QuestionNotFoundException {

        questionService.deleteQuestion(questionCode);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", "Question successfully deleted", request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================== OTHER ==================================



    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================


}
