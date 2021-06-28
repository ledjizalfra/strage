package it.buniva.strage.controller;

import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.constant.ClassroomConstant;
import it.buniva.strage.constant.ProfessorConstant;
import it.buniva.strage.entity.Professor;
import it.buniva.strage.event.UserAddedSuccessfullyEvent;
import it.buniva.strage.exception.admin.AdminNotFoundException;
import it.buniva.strage.exception.classroom.EmptyClassroomListException;
import it.buniva.strage.exception.professor.EmptyProfessorListException;
import it.buniva.strage.exception.professor.ProfessorExceptionHandling;
import it.buniva.strage.exception.professor.ProfessorNotFoundException;
import it.buniva.strage.exception.role.RoleNotFoundException;
import it.buniva.strage.exception.student.DuplicatePersonalDataException;
import it.buniva.strage.exception.student.StudentNotFoundException;
import it.buniva.strage.exception.subject.SubjectAlreadyExistsException;
import it.buniva.strage.exception.subject.SubjectNotFoundException;
import it.buniva.strage.exception.user.DuplicateUsernameException;
import it.buniva.strage.exception.user.UserNotFoundException;
import it.buniva.strage.payload.request.PersonalDataRequest;
import it.buniva.strage.payload.request.ProfessorInClassRequest;
import it.buniva.strage.payload.request.ProfessorRequest;
import it.buniva.strage.payload.request.SubjectToProfessorRequest;
import it.buniva.strage.payload.response.ProfessorResponse;
import it.buniva.strage.payload.response.SubjectResponse;
import it.buniva.strage.service.ProfessorService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/professors")
public class ProfessorController extends ProfessorExceptionHandling {

    @Autowired
    private ProfessorService professorService;

    @Autowired
    private ApplicationEventPublisher publisher;


    // ============================= CREATE ===============================
    @PostMapping(value = "/register-professor")
//    @PreAuthorize("hasAuthority('professor:create')")
    public ResponseEntity<ApiResponseCustom> registerProfessor(
            @RequestBody @Valid ProfessorRequest professorRequest,
            HttpServletRequest request)
            throws DuplicateUsernameException, UserNotFoundException, ProfessorNotFoundException,
            RoleNotFoundException, DuplicatePersonalDataException, MessagingException {

        Professor professor = professorService.registerProfessor(professorRequest);

        // Publish an event to notifier the send mail Scheduling that it can start sending mail
        publisher.publishEvent(new UserAddedSuccessfullyEvent(this, true));

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 201,
                HttpStatus.CREATED, "", ProfessorResponse.createFromProfessor(professor), request.getRequestURI()),
                HttpStatus.CREATED);
    }

    // ============================== READ ================================
    @GetMapping(value = "/get-professor-by-id/{professorId}")
//    @PreAuthorize("hasAuthority('professor:read')")
    public ResponseEntity<ApiResponseCustom> getById(
            @PathVariable("professorId") Long professorId,
            HttpServletRequest request) throws ProfessorNotFoundException {

        Professor professor = professorService.getProfessorByIdAndEnabledTrueAndDeletedFalse(professorId);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", ProfessorResponse.createFromProfessor(professor), request.getRequestURI()),
                HttpStatus.OK);
    }

    @GetMapping(value = "/get-all-professors")
//    @PreAuthorize("hasAuthority('professor:read')")
    public ResponseEntity<ApiResponseCustom> getAll(HttpServletRequest request) throws EmptyProfessorListException {
        List<Professor> professorList = professorService.getAllProfessorsByEnabledTrueAndDeletedFalse();
        List<ProfessorResponse> professorResponseList = new ArrayList<>();

        for (Professor professor : professorList) {
            professorResponseList.add(ProfessorResponse.createFromProfessor(professor));
        }

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", professorResponseList, request.getRequestURI()),
                HttpStatus.OK);
    }

    @GetMapping("/get-all-subjects-of-professor/{profEmail}")
    //    @PreAuthorize("hasAuthority('subject:read') and hasAuthority('professor:read')")
    public ResponseEntity<ApiResponseCustom> getAllSubjectsOfProfessor(
            @PathVariable("profEmail") String profEmail,
            HttpServletRequest request) throws ProfessorNotFoundException {

        // List<Subject> subjectList = professorService.getAllSubjectsOfProfessor(profEmail);
        List<SubjectResponse> subjectResponseList =
                professorService.getAllSubjectsOfProfessor(profEmail).stream()
                        .map(SubjectResponse::createFromSubject)
                        .collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, StringUtils.EMPTY, subjectResponseList, request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================ UPDATE =================================
    @PutMapping(value = "/enable-disable/{professorId}")
//    @PreAuthorize("hasAuthority('professor:update')")
    public ResponseEntity<ApiResponseCustom> enableDisableProfessor(
            @PathVariable Long professorId,
            HttpServletRequest request)
            throws ProfessorNotFoundException, UserNotFoundException {

        Professor professor = professorService.enableDisableProfessor(professorId);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", ProfessorResponse.createFromProfessor(professor), request.getRequestURI()),
                HttpStatus.OK);
    }

    @PutMapping(value = "/assign-subject-to-professor")
//    @PreAuthorize("hasAuthority('subject:update') and hasAuthority('professor:update')")
    public ResponseEntity<ApiResponseCustom> assignSubjectToProfessor(
            @RequestBody @Valid SubjectToProfessorRequest subjectToProfessorRequest,
            HttpServletRequest request) throws SubjectNotFoundException, ProfessorNotFoundException, SubjectAlreadyExistsException {

        professorService.assignSubjectToProfessor(subjectToProfessorRequest);

        String message = String.format(
                ProfessorConstant.SUBJECT_ASSIGNED_TO_PROFESSOR_MSG,
                subjectToProfessorRequest.getSubjectCode(),
                subjectToProfessorRequest.getProfessorEmail()
        );

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", message, request.getRequestURI()),
                HttpStatus.OK);
    }

    @PutMapping(value = "/update-professor-personal-data/{professorId}")
//    @PreAuthorize("hasAuthority('professor:update')")
    public ResponseEntity<ApiResponseCustom> updatePersonalData(
            @PathVariable Long professorId,
            @RequestBody @Valid PersonalDataRequest personalDataRequest,
            HttpServletRequest request) throws ProfessorNotFoundException, DuplicatePersonalDataException {

        Professor professor = professorService.updatePersonalData(professorId, personalDataRequest);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", ProfessorResponse.createFromProfessor(professor), request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================= DELETE ================================
    @PutMapping(value = "/delete-professor/{professorId}")
//    @PreAuthorize("hasAuthority('professor:delete')")
    public ResponseEntity<ApiResponseCustom> setDeleted(
            @PathVariable Long professorId,
            HttpServletRequest request)
            throws UserNotFoundException, ProfessorNotFoundException {

        professorService.deleteProfessor(professorId);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", "Professor successfully deleted", request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================== OTHER ==================================





    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================


}
