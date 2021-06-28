package it.buniva.strage.controller;

import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.entity.Subject;
import it.buniva.strage.exception.subject.DuplicateSubjectCodeException;
import it.buniva.strage.exception.subject.EmptySubjectListException;
import it.buniva.strage.exception.subject.SubjectExceptionHandling;
import it.buniva.strage.exception.subject.SubjectNotFoundException;
import it.buniva.strage.payload.request.SubjectRequest;
import it.buniva.strage.payload.response.SubjectResponse;
import it.buniva.strage.service.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/subjects")
public class SubjectController extends SubjectExceptionHandling {

    @Autowired
    private SubjectService subjectService;


    // ============================= CREATE ===============================
    @PostMapping(value = "/add-subject")
//    @PreAuthorize("hasAuthority('subject:create')")
    public ResponseEntity<ApiResponseCustom> addSubject(
            @RequestBody @Valid SubjectRequest subjectRequest,
            HttpServletRequest request) throws DuplicateSubjectCodeException, SubjectNotFoundException {

        Subject subject = subjectService.addSubject(subjectRequest);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 201,
                HttpStatus.CREATED, "", SubjectResponse.createFromSubject(subject), request.getRequestURI()),
                HttpStatus.CREATED);
    }

    // ============================== READ ================================
    @GetMapping(value = "/get-subject-by-id/{subjectId}")
//    @PreAuthorize("hasAuthority('subject:read')")
    public ResponseEntity<ApiResponseCustom> getById(
            @PathVariable("subjectId") Long subjectId,
            HttpServletRequest request) throws SubjectNotFoundException {

        Subject subject = subjectService.getSubjectByIdAndEnabledTrueAndDeletedFalse(subjectId);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", SubjectResponse.createFromSubject(subject), request.getRequestURI()),
                HttpStatus.OK);
    }

    @GetMapping(value = "/get-all-subjects")
//    @PreAuthorize("hasAuthority('subject:read')")
    public ResponseEntity<ApiResponseCustom> getAllSubjects(HttpServletRequest request) throws EmptySubjectListException {

        List<Subject> subjectList = subjectService.getAllSubjects();
        List<SubjectResponse> subjectsResponseList = new ArrayList<>();

        for (Subject subject : subjectList) {
            subjectsResponseList.add(SubjectResponse.createFromSubject(subject));
        }

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", subjectsResponseList, request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================ UPDATE =================================
    @PutMapping(value = "/enable-disable/{subjectCode}")
//    @PreAuthorize("hasAuthority('subject:update')")
    public ResponseEntity<ApiResponseCustom> enableDisableSubject(
            @PathVariable String subjectCode,
            HttpServletRequest request) throws SubjectNotFoundException {

        Subject subject = subjectService.enableDisableSubject(subjectCode);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", SubjectResponse.createFromSubject(subject), request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================= DELETE ================================
    @PutMapping(value = "/delete-subject/{subjectId}")
//    @PreAuthorize("hasAuthority('subject:delete')")
    public ResponseEntity<ApiResponseCustom> setDeleted(
            @PathVariable Long subjectId,
            HttpServletRequest request) throws SubjectNotFoundException {

        subjectService.deleteSubject(subjectId);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", "Subject successfully deleted", request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================== OTHER ==================================





    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================


}
