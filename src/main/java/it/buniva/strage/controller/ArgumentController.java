package it.buniva.strage.controller;

import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.entity.Argument;
import it.buniva.strage.exception.argument.ArgumentExceptionHandling;
import it.buniva.strage.exception.argument.ArgumentNotFoundException;
import it.buniva.strage.exception.argument.DuplicateArgumentCodeException;
import it.buniva.strage.exception.argument.EmptyArgumentListException;
import it.buniva.strage.exception.subject.SubjectNotFoundException;
import it.buniva.strage.payload.request.ArgumentRequest;
import it.buniva.strage.payload.response.ArgumentResponse;
import it.buniva.strage.service.ArgumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/arguments")
public class ArgumentController extends ArgumentExceptionHandling {

    @Autowired
    private ArgumentService argumentService;


    // ============================= CREATE ===============================
    @PostMapping(value = "/add-argument")
//    @PreAuthorize("hasAuthority('argument:create')")
    public ResponseEntity<ApiResponseCustom> addArgument(
            @RequestBody @Valid ArgumentRequest argumentRequest,
            HttpServletRequest request) throws DuplicateArgumentCodeException, ArgumentNotFoundException, SubjectNotFoundException {

        Argument argument = argumentService.addArgument(argumentRequest);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 201,
                HttpStatus.CREATED, "", ArgumentResponse.createFromArgument(argument), request.getRequestURI()),
                HttpStatus.CREATED);
    }

    // ============================== READ ================================
    @GetMapping(value = "/get-argument-by-id/{argumentId}")
//    @PreAuthorize("hasAuthority('argument:read')")
    public ResponseEntity<ApiResponseCustom> getById(
            @PathVariable("argumentId") Long argumentId,
            HttpServletRequest request) throws ArgumentNotFoundException {

        Argument argument = argumentService.getArgumentByIdAndEnabledTrueAndDeletedFalse(argumentId);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", ArgumentResponse.createFromArgument(argument), request.getRequestURI()),
                HttpStatus.OK);
    }

    @GetMapping(value = "/get-all-arguments")
//    @PreAuthorize("hasAuthority('argument:read')")
    public ResponseEntity<ApiResponseCustom> getAllArguments(HttpServletRequest request) throws EmptyArgumentListException {

        List<Argument> argumentList = argumentService.getAllArguments();
        List<ArgumentResponse> argumentsResponseList = new ArrayList<>();
        /*argumentList.stream()
                .map(ArgumentResponse::createFromArgument)
                .collect(Collectors.toList());*/

        for (Argument argument : argumentList) {
            argumentsResponseList.add(ArgumentResponse.createFromArgument(argument));
        }

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", argumentsResponseList, request.getRequestURI()),
                HttpStatus.OK);
    }

    @GetMapping(value = "/get-all-arguments-by-subject/{argumentCode}")
//    @PreAuthorize("hasAuthority('argument:read')")
    public ResponseEntity<ApiResponseCustom> getAllArgumentsBySubject(
            @PathVariable("argumentCode") String argumentCode,
            HttpServletRequest request) throws EmptyArgumentListException, SubjectNotFoundException {

        List<Argument> argumentList = argumentService.getAllArgumentsBySubjectAndEnabledTrueAndDeletedFalse(argumentCode);

        List<ArgumentResponse> argumentsResponseList = argumentList.stream()
                .map(ArgumentResponse::createFromArgument)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", argumentsResponseList, request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================ UPDATE =================================
    @PutMapping(value = "/enable-disable/{argumentCode}")
//    @PreAuthorize("hasAuthority('argument:update')")
    public ResponseEntity<ApiResponseCustom> enableDisableArgument(
            @PathVariable String argumentCode,
            HttpServletRequest request) throws ArgumentNotFoundException {

        Argument argument = argumentService.enableDisableArgument(argumentCode);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", ArgumentResponse.createFromArgument(argument), request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================= DELETE ================================
    @PutMapping(value = "/delete-argument/{argumentId}")
//    @PreAuthorize("hasAuthority('argument:delete')")
    public ResponseEntity<ApiResponseCustom> setDeleted(
            @PathVariable Long argumentId,
            HttpServletRequest request) throws ArgumentNotFoundException {

        argumentService.deleteArgument(argumentId);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", "Argument successfully deleted", request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================== OTHER ==================================





    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================



}
