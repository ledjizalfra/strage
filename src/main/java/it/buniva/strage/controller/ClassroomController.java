package it.buniva.strage.controller;

import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.constant.ClassroomConstant;
import it.buniva.strage.entity.Classroom;
import it.buniva.strage.entity.Professor;
import it.buniva.strage.exception.classroom.ClassroomExceptionHandling;
import it.buniva.strage.exception.classroom.ClassroomNotFoundException;
import it.buniva.strage.exception.classroom.DuplicateClassroomNameException;
import it.buniva.strage.exception.classroom.EmptyClassroomListException;
import it.buniva.strage.exception.professor.ProfessorAlreadyExistsException;
import it.buniva.strage.exception.professor.ProfessorNotFoundException;
import it.buniva.strage.exception.student.EmptyStudentListException;
import it.buniva.strage.exception.subject.SubjectAlreadyExistsException;
import it.buniva.strage.exception.subject.SubjectNotFoundException;
import it.buniva.strage.payload.request.*;
import it.buniva.strage.payload.response.ClassroomResponse;
import it.buniva.strage.payload.response.ProfessorResponse;
import it.buniva.strage.payload.response.StudentResponse;
import it.buniva.strage.payload.response.SubjectResponse;
import it.buniva.strage.service.ClassroomService;
import org.apache.commons.lang3.StringUtils;
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
@RequestMapping("/classrooms")
public class ClassroomController extends ClassroomExceptionHandling {

    @Autowired
    private ClassroomService classroomService;


    // ============================= CREATE ===============================
    @PostMapping(value = "/add-classroom")
//    @PreAuthorize("hasAuthority('classroom:create')")
    public ResponseEntity<ApiResponseCustom> registerStudent(
            @RequestBody @Valid ClassroomRequest classroomRequest,
            HttpServletRequest request) throws DuplicateClassroomNameException, ClassroomNotFoundException {

        Classroom classroom = classroomService.addClassroom(classroomRequest);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 201,
                HttpStatus.CREATED, "", ClassroomResponse.createFrom(classroom), request.getRequestURI()),
                HttpStatus.CREATED);
    }

    @PostMapping("/add-many-classrooms")
    //    @PreAuthorize("hasAuthority('classroom:create')")
    public ResponseEntity<ApiResponseCustom> addManyClassrooms(
            @RequestBody @Valid ClassroomListRequest classroomListRequest,
            HttpServletRequest request) throws DuplicateClassroomNameException, ClassroomNotFoundException {

        List<Classroom> newClassroomList = classroomService.addManyClassroom(classroomListRequest);

        List<ClassroomResponse> classroomResponseList = newClassroomList.stream()
                .map(ClassroomResponse::createFrom)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, StringUtils.EMPTY, classroomResponseList, request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================== READ ================================
    @GetMapping("/get-classroom-by-name/{classroomName}")
    //    @PreAuthorize("hasAuthority('classroom:read')")
    public ResponseEntity<ApiResponseCustom> getClassroomByName(
            @PathVariable("classroomName") String classroomName,
            HttpServletRequest request) throws ClassroomNotFoundException {

        Classroom classroom =
                classroomService.getClassroomByNameAndEnabledTrueAndDeletedFalse(classroomName);

        ClassroomResponse classroomResponse = ClassroomResponse.createFrom(classroom);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, StringUtils.EMPTY, classroomResponse, request.getRequestURI()),
                HttpStatus.OK);
    }

    @GetMapping("/get-all-classrooms")
    //    @PreAuthorize("hasAuthority('classroom:read')")
    public ResponseEntity<ApiResponseCustom> getAllClassrooms(HttpServletRequest request) throws EmptyClassroomListException {

        List<ClassroomResponse> classroomResponseList =
                classroomService.getAllClassroomsAndEnabledTrueAndDeletedFalse().stream()
                .map(ClassroomResponse::createFrom)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, StringUtils.EMPTY, classroomResponseList, request.getRequestURI()),
                HttpStatus.OK);
    }

    @GetMapping("/get-all-professors-in-classroom/{classroomName}")
    //    @PreAuthorize("hasAuthority('professor:read') and hasAuthority('classroom:read')")
    public ResponseEntity<ApiResponseCustom> getAllProfessorsInClassroom(
            @PathVariable("classroomName") String classroomName,
            HttpServletRequest request) throws EmptyClassroomListException, ClassroomNotFoundException {

        // List<Professor> professorList = classroomService.getAllProfessorsInClassroom(classroomName);
        List<ProfessorResponse> professorResponseList =
                classroomService.getAllProfessorsInClassroom(classroomName).stream()
                .map(ProfessorResponse::createFromProfessor)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, StringUtils.EMPTY, professorResponseList, request.getRequestURI()),
                HttpStatus.OK);
    }

    @GetMapping("/get-all-subjects-in-classroom/{classroomName}")
    //    @PreAuthorize("hasAuthority('subject:read') and hasAuthority('classroom:read')")
    public ResponseEntity<ApiResponseCustom> getAllSubjectsInClassrooms(
            @PathVariable("classroomName") String classroomName,
            HttpServletRequest request) throws EmptyClassroomListException, ClassroomNotFoundException {

        // List<Subject> subjectList = classroomService.getAllSubjectsInClassroom(classroomName);
        List<SubjectResponse> subjectResponseList =
                classroomService.getAllSubjectsInClassroom(classroomName).stream()
                        .map(SubjectResponse::createFromSubject)
                        .collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, StringUtils.EMPTY, subjectResponseList, request.getRequestURI()),
                HttpStatus.OK);
    }

    @GetMapping("/get-all-students-in-classroom/{classroomName}")
    public ResponseEntity<ApiResponseCustom> getAllStudentsInClassroom(
            @PathVariable("classroomName") String classroomName,
            HttpServletRequest request) throws  EmptyStudentListException {

        List<StudentResponse> studentResponseList =
                classroomService.getAllStudentsInClassroom(classroomName).stream()
                        .map(StudentResponse::createFromStudent)
                        .collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, StringUtils.EMPTY, studentResponseList, request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================ UPDATE =================================
    @PutMapping(value = "/enable-disable/{classroomName}")
//    @PreAuthorize("hasAuthority('classroom:update')")
    public ResponseEntity<ApiResponseCustom> enableDisableClassroom(
            @PathVariable("classroomName") String classroomName,
            HttpServletRequest request) throws ClassroomNotFoundException {

        Classroom classroom = classroomService.enableDisableClassroom(classroomName);

        String status = classroom.isEnabled()? "enabled" : "disabled";

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", "Classroom successfully " + status, request.getRequestURI()),
                HttpStatus.OK);
    }

    @PutMapping(value = "/add-professor-in-classroom")
