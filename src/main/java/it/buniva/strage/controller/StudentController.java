package it.buniva.strage.controller;

import it.buniva.strage.api.ApiResponseCustom;
import it.buniva.strage.entity.Student;
import it.buniva.strage.exception.role.RoleNotFoundException;
import it.buniva.strage.exception.student.DuplicatePersonalDataException;
import it.buniva.strage.exception.student.EmptyStudentListException;
import it.buniva.strage.exception.student.StudentExceptionHandling;
import it.buniva.strage.exception.student.StudentNotFoundException;
import it.buniva.strage.exception.user.DuplicateUsernameException;
import it.buniva.strage.exception.user.UserNotFoundException;
import it.buniva.strage.payload.request.PersonalDataRequest;
import it.buniva.strage.payload.request.StudentRequest;
import it.buniva.strage.payload.response.StudentResponse;
import it.buniva.strage.service.StudentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/students")
public class StudentController extends StudentExceptionHandling {

    @Autowired
    private StudentService studentService;


    // ============================= CREATE ===============================
    @PostMapping(value = "/register-student")
//    @PreAuthorize("hasAuthority('student:create')")
    public ResponseEntity<ApiResponseCustom> registerStudent(
            @RequestBody @Valid StudentRequest studentRequest,
            HttpServletRequest request)
            throws DuplicateUsernameException, UserNotFoundException, RoleNotFoundException,
            StudentNotFoundException, DuplicatePersonalDataException, MessagingException {

        Student student = studentService.registerStudent(studentRequest);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 201,
                HttpStatus.CREATED, "", StudentResponse.createFromStudent(student), request.getRequestURI()),
                HttpStatus.CREATED);
    }


    @PostMapping("/register-many-students-from-csvfile")
    //    @PreAuthorize("hasAuthority('student:create')")
    public ResponseEntity<ApiResponseCustom> registerStudentsFromCsvFile(
            @RequestParam("classroomName") String classroomName,
            @RequestParam("csvFile") MultipartFile csvFile,
            HttpServletRequest request)
            throws Exception {

        List<Student> studentList = studentService.registerStudentsFromCsvFile(classroomName, csvFile);

        List<StudentResponse> studentInfoResponseList = studentList.stream()
                .map(StudentResponse::createFromStudent)
                .collect(Collectors.toList());

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, StringUtils.EMPTY, studentInfoResponseList, request.getRequestURI()),
                HttpStatus.OK);
    }


    // ============================== READ ================================
    @GetMapping(value = "/get-student-by-id/{studentId}")
//    @PreAuthorize("hasAuthority('student:read')")
    public ResponseEntity<ApiResponseCustom> getById(
            @PathVariable("studentId") Long studentId,
            HttpServletRequest request) throws StudentNotFoundException {

        Student student = studentService.getStudentByIdAndEnabledTrueAndDeletedFalse(studentId);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", StudentResponse.createFromStudent(student), request.getRequestURI()),
                HttpStatus.OK);
    }

    @GetMapping(value = "/get-all-students")
//    @PreAuthorize("hasAuthority('student:read')")
    public ResponseEntity<ApiResponseCustom> getAllStudents(HttpServletRequest request) throws EmptyStudentListException {

        List<Student> studentList = studentService.getAllStudentsByEnabledTrueAndDeletedFalse();
        List<StudentResponse> studentResponseList = new ArrayList<>();

        for (Student student : studentList) {
            studentResponseList.add(StudentResponse.createFromStudent(student));
        }

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", studentResponseList, request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================ UPDATE =================================
    @PutMapping(value = "/enable-disable/{studentId}")
//    @PreAuthorize("hasAuthority('student:update')")
    public ResponseEntity<ApiResponseCustom> enableDisableStudent(
            @PathVariable Long studentId,
            HttpServletRequest request)
            throws StudentNotFoundException, UserNotFoundException {

        Student student = studentService.enableDisableStudent(studentId);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", StudentResponse.createFromStudent(student), request.getRequestURI()),
                HttpStatus.OK);
    }

    @PutMapping(value = "/enable-disable-sced/{studentId}")
//    @PreAuthorize("hasAuthority('student:update')")
    public ResponseEntity<ApiResponseCustom> enableDisableScedStudent(
            @PathVariable Long studentId,
            HttpServletRequest request) throws StudentNotFoundException {

        Student student = studentService.enableDisableScedStudent(studentId);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", StudentResponse.createFromStudent(student), request.getRequestURI()),
                HttpStatus.OK);
    }

    @PutMapping(value = "/update-student-personal-data/{studentId}")
//    @PreAuthorize("hasAuthority('student:update')")
    public ResponseEntity<ApiResponseCustom> updatePersonalData(
            @PathVariable Long studentId,
            @RequestBody @Valid PersonalDataRequest personalDataRequest,
            HttpServletRequest request) throws StudentNotFoundException, DuplicatePersonalDataException {

        Student student = studentService.updatePersonalData(studentId, personalDataRequest);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", StudentResponse.createFromStudent(student), request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================= DELETE ================================
    @PutMapping(value = "/delete-student/{studentId}")
//    @PreAuthorize("hasAuthority('student:delete')")
    public ResponseEntity<ApiResponseCustom> setDeleted(
            @PathVariable Long studentId,
            HttpServletRequest request) throws UserNotFoundException, StudentNotFoundException {

        studentService.deleteStudent(studentId);

        return new ResponseEntity<>(new ApiResponseCustom(Instant.now(), 200,
                HttpStatus.OK, "", "Student successfully deleted", request.getRequestURI()),
                HttpStatus.OK);
    }

    // ============================== OTHER ==================================





    // ===========================================================
    // ==================== PRIVATE METHOD =======================
    // ===========================================================


}