//    @PreAuthorize("hasAuthority('classroom:update')")
    public ResponseEntity<ApiResponseCustom> addProfessorInClassroom(
            @RequestBody @Valid ProfessorInClassRequest professorInClassRequest,
            HttpServletRequest request) throws ClassroomNotFoundException, ProfessorNotFoundException, ProfessorAlreadyExistsException {

        classroomService.addProfessorInClassroom(professorInClassRequest);

        String message = String.format(
                ClassroomConstant.PROFESSOR_ADDED_IN_CLASSROOM_MSG,
                professorInClassRequest.getProfessorEmail(),
                professorInClassRequest.getClassroomName()
        );

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", message, request.getRequestURI()),
                HttpStatus.OK);
    }

    @PutMapping(value = "/add-subject-in-classroom")
//    @PreAuthorize("hasAuthority('classroom:update')")
    public ResponseEntity<ApiResponseCustom> addSubjectInClassroom(
            @RequestBody @Valid SubjectInClassRequest subjectInClassRequest,
            HttpServletRequest request) throws ClassroomNotFoundException, SubjectNotFoundException, SubjectAlreadyExistsException, ProfessorAlreadyExistsException {

        classroomService.addSubjectInClassroom(subjectInClassRequest);

        String message = String.format(
                ClassroomConstant.SUBJECT_ADDED_IN_CLASSROOM_MSG,
                subjectInClassRequest.getSubjectCode(),
                subjectInClassRequest.getClassroomName()
        );

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", message, request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================= DELETE ================================

    // ============================== OTHER ==================================





    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================


}
